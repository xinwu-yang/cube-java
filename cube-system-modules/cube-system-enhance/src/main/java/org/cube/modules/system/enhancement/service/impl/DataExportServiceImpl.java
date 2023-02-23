package org.cube.modules.system.enhancement.service.impl;

import org.cube.plugin.sqlexport.SQLExporter;
import org.cube.modules.system.enhancement.mapper.CubeDictItemMapper;
import org.cube.modules.system.enhancement.service.IDataExportService;
import org.cube.modules.system.mapper.SysDictMapper;
import org.cube.modules.system.mapper.SysPermissionMapper;
import org.cube.modules.system.mapper.SysRoleMapper;
import org.cube.modules.system.mapper.SysUserMapper;
import org.cube.modules.system.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DataExportServiceImpl implements IDataExportService {

    @Autowired
    private CubeDictItemMapper cubeDictItemMapper;
    @Autowired
    private SQLExporter sqlExporter;
    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Override
    public List<String> exportInserts(List<String> permissionIds, List<String> userIds, List<String> roleIds, List<String> dictIds) {
        List<String> sqlData = new ArrayList<>();
        sqlData.add(sqlExporter.comment("导出时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        sqlData.add(sqlExporter.foreignKeyChecks(true));
        // 导出选中字典
        if (Objects.nonNull(dictIds) && dictIds.size() > 0) {
            sqlData.add(sqlExporter.comment("字典"));
            List<SysDict> dicts = sysDictMapper.selectBatchIds(dictIds);
            // 导出前先删除
            sqlData.addAll(sqlExporter.deletes(dicts));
            sqlData.addAll(sqlExporter.inserts(dicts));
            for (SysDict dict : dicts) {
                List<SysDictItem> items = cubeDictItemMapper.selectItemsByMainId(dict.getId());
                sqlData.addAll(sqlExporter.deletes(items));
                sqlData.addAll(sqlExporter.inserts(items));
            }
        }
        // 导出角色
        if (Objects.nonNull(roleIds) && roleIds.size() > 0) {
            sqlData.add(sqlExporter.comment("角色"));
            List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
            sqlData.addAll(sqlExporter.deletes(roles));
            sqlData.addAll(sqlExporter.inserts(roles));
        }
        // 导出用户
        if (Objects.nonNull(userIds) && userIds.size() > 0) {
            sqlData.add(sqlExporter.comment("用户"));
            List<SysUser> users = sysUserMapper.selectBatchIds(userIds);
            sqlData.addAll(sqlExporter.deletes(users));
            sqlData.addAll(sqlExporter.inserts(users));
        }
        // 导出菜单
        if (Objects.nonNull(permissionIds) && permissionIds.size() > 0) {
            sqlData.add(sqlExporter.comment("菜单"));
            List<SysPermission> permissions = sysPermissionMapper.selectBatchIds(permissionIds);
            sqlData.addAll(sqlExporter.deletes(permissions));
            sqlData.addAll(sqlExporter.inserts(permissions));
        }
        sqlData.add(sqlExporter.foreignKeyChecks(false));
        return sqlData;
    }
}
