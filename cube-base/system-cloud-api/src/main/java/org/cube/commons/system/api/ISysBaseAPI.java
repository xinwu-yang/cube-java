package org.cube.commons.system.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.cube.commons.system.api.factory.SysBaseAPIFallbackFactory;
import org.cube.commons.api.CommonAPI;
import org.cube.commons.model.*;
import org.cube.modules.system.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 微服务API
 */
@Component
@FeignClient(contextId = "sysBaseRemoteApi", value = "cube-system", path = "/cube/sys/api", fallbackFactory = SysBaseAPIFallbackFactory.class)
public interface ISysBaseAPI extends CommonAPI {

    @Override
    @GetMapping("/translateDictListFromTable")
    List<DictModel> translateDictFromTable(@RequestParam String table, @RequestParam String text, @RequestParam String code, @RequestParam List<String> keys);

    @Override
    @GetMapping("/translateDictKeyFromTable")
    String translateDictKeyFromTable(@RequestParam String table, @RequestParam String text, @RequestParam String code, @RequestParam String value);

    @Override
    @GetMapping("/translateDictKey")
    String translateDictKey(@RequestParam String code, @RequestParam String text);

    @Override
    @PostMapping("/addLogByDTO")
    void addLog(@RequestBody LogDTO logDTO);

    @Override
    @PostMapping("/addLog")
    void addLog(@RequestParam String logContent, @RequestParam Integer logType, @RequestParam Integer operateType);

    /**
     * 发送系统消息
     *
     * @param message 使用构造器赋值参数 如果不设置category(消息类型)则默认为2 发送系统消息
     */
    @PostMapping("/sendSysAnnouncement")
    void sendSysAnnouncement(@RequestBody MessageDTO message);

    /**
     * 发送消息 附带业务参数
     *
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sendBusAnnouncement")
    void sendBusAnnouncement(@RequestBody BusMessageDTO message);

    /**
     * 通过模板发送消息
     *
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sendTemplateAnnouncement")
    void sendTemplateAnnouncement(@RequestBody TemplateMessageDTO message);

    /**
     * 通过模板发送消息 附带业务参数
     *
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sendBusTemplateAnnouncement")
    void sendBusTemplateAnnouncement(@RequestBody BusTemplateMessageDTO message);

    /**
     * 根据用户id查询用户信息
     */
    @GetMapping("/getUserById")
    LoginUser getUserById(@RequestParam("id") String id);

    /**
     * 通过用户账号查询角色集合
     */
    @GetMapping("/getRolesByUsername")
    List<String> getRolesByUsername(@RequestParam("username") String username);

    /**
     * 通过用户账号查询部门集合
     *
     * @return 部门 id
     */
    @GetMapping("/getDepartIdsByUsername")
    List<String> getDepartIdsByUsername(@RequestParam("username") String username);

    /**
     * 通过用户账号查询部门 name
     *
     * @return 部门 name
     */
    @GetMapping("/getDepartNamesByUsername")
    List<String> getDepartNamesByUsername(@RequestParam("username") String username);

    /**
     * 通过Code获取数据字典 +
     */
    @GetMapping("/queryDictItemsByCode")
    List<DictModel> queryDictItemsByCode(@RequestParam("code") String code);

    /**
     * 查询所有的父级字典
     *
     * @apiNote 按照create_time排序
     */
    @GetMapping("/queryAllDict")
    List<DictModel> queryAllDict();

    /**
     * 查询所有分类字典
     */
    @GetMapping("/queryAllCategory")
    List<SysCategoryModel> queryAllCategory();

    /**
     * 获取表数据字典 +
     */
    @GetMapping("/queryTableDictItemsByCode")
    List<DictModel> queryTableDictItemsByCode(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code);

    /**
     * 查询所有部门
     *
     * @apiNote 作为字典信息 id -->value,departName -->text
     */
    @GetMapping("/queryAllDepartBackDictModel")
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 根据业务类型及业务id更新消息已读状态
     */
    @GetMapping("/updateSysAnnounReadFlag")
    void updateSysAnnounReadFlag(@RequestParam("busType") String busType, @RequestParam("busId") String busId);

    /**
     * 查询表字典
     *
     * @apiNote 支持过滤数据
     */
    @GetMapping("/queryFilterTableDictInfo")
    List<DictModel> queryFilterTableDictInfo(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("filterSql") String filterSql);

    /**
     * 根据Keys查询表字典值 +
     */
    @GetMapping("/queryTableDictByKeys")
    List<String> queryTableDictByKeys(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("keyArray") String[] keyArray);

    /**
     * 查询所有用户
     */
    @GetMapping("/queryAllUserBackCombo")
    List<ComboModel> queryAllUserBackCombo();

    /**
     * 分页查询用户
     */
    @GetMapping("/queryAllUser")
    JsonNode queryAllUser(@RequestParam(name = "userIds", required = false) String userIds, @RequestParam(name = "pageNo", required = false) Integer pageNo, @RequestParam(name = "pageSize", required = false) int pageSize);

    /**
     * 获取所有角色
     *
     * @param roleIds 默认选中角色
     */
    @GetMapping("/queryAllRole")
    List<ComboModel> queryAllRole(@RequestParam(name = "roleIds", required = false) String[] roleIds);

    /**
     * 通过用户账号查询角色Id集合
     */
    @GetMapping("/getRoleIdsByUsername")
    List<String> getRoleIdsByUsername(@RequestParam("username") String username);

    /**
     * 通过部门编号查询部门id
     */
    @GetMapping("/getDepartIdsByOrgCode")
    String getDepartIdsByOrgCode(@RequestParam("orgCode") String orgCode);

    /**
     * 查询所有部门
     */
    @GetMapping("/getAllSysDepart")
    List<SysDepartModel> getAllSysDepart();

    /**
     * 查找父级部门
     */
    @GetMapping("/getParentDepartId")
    DictModel getParentDepartId(@RequestParam("departId") String departId);

    /**
     * 根据部门Id获取部门负责人
     */
    @GetMapping("/getDeptHeadByDepId")
    List<String> getDeptHeadByDepId(@RequestParam("deptId") String deptId);

    /**
     * 给指定用户发消息
     */
    @GetMapping("/sendWebSocketMsg")
    void sendWebSocketMsg(@RequestParam("userIds") String[] userIds, @RequestParam("cmd") String cmd);

    /**
     * 根据id获取所有参与用户
     */
    @GetMapping("/queryAllUserByIds")
    List<LoginUser> queryAllUserByIds(@RequestParam("userIds") String[] userIds);

    /**
     * 根据name获取所有参与用户
     */
    @GetMapping("/queryUserByNames")
    List<LoginUser> queryUserByNames(@RequestParam("userNames") String[] userNames);

    /**
     * 通过部门id获取部门全部信息
     */
    @GetMapping("/selectAllById")
    SysDepartModel selectAllById(@RequestParam("id") String id);

    /**
     * 根据用户id查询用户所属公司下所有用户ids
     */
    @GetMapping("/queryDeptUsersByUserId")
    List<String> queryDeptUsersByUserId(@RequestParam("userId") String userId);

    /**
     * 查询用户角色信息 +
     */
    @GetMapping("/queryUserRoles")
    List<String> queryUserRoles(@RequestParam("username") String username);

    /**
     * 查询用户权限信息 +
     */
    @GetMapping("/queryUserAuths")
    List<String> queryUserAuths(@RequestParam("username") String username);

    /**
     * 根据 id 查询数据库中存储的 DynamicDataSourceModel +
     */
    @GetMapping("/getDynamicDbSourceById")
    DynamicDataSourceModel getDynamicDbSourceById(@RequestParam("dbSourceId") String dbSourceId);

    /**
     * 根据 code 查询数据库中存储的 DynamicDataSourceModel +
     */
    @GetMapping("/getDynamicDbSourceByCode")
    DynamicDataSourceModel getDynamicDbSourceByCode(@RequestParam("dbSourceCode") String dbSourceCode);

    /**
     * 根据用户账号查询用户信息 +
     *
     * @param username 用户账号
     * @apiNote CommonAPI 中定义
     */
    @GetMapping("/getUserByName")
    LoginUser getUserByName(@RequestParam("username") String username);

    /**
     * 字典表翻译 +
     */
    @GetMapping("/translateDictFromTable")
    String translateDictFromTable(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("key") String key);

    /**
     * 普通字典的翻译 +
     */
    @GetMapping("/translateDict")
    String translateDict(@RequestParam("code") String code, @RequestParam("key") String key);

    /**
     * 查询数据权限 +
     */
    @GetMapping("/queryPermissionDataRule")
    List<SysPermissionDataRuleModel> queryPermissionDataRule(@RequestParam("component") String component, @RequestParam("requestPath") String requestPath, @RequestParam("username") String username);

    /**
     * 根据多个用户账号(逗号分隔)，查询返回多个用户信息
     */
    @GetMapping("/queryUsersByUsernames")
    JsonNode queryUsersByUsernames(String usernames);

    /**
     * 根据多个部门编码(逗号分隔)，查询返回多个部门信息
     */
    @GetMapping("/queryDepartsByOrgCodes")
    JsonNode queryDepartsByOrgCodes(String orgCodes);

    /**
     * 添加数据修改日志
     *
     * @param dataLog 数据日志
     */
    @PostMapping("/addDataLog")
    String addDataLog(@RequestBody DataLogDTO dataLog);
}
