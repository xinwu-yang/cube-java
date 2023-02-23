package org.cube.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.base.Result;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.utils.FillRuleUtil;
import org.cube.commons.utils.spring.RedisUtil;
import org.cube.modules.system.entity.*;
import org.cube.modules.system.mapper.*;
import org.cube.modules.system.model.DepartIdModel;
import org.cube.modules.system.model.SysDepartTreeModel;
import org.cube.modules.system.service.ISysDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 *
 * @author Steve
 * @since 2019-01-22
 */
@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {

    @Autowired
    private SysUserDepartMapper userDepartMapper;
    @Autowired
    private SysDepartRoleMapper sysDepartRoleMapper;
    @Autowired
    private SysDepartPermissionMapper departPermissionMapper;
    @Autowired
    private SysDepartRolePermissionMapper departRolePermissionMapper;
    @Autowired
    private SysDepartRoleUserMapper departRoleUserMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<SysDepartTreeModel> queryMyDeptTreeList(String departIds) {
        //根据部门id获取所负责部门
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        String[] codeArr = this.getMyDeptParentOrgCode(departIds);
        if (codeArr != null) {
            for (String s : codeArr) {
                query.or().likeRight(SysDepart::getOrgCode, s);
            }
        }
        query.eq(SysDepart::getDelFlag, CommonConst.NOT_DELETED.toString());
        query.orderByAsc(SysDepart::getDepartOrder);
        //将父节点ParentId设为null
        List<SysDepart> listDepts = this.list(query);
        if (codeArr != null) {
            for (String s : codeArr) {
                for (SysDepart dept : listDepts) {
                    if (dept.getOrgCode().equals(s)) {
                        dept.setParentId(null);
                    }
                }
            }
        }
        return SysDepartTreeModel.toTreeList(listDepts);
    }

    /**
     * queryTreeList 查询所有的部门数据,以树结构形式响应给前端
     */
    @Override
    @Cacheable(CacheConst.SYS_DEPARTS_CACHE_ALL)
    public List<SysDepartTreeModel> queryTreeList() {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDelFlag, CommonConst.NOT_DELETED);
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        return SysDepartTreeModel.toTreeList(list);
    }

    @Override
    @Cacheable(CacheConst.SYS_DEPART_IDS_CACHE)
    public List<DepartIdModel> queryDepartIdTreeList() {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDelFlag, CommonConst.NOT_DELETED.toString());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        return SysDepartTreeModel.toIdTreeList(list);
    }

    /**
     * saveDepartData 对应 add 保存用户在页面添加的新的部门对象数据
     */
    @Override
    @Transactional
    public Result<?> saveDepartData(SysDepart sysDepart) {
        if (sysDepart.getParentId() == null) {
            sysDepart.setParentId("");
        }
        String s = UUID.randomUUID().toString().replace("-", "");
        sysDepart.setId(s);
        // 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
        // 获取父级ID
        String parentId = sysDepart.getParentId();
        //update-begin--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
        JSONObject formData = JSONUtil.createObj();
        formData.set("parentId", parentId);
        String[] codeArray = (String[]) FillRuleUtil.executeRule("org_num_role", formData.toString());
        if (codeArray == null) {
            return Result.error("生成部门OrgCode失败！");
        }
        //update-end--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
        sysDepart.setOrgCode(codeArray[0]);
        String orgType = codeArray[1];
        sysDepart.setOrgType(String.valueOf(orgType));
        sysDepart.setCreateTime(new Date());
        sysDepart.setDelFlag(CommonConst.NOT_DELETED);
        this.save(sysDepart);
        redisUtil.prefixDel(CacheConst.SYS_DEPARTS_CACHE);
        return Result.ok();
    }

    /**
     * updateDepartDataById 对应 edit 根据部门主键来更新对应的部门数据
     */
    @Override
    @Transactional
    public void updateDepartDataById(SysDepart sysDepart, String username) {
        sysDepart.setUpdateTime(new Date());
        sysDepart.setUpdateBy(username);
        this.updateById(sysDepart);
        redisUtil.prefixDel(CacheConst.SYS_DEPARTS_CACHE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchWithChildren(List<String> ids) {
        List<String> idList = new ArrayList<>();
        for (String id : ids) {
            idList.add(id);
            this.checkChildrenExists(id, idList);
        }
        this.removeByIds(idList);
        //根据部门id获取部门角色id
        List<String> roleIdList = new ArrayList<>();
        LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
        query.select(SysDepartRole::getId).in(SysDepartRole::getDepartId, idList);
        List<SysDepartRole> depRoleList = sysDepartRoleMapper.selectList(query);
        for (SysDepartRole deptRole : depRoleList) {
            roleIdList.add(deptRole.getId());
        }
        //根据部门id删除用户与部门关系
        userDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getDepId, idList));
        //根据部门id删除部门授权
        departPermissionMapper.delete(new LambdaQueryWrapper<SysDepartPermission>().in(SysDepartPermission::getDepartId, idList));
        //根据部门id删除部门角色
        sysDepartRoleMapper.delete(new LambdaQueryWrapper<SysDepartRole>().in(SysDepartRole::getDepartId, idList));
        if (roleIdList.size() > 0) {
            //根据角色id删除部门角色授权
            departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().in(SysDepartRolePermission::getRoleId, roleIdList));
            //根据角色id删除部门角色用户信息
            departRoleUserMapper.delete(new LambdaQueryWrapper<SysDepartRoleUser>().in(SysDepartRoleUser::getDroleId, roleIdList));
        }
        redisUtil.prefixDel(CacheConst.SYS_DEPARTS_CACHE);
    }

    @Override
    public List<String> getSubDepIdsByDepId(String departId) {
        return this.baseMapper.getSubDepIdsByDepId(departId);
    }

    @Override
    public List<String> getMySubDepIdsByDepId(String departIds) {
        //根据部门id获取所负责部门
        String[] codeArr = this.getMyDeptParentOrgCode(departIds);
        return this.baseMapper.getSubDepIdsByOrgCodes(codeArr);
    }

    /**
     * <p>
     * 根据关键字搜索相关的部门数据
     * </p>
     */
    @Override
    public List<SysDepartTreeModel> searchBy(String keyWord, String myDeptSearch, String departIds) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDelFlag, CommonConst.NOT_DELETED.toString());
        List<SysDepartTreeModel> newList = new ArrayList<>();
        //myDeptSearch不为空时为我的部门搜索，只搜索所负责部门
        if (StrUtil.isNotEmpty(myDeptSearch)) {
            //departIds 为空普通用户或没有管理部门
            if (StrUtil.isEmpty(departIds)) {
                return newList;
            }
            //根据部门id获取所负责部门
            String[] codeArr = this.getMyDeptParentOrgCode(departIds);
            if (codeArr != null && codeArr.length > 0) {
                // fix 多部门下要有优先级
                query.nested(i -> {
                    for (String s : codeArr) {
                        i.or().likeRight(SysDepart::getOrgCode, s);
                    }
                });
            }
        }
        query.like(SysDepart::getDepartName, keyWord);
        //update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索回显优化--------------------
        SysDepartTreeModel model;
        List<SysDepart> departList = this.list(query);
        if (departList.size() > 0) {
            for (SysDepart depart : departList) {
                model = new SysDepartTreeModel(depart);
                model.setChildren(null);
                //update-end--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索功回显优化----------------------
                newList.add(model);
            }
            return newList;
        }
        return null;
    }

    /**
     * 根据部门id删除并且删除其可能存在的子级任何部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        this.checkChildrenExists(id, idList);
        //清空部门树内存
        //FindsDepartsChildrenUtil.clearDepartIdModel();
        boolean ok = this.removeByIds(idList);
        //根据部门id获取部门角色id
        List<String> roleIdList = new ArrayList<>();
        LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
        query.select(SysDepartRole::getId).in(SysDepartRole::getDepartId, idList);
        List<SysDepartRole> depRoleList = sysDepartRoleMapper.selectList(query);
        for (SysDepartRole deptRole : depRoleList) {
            roleIdList.add(deptRole.getId());
        }
        //根据部门id删除用户与部门关系
        userDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getDepId, idList));
        //根据部门id删除部门授权
        departPermissionMapper.delete(new LambdaQueryWrapper<SysDepartPermission>().in(SysDepartPermission::getDepartId, idList));
        //根据部门id删除部门角色
        sysDepartRoleMapper.delete(new LambdaQueryWrapper<SysDepartRole>().in(SysDepartRole::getDepartId, idList));
        if (roleIdList.size() > 0) {
            //根据角色id删除部门角色授权
            departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().in(SysDepartRolePermission::getRoleId, roleIdList));
            //根据角色id删除部门角色用户信息
            departRoleUserMapper.delete(new LambdaQueryWrapper<SysDepartRoleUser>().in(SysDepartRoleUser::getDroleId, roleIdList));
        }
        redisUtil.prefixDel(CacheConst.SYS_DEPARTS_CACHE);
        return ok;
    }

    /**
     * delete 方法调用
     */
    private void checkChildrenExists(String id, List<String> idList) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getParentId, id);
        List<SysDepart> departList = this.list(query);
        if (departList != null && departList.size() > 0) {
            for (SysDepart depart : departList) {
                idList.add(depart.getId());
                this.checkChildrenExists(depart.getId(), idList);
            }
        }
    }

    @Override
    public List<SysDepart> queryUserDeparts(String userId) {
        return baseMapper.queryUserDeparts(userId);
    }

    @Override
    public List<SysDepart> queryDepartsByUsername(String username) {
        return baseMapper.queryDepartsByUsername(username);
    }

    /**
     * 根据用户所负责部门ids获取父级部门编码
     */
    private String[] getMyDeptParentOrgCode(String departIds) {
        //根据部门id查询所负责部门
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDelFlag, CommonConst.NOT_DELETED.toString());
        query.in(SysDepart::getId, Arrays.asList(departIds.split(",")));
        query.orderByAsc(SysDepart::getOrgCode);
        List<SysDepart> list = this.list(query);
        //查找根部门
        if (list == null || list.size() == 0) {
            return null;
        }
        String orgCode = this.getMyDeptParentNode(list);
        return orgCode.split(",");
    }

    /**
     * 获取负责部门父节点
     */
    private String getMyDeptParentNode(List<SysDepart> list) {
        Map<String, String> map = new HashMap<>();
        //1.先将同一公司归类
        for (SysDepart dept : list) {
            String code = dept.getOrgCode().substring(0, 3);
            if (map.containsKey(code)) {
                String mapCode = map.get(code) + "," + dept.getOrgCode();
                map.put(code, mapCode);
            } else {
                map.put(code, dept.getOrgCode());
            }
        }
        StringBuilder parentOrgCode = new StringBuilder();
        //2.获取同一公司的根节点
        for (String str : map.values()) {
            String[] arrStr = str.split(",");
            parentOrgCode.append(",").append(this.getMinLengthNode(arrStr));
        }
        return parentOrgCode.substring(1);
    }

    /**
     * 获取同一公司中部门编码长度最小的部门
     */
    private String getMinLengthNode(String[] str) {
        int min = str[0].length();
        StringBuilder orgCode = new StringBuilder(str[0]);
        for (int i = 1; i < str.length; i++) {
            if (str[i].length() <= min) {
                min = str[i].length();
                orgCode.append(",").append(str[i]);
            }
        }
        return orgCode.toString();
    }

    /**
     * 获取部门树信息根据关键字
     */
    @Override
    public List<SysDepartTreeModel> queryTreeByKeyWord(String keyWord) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDelFlag, CommonConst.NOT_DELETED.toString());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeModel> listResult = SysDepartTreeModel.toTreeList(list);
        List<SysDepartTreeModel> treelist = new ArrayList<>();
        if (StrUtil.isNotBlank(keyWord)) {
            this.getTreeByKeyWord(keyWord, listResult, treelist);
        } else {
            return listResult;
        }
        return treelist;
    }

    @Override
    public SysDepart getParentDepartId(String departId) {
        return baseMapper.getParentDepartId(departId);
    }

    @Override
    public String queryDepartIdByOrgCode(String orgCode) {
        return baseMapper.queryDepartIdByOrgCode(orgCode);
    }

    @Override
    public List<String> getSubDepIdsByOrgCodes(String[] orgCodes) {
        return baseMapper.getSubDepIdsByOrgCodes(orgCodes);
    }

    /**
     * 根据关键字筛选部门信息
     */
    public void getTreeByKeyWord(String keyWord, List<SysDepartTreeModel> allResult, List<SysDepartTreeModel> newResult) {
        for (SysDepartTreeModel model : allResult) {
            if (model.getDepartName().contains(keyWord)) {
                newResult.add(model);
            } else if (model.getChildren() != null) {
                getTreeByKeyWord(keyWord, model.getChildren(), newResult);
            }
        }
    }
}
