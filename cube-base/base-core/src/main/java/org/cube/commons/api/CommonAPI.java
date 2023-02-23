package org.cube.commons.api;

import org.cube.commons.model.LogDTO;
import org.cube.modules.system.model.DictModel;
import org.cube.modules.system.model.DynamicDataSourceModel;
import org.cube.modules.system.model.LoginUser;
import org.cube.modules.system.model.SysPermissionDataRuleModel;

import java.util.List;

/**
 * 通用底层接口
 *
 * @author 杨欣武
 * @version 2.0.0
 * @since 2021-01-01
 */
public interface CommonAPI {

    /**
     * 查询用户角色信息
     */
    List<String> queryUserRoles(String username);

    /**
     * 查询用户权限信息
     */
    List<String> queryUserAuths(String username);

    /**
     * 根据 id 查询数据库中存储的数据库配置
     */
    DynamicDataSourceModel getDynamicDbSourceById(String dbSourceId);

    /**
     * 根据 code 查询数据库中存储的数据库配置
     */
    DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode);

    /**
     * 根据用户账号查询用户信息
     */
    LoginUser getUserByName(String username);

    /**
     * 字典表的翻译
     */
    String translateDictFromTable(String table, String text, String code, String key);

    /**
     * 表字典的批量翻译
     */
    List<DictModel> translateDictFromTable(String table, String text, String code, List<String> keys);

    /**
     * 字典表的值翻译返回ID
     */
    String translateDictKeyFromTable(String table, String text, String code, String value);

    /**
     * 普通字典的翻译
     */
    String translateDict(String code, String key);

    /**
     * 普通字典映射回key
     */
    String translateDictKey(String code, String text);

    /**
     * 查询数据权限
     */
    List<SysPermissionDataRuleModel> queryPermissionDataRule(String component, String requestPath, String username);

    /**
     * 获取表数据字典
     */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    /**
     * 获取数据字典
     */
    List<DictModel> queryDictItemsByCode(String code);

    /**
     * 保存日志
     */
    void addLog(LogDTO logDTO);

    /**
     * 保存日志
     *
     * @param LogContent  日志内容
     * @param logType     日志类型
     * @param operateType 操作类型
     */
    void addLog(String LogContent, Integer logType, Integer operateType);
}