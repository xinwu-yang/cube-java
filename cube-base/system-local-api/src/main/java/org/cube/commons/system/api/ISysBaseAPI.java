package org.cube.commons.system.api;

import cn.hutool.json.JSONObject;
import org.cube.commons.api.CommonAPI;
import org.cube.commons.base.Result;
import org.cube.commons.model.BusMessageDTO;
import org.cube.commons.model.BusTemplateMessageDTO;
import org.cube.commons.model.MessageDTO;
import org.cube.commons.model.TemplateMessageDTO;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.model.*;

import java.util.List;

/**
 * 底层共通业务API，提供其他独立模块调用
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-09-09
 */
public interface ISysBaseAPI extends CommonAPI {

    /**
     * 发送系统消息
     *
     * @param message 使用构造器赋值参数 如果不设置category(消息类型)则默认为2 发送系统消息
     */
    void sendSysAnnouncement(MessageDTO message);

    /**
     * 发送消息 附带业务参数
     *
     * @param message 使用构造器赋值参数
     */
    void sendBusAnnouncement(BusMessageDTO message);

    /**
     * 通过模板发送消息
     *
     * @param message 使用构造器赋值参数
     */
    void sendTemplateAnnouncement(TemplateMessageDTO message);

    /**
     * 通过模板发送消息 附带业务参数
     *
     * @param message 使用构造器赋值参数
     */
    void sendBusTemplateAnnouncement(BusTemplateMessageDTO message);

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    LoginUser getUserById(String id);

    /**
     * 通过用户账号查询角色集合
     *
     * @param username 账号
     * @return 角色列表
     */
    List<String> getRolesByUsername(String username);

    /**
     * 通过用户账号查询部门id
     *
     * @param username 账号
     * @return 部门id列表
     */
    List<String> getDepartIdsByUsername(String username);

    /**
     * 通过用户账号查询部门名称
     *
     * @param username 账号
     * @return 部门名称列表
     */
    List<String> getDepartNamesByUsername(String username);

    /**
     * 查询所有的父级字典，按照create_time排序
     */
    List<DictModel> queryAllDict();

    /**
     * 查询所有分类字典
     */
    List<SysCategoryModel> queryAllCategory();

    /**
     * 查询所有部门
     *
     * @apiNote 作为字典信息 id --> value, departName --> text
     */
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 根据业务类型及业务id更新消息已读状态
     *
     * @param busType 业务类型
     * @param busId   业务id
     */
    void updateAnnouncementReadFlag(String busType, String busId);

    /**
     * 查询表字典 支持过滤数据
     *
     * @param table     表名
     * @param text      展示列
     * @param code      id列
     * @param filterSql 过滤条件
     * @return 字典列表
     */
    List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql);

    /**
     * 查询所有用户 返回ComboModel
     */
    List<ComboModel> queryAllUserBackCombo();

    /**
     * 分页查询用户 返回JSONObject
     */
    JSONObject queryAllUser(String userIds, Integer pageNo, Integer pageSize);

    /**
     * 获取所有角色 +
     */
    List<ComboModel> queryAllRole();

    /**
     * 获取所有角色 带参
     * roleIds 默认选中角色
     */
    List<ComboModel> queryAllRole(String[] roleIds);

    /**
     * 通过用户账号查询角色Id集合
     *
     * @param username 账号
     * @return 角色列表id
     */
    List<String> getRoleIdsByUsername(String username);

    /**
     * 通过部门编号查询部门id
     *
     * @param orgCode 部门编号
     * @return 部门id
     */
    String getDepartIdsByOrgCode(String orgCode);

    /**
     * 查询所有部门
     */
    List<SysDepartModel> getAllSysDepart();

    /**
     * 查找父级部门
     *
     * @param departId 部门id
     * @return 父级部门id
     */
    DictModel getParentDepartId(String departId);

    /**
     * 根据部门Id获取部门负责人
     *
     * @param deptId 部门id
     * @return 部门负责人
     */
    List<String> getDeptHeadByDepId(String deptId);

    /**
     * 给指定用户发消息
     *
     * @param userIds 用户ids
     * @param cmd     消息
     */
    void sendWebSocketMsg(String[] userIds, String cmd);

    /**
     * 根据id获取所有参与用户
     *
     * @param userIds 用户ids
     * @return 用户信息
     */
    List<LoginUser> queryAllUserByIds(String[] userIds);

    /**
     * 根据name获取所有参与用户
     *
     * @param userNames 用户名称
     */
    List<LoginUser> queryUserByNames(String[] userNames);

    /**
     * 通过部门id获取部门全部信息
     */
    SysDepartModel selectAllById(String id);

    /**
     * 根据用户id查询用户所属公司下所有用户ids
     *
     * @param userId 用户id
     * @return 所属公司下所有用户id
     */
    List<String> queryDeptUsersByUserId(String userId);

    /**
     * 根据多个用户账号(逗号分隔)，查询返回多个用户信息
     *
     * @param usernames 多个账号
     * @return 对应多个账号信息
     */
    JSONObject queryUsersByUsernames(String usernames);

    /**
     * 根据多个部门编码(逗号分隔)，查询返回多个部门信息
     *
     * @param orgCodes 部门编号
     * @return 部门编号信息
     */
    JSONObject queryDepartsByOrgCodes(String orgCodes);

    /**
     * 添加数据修改日志
     *
     * @param tableName   表名
     * @param dataId      数据id
     * @param dataContent 数据内容
     */
    void addDataLog(String tableName, String dataId, String dataContent);

    /**
     * 检查用户状态 +
     *
     * @param sysUser 用户
     * @return 是否正常
     */
    Result<?> checkUserState(SysUser sysUser);

    /**
     * 批量新增用户 +
     *
     * @param sysUsers 新增用户列表
     */
    void batchAddSysUser(List<SysUser> sysUsers);
}
