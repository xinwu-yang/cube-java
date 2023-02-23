package org.cube.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.base.Result;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.constant.DataBaseConst;
import org.cube.commons.constant.WebSocketConst;
import org.cube.commons.enums.AnnouncementType;
import org.cube.commons.exception.CubeAppException;
import org.cube.commons.model.*;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.system.api.ISysBaseAPI;
import org.cube.commons.utils.EntityConvertUtil;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.YouBianCodeUtil;
import org.cube.commons.utils.crypto.SecurityUtil;
import org.cube.commons.utils.db.DynamicDBUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.*;
import org.cube.modules.system.extra.ws.AppWebSocketHandler;
import org.cube.modules.system.mapper.*;
import org.cube.modules.system.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 底层共通业务API，提供其他独立模块调用
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2019-4-20
 */
@Slf4j
@Service
@Transactional
public class SysBaseApiImpl implements ISysBaseAPI {

    // 当前系统数据库类型
    private static String DB_TYPE = "";

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysDepartMapper sysDepartMapper;
    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysAnnouncementMapper sysAnnouncementMapper;
    @Autowired
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysCategoryMapper sysCategoryMapper;
    @Autowired
    private SysDataSourceMapper sysDataSourceMapper;
    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysPermissionDataRuleMapper sysPermissionDataRuleMapper;
    @Autowired
    private SysDataLogMapper sysDataLogMapper;
    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private AppWebSocketHandler appWebSocketHandler;

    @Override
    @Cacheable(CacheConst.SYS_USERS_CACHE)
    public LoginUser getUserByName(String username) {
        log.info("无缓存LoginUser[{}]的时候调用这里！", username);
        if (StrUtil.isEmpty(username)) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = sysUserMapper.getUserByName(username);
        if (sysUser == null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        // 查询用户角色信息
        List<String> roles = sysUserRoleMapper.getRoleIdByUserName(username);
        loginUser.setRoleIds(roles);
        // 查询用户部门信息
        List<String> orgCodes = sysDepartMapper.queryDepartOrgCodesByUsername(username);
        loginUser.setOrgCodes(ArrayUtil.join(orgCodes.toArray(), ","));
        return loginUser;
    }

    @Override
    public String translateDictFromTable(String table, String text, String code, String key) {
        List<DictModel> dictModels = sysDictMapper.queryTableDictByKeys(table, text, code, ListUtil.of(key));
        if (dictModels.size() > 0) {
            return dictModels.get(0).getText();
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = CacheConst.SYS_DICT_TABLE_CACHE)
    public List<DictModel> translateDictFromTable(String table, String text, String code, List<String> keys) {
        return sysDictMapper.queryTableDictByKeys(table, text, code, keys);
    }

    @Override
    public String translateDictKeyFromTable(String table, String text, String code, String value) {
        return sysDictMapper.queryTableDictByText(table, text, code, value);
    }

    @Override
    @Cacheable(cacheNames = CacheConst.SYS_DICT_CACHE)
    public String translateDict(String code, String key) {
        return sysDictMapper.queryDictTextByKey(code, key);
    }

    @Override
    public String translateDictKey(String code, String text) {
        return sysDictMapper.queryDictKeyByText(code, text);
    }

    @Override
    @Cacheable(cacheNames = CacheConst.SYS_DATA_PERMISSIONS_CACHE)
    public List<SysPermissionDataRuleModel> queryPermissionDataRule(String component, String requestPath, String username) {
        List<SysPermission> currentSysPermission;
        if (StrUtil.isNotEmpty(component)) {
            //1.通过注解属性pageComponent 获取菜单
            LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
            query.eq(SysPermission::getDelFlag, 0);
            query.eq(SysPermission::getComponent, component);
            currentSysPermission = sysPermissionMapper.selectList(query);
        } else {
            //1.直接通过前端请求地址查询菜单
            LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
            query.eq(SysPermission::getMenuType, 2);
            query.eq(SysPermission::getDelFlag, 0);
            query.eq(SysPermission::getUrl, requestPath);
            currentSysPermission = sysPermissionMapper.selectList(query);
            //2.未找到 再通过正则匹配获取菜单
            if (currentSysPermission == null || currentSysPermission.size() == 0) {
                //通过正则匹配权限配置
                String regUrl = getRegexpUrl(requestPath);
                if (regUrl != null) {
                    LambdaQueryWrapper<SysPermission> regexpQuery = new LambdaQueryWrapper<>();
                    regexpQuery.eq(SysPermission::getMenuType, 2);
                    regexpQuery.eq(SysPermission::getDelFlag, 0);
                    currentSysPermission = sysPermissionMapper.selectList(regexpQuery);
                }
            }
        }
        if (currentSysPermission == null || currentSysPermission.size() == 0) {
            return null;
        }
        List<SysPermissionDataRuleModel> dataRules = new ArrayList<>();
        for (SysPermission sysPermission : currentSysPermission) {
            List<String> idsList = sysPermissionDataRuleMapper.queryDataRuleIds(username, sysPermission.getId());
            if (idsList == null || idsList.size() == 0) {
                return null;
            }
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
            List<SysPermissionDataRule> temp = null;
            QueryWrapper<SysPermissionDataRule> wrapper = new QueryWrapper<>();
            wrapper.in("id", set);
            wrapper.eq("status", CommonConst.STATUS_1);
            if (set.size() > 0) {
                temp = sysPermissionDataRuleMapper.selectList(wrapper);
            }
            if (temp != null && temp.size() > 0) {
                dataRules = EntityConvertUtil.entityListToModelList(temp, SysPermissionDataRuleModel.class);
            }
        }
        return dataRules;
    }

    /**
     * 匹配前端传过来的地址 匹配成功返回正则地址
     * AntPathMatcher匹配地址
     * ()* 匹配0个或多个字符
     * ()**匹配0个或多个目录
     */
    private String getRegexpUrl(String url) {
        List<String> list = sysPermissionMapper.queryPermissionUrlWithStar();
        if (list != null && list.size() > 0) {
            for (String p : list) {
                PathMatcher matcher = new AntPathMatcher();
                if (matcher.match(p, url)) {
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public LoginUser getUserById(String id) {
        if (StrUtil.isEmpty(id)) {
            return null;
        }
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    @Override
    public List<String> getDepartIdsByUsername(String username) {
        List<SysDepart> list = sysDepartMapper.queryDepartsByUsername(username);
        List<String> result = new ArrayList<>(list.size());
        for (SysDepart depart : list) {
            result.add(depart.getId());
        }
        return result;
    }

    @Override
    public List<String> getDepartNamesByUsername(String username) {
        List<SysDepart> list = sysDepartMapper.queryDepartsByUsername(username);
        List<String> result = new ArrayList<>(list.size());
        for (SysDepart depart : list) {
            result.add(depart.getDepartName());
        }
        return result;
    }

    @Override
    public DictModel getParentDepartId(String departId) {
        SysDepart depart = sysDepartMapper.getParentDepartId(departId);
        return new DictModel(depart.getId(), depart.getParentId());
    }

    @Override
    @Cacheable(cacheNames = CacheConst.SYS_DICT_CACHE, key = "#code")
    public List<DictModel> queryDictItemsByCode(String code) {
        return sysDictMapper.queryDictItemsByCode(code);
    }

    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        //update-begin-author:taoyan date:20200820 for:【Online+系统】字典表加权限控制机制逻辑，想法不错 LOWCOD-799
        if (table.contains("#{")) {
            table = QueryGenerator.getSqlRuleValue(table);
        }
        //update-end-author:taoyan date:20200820 for:【Online+系统】字典表加权限控制机制逻辑，想法不错 LOWCOD-799
        return sysDictMapper.queryTableDictItemsByCodeAndFilter(table, text, code, null);
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return sysDictMapper.queryAllDepartBackDictModel();
    }

    @Override
    public void sendSysAnnouncement(MessageDTO message) {
        this.sendSysAnnouncement(message.getFromUser(), message.getToUser(), message.getTitle(), message.getContent(), message.getCategory());
    }

    @Override
    public void sendBusAnnouncement(BusMessageDTO message) {
        sendBusAnnouncement(message.getFromUser(), message.getToUser(), message.getTitle(), message.getContent(), message.getCategory(), message.getBusType(), message.getBusId());
    }

    @Override
    public void sendTemplateAnnouncement(TemplateMessageDTO message) {
        String title = message.getTitle();
        String fromUser = message.getFromUser();
        String toUser = message.getToUser();
        String content = message.getContent();
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitle(title);
        announcement.setMsgContent(content);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConst.PRIORITY_M);
        announcement.setMsgType(CommonConst.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConst.HAS_SEND);
        announcement.setMsgCategory(CommonConst.MSG_CATEGORY_SYSTEM);
        announcement.setDelFlag(CommonConst.NOT_DELETED);
        announcement.setSendTime(new Date());
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String[] userIds = toUser.split(",");
        for (String userId : userIds) {
            if (StrUtil.isNotEmpty(userId)) {
                SysUser sysUser = sysUserMapper.getUserByName(userId);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(announcement.getId());
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConst.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject root = JSONUtil.createObj();
                root.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_USER);
                root.set(WebSocketConst.MSG_USER_ID, sysUser.getId());
                root.set(WebSocketConst.MSG_ID, announcement.getId());
                root.set(WebSocketConst.MSG_TXT, announcement.getTitle());
                appWebSocketHandler.sendOneMessage(sysUser.getId(), root.toString());
            }
        }
    }

    @Override
    public void sendBusTemplateAnnouncement(BusTemplateMessageDTO message) {
        String title = message.getTitle();
        String fromUser = message.getFromUser();
        String toUser = message.getToUser();
        String busId = message.getBusId();
        String busType = message.getBusType();
        String content = message.getContent();

        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitle(title);
        announcement.setMsgContent(content);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConst.PRIORITY_M);
        announcement.setMsgType(CommonConst.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConst.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(CommonConst.MSG_CATEGORY_SYSTEM);
        announcement.setDelFlag(CommonConst.NOT_DELETED);
        announcement.setBusId(busId);
        announcement.setBusType(busType);
        announcement.setOpenType(Objects.requireNonNull(AnnouncementType.getByType(busType)).getOpenType());
        announcement.setOpenPage(Objects.requireNonNull(AnnouncementType.getByType(busType)).getOpenPage());
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String[] userIds = toUser.split(",");
        for (String userId : userIds) {
            if (StrUtil.isNotEmpty(userId)) {
                SysUser sysUser = sysUserMapper.getUserByName(userId);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(announcement.getId());
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConst.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = JSONUtil.createObj();
                obj.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_USER);
                obj.set(WebSocketConst.MSG_USER_ID, sysUser.getId());
                obj.set(WebSocketConst.MSG_ID, announcement.getId());
                obj.set(WebSocketConst.MSG_TXT, announcement.getTitle());
                appWebSocketHandler.sendOneMessage(sysUser.getId(), obj.toString());
            }
        }
    }

    @Override
    public void updateAnnouncementReadFlag(String busType, String busId) {
        SysAnnouncement announcement = sysAnnouncementMapper.selectOne(new QueryWrapper<SysAnnouncement>().eq("bus_type", busType).eq("bus_id", busId));
        LoginUser sysUser = SystemContextUtil.currentLoginUser();
        if (announcement != null && sysUser != null) {
            String userId = sysUser.getId();
            UpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("read_flag", CommonConst.HAS_READ_FLAG);
            updateWrapper.set("read_time", new Date());
            updateWrapper.last("where annt_id ='" + announcement.getId() + "' and user_id ='" + userId + "'");
            sysAnnouncementSendMapper.update(new SysAnnouncementSend(), updateWrapper);
        }
    }

    /**
     * 获取数据库类型
     */
    private String getDatabaseTypeByDataSource(DataSource dataSource) {
        if ("".equals(DB_TYPE)) {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toLowerCase();
                if (dbType.contains("mysql")) {
                    DB_TYPE = DataBaseConst.DB_TYPE_MYSQL;
                } else if (dbType.contains("oracle")) {
                    DB_TYPE = DataBaseConst.DB_TYPE_ORACLE;
                } else if (dbType.contains("sqlserver") || dbType.contains("sql server")) {
                    DB_TYPE = DataBaseConst.DB_TYPE_SQLSERVER;
                } else if (dbType.contains("postgresql")) {
                    DB_TYPE = DataBaseConst.DB_TYPE_POSTGRESQL;
                } else {
                    throw new CubeAppException("数据库类型:[" + dbType + "]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return DB_TYPE;
    }

    @Override
    public List<DictModel> queryAllDict() {
        // 查询并排序
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("create_time");
        List<SysDict> sysDictList = sysDictMapper.selectList(queryWrapper);
        // 封装成 model
        List<DictModel> list = new ArrayList<>();
        for (SysDict dict : sysDictList) {
            list.add(new DictModel(dict.getDictCode(), dict.getDictName()));
        }
        return list;
    }

    @Override
    public List<SysCategoryModel> queryAllCategory() {
        List<SysCategory> ls = sysCategoryMapper.selectList(null);
        return EntityConvertUtil.entityListToModelList(ls, SysCategoryModel.class);
    }

    @Override
    public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql) {
        return sysDictMapper.queryTableDictItemsByCodeAndFilter(table, text, code, filterSql);
    }

    @Override
    public List<ComboModel> queryAllUserBackCombo() {
        List<ComboModel> list = new ArrayList<>();
        List<SysUser> userList = sysUserMapper.selectList(new QueryWrapper<SysUser>().eq("status", 1).eq("del_flag", 0));
        for (SysUser user : userList) {
            ComboModel model = new ComboModel();
            model.setTitle(user.getRealname());
            model.setId(user.getId());
            model.setUsername(user.getUsername());
            list.add(model);
        }
        return list;
    }

    @Override
    public JSONObject queryAllUser(String userIds, Integer pageNo, Integer pageSize) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status", 1).eq("del_flag", 0);
        List<ComboModel> list = new ArrayList<>();
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        IPage<SysUser> pageList = sysUserMapper.selectPage(page, queryWrapper);
        for (SysUser user : pageList.getRecords()) {
            ComboModel model = new ComboModel();
            model.setUsername(user.getUsername());
            model.setTitle(user.getRealname());
            model.setId(user.getId());
            model.setEmail(user.getEmail());
            if (StrUtil.isNotEmpty(userIds)) {
                String[] temp = userIds.split(",");
                for (String s : temp) {
                    if (s.equals(user.getId())) {
                        model.setChecked(true);
                    }
                }
            }
            list.add(model);
        }
        JSONObject json = JSONUtil.createObj();
        json.set("list", list);
        json.set("total", pageList.getTotal());
        return json;
    }

    @Override
    public List<ComboModel> queryAllRole() {
        List<ComboModel> list = new ArrayList<>();
        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<>());
        for (SysRole role : roleList) {
            ComboModel model = new ComboModel();
            model.setTitle(role.getRoleName());
            model.setId(role.getId());
            list.add(model);
        }
        return list;
    }

    @Override
    public List<ComboModel> queryAllRole(String[] roleIds) {
        List<ComboModel> list = new ArrayList<>();
        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<>());
        for (SysRole role : roleList) {
            ComboModel model = new ComboModel();
            model.setTitle(role.getRoleName());
            model.setId(role.getId());
            model.setRoleCode(role.getRoleCode());
            if (roleIds != null) {
                for (String roleId : roleIds) {
                    if (roleId.equals(role.getId())) {
                        model.setChecked(true);
                    }
                }
            }
            list.add(model);
        }
        return list;
    }

    @Override
    public List<String> getRoleIdsByUsername(String username) {
        return sysUserRoleMapper.getRoleIdByUserName(username);
    }

    @Override
    public String getDepartIdsByOrgCode(String orgCode) {
        return sysDepartMapper.queryDepartIdByOrgCode(orgCode);
    }

    @Override
    public List<SysDepartModel> getAllSysDepart() {
        List<SysDepartModel> departModelList = new ArrayList<>();
        List<SysDepart> departList = sysDepartMapper.selectList(new QueryWrapper<SysDepart>().eq("del_flag", "0"));
        for (SysDepart depart : departList) {
            SysDepartModel model = new SysDepartModel();
            BeanUtils.copyProperties(depart, model);
            departModelList.add(model);
        }
        return departModelList;
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceById(String dbSourceId) {
        SysDataSource dbSource = sysDataSourceMapper.selectById(dbSourceId);
        if (dbSource != null && StrUtil.isNotBlank(dbSource.getDbPassword())) {
            String dbPassword = dbSource.getDbPassword();
            String decodedStr = SecurityUtil.decrypt(dbPassword);
            dbSource.setDbPassword(decodedStr);
        }
        return new DynamicDataSourceModel(dbSource);
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode) {
        SysDataSource dbSource = sysDataSourceMapper.selectOne(new LambdaQueryWrapper<SysDataSource>().eq(SysDataSource::getCode, dbSourceCode));
        if (dbSource != null && StrUtil.isNotBlank(dbSource.getDbPassword())) {
            String dbPassword = dbSource.getDbPassword();
            String decodedStr = SecurityUtil.decrypt(dbPassword);
            dbSource.setDbPassword(decodedStr);
        }
        return new DynamicDataSourceModel(dbSource);
    }

    @Override
    public List<String> getDeptHeadByDepId(String deptId) {
        List<SysUser> userList = sysUserMapper.selectList(new QueryWrapper<SysUser>().like("depart_ids", deptId).eq("status", 1).eq("del_flag", 0));
        List<String> list = new ArrayList<>();
        for (SysUser user : userList) {
            list.add(user.getUsername());
        }
        return list;
    }

    @Override
    public void sendWebSocketMsg(String[] userIds, String cmd) {
        JSONObject obj = JSONUtil.createObj();
        obj.set(WebSocketConst.MSG_CMD, cmd);
        appWebSocketHandler.sendMoreMessage(userIds, obj.toString());
    }

    @Override
    public List<LoginUser> queryAllUserByIds(String[] userIds) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("del_flag", 0);
        queryWrapper.in("id", ListUtil.toList(userIds));
        List<LoginUser> loginUsers = new ArrayList<>();
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        for (SysUser user : sysUsers) {
            LoginUser loginUser = new LoginUser();
            BeanUtils.copyProperties(user, loginUser);
            loginUsers.add(loginUser);
        }
        return loginUsers;
    }

    @Override
    public List<LoginUser> queryUserByNames(String[] userNames) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status", 1).eq("del_flag", 0);
        queryWrapper.in("username", ListUtil.toList(userNames));
        List<LoginUser> loginUsers = new ArrayList<>();
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        for (SysUser user : sysUsers) {
            LoginUser loginUser = new LoginUser();
            BeanUtils.copyProperties(user, loginUser);
            loginUsers.add(loginUser);
        }
        return loginUsers;
    }

    @Override
    public SysDepartModel selectAllById(String id) {
        SysDepart sysDepart = sysDepartMapper.selectById(id);
        SysDepartModel sysDepartModel = new SysDepartModel();
        BeanUtils.copyProperties(sysDepart, sysDepartModel);
        return sysDepartModel;
    }

    @Override
    public List<String> queryDeptUsersByUserId(String userId) {
        List<String> userIds = new ArrayList<>();
        List<SysUserDepart> userDepartList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().eq("user_id", userId));
        if (userDepartList != null) {
            //查找所属公司
            StringBuilder orgCodes = new StringBuilder();
            for (SysUserDepart userDepart : userDepartList) {
                //查询所属公司编码
                SysDepart depart = sysDepartMapper.selectById(userDepart.getDepId());
                int length = YouBianCodeUtil.zhanweiLength;
                String compyOrgCode;
                if (depart != null && depart.getOrgCode() != null) {
                    compyOrgCode = depart.getOrgCode().substring(0, length);
                    if (!orgCodes.toString().contains(compyOrgCode)) {
                        orgCodes.append(",").append(compyOrgCode);
                    }
                }
            }
            if (StrUtil.isNotEmpty(orgCodes.toString())) {
                orgCodes = new StringBuilder(orgCodes.substring(1));
                List<String> listIds = sysDepartMapper.getSubDepIdsByOrgCodes(orgCodes.toString().split(","));
                List<SysUserDepart> userList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().in("dep_id", listIds));
                for (SysUserDepart userDepart : userList) {
                    if (!userIds.contains(userDepart.getUserId())) {
                        userIds.add(userDepart.getUserId());
                    }
                }
            }
        }
        return userIds;
    }

    /**
     * 查询用户拥有的角色集合 common api 里面的接口实现
     */
    @Override
    public List<String> queryUserRoles(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    /**
     * 查询用户拥有的权限集合 common api 里面的接口实现
     */
    @Override
    public List<String> queryUserAuths(String username) {
        List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
        return permissionList.stream().map(SysPermission::getPerms).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
    }

    /**
     * 36根据多个用户账号(逗号分隔)，查询返回多个用户信息
     */
    @Override
    @SneakyThrows
    public JSONObject queryUsersByUsernames(String usernames) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getUsername, ListUtil.toList(usernames.split(",")));
        return JSONUtil.parseObj(sysUserMapper.selectList(queryWrapper));
    }

    /**
     * 37根据多个部门编码(逗号分隔)，查询返回多个部门信息
     */
    @Override
    public JSONObject queryDepartsByOrgCodes(String orgCodes) {
        LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysDepart::getOrgCode, ListUtil.toList(orgCodes.split(",")));
        return JSONUtil.parseObj(sysDepartMapper.selectList(queryWrapper));
    }

    @Override
    public void addDataLog(String tableName, String dataId, String dataContent) {
        String versionNumber = "0";
        String dataVersion = sysDataLogMapper.queryMaxDataVer(tableName, dataId);
        if (dataVersion != null) {
            versionNumber = String.valueOf(Integer.parseInt(dataVersion) + 1);
        }
        SysDataLog log = new SysDataLog();
        log.setDataTable(tableName);
        log.setDataId(dataId);
        log.setDataContent(dataContent);
        log.setDataVersion(versionNumber);
        sysDataLogMapper.insert(log);
    }

    @Override
    public Result<?> checkUserState(SysUser sysUser) {
        if (sysUser == null) {
            return Result.error("该用户不存在，请注册！");
        }
        //情况2：根据用户信息查询，该用户已注销
        if (sysUser.getDelFlag() == 1) {
            return Result.error("该用户已删除！");
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConst.USER_FREEZE.equals(sysUser.getStatus())) {
            return Result.error("该用户已冻结！");
        }
        return Result.ok();
    }

    @Override
    public void batchAddSysUser(List<SysUser> sysUsers) {
        JdbcTemplate jdbcTemplate = DynamicDBUtil.getJdbcTemplate("master");
        String sql = "INSERT INTO sys_user ( id, username, realname, password, salt, status, del_flag, work_no, create_by, create_time, user_identity, depart_ids, rel_tenant_ids, first_login ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        List<Object[]> data = new ArrayList<>(sysUsers.size());
        for (SysUser sysUser : sysUsers) {
            Object[] params = new Object[14];
            params[0] = sysUser.getId();
            params[1] = sysUser.getUsername();
            params[2] = sysUser.getRealname();
            params[3] = sysUser.getPassword();
            params[4] = sysUser.getSalt();
            params[5] = sysUser.getStatus();
            params[6] = sysUser.getDelFlag();
            params[7] = sysUser.getWorkNo();
            params[8] = sysUser.getCreateBy();
            params[9] = sysUser.getCreateTime();
            params[10] = sysUser.getUserIdentity();
            params[11] = sysUser.getDepartIds();
            params[12] = sysUser.getRelTenantIds();
            params[13] = sysUser.getFirstLogin();
            data.add(params);
        }
        jdbcTemplate.batchUpdate(sql, data);
    }

    /**
     * 发消息
     */
    private void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, Integer setMsgCategory) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitle(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConst.PRIORITY_M);
        announcement.setMsgType(CommonConst.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConst.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(setMsgCategory);
        announcement.setDelFlag(CommonConst.NOT_DELETED);
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String[] userIds = toUser.split(",");
        for (String userId : userIds) {
            if (StrUtil.isNotEmpty(userId)) {
                SysUser sysUser = sysUserMapper.getUserByName(userId);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(announcement.getId());
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConst.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = JSONUtil.createObj();
                obj.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_USER);
                obj.set(WebSocketConst.MSG_USER_ID, sysUser.getId());
                obj.set(WebSocketConst.MSG_ID, announcement.getId());
                obj.set(WebSocketConst.MSG_TXT, announcement.getTitle());
                appWebSocketHandler.sendOneMessage(sysUser.getId(), obj.toString());
            }
        }
    }

    /**
     * 发消息 带业务参数
     */
    private void sendBusAnnouncement(String fromUser, String toUser, String title, String msgContent, Integer setMsgCategory, String busType, String busId) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitle(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConst.PRIORITY_M);
        announcement.setMsgType(CommonConst.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConst.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(setMsgCategory);
        announcement.setDelFlag(CommonConst.NOT_DELETED);
        announcement.setBusId(busId);
        announcement.setBusType(busType);
        announcement.setOpenType(Objects.requireNonNull(AnnouncementType.getByType(busType)).getOpenType());
        announcement.setOpenPage(Objects.requireNonNull(AnnouncementType.getByType(busType)).getOpenPage());
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String[] userIds = toUser.split(",");
        for (String userId : userIds) {
            if (StrUtil.isNotEmpty(userId)) {
                SysUser sysUser = sysUserMapper.getUserByName(userId);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(announcement.getId());
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConst.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = JSONUtil.createObj();
                obj.set(WebSocketConst.MSG_CMD, WebSocketConst.CMD_USER);
                obj.set(WebSocketConst.MSG_USER_ID, sysUser.getId());
                obj.set(WebSocketConst.MSG_ID, announcement.getId());
                obj.set(WebSocketConst.MSG_TXT, announcement.getTitle());
                appWebSocketHandler.sendOneMessage(sysUser.getId(), obj.toString());
            }
        }
    }

    @Override
    public void addLog(LogDTO logDTO) {
        if (StrUtil.isEmpty(logDTO.getId())) {
            logDTO.setId(String.valueOf(IdWorker.getId()));
        }
        sysLogMapper.saveLog(logDTO);
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operateType) {
        LogDTO sysLog = new LogDTO();
        sysLog.setId(String.valueOf(IdWorker.getId()));
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operateType);
        try {
            //获取request
            HttpServletRequest request = SystemContextUtil.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(HttpServletUtil.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        //获取登录用户信息
        if (StpUtil.isLogin()) {
            LoginUser user = SystemContextUtil.currentLoginUser();
            if (user != null) {
                sysLog.setUserid(user.getUsername());
                sysLog.setUsername(user.getRealname());
            }
        }
        sysLog.setCreateTime(new Date());
        //保存系统日志
        sysLogMapper.saveLog(sysLog);
    }
}