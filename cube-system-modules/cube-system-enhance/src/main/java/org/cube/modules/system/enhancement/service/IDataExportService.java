package org.cube.modules.system.enhancement.service;

import java.util.List;

public interface IDataExportService {

    /**
     * 导出SQL
     *
     * @param permissionIds 菜单id
     * @param userIds       用户id
     * @param roleIds       角色id
     * @param dictIds       字典id
     */
    List<String> exportInserts(List<String> permissionIds, List<String> userIds, List<String> roleIds, List<String> dictIds);
}
