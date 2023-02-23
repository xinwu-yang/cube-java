package org.cube.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.mapper.SysPermissionDataRuleMapper;
import org.cube.modules.system.mapper.SysPermissionMapper;
import org.cube.modules.system.service.ISysPermissionDataRuleService;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.modules.system.entity.SysPermission;
import org.cube.modules.system.entity.SysPermissionDataRule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysPermissionDataRuleImpl extends ServiceImpl<SysPermissionDataRuleMapper, SysPermissionDataRule> implements ISysPermissionDataRuleService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 根据菜单id查询其对应的权限数据
     */
    @Override
    public List<SysPermissionDataRule> getPermRuleListByPermId(String permissionId) {
        LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<>();
        query.eq(SysPermissionDataRule::getPermissionId, permissionId);
        query.orderByDesc(SysPermissionDataRule::getCreateTime);
        return this.list(query);
    }

    /**
     * 根据前端传递的权限名称和权限值参数来查询权限数据
     */
    @Override
    public List<SysPermissionDataRule> queryPermissionRule(SysPermissionDataRule permRule) {
        QueryWrapper<SysPermissionDataRule> queryWrapper = QueryGenerator.initQueryWrapper(permRule, null);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysPermissionDataRule> queryPermissionDataRules(String username, String permissionId) {
        List<String> idsList = baseMapper.queryDataRuleIds(username, permissionId);
        //update-begin--Author:scott  Date:20191119  for：数据权限失效问题处理--------------------
        if (idsList == null || idsList.size() == 0) {
            return null;
        }
        //update-end--Author:scott  Date:20191119  for：数据权限失效问题处理--------------------
        Set<String> set = new HashSet<>();
        for (String ids : idsList) {
            if (StrUtil.isEmpty(ids)) {
                continue;
            }
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (StrUtil.isNotEmpty(id)) {
                    set.add(id);
                }
            }
        }
        if (set.size() == 0) {
            return null;
        }
        return baseMapper.selectList(new QueryWrapper<SysPermissionDataRule>().in("id", set).eq("status", CommonConst.STATUS_1));
    }

    @Override
    @Transactional
    public void savePermissionDataRule(SysPermissionDataRule sysPermissionDataRule) {
        this.save(sysPermissionDataRule);
        SysPermission permission = sysPermissionMapper.selectById(sysPermissionDataRule.getPermissionId());
        if (permission != null && (permission.getRuleFlag() == null || permission.getRuleFlag().equals(CommonConst.RULE_FLAG_0))) {
            permission.setRuleFlag(CommonConst.RULE_FLAG_1);
            sysPermissionMapper.updateById(permission);
        }
    }

    @Override
    @Transactional
    public void deletePermissionDataRule(String dataRuleId) {
        SysPermissionDataRule dataRule = baseMapper.selectById(dataRuleId);
        if (dataRule != null) {
            this.removeById(dataRuleId);
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<SysPermissionDataRule>().eq(SysPermissionDataRule::getPermissionId, dataRule.getPermissionId()));
            //注:同一个事务中删除后再查询是会认为数据已被删除的 若事务回滚上述删除无效
            if (count == null || count == 0) {
                SysPermission permission = sysPermissionMapper.selectById(dataRule.getPermissionId());
                if (permission != null && permission.getRuleFlag().equals(CommonConst.RULE_FLAG_1)) {
                    permission.setRuleFlag(CommonConst.RULE_FLAG_0);
                    sysPermissionMapper.updateById(permission);
                }
            }
        }
    }
}
