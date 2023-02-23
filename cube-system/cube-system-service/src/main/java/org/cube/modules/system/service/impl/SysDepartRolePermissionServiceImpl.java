package org.cube.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.SysDepartRolePermission;
import org.cube.modules.system.mapper.SysDepartRolePermissionMapper;
import org.cube.modules.system.service.ISysDepartRolePermissionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SysDepartRolePermissionServiceImpl extends ServiceImpl<SysDepartRolePermissionMapper, SysDepartRolePermission> implements ISysDepartRolePermissionService {

    @Override
    public void saveDeptRolePermission(String roleId, String permissionIds, String lastPermissionIds) {
        String ip;
        try {
            //获取request
            HttpServletRequest request = SystemContextUtil.getHttpServletRequest();
            //获取IP地址
            ip = HttpServletUtil.getIpAddr(request);
        } catch (Exception e) {
            ip = "127.0.0.1";
        }
        List<String> add = getDiff(lastPermissionIds, permissionIds);
        if (add != null && add.size() > 0) {
            List<SysDepartRolePermission> list = new ArrayList<>();
            for (String p : add) {
                if (StrUtil.isNotEmpty(p)) {
                    SysDepartRolePermission rolepms = new SysDepartRolePermission(roleId, p);
                    rolepms.setOperateDate(new Date());
                    rolepms.setOperateIp(ip);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }
        List<String> delete = getDiff(permissionIds, lastPermissionIds);
        if (delete != null && delete.size() > 0) {
            for (String permissionId : delete) {
                baseMapper.delete(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getRoleId, roleId).eq(SysDepartRolePermission::getPermissionId, permissionId));
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
