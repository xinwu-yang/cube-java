package org.cube.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.mapper.SysDepartRoleMapper;
import org.cube.modules.system.mapper.SysDepartRoleUserMapper;
import org.cube.modules.system.service.ISysDepartRoleUserService;
import org.cube.modules.system.entity.SysDepartRole;
import org.cube.modules.system.entity.SysDepartRoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门角色人员信息
 *
 * @author jeecg-boot
 * @version V1.0
 * @since 2020-02-13
 */
@Service
public class SysDepartRoleUserServiceImpl extends ServiceImpl<SysDepartRoleUserMapper, SysDepartRoleUser> implements ISysDepartRoleUserService {

    @Autowired
    private SysDepartRoleMapper sysDepartRoleMapper;

    @Override
    public void deptRoleUserAdd(String userId, String newRoleId, String oldRoleId) {
        List<String> add = getDiff(oldRoleId, newRoleId);
        if (add != null && add.size() > 0) {
            List<SysDepartRoleUser> list = new ArrayList<>();
            for (String roleId : add) {
                if (StrUtil.isNotEmpty(roleId)) {
                    SysDepartRoleUser sysDepartRoleUser = new SysDepartRoleUser(userId, roleId);
                    list.add(sysDepartRoleUser);
                }
            }
            this.saveBatch(list);
        }
        List<String> remove = getDiff(newRoleId, oldRoleId);
        if (remove != null && remove.size() > 0) {
            for (String roleId : remove) {
                this.remove(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, userId).eq(SysDepartRoleUser::getDroleId, roleId));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDeptRoleUser(List<String> userIds, String depId) {
        for (String userId : userIds) {
            List<SysDepartRole> sysDepartRoleList = sysDepartRoleMapper.selectList(new QueryWrapper<SysDepartRole>().eq("depart_id", depId));
            List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
            if (roleIds.size() > 0) {
                QueryWrapper<SysDepartRoleUser> query = new QueryWrapper<>();
                query.eq("user_id", userId).in("drole_id", roleIds);
                this.remove(query);
            }
        }
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
}
