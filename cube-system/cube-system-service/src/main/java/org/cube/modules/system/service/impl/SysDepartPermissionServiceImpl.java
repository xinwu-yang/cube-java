package org.cube.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.exception.CubeAppException;
import org.cube.modules.system.entity.*;
import org.cube.modules.system.mapper.*;
import org.cube.modules.system.model.TreeModel;
import org.cube.modules.system.model.api.response.LoadDataRuleResponse;
import org.cube.modules.system.model.api.response.TreeListForDeptRoleResponse;
import org.cube.modules.system.service.ISysDepartPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysDepartPermissionServiceImpl extends ServiceImpl<SysDepartPermissionMapper, SysDepartPermission> implements ISysDepartPermissionService {

    @Autowired
    private SysPermissionDataRuleMapper ruleMapper;
    @Autowired
    private SysDepartRoleMapper sysDepartRoleMapper;
    @Autowired
    private SysDepartRolePermissionMapper departRolePermissionMapper;
    @Autowired
    private SysPermissionDataRuleMapper sysPermissionDataRuleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDepartPermission(String departId, String permissionIds, String lastPermissionIds) {
        List<String> add = getDiff(lastPermissionIds, permissionIds);
        if (add != null && add.size() > 0) {
            List<SysDepartPermission> list = new ArrayList<>();
            for (String p : add) {
                if (StrUtil.isNotEmpty(p)) {
                    SysDepartPermission rolepms = new SysDepartPermission(departId, p);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }
        List<String> delete = getDiff(permissionIds, lastPermissionIds);
        if (delete != null && delete.size() > 0) {
            for (String permissionId : delete) {
                this.remove(new QueryWrapper<SysDepartPermission>().lambda().eq(SysDepartPermission::getDepartId, departId).eq(SysDepartPermission::getPermissionId, permissionId));
                //删除部门权限时，删除部门角色中已授权的权限
                List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(new LambdaQueryWrapper<SysDepartRole>().eq(SysDepartRole::getDepartId, departId));
                List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
                if (roleIds.size() > 0) {
                    departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().eq(SysDepartRolePermission::getPermissionId, permissionId));
                }
            }
        }
    }

    @Override
    public List<SysPermissionDataRule> getPermRuleListByDeptIdAndPermId(String departId, String permissionId) {
        SysDepartPermission departPermission = this.getOne(new QueryWrapper<SysDepartPermission>().lambda().eq(SysDepartPermission::getDepartId, departId).eq(SysDepartPermission::getPermissionId, permissionId));
        if (departPermission != null) {
            LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<>();
            query.in(SysPermissionDataRule::getId, Arrays.asList(departPermission.getDataRuleIds().split(",")));
            query.orderByDesc(SysPermissionDataRule::getCreateTime);
            return this.ruleMapper.selectList(query);
        } else {
            return null;
        }
    }

    @Override
    public LoadDataRuleResponse loadDataRule(String permissionId, String departId) {
        LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<>();
        query.eq(SysPermissionDataRule::getPermissionId, permissionId);
        query.orderByDesc(SysPermissionDataRule::getCreateTime);
        List<SysPermissionDataRule> dataRules = sysPermissionDataRuleMapper.selectList(query);
        if (dataRules == null || dataRules.size() == 0) {
            throw new CubeAppException("未找到菜单数据权限信息！");
        }
        LoadDataRuleResponse response = new LoadDataRuleResponse();
        LambdaQueryWrapper<SysDepartPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartPermission::getPermissionId, permissionId);
        wrapper.eq(SysDepartPermission::getDepartId, departId);
        SysDepartPermission sysDepartPermission = baseMapper.selectOne(wrapper);
        if (sysDepartPermission == null) {
            throw new CubeAppException("未找到部门菜单信息！");
        } else {
            String drChecked = sysDepartPermission.getDataRuleIds();
            if (StrUtil.isNotEmpty(drChecked)) {
                response.setDrChecked(drChecked.endsWith(",") ? drChecked.substring(0, drChecked.length() - 1) : drChecked);
            }
        }
        response.setDatarule(dataRules);
        return response;
    }

    @Override
    @Transactional
    public void saveDataRule(String permissionId, String departId, String dataRuleIds) {
        LambdaQueryWrapper<SysDepartPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysDepartPermission::getPermissionId, permissionId);
        query.eq(SysDepartPermission::getDepartId, departId);
        SysDepartPermission sysDepartPermission = baseMapper.selectOne(query);
        if (sysDepartPermission == null) {
            throw new CubeAppException("请先保存部门菜单权限！");
        }
        sysDepartPermission.setDataRuleIds(dataRuleIds);
        baseMapper.updateById(sysDepartPermission);
    }

    @Override
    public TreeListForDeptRoleResponse queryTreeListForDeptRole(String departId) {
        //全部权限ids
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPermission::getDelFlag, CommonConst.NOT_DELETED);
        query.orderByAsc(SysPermission::getSortNo);
        query.inSql(SysPermission::getId, "select permission_id from sys_depart_permission where depart_id = '" + departId + "'");
        List<SysPermission> list = sysPermissionMapper.selectList(query);
        List<String> ids = new ArrayList<>();
        for (SysPermission sysPer : list) {
            ids.add(sysPer.getId());
        }
        List<TreeModel> treeList = new ArrayList<>();
        getTreeModelList(treeList, list, null);
        TreeListForDeptRoleResponse response = new TreeListForDeptRoleResponse();
        response.setTreeList(treeList);
        response.setIds(ids);
        return response;
    }

    /**
     * 从diff中找出main中没有的元素
     */
    private List<String> getDiff(String main, String diff) {
        if (StrUtil.isEmpty(diff)) {
            return null;
        }
        if (StrUtil.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }
        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<>();
        for (String key : diffArr) {
            if (StrUtil.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }

    /**
     * 递归所有部门
     *
     * @param treeList 部门数
     * @param metaList 部门信息
     * @param temp     子部门
     */
    private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission.getId(), tempPid, permission.getName(), permission.getRuleFlag(), permission.isLeaf());
            if (temp == null && StrUtil.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.isLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.isLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }
        }
    }
}
