package org.cube.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.exception.CubeAppException;
import org.cube.modules.system.entity.SysPermission;
import org.cube.modules.system.entity.SysPermissionDataRule;
import org.cube.modules.system.mapper.SysDepartPermissionMapper;
import org.cube.modules.system.mapper.SysDepartRolePermissionMapper;
import org.cube.modules.system.mapper.SysPermissionMapper;
import org.cube.modules.system.mapper.SysRolePermissionMapper;
import org.cube.modules.system.model.TreeModel;
import org.cube.modules.system.service.ISysPermissionDataRuleService;
import org.cube.modules.system.service.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private ISysPermissionDataRuleService permissionDataRuleService;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private SysDepartPermissionMapper sysDepartPermissionMapper;
    @Autowired
    private SysDepartRolePermissionMapper sysDepartRolePermissionMapper;

    @Override
    public List<TreeModel> queryListByParentId(String parentId) {
        return baseMapper.queryListByParentId(parentId);
    }

    /**
     * 真实删除
     */
    @Override
    @Transactional
    @CacheEvict(value = CacheConst.SYS_DATA_PERMISSIONS_CACHE, allEntries = true)
    public void deletePermission(String id) throws CubeAppException {
        SysPermission sysPermission = this.getById(id);
        if (sysPermission == null) {
            throw new CubeAppException("未找到菜单信息");
        }
        String pid = sysPermission.getParentId();
        if (StrUtil.isNotEmpty(pid)) {
            long count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
            if (count == 1) {
                //若父节点无其他子节点，则该父节点是叶子节点
                baseMapper.setMenuLeaf(pid, 1);
            }
        }
        baseMapper.deleteById(id);
        // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
        this.removeChildrenBy(sysPermission.getId());
        //关联删除
        Map<String, Object> map = new HashMap<>();
        map.put("permission_id", id);
        //删除数据规则
        this.deletePermRuleByPermId(id);
        //删除角色授权表
        sysRolePermissionMapper.deleteByMap(map);
        //删除部门权限表
        sysDepartPermissionMapper.deleteByMap(map);
        //删除部门角色授权
        sysDepartRolePermissionMapper.deleteByMap(map);
    }

    /**
     * 根据父id删除其关联的子节点数据
     */
    public void removeChildrenBy(String parentId) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(SysPermission::getParentId, parentId);
        // 查出该主键下的所有子级
        List<SysPermission> permissionList = this.list(query);
        if (permissionList != null && permissionList.size() > 0) {
            String id; // id
            long num; // 查出的子级数量
            // 如果查出的集合不为空, 则先删除所有
            this.remove(query);
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (SysPermission sysPermission : permissionList) {
                id = sysPermission.getId();
                Map<String, Object> map = new HashMap<>();
                map.put("permission_id", id);
                //删除数据规则
                this.deletePermRuleByPermId(id);
                //删除角色授权表
                sysRolePermissionMapper.deleteByMap(map);
                //删除部门权限表
                sysDepartPermissionMapper.deleteByMap(map);
                //删除部门角色授权
                sysDepartRolePermissionMapper.deleteByMap(map);
                num = this.count(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    this.removeChildrenBy(id);
                }
            }
        }
    }

    /**
     * 逻辑删除
     */
    @Override
    @CacheEvict(value = CacheConst.SYS_DATA_PERMISSIONS_CACHE, allEntries = true)
    public void deletePermissionLogical(String id) throws CubeAppException {
        SysPermission sysPermission = this.getById(id);
        if (sysPermission == null) {
            throw new CubeAppException("未找到菜单信息");
        }
        String pid = sysPermission.getParentId();
        long count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
        if (count == 1) {
            //若父节点无其他子节点，则该父节点是叶子节点
            baseMapper.setMenuLeaf(pid, 1);
        }
        sysPermission.setDelFlag(1);
        this.updateById(sysPermission);
    }

    @Override
    @CacheEvict(value = CacheConst.SYS_DATA_PERMISSIONS_CACHE, allEntries = true)
    public void addPermission(SysPermission sysPermission) throws CubeAppException {
        //----------------------------------------------------------------------
        //判断是否是一级菜单，是的话清空父菜单
        if (CommonConst.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
            sysPermission.setParentId(null);
        }
        //----------------------------------------------------------------------
        String pid = sysPermission.getParentId();
        if (StrUtil.isNotEmpty(pid)) {
            //设置父节点不为叶子节点
            baseMapper.setMenuLeaf(pid, 0);
        }
        sysPermission.setCreateTime(new Date());
        sysPermission.setDelFlag(0);
        sysPermission.setLeaf(true);
        this.save(sysPermission);
    }

    @Override
    @CacheEvict(value = CacheConst.SYS_DATA_PERMISSIONS_CACHE, allEntries = true)
    public void editPermission(SysPermission sysPermission) throws CubeAppException {
        SysPermission p = this.getById(sysPermission.getId());
        //TODO 该节点判断是否还有子节点
        if (p == null) {
            throw new CubeAppException("未找到菜单信息");
        } else {
            sysPermission.setUpdateTime(new Date());
            //----------------------------------------------------------------------
            //Step1.判断是否是一级菜单，是的话清空父菜单ID
            if (CommonConst.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
                sysPermission.setParentId("");
            }
            //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
            long count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, sysPermission.getId()));
            if (count == 0) {
                sysPermission.setLeaf(true);
            }
            //----------------------------------------------------------------------
            this.updateById(sysPermission);
            //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
            String pid = sysPermission.getParentId();
            if ((StrUtil.isNotEmpty(pid) && !pid.equals(p.getParentId())) || StrUtil.isEmpty(pid) && StrUtil.isNotEmpty(p.getParentId())) {
                //a.设置新的父菜单不为叶子节点
                baseMapper.setMenuLeaf(pid, 0);
                //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
                long cc = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, p.getParentId()));
                if (cc == 0) {
                    if (StrUtil.isNotEmpty(p.getParentId())) {
                        baseMapper.setMenuLeaf(p.getParentId(), 1);
                    }
                }
            }
        }
    }

    @Override
    public List<SysPermission> queryByUser(String username) {
        return baseMapper.queryByUser(username);
    }

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     */
    @Override
    public void deletePermRuleByPermId(String id) {
        LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<>();
        query.eq(SysPermissionDataRule::getPermissionId, id);
        long countValue = this.permissionDataRuleService.count(query);
        if (countValue > 0) {
            this.permissionDataRuleService.remove(query);
        }
    }

    /**
     * 获取模糊匹配规则的数据权限URL
     */
    @Override
    @Cacheable(value = CacheConst.SYS_DATA_PERMISSIONS_CACHE)
    public List<String> queryPermissionUrlWithStar() {
        return baseMapper.queryPermissionUrlWithStar();
    }

    @Override
    public boolean hasPermission(String username, SysPermission sysPermission) {
        int count = baseMapper.queryCountByUsername(username, sysPermission);
        return count > 0;
    }

    @Override
    public boolean hasPermission(String username, String url) {
        SysPermission sysPermission = new SysPermission();
        sysPermission.setUrl(url);
        int count = baseMapper.queryCountByUsername(username, sysPermission);
        return count > 0;
    }

    @Override
    public int queryCountByUsername(String username, SysPermission sysPermission) {
        return baseMapper.queryCountByUsername(username, sysPermission);
    }
}
