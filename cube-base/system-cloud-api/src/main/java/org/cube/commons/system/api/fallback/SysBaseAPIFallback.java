package org.cube.commons.system.api.fallback;

import com.fasterxml.jackson.databind.JsonNode;
import org.cube.commons.system.api.ISysBaseAPI;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.model.*;
import org.cube.modules.system.model.*;

import java.util.List;

/**
 * 进入fallback的方法 检查是否token未设置
 */
@Slf4j
public class SysBaseAPIFallback implements ISysBaseAPI {

    @Setter
    private Throwable cause;

    @Override
    public void sendSysAnnouncement(MessageDTO message) {
        log.error("发送消息失败", cause);
    }

    @Override
    public void sendBusAnnouncement(BusMessageDTO message) {
        log.error("发送消息失败", cause);
    }

    @Override
    public void sendTemplateAnnouncement(TemplateMessageDTO message) {
        log.error("发送消息失败", cause);
    }

    @Override
    public void sendBusTemplateAnnouncement(BusTemplateMessageDTO message) {
        log.error("发送消息失败", cause);
    }

    @Override
    public LoginUser getUserById(String id) {
        return null;
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return null;
    }

    @Override
    public List<String> getDepartIdsByUsername(String username) {
        return null;
    }

    @Override
    public List<String> getDepartNamesByUsername(String username) {
        return null;
    }

    @Override
    public List<DictModel> queryDictItemsByCode(String code) {
        return null;
    }

    @Override
    public void addLog(LogDTO logDTO) {
    }

    @Override
    public void addLog(String LogContent, Integer logType, Integer operateType) {
    }

    @Override
    public List<DictModel> queryAllDict() {
        return null;
    }

    @Override
    public List<SysCategoryModel> queryAllCategory() {
        return null;
    }

    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        return null;
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return null;
    }

    @Override
    public void updateSysAnnounReadFlag(String busType, String busId) {
    }

    @Override
    public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql) {
        return null;
    }

    @Override
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        log.error("queryTableDictByKeys查询失败", cause);
        return null;
    }

    @Override
    public List<ComboModel> queryAllUserBackCombo() {
        return null;
    }

    @Override
    public JsonNode queryAllUser(String userIds, Integer pageNo, int pageSize) {
        return null;
    }

    @Override
    public List<ComboModel> queryAllRole(String[] roleIds) {
        log.error("获取角色信息失败", cause);
        return null;
    }

    @Override
    public List<String> getRoleIdsByUsername(String username) {
        return null;
    }

    @Override
    public String getDepartIdsByOrgCode(String orgCode) {
        return null;
    }

    @Override
    public List<SysDepartModel> getAllSysDepart() {
        return null;
    }

    @Override
    public DictModel getParentDepartId(String departId) {
        return null;
    }

    @Override
    public List<String> getDeptHeadByDepId(String deptId) {
        return null;
    }

    @Override
    public void sendWebSocketMsg(String[] userIds, String cmd) {
    }

    @Override
    public List<LoginUser> queryAllUserByIds(String[] userIds) {
        return null;
    }

    @Override
    public List<LoginUser> queryUserByNames(String[] userNames) {
        return null;
    }

    @Override
    public SysDepartModel selectAllById(String id) {
        return null;
    }

    @Override
    public List<String> queryDeptUsersByUserId(String userId) {
        return null;
    }

    @Override
    public List<String> queryUserRoles(String username) {
        return null;
    }

    @Override
    public List<String> queryUserAuths(String username) {
        return null;
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceById(String dbSourceId) {
        return null;
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode) {
        return null;
    }

    @Override
    public LoginUser getUserByName(String username) {
        log.error("通过用户名获取当前登录用户信息", cause);
        return null;
    }

    @Override
    public String translateDictFromTable(String table, String text, String code, String key) {
        return null;
    }

    @Override
    public List<DictModel> translateDictFromTable(String table, String text, String code, List<String> keys) {
        return null;
    }

    @Override
    public String translateDictKeyFromTable(String table, String text, String code, String value) {
        return null;
    }

    @Override
    public String translateDict(String code, String key) {
        return null;
    }

    @Override
    public String translateDictKey(String code, String value) {
        return null;
    }

    @Override
    public List<SysPermissionDataRuleModel> queryPermissionDataRule(String component, String requestPath, String username) {
        return null;
    }

    @Override
    public JsonNode queryUsersByUsernames(String usernames) {
        return null;
    }

    @Override
    public JsonNode queryDepartsByOrgCodes(String orgCodes) {
        return null;
    }

    @Override
    public String addDataLog(DataLogDTO dataLog) {
        return null;
    }
}
