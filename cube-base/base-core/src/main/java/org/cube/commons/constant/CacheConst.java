package org.cube.commons.constant;

/**
 * 缓存常量
 *
 * @author xinwuy
 * @since 2019-06-14
 */
public class CacheConst {

    /**
     * 字典信息缓存
     */
    public static final String SYS_DICT_CACHE = "sys:cache:dict";

    /**
     * 表字典信息缓存
     */
    public static final String SYS_DICT_TABLE_CACHE = SYS_DICT_CACHE + ":table";
    public static final String SYS_DICT_TABLE_BY_KEYS_CACHE = SYS_DICT_TABLE_CACHE + ":by:keys";

    /**
     * 数据权限配置缓存
     */
    public static final String SYS_DATA_PERMISSIONS_CACHE = "sys:cache:permission:data:rules";

    /**
     * 缓存用户信息
     */
    public static final String SYS_USERS_CACHE = "sys:cache:user";

    /**
     * 全部部门信息缓存
     */
    public static final String SYS_DEPARTS_CACHE = "sys:cache:depart";

    public static final String SYS_DEPARTS_CACHE_ALL = SYS_DEPARTS_CACHE + ":all";

    /**
     * 全部部门id缓存
     */
    public static final String SYS_DEPART_IDS_CACHE = "sys:cache:depart:id:all";

    /**
     * 字典信息缓存
     */
    public static final String SYS_DYNAMIC_DB_CACHE = "sys:cache:db:connect:dynamic:";

    /**
     * gateway路由缓存
     */
    public static final String GATEWAY_ROUTES = "gateway_routes";

    /**
     * 防止重复提交
     */
    public static final String LIMIT_SUBMIT = "limit_submit:";
}
