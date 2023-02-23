package org.cube.modules.system.controller;

import cn.hutool.json.JSONObject;
import org.cube.commons.system.api.ISysBaseAPI;
import org.cube.modules.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cube.commons.model.*;
import org.cube.modules.system.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微服务API
 *
 * @author 杨欣武
 * @apiNote 微服务化后系统胡模块对外的API
 */
@Tag(name = "微服务相关接口")
@RestController
@RequestMapping("/sys/api")
public class SystemAPIController {

    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 发送系统消息
     *
     * @param message 消息
     * @apiNote 使用构造器赋值参数，如果不设置category(消息类型)则默认为2[发送系统消息]
     */
    @PostMapping("/sendSysAnnouncement")
    public void sendSysAnnouncement(@RequestBody MessageDTO message) {
        sysBaseAPI.sendSysAnnouncement(message);
    }

    /**
     * 发送系统消息（附带业务参数）
     *
     * @param message 使用构造器赋值参数
     * @apiNote 使用构造器赋值参数，附带业务参数
     */
    @PostMapping("/sendBusAnnouncement")
    public void sendBusAnnouncement(@RequestBody BusMessageDTO message) {
        sysBaseAPI.sendBusAnnouncement(message);
    }

    /**
     * 通过模板发送消息
     *
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sendTemplateAnnouncement")
    public void sendTemplateAnnouncement(@RequestBody TemplateMessageDTO message) {
        sysBaseAPI.sendTemplateAnnouncement(message);
    }

    /**
     * 通过模板发送消息 附带业务参数
     *
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sendBusTemplateAnnouncement")
    public void sendBusTemplateAnnouncement(@RequestBody BusTemplateMessageDTO message) {
        sysBaseAPI.sendBusTemplateAnnouncement(message);
    }

    /**
     * 根据用户id查询用户信息
     */
    @GetMapping("/getUserById")
    public LoginUser getUserById(@RequestParam("id") String id) {
        return sysBaseAPI.getUserById(id);
    }

    /**
     * 通过用户账号查询角色集合
     */
    @GetMapping("/getRolesByUsername")
    public List<String> getRolesByUsername(@RequestParam("username") String username) {
        return sysBaseAPI.getRolesByUsername(username);
    }

    /**
     * 通过用户账号查询部门集合
     */
    @GetMapping("/getDepartIdsByUsername")
    public List<String> getDepartIdsByUsername(@RequestParam("username") String username) {
        return sysBaseAPI.getDepartIdsByUsername(username);
    }

    /**
     * 通过用户账号查询部门 name
     */
    @GetMapping("/getDepartNamesByUsername")
    public List<String> getDepartNamesByUsername(@RequestParam("username") String username) {
        return sysBaseAPI.getDepartNamesByUsername(username);
    }

    /**
     * 获取数据字典
     */
    @GetMapping("/queryDictItemsByCode")
    public List<DictModel> queryDictItemsByCode(@RequestParam("code") String code) {
        return sysBaseAPI.queryDictItemsByCode(code);
    }

    /**
     * 查询所有的父级字典
     *
     * @apiNote 按照create_time排序
     */
    @GetMapping("/queryAllDict")
    public List<DictModel> queryAllDict() {
        return sysBaseAPI.queryAllDict();
    }

    /**
     * 查询所有分类字典
     */
    @GetMapping("/queryAllCategory")
    public List<SysCategoryModel> queryAllCategory() {
        return sysBaseAPI.queryAllCategory();
    }

    /**
     * 获取表数据字典
     */
    @GetMapping("/queryTableDictItemsByCode")
    public List<DictModel> queryTableDictItemsByCode(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code) {
        return sysBaseAPI.queryTableDictItemsByCode(table, text, code);
    }

    /**
     * 查询所有部门
     *
     * @apiNote 作为字典信息 id -> value,departName -> text
     */
    @GetMapping("/queryAllDepartBackDictModel")
    public List<DictModel> queryAllDepartBackDictModel() {
        return sysBaseAPI.queryAllDepartBackDictModel();
    }

    /**
     * 查询表字典 支持过滤数据
     */
    @GetMapping("/queryFilterTableDictInfo")
    public List<DictModel> queryFilterTableDictInfo(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("filterSql") String filterSql) {
        return sysBaseAPI.queryFilterTableDictInfo(table, text, code, filterSql);
    }

    /**
     * 获取所有角色
     *
     * @param roleIds 默认选中角色
     */
    @GetMapping("/queryAllRole")
    public List<ComboModel> queryAllRole(@RequestParam(required = false) String[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return sysBaseAPI.queryAllRole();
        } else {
            return sysBaseAPI.queryAllRole(roleIds);
        }
    }

    /**
     * 通过用户账号查询角色Id集合
     */
    @GetMapping("/getRoleIdsByUsername")
    public List<String> getRoleIdsByUsername(@RequestParam String username) {
        return sysBaseAPI.getRoleIdsByUsername(username);
    }

    /**
     * 通过部门编号查询部门id
     */
    @GetMapping("/getDepartIdsByOrgCode")
    public String getDepartIdsByOrgCode(@RequestParam String orgCode) {
        return sysBaseAPI.getDepartIdsByOrgCode(orgCode);
    }

    /**
     * 查询所有部门
     */
    @GetMapping("/getAllSysDepart")
    public List<SysDepartModel> getAllSysDepart() {
        return sysBaseAPI.getAllSysDepart();
    }

    /**
     * 根据id查询数据库中存储的数据源
     */
    @GetMapping("/getDynamicDbSourceById")
    public DynamicDataSourceModel getDynamicDbSourceById(@RequestParam String dbSourceId) {
        return sysBaseAPI.getDynamicDbSourceById(dbSourceId);
    }

    /**
     * 根据部门id获取部门负责人
     */
    @GetMapping("/getDeptHeadByDepId")
    public List<String> getDeptHeadByDepId(@RequestParam String deptId) {
        return sysBaseAPI.getDeptHeadByDepId(deptId);
    }

    /**
     * 查找父级部门
     */
    @GetMapping("/getParentDepartId")
    public DictModel getParentDepartId(@RequestParam("departId") String departId) {
        return sysBaseAPI.getParentDepartId(departId);
    }

    /**
     * 根据code查询数据库中存储的数据源
     */
    @GetMapping("/getDynamicDbSourceByCode")
    public DynamicDataSourceModel getDynamicDbSourceByCode(@RequestParam("dbSourceCode") String dbSourceCode) {
        return sysBaseAPI.getDynamicDbSourceByCode(dbSourceCode);
    }

    /**
     * 给指定用户发消息
     */
    @GetMapping("/sendWebSocketMsg")
    public void sendWebSocketMsg(String[] userIds, String cmd) {
        sysBaseAPI.sendWebSocketMsg(userIds, cmd);
    }

    /**
     * 根据id获取所有参与用户
     */
    @GetMapping("/queryAllUserByIds")
    public List<LoginUser> queryAllUserByIds(@RequestParam String[] userIds) {
        return sysBaseAPI.queryAllUserByIds(userIds);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/queryAllUserBackCombo")
    public List<ComboModel> queryAllUserBackCombo() {
        return sysBaseAPI.queryAllUserBackCombo();
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/queryAllUser")
    public JSONObject queryAllUser(@RequestParam(required = false) String userIds, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) int pageSize) {
        return sysBaseAPI.queryAllUser(userIds, pageNo, pageSize);
    }

    /**
     * 根据name获取所有参与用户
     */
    @GetMapping("/queryUserByNames")
    public List<LoginUser> queryUserByNames(@RequestParam String[] userNames) {
        return sysBaseAPI.queryUserByNames(userNames);
    }

    /**
     * 查询用户角色信息
     */
    @GetMapping("/queryUserRoles")
    public List<String> queryUserRoles(@RequestParam String username) {
        return sysBaseAPI.queryUserRoles(username);
    }

    /**
     * 查询用户权限信息
     */
    @GetMapping("/queryUserAuths")
    public List<String> queryUserAuths(@RequestParam String username) {
        return sysBaseAPI.queryUserAuths(username);
    }

    /**
     * 通过部门id获取部门全部信息
     */
    @GetMapping("/selectAllById")
    public SysDepartModel selectAllById(@RequestParam String id) {
        return sysBaseAPI.selectAllById(id);
    }

    /**
     * 根据用户id查询用户所属公司下所有用户ids
     */
    @GetMapping("/queryDeptUsersByUserId")
    public List<String> queryDeptUsersByUserId(@RequestParam String userId) {
        return sysBaseAPI.queryDeptUsersByUserId(userId);
    }

    /**
     * 查询数据权限
     */
    @GetMapping("/queryPermissionDataRule")
    public List<SysPermissionDataRuleModel> queryPermissionDataRule(@RequestParam String component, @RequestParam String requestPath, @RequestParam String username) {
        return sysBaseAPI.queryPermissionDataRule(component, requestPath, username);
    }

    /**
     * 字典表翻译
     */
    @GetMapping("/translateDictFromTable")
    public String translateDictFromTable(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("key") String key) {
        return sysBaseAPI.translateDictFromTable(table, text, code, key);
    }

    /**
     * 字典表多键值翻译
     */
    @GetMapping("/translateDictListFromTable")
    public List<DictModel> translateDictListFromTable(@RequestParam String table, @RequestParam String text, @RequestParam String code, @RequestParam List<String> keys) {
        return sysBaseAPI.translateDictFromTable(table, text, code, keys);
    }

    /**
     * 字典表键值翻译回键
     */
    @GetMapping("/translateDictKeyFromTable")
    public String translateDictKeyFromTable(@RequestParam("table") String table, @RequestParam String text, @RequestParam String code, @RequestParam String value) {
        return sysBaseAPI.translateDictKeyFromTable(table, text, code, value);
    }

    /**
     * 普通字典的翻译
     */
    @GetMapping("/translateDict")
    public String translateDict(@RequestParam("code") String code, @RequestParam("key") String key) {
        return sysBaseAPI.translateDict(code, key);
    }

    /**
     * 数据字典键值翻译
     */
    @GetMapping("/translateDictKey")
    public String translateDictKey(@RequestParam String code, @RequestParam String text) {
        return sysBaseAPI.translateDictKey(code, text);
    }

    /**
     * 添加操作日志
     *
     * @param logDTO 操作日志
     */
    @PostMapping("/addLogByDTO")
    public void addLog(@RequestBody LogDTO logDTO) {
        sysBaseAPI.addLog(logDTO);
    }

    /**
     * 添加操作日志
     *
     * @param logContent  日志内容
     * @param logType     日志类型
     * @param operateType 操作类型
     */
    @PostMapping("/addLog")
    public void addLog(@RequestParam String logContent, @RequestParam Integer logType, @RequestParam Integer operateType) {
        sysBaseAPI.addLog(logContent, logType, operateType);
    }

    /**
     * 根据多个用户账号
     *
     * @apiNote 以逗号分隔的方式，查询返回多个用户信息
     */
    @GetMapping("/queryUsersByUsernames")
    public JSONObject queryUsersByUsernames(String usernames) {
        return this.sysBaseAPI.queryUsersByUsernames(usernames);
    }

    /**
     * 根据多个部门编码
     *
     * @apiNote 以逗号分隔的方式，返回多个部门信息
     */
    @GetMapping("/queryDepartsByOrgCodes")
    public JSONObject queryDepartsByOrgCodes(String orgCodes) {
        return this.sysBaseAPI.queryDepartsByOrgCodes(orgCodes);
    }

    /**
     * 添加数据日志
     *
     * @param dataLog 数据日志内容
     */
    @PostMapping("/addDataLog")
    public void addDataLog(@RequestBody DataLogDTO dataLog) {
        this.sysBaseAPI.addDataLog(dataLog.getTableName(), dataLog.getDataId(), dataLog.getDataContent());
    }

    /**
     * 根据业务类型busType及业务busId修改消息已读
     */
    @GetMapping("/updateAnnouncementReadFlag")
    public void updateAnnouncementReadFlag(@RequestParam String busType, @RequestParam String busId) {
        sysBaseAPI.updateAnnouncementReadFlag(busType, busId);
    }

    /**
     * 根据用户账号查询用户信息
     */
    @GetMapping("/getUserByName")
    public LoginUser getUserByName(@RequestParam String username) {
        return sysBaseAPI.getUserByName(username);
    }
}
