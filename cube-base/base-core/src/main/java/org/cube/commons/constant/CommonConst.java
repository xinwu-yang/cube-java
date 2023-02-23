package org.cube.commons.constant;

public interface CommonConst {

    /**
     * 已删除
     */
    Integer DELETED = 1;

    /**
     * 未删除
     */
    Integer NOT_DELETED = 0;

    /**
     * 登录日志类型
     */
    int LOGIN_LOG = 1;

    /**
     * 操作日志类型
     */
    int OPERATE_LOG = 2;

    /**
     * 操作日志类型： 查询
     */
    int OPERATE_SELECT = 1;

    /**
     * 操作日志类型： 添加
     */
    int OPERATE_ADD = 2;

    /**
     * 操作日志类型： 更新
     */
    int OPERATE_UPDATE = 3;

    /**
     * 操作日志类型： 删除
     */
    int OPERATE_DELETE = 4;

    /**
     * 操作日志类型： 导入
     */
    int OPERATE_IMPORT = 5;

    /**
     * 操作日志类型： 导出
     */
    int OPERATE_EXPORT = 6;

    /**
     * 凭证失效！
     */
    Integer NOT_LOGIN = 501;

    /**
     * 0：一级菜单
     */
    Integer MENU_TYPE_0 = 0;

    /**
     * 1：子菜单
     */
    Integer MENU_TYPE_1 = 1;

    /**
     * 2：按钮权限
     */
    Integer MENU_TYPE_2 = 2;

    /**
     * 通告对象类型（USER:指定用户，ALL:全体用户）
     */
    String MSG_TYPE_UESR = "USER";
    String MSG_TYPE_ALL = "ALL";

    /**
     * 发布状态（0未发布，1已发布，2已撤销）
     */
    int NO_SEND = 0;
    int HAS_SEND = 1;
    int HAS_CANCEL = 2;

    /**
     * 阅读状态（0未读，1已读）
     */
    int NO_READ_FLAG = 0;
    int HAS_READ_FLAG = 1;

    /**
     * 优先级（L低，M中，H高）
     */
    String PRIORITY_L = "L";
    String PRIORITY_M = "M";
    String PRIORITY_H = "H";

    /**
     * 状态(0无效1有效)
     */
    String STATUS_0 = "0";
    String STATUS_1 = "1";

    /**
     * 消息类型1:通知公告2:系统消息
     */
    int MSG_CATEGORY_NOTICE = 1;
    int MSG_CATEGORY_SYSTEM = 2;

    /**
     * 是否配置菜单的数据权限 1是0否
     */
    Integer RULE_FLAG_0 = 0;
    Integer RULE_FLAG_1 = 1;

    /**
     * 是否用户已被冻结 1正常(解冻) 2冻结
     */
    Integer USER_UNFREEZE = 1;
    Integer USER_FREEZE = 2;

    /**
     * 字典翻译文本后缀
     */
    String DICT_TEXT_SUFFIX = "_dictText";

    /**
     * 员工身份 （1:普通员工  2:上级）
     */
    Integer USER_IDENTITY_1 = 1;
    Integer USER_IDENTITY_2 = 2;

    /**
     * sys_user 表 username 唯一键索引
     */
    String SQL_INDEX_UNIQ_SYS_USER_USERNAME = "uniq_sys_user_username";

    /**
     * sys_user 表 work_no 唯一键索引
     */
    String SQL_INDEX_UNIQ_SYS_USER_WORK_NO = "uniq_sys_user_work_no";

    /**
     * sys_user 表 phone 唯一键索引
     */
    String SQL_INDEX_UNIQ_SYS_USER_PHONE = "uniq_sys_user_phone";

    /**
     * sys_user 表 email 唯一键索引
     */
    String SQL_INDEX_UNIQ_SYS_USER_EMAIL = "uniq_sys_user_email";

    /**
     * sys_quartz_job 表 job_class_name 唯一键索引
     */
    String SQL_INDEX_UNIQ_JOB_CLASS_NAME = "uniq_job_class_name";

    /**
     * sys_position 表 code 唯一键索引
     */
    String SQL_INDEX_UNIQ_CODE = "uniq_code";

    /**
     * sys_role 表 code 唯一键索引
     */
    String SQL_INDEX_UNIQ_SYS_ROLE_CODE = "uniq_sys_role_role_code";

    /**
     * sys_depart 表 code 唯一键索引
     */
    String SQL_INDEX_UNIQ_DEPART_ORG_CODE = "uniq_depart_org_code";

    String X_ACCESS_TOKEN = "X-Access-Token";

    /**
     * 多租户 请求头
     */
    String TENANT_ID = "tenant_id";

    /**
     * 微服务读取配置文件属性 服务地址
     */
    String CLOUD_SERVER_KEY = "spring.cloud.nacos.discovery.server-addr";

    /**
     * 第三方登录 验证密码/创建用户 都需要设置一个操作码 防止被恶意调用
     */
    String THIRD_LOGIN_CODE = "third_login_code";
}
