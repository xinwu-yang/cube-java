/*
 魔方基础数据库脚本-MySQL5.7
 
 Release Date: 2022-12-05
 Version: 2.6.4
*/

DROP DATABASE IF EXISTS `cube`;
CREATE DATABASE `cube` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `cube`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_announcement
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement`;
CREATE TABLE `sys_announcement`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `msg_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `sender` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布人',
  `priority` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '优先级（L低，M中，H高）',
  `msg_type` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '通告对象类型（USER:指定用户，ALL:全体用户）',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '撤销时间',
  `msg_category` tinyint(1) NOT NULL DEFAULT 2 COMMENT '消息类型（1通知公告，2系统消息）',
  `send_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '发布状态（0未发布，1已发布，2已撤销）',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态（0正常，1已删除）',
  `bus_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业务类型(email:邮件，bpm:流程)',
  `bus_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业务id',
  `open_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '打开方式(component:组件，url:路由)',
  `open_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件/路由 地址',
  `user_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '指定用户',
  `msg_abstract` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '摘要',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统通告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_announcement
-- ----------------------------

-- ----------------------------
-- Table structure for sys_announcement_send
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement_send`;
CREATE TABLE `sys_announcement_send`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `annt_id` bigint(20) NOT NULL COMMENT '通告ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户id',
  `read_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '阅读状态（0未读，1已读）',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户通告阅读标记表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_announcement_send
-- ----------------------------

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '分布式id-雪花算法',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gb_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `post_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `level` int(11) NULL DEFAULT NULL COMMENT '菜单等级',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '全路径',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `has_children` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_area
-- ----------------------------

-- ----------------------------
-- Table structure for sys_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_category`;
CREATE TABLE `sys_category`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `pid` bigint(20) NULL COMMENT '父级节点ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型编码',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '所属部门',
  `has_child` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否有子节点',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_category
-- ----------------------------

-- ----------------------------
-- Table structure for sys_check_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_check_rule`;
CREATE TABLE `sys_check_rule`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则Code',
  `rule_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则JSON',
  `rule_description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则描述',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_sys_check_rule_code`(`rule_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_check_rule
-- ----------------------------
INSERT INTO `sys_check_rule` VALUES ('1224980593992388610', '通用编码规则', 'common', '[{\"digits\":\"1\",\"pattern\":\"^[a-z|A-Z]$\",\"message\":\"第一位只能是字母\"},{\"digits\":\"*\",\"pattern\":\"^[0-9|a-z|A-Z|_]{0,}$\",\"message\":\"只能填写数字、大小写字母、下划线\"},{\"digits\":\"*\",\"pattern\":\"^.{3,}$\",\"message\":\"最少输入3位数\"},{\"digits\":\"*\",\"pattern\":\"^.{3,12}$\",\"message\":\"最多输入12位数\"}]', '规则：1、首位只能是字母；2、只能填写数字、大小写字母、下划线；3、最少3位数，最多12位数。', 'admin', '2020-02-07 11:25:48', 'admin', '2020-02-05 16:58:27');
INSERT INTO `sys_check_rule` VALUES ('1225001845524004866', '负责的功能测试', 'test', '[{\"digits\":\"*\",\"pattern\":\"^.{3,12}$\",\"message\":\"只能输入3-12位字符\"},{\"digits\":\"3\",\"pattern\":\"^\\\\d{3}$\",\"message\":\"前3位必须是数字\"},{\"digits\":\"*\",\"pattern\":\"^[^pP]*$\",\"message\":\"不能输入P\"},{\"digits\":\"4\",\"pattern\":\"^@{4}$\",\"message\":\"第4-7位必须都为 @\"},{\"digits\":\"2\",\"pattern\":\"^#=$\",\"message\":\"第8-9位必须是 #=\"},{\"digits\":\"1\",\"pattern\":\"^O$\",\"message\":\"第10位必须为大写的O\"},{\"digits\":\"*\",\"pattern\":\"^.*。$\",\"message\":\"必须以。结尾\"}]', '包含长度校验、特殊字符校验等', 'admin', '2020-02-07 11:57:31', 'admin', '2020-02-05 18:22:54');

-- ----------------------------
-- Table structure for sys_data_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_log`;
CREATE TABLE `sys_data_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'id',
  `data_table` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表名',
  `data_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据ID',
  `data_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '数据内容',
  `data_version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sindex`(`data_table`, `data_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_data_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_data_source
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_source`;
CREATE TABLE `sys_data_source`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `db_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `db_driver` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '驱动类',
  `db_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源地址',
  `db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `db_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `db_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sys_data_source_code_uni`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_data_source
-- ----------------------------
INSERT INTO `sys_data_source` VALUES ('1209779538310004737', 'local_mysql', 'MySQL5.7', '本地数据库MySQL5.7', '1', 'com.mysql.jdbc.Driver', 'jdbc:mysql://127.0.0.1:3306/cube?characterEncoding=UTF-8&useUnicode=true&useSSL=false', 'roots', 'root', 'f5b6775e8d1749483f2320627de0e706', 'admin', '2019-12-25 18:14:53', 'admin', '2020-07-10 16:54:42', 'A01');

-- ----------------------------
-- Table structure for sys_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart`;
CREATE TABLE `sys_depart`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '父机构ID',
  `depart_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '机构/部门名称',
  `depart_name_en` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '英文名',
  `depart_name_abbr` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '缩写',
  `depart_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '地区Id',
  `gb_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '国标编码',
  `org_category` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '机构类别 1组织机构，2岗位',
  `org_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '机构类型 1一级部门 2子部门',
  `org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '机构编码',
  `mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `fax` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '传真',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `memo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态（1启用，0不启用）',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态（0，正常，1已删除）',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_depart_org_code`(`org_code`) USING BTREE,
  INDEX `index_depart_parent_id`(`parent_id`) USING BTREE,
  INDEX `index_depart_depart_order`(`depart_order`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '组织机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_depart
-- ----------------------------
INSERT INTO `sys_depart` VALUES ('c6d7cb4deeac411cb3384b1b31278596', '', '四川天翼网络服务有限公司', NULL, NULL, 0, NULL, NULL, NULL, '1', '1', 'A01', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-11 14:21:51', 'admin', '2020-05-02 18:21:27');

-- ----------------------------
-- Table structure for sys_depart_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_permission`;
CREATE TABLE `sys_depart_permission`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门id',
  `permission_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据规则id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_depart_permission
-- ----------------------------

-- ----------------------------
-- Table structure for sys_depart_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role`;
CREATE TABLE `sys_depart_role`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门id',
  `role_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门角色名称',
  `role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门角色编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_depart_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_depart_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role_permission`;
CREATE TABLE `sys_depart_role_permission`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门id',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据权限ids',
  `operate_date` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `operate_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作ip',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE,
  INDEX `index_group_role_id`(`role_id`) USING BTREE,
  INDEX `index_group_per_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门角色权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_depart_role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for sys_depart_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role_user`;
CREATE TABLE `sys_depart_role_user`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户id',
  `drole_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门角色用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_depart_role_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典名称',
  `dict_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '字典类型0为string,1为number',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `indextable_dict_code`(`dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('0b5d19e1fce4b2e6647e6b4a17760c14', '通告类型', 'msg_category', '消息类型1:通知公告2:系统消息', 0, 'admin', '2019-04-22 18:01:35', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1174509082208395266', '职务职级', 'position_rank', '职务表职级字典', 0, 'admin', '2019-09-19 10:22:41', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1174511106530525185', '机构类型', 'org_category', '机构类型 1公司，2部门 3岗位', 0, 'admin', '2019-09-19 10:30:43', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1199517671259906049', '紧急程度', 'urgent_level', '日程计划紧急程度', 0, 'admin', '2019-11-27 10:37:53', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1199518099888414722', '日程计划类型', 'eoa_plan_type', '', 0, 'admin', '2019-11-27 10:39:36', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1199520177767587841', '分类栏目类型', 'eoa_cms_menu_type', '', 0, 'admin', '2019-11-27 10:47:51', 'admin', '2019-11-27 10:49:35', 0);
INSERT INTO `sys_dict` VALUES ('1199525215290306561', '日程计划状态', 'eoa_plan_status', '', 0, 'admin', '2019-11-27 11:07:52', 'admin', '2019-11-27 11:10:11', 0);
INSERT INTO `sys_dict` VALUES ('1209733563293962241', '数据库类型', 'database_type', '', 0, 'admin', '2019-12-25 15:12:12', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1250687930947620866', '定时任务状态', 'quartz_status', '', 0, 'admin', '2020-04-16 15:30:14', '', NULL, NULL);
INSERT INTO `sys_dict` VALUES ('1280401766745718786', '租户状态', 'tenant_status', '租户状态', 0, 'admin', '2020-07-07 15:22:25', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('236e8a4baff0db8c62c00dd95632834f', '同步工作流引擎', 'activiti_sync', '同步工作流引擎', 0, 'admin', '2019-05-15 15:27:33', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('2e02df51611a4b9632828ab7e5338f00', '权限策略', 'perms_type', '权限策略', 0, 'admin', '2019-04-26 18:26:55', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('2f0320997ade5dd147c90130f7218c3e', '推送类别', 'msg_type', '', 0, 'admin', '2019-03-17 21:21:32', 'admin', '2019-03-26 19:57:45', 0);
INSERT INTO `sys_dict` VALUES ('3486f32803bb953e7155dab3513dc68b', '删除状态', 'del_flag', NULL, 0, 'admin', '2019-01-18 21:46:26', 'admin', '2019-03-30 11:17:11', 0);
INSERT INTO `sys_dict` VALUES ('3d9a351be3436fbefb1307d4cfb49bf2', '性别', 'sex', NULL, 0, NULL, '2019-01-04 14:56:32', 'admin', '2019-03-30 11:28:27', 1);
INSERT INTO `sys_dict` VALUES ('4274efc2292239b6f000b153f50823ff', '全局权限策略', 'global_perms_type', '全局权限策略', 0, 'admin', '2019-05-10 17:54:05', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('4c753b5293304e7a445fd2741b46529d', '字典状态', 'dict_item_status', NULL, 0, 'admin', '2020-06-18 23:18:42', 'admin', '2019-03-30 19:33:52', 1);
INSERT INTO `sys_dict` VALUES ('4d7fec1a7799a436d26d02325eff295e', '优先级', 'priority', '优先级', 0, 'admin', '2019-03-16 17:03:34', 'admin', '2019-04-16 17:39:23', 0);
INSERT INTO `sys_dict` VALUES ('4e4602b3e3686f0911384e188dc7efb4', '条件规则', 'rule_conditions', '', 0, 'admin', '2019-04-01 10:15:03', 'admin', '2019-04-01 10:30:47', 0);
INSERT INTO `sys_dict` VALUES ('4f69be5f507accea8d5df5f11346181a', '发送消息类型', 'msgType', NULL, 0, 'admin', '2019-04-11 14:27:09', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('68168534ff5065a152bfab275c2136f8', '有效无效状态', 'valid_status', '有效无效状态', 0, 'admin', '2020-09-26 19:21:14', 'admin', '2019-04-26 19:21:23', 0);
INSERT INTO `sys_dict` VALUES ('78bda155fe380b1b3f175f1e88c284c6', '流程状态', 'bpm_status', '流程状态', 0, 'admin', '2019-05-09 16:31:52', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('83bfb33147013cc81640d5fd9eda030c', '日志类型', 'log_type', NULL, 0, 'admin', '2019-03-18 23:22:19', NULL, NULL, 1);
INSERT INTO `sys_dict` VALUES ('845da5006c97754728bf48b6a10f79cc', '状态', 'status', NULL, 0, 'admin', '2019-03-18 21:45:25', 'admin', '2019-03-18 21:58:25', 0);
INSERT INTO `sys_dict` VALUES ('880a895c98afeca9d9ac39f29e67c13e', '操作类型', 'operate_type', '操作类型', 0, 'admin', '2019-07-22 10:54:29', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('8dfe32e2d29ea9430a988b3b558bf233', '发布状态', 'send_status', '发布状态', 0, 'admin', '2019-04-16 17:40:42', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('a7adbcd86c37f7dbc9b66945c82ef9e6', '1是0否', 'yn', '', 0, 'admin', '2019-05-22 19:29:29', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('a9d9942bd0eccb6e89de92d130ec4c4a', '消息发送状态', 'msgSendStatus', NULL, 0, 'admin', '2019-04-12 18:18:17', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('ac2f7c0c5c5775fcea7e2387bcb22f01', '菜单类型', 'menu_type', NULL, 0, 'admin', '2020-12-18 23:24:32', 'admin', '2019-04-01 15:27:06', 1);
INSERT INTO `sys_dict` VALUES ('c36169beb12de8a71c8683ee7c28a503', '部门状态', 'depart_status', NULL, 0, 'admin', '2019-03-18 21:59:51', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('d6e1152968b02d69ff358c75b48a6ee1', '流程类型', 'bpm_process_type', NULL, 0, 'admin', '2021-02-22 19:26:54', 'admin', '2019-03-30 18:14:44', 0);
INSERT INTO `sys_dict` VALUES ('fc6cd58fde2e8481db10d3a1e68ce70c', '用户状态', 'user_status', NULL, 0, 'admin', '2019-03-18 21:57:25', 'admin', '2019-03-18 23:11:58', 1);

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `dict_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '字典id',
  `item_text` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典项文本',
  `item_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典项值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `sort_order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1启用 0不启用）',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_table_dict_id`(`dict_id`) USING BTREE,
  INDEX `index_table_sort_order`(`sort_order`) USING BTREE,
  INDEX `index_table_dict_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES ('0072d115e07c875d76c9b022e2179128', '4d7fec1a7799a436d26d02325eff295e', '低', 'L', '低', 3, 1, 'admin', '2019-04-16 17:04:59', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('05a2e732ce7b00aa52141ecc3e330b4e', '3486f32803bb953e7155dab3513dc68b', '已删除', '1', NULL, NULL, 1, 'admin', '2025-10-18 21:46:56', 'admin', '2019-03-28 22:23:20');
INSERT INTO `sys_dict_item` VALUES ('0c9532916f5cd722017b46bc4d953e41', '2f0320997ade5dd147c90130f7218c3e', '指定用户', 'USER', NULL, NULL, 1, 'admin', '2019-03-17 21:22:19', 'admin', '2019-03-17 21:22:28');
INSERT INTO `sys_dict_item` VALUES ('1030a2652608f5eac3b49d70458b8532', '2e02df51611a4b9632828ab7e5338f00', '禁用', '2', '禁用', 2, 1, 'admin', '2021-03-26 18:27:28', 'admin', '2019-04-26 18:39:11');
INSERT INTO `sys_dict_item` VALUES ('1174509082208395266', '1174511106530525185', '岗位', '3', '岗位', 1, 1, 'admin', '2019-09-19 10:31:16', '', NULL);
INSERT INTO `sys_dict_item` VALUES ('1174509601047994369', '1174509082208395266', '员级', '1', '', 1, 1, 'admin', '2019-09-19 10:24:45', 'admin', '2019-09-23 11:46:39');
INSERT INTO `sys_dict_item` VALUES ('1174509667297026049', '1174509082208395266', '助级', '2', '', 2, 1, 'admin', '2019-09-19 10:25:01', 'admin', '2019-09-23 11:46:47');
INSERT INTO `sys_dict_item` VALUES ('1174509713568587777', '1174509082208395266', '中级', '3', '', 3, 1, 'admin', '2019-09-19 10:25:12', 'admin', '2019-09-23 11:46:56');
INSERT INTO `sys_dict_item` VALUES ('1174509788361416705', '1174509082208395266', '副高级', '4', '', 4, 1, 'admin', '2019-09-19 10:25:30', 'admin', '2019-09-23 11:47:06');
INSERT INTO `sys_dict_item` VALUES ('1174509835803189250', '1174509082208395266', '正高级', '5', '', 5, 1, 'admin', '2019-09-19 10:25:41', 'admin', '2019-09-23 11:47:12');
INSERT INTO `sys_dict_item` VALUES ('1174511197735665665', '1174511106530525185', '公司', '1', '公司', 1, 1, 'admin', '2019-09-19 10:31:05', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1174511244036587521', '1174511106530525185', '部门', '2', '部门', 1, 1, 'admin', '2019-09-19 10:31:16', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199517884758368257', '1199517671259906049', '一般', '1', '', 1, 1, 'admin', '2019-11-27 10:38:44', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199517914017832962', '1199517671259906049', '重要', '2', '', 1, 1, 'admin', '2019-11-27 10:38:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199517941339529217', '1199517671259906049', '紧急', '3', '', 1, 1, 'admin', '2019-11-27 10:38:58', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518186144276482', '1199518099888414722', '日常记录', '1', '', 1, 1, 'admin', '2019-11-27 10:39:56', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518214858481666', '1199518099888414722', '本周工作', '2', '', 1, 1, 'admin', '2019-11-27 10:40:03', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518235943247874', '1199518099888414722', '下周计划', '3', '', 1, 1, 'admin', '2019-11-27 10:40:08', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199520817285701634', '1199520177767587841', '列表', '1', '', 1, 1, 'admin', '2019-11-27 10:50:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199520835035996161', '1199520177767587841', '链接', '2', '', 1, 1, 'admin', '2019-11-27 10:50:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525468672405505', '1199525215290306561', '未开始', '0', '', 1, 1, 'admin', '2019-11-27 11:08:52', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525490575060993', '1199525215290306561', '进行中', '1', '', 1, 1, 'admin', '2019-11-27 11:08:58', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525506429530114', '1199525215290306561', '已完成', '2', '', 1, 1, 'admin', '2019-11-27 11:09:02', 'admin', '2019-11-27 11:10:02');
INSERT INTO `sys_dict_item` VALUES ('1199607547704647681', '4f69be5f507accea8d5df5f11346181a', '系统', '4', '', 1, 1, 'admin', '2019-11-27 16:35:02', 'admin', '2019-11-27 19:37:46');
INSERT INTO `sys_dict_item` VALUES ('1209733775114702850', '1209733563293962241', 'MySQL', '1', '', 1, 1, 'admin', '2019-12-25 15:13:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1209733839933476865', '1209733563293962241', 'Oracle', '2', '', 1, 1, 'admin', '2019-12-25 15:13:18', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1209733903020003330', '1209733563293962241', 'SQLServer', '3', '', 1, 1, 'admin', '2019-12-25 15:13:33', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1234371726545010689', '4e4602b3e3686f0911384e188dc7efb4', '左模糊', 'LEFT_LIKE', '左模糊', 7, 1, 'admin', '2020-03-02 14:55:27', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1234371809495760898', '4e4602b3e3686f0911384e188dc7efb4', '右模糊', 'RIGHT_LIKE', '右模糊', 7, 1, 'admin', '2020-03-02 14:55:47', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1250688147579228161', '1250687930947620866', '正常', '0', '', 1, 1, 'admin', '2020-04-16 15:31:05', '', NULL);
INSERT INTO `sys_dict_item` VALUES ('1250688201064992770', '1250687930947620866', '停止', '-1', '', 1, 1, 'admin', '2020-04-16 15:31:18', '', NULL);
INSERT INTO `sys_dict_item` VALUES ('1280401815068295170', '1280401766745718786', '正常', '1', '', 1, 1, 'admin', '2020-07-07 15:22:36', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1280401847607705602', '1280401766745718786', '冻结', '0', '', 1, 1, 'admin', '2020-07-07 15:22:44', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('147c48ff4b51545032a9119d13f3222a', 'd6e1152968b02d69ff358c75b48a6ee1', '测试流程', 'test', NULL, 1, 1, 'admin', '2019-03-22 19:27:05', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('222705e11ef0264d4214affff1fb4ff9', '4f69be5f507accea8d5df5f11346181a', '短信', '1', '', 1, 1, 'admin', '2023-02-28 10:50:36', 'admin', '2019-04-28 10:58:11');
INSERT INTO `sys_dict_item` VALUES ('23a5bb76004ed0e39414e928c4cde155', '4e4602b3e3686f0911384e188dc7efb4', '不等于', '!=', '不等于', 3, 1, 'admin', '2019-04-01 16:46:15', 'admin', '2019-04-01 17:48:40');
INSERT INTO `sys_dict_item` VALUES ('25847e9cb661a7c711f9998452dc09e6', '4e4602b3e3686f0911384e188dc7efb4', '小于等于', '<=', '小于等于', 6, 1, 'admin', '2019-04-01 16:44:34', 'admin', '2019-04-01 17:49:10');
INSERT INTO `sys_dict_item` VALUES ('2d51376643f220afdeb6d216a8ac2c01', '68168534ff5065a152bfab275c2136f8', '有效', '1', '有效', 2, 1, 'admin', '2019-04-26 19:22:01', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('308c8aadf0c37ecdde188b97ca9833f5', '8dfe32e2d29ea9430a988b3b558bf233', '已发布', '1', '已发布', 2, 1, 'admin', '2019-04-16 17:41:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('333e6b2196e01ef9a5f76d74e86a6e33', '8dfe32e2d29ea9430a988b3b558bf233', '未发布', '0', '未发布', 1, 1, 'admin', '2019-04-16 17:41:12', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('33bc9d9f753cf7dc40e70461e50fdc54', 'a9d9942bd0eccb6e89de92d130ec4c4a', '发送失败', '2', NULL, 3, 1, 'admin', '2019-04-12 18:20:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('3fbc03d6c994ae06d083751248037c0e', '78bda155fe380b1b3f175f1e88c284c6', '已完成', '3', '已完成', 3, 1, 'admin', '2019-05-09 16:33:25', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('41d7aaa40c9b61756ffb1f28da5ead8e', '0b5d19e1fce4b2e6647e6b4a17760c14', '通知公告', '1', NULL, 1, 1, 'admin', '2019-04-22 18:01:57', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('41fa1e9571505d643aea87aeb83d4d76', '4e4602b3e3686f0911384e188dc7efb4', '等于', '=', '等于', 4, 1, 'admin', '2019-04-01 16:45:24', 'admin', '2019-04-01 17:49:00');
INSERT INTO `sys_dict_item` VALUES ('43d2295b8610adce9510ff196a49c6e9', '845da5006c97754728bf48b6a10f79cc', '正常', '1', NULL, NULL, 1, 'admin', '2019-03-18 21:45:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('4f05fb5376f4c61502c5105f52e4dd2b', '83bfb33147013cc81640d5fd9eda030c', '操作日志', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:22:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('50223341bfb5ba30bf6319789d8d17fe', 'd6e1152968b02d69ff358c75b48a6ee1', '业务办理', 'business', NULL, 3, 1, 'admin', '2023-04-22 19:28:05', 'admin', '2019-03-22 23:24:39');
INSERT INTO `sys_dict_item` VALUES ('51222413e5906cdaf160bb5c86fb827c', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '是', '1', '', 1, 1, 'admin', '2019-05-22 19:29:45', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('538fca35afe004972c5f3947c039e766', '2e02df51611a4b9632828ab7e5338f00', '显示', '1', '显示', 1, 1, 'admin', '2025-03-26 18:27:13', 'admin', '2019-04-26 18:39:07');
INSERT INTO `sys_dict_item` VALUES ('5584c21993bde231bbde2b966f2633ac', '4e4602b3e3686f0911384e188dc7efb4', '自定义SQL表达式', 'USE_SQL_RULES', '自定义SQL表达式', 9, 1, 'admin', '2019-04-01 10:45:24', 'admin', '2019-04-01 17:49:27');
INSERT INTO `sys_dict_item` VALUES ('5b65a88f076b32e8e69d19bbaadb52d5', '2f0320997ade5dd147c90130f7218c3e', '全体用户', 'ALL', NULL, NULL, 1, 'admin', '2020-10-17 21:22:43', 'admin', '2019-03-28 22:17:09');
INSERT INTO `sys_dict_item` VALUES ('5d833f69296f691843ccdd0c91212b6b', '880a895c98afeca9d9ac39f29e67c13e', '修改', '3', '', 3, 1, 'admin', '2019-07-22 10:55:07', 'admin', '2019-07-22 10:55:41');
INSERT INTO `sys_dict_item` VALUES ('5d84a8634c8fdfe96275385075b105c9', '3d9a351be3436fbefb1307d4cfb49bf2', '女', '2', NULL, 2, 1, NULL, '2019-01-04 14:56:56', NULL, '2019-01-04 17:38:12');
INSERT INTO `sys_dict_item` VALUES ('66c952ae2c3701a993e7db58f3baf55e', '4e4602b3e3686f0911384e188dc7efb4', '大于', '>', '大于', 1, 1, 'admin', '2019-04-01 10:45:46', 'admin', '2019-04-01 17:48:29');
INSERT INTO `sys_dict_item` VALUES ('69cacf64e244100289ddd4aa9fa3b915', 'a9d9942bd0eccb6e89de92d130ec4c4a', '未发送', '0', NULL, 1, 1, 'admin', '2019-04-12 18:19:23', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('6a7a9e1403a7943aba69e54ebeff9762', '4f69be5f507accea8d5df5f11346181a', '邮件', '2', '', 2, 1, 'admin', '2031-02-28 10:50:44', 'admin', '2019-04-28 10:59:03');
INSERT INTO `sys_dict_item` VALUES ('6d404fd2d82311fbc87722cd302a28bc', '4e4602b3e3686f0911384e188dc7efb4', '模糊', 'LIKE', '模糊', 7, 1, 'admin', '2019-04-01 16:46:02', 'admin', '2019-04-01 17:49:20');
INSERT INTO `sys_dict_item` VALUES ('6d4e26e78e1a09699182e08516c49fc4', '4d7fec1a7799a436d26d02325eff295e', '高', 'H', '高', 1, 1, 'admin', '2019-04-16 17:04:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('71b924faa93805c5c1579f12e001c809', 'd6e1152968b02d69ff358c75b48a6ee1', 'OA办公', 'oa', NULL, 2, 1, 'admin', '2021-03-22 19:27:17', 'admin', '2019-03-22 23:24:36');
INSERT INTO `sys_dict_item` VALUES ('75b260d7db45a39fc7f21badeabdb0ed', 'c36169beb12de8a71c8683ee7c28a503', '不启用', '0', NULL, NULL, 1, 'admin', '2019-03-18 23:29:41', 'admin', '2019-03-18 23:29:54');
INSERT INTO `sys_dict_item` VALUES ('7688469db4a3eba61e6e35578dc7c2e5', 'c36169beb12de8a71c8683ee7c28a503', '启用', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:29:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('78ea6cadac457967a4b1c4eb7aaa418c', 'fc6cd58fde2e8481db10d3a1e68ce70c', '正常', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:30:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('7ccf7b80c70ee002eceb3116854b75cb', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '按钮权限', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:25:40', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('81fb2bb0e838dc68b43f96cc309f8257', 'fc6cd58fde2e8481db10d3a1e68ce70c', '冻结', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:30:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('83250269359855501ec4e9c0b7e21596', '4274efc2292239b6f000b153f50823ff', '可见/可访问(授权后可见/可访问)', '1', '', 1, 1, 'admin', '2019-05-10 17:54:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('84778d7e928bc843ad4756db1322301f', '4e4602b3e3686f0911384e188dc7efb4', '大于等于', '>=', '大于等于', 5, 1, 'admin', '2019-04-01 10:46:02', 'admin', '2019-04-01 17:49:05');
INSERT INTO `sys_dict_item` VALUES ('84dfc178dd61b95a72900fcdd624c471', '78bda155fe380b1b3f175f1e88c284c6', '处理中', '2', '处理中', 2, 1, 'admin', '2019-05-09 16:33:01', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('86f19c7e0a73a0bae451021ac05b99dd', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '子菜单', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:25:27', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('8bccb963e1cd9e8d42482c54cc609ca2', '4f69be5f507accea8d5df5f11346181a', '微信', '3', NULL, 3, 1, 'admin', '2021-05-11 14:29:12', 'admin', '2019-04-11 14:29:31');
INSERT INTO `sys_dict_item` VALUES ('8c618902365ca681ebbbe1e28f11a548', '4c753b5293304e7a445fd2741b46529d', '启用', '1', '', 0, 1, 'admin', '2020-07-18 23:19:27', 'admin', '2019-05-17 14:51:18');
INSERT INTO `sys_dict_item` VALUES ('8cdf08045056671efd10677b8456c999', '4274efc2292239b6f000b153f50823ff', '可编辑(未授权时禁用)', '2', '', 2, 1, 'admin', '2019-05-10 17:55:38', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('8ff48e657a7c5090d4f2a59b37d1b878', '4d7fec1a7799a436d26d02325eff295e', '中', 'M', '中', 2, 1, 'admin', '2019-04-16 17:04:40', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('948923658baa330319e59b2213cda97c', '880a895c98afeca9d9ac39f29e67c13e', '添加', '2', '', 2, 1, 'admin', '2019-07-22 10:54:59', 'admin', '2019-07-22 10:55:36');
INSERT INTO `sys_dict_item` VALUES ('9a96c4a4e4c5c9b4e4d0cbf6eb3243cc', '4c753b5293304e7a445fd2741b46529d', '不启用', '0', NULL, 1, 1, 'admin', '2019-03-18 23:19:53', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('a1e7d1ca507cff4a480c8caba7c1339e', '880a895c98afeca9d9ac39f29e67c13e', '导出', '6', '', 6, 1, 'admin', '2019-07-22 12:06:50', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('a2be752dd4ec980afaec1efd1fb589af', '8dfe32e2d29ea9430a988b3b558bf233', '已撤销', '2', '已撤销', 3, 1, 'admin', '2019-04-16 17:41:39', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('aa0d8a8042a18715a17f0a888d360aa4', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '一级菜单', '0', NULL, NULL, 1, 'admin', '2019-03-18 23:24:52', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('adcf2a1fe93bb99a84833043f475fe0b', '4e4602b3e3686f0911384e188dc7efb4', '包含', 'IN', '包含', 8, 1, 'admin', '2019-04-01 16:45:47', 'admin', '2019-04-01 17:49:24');
INSERT INTO `sys_dict_item` VALUES ('b029a41a851465332ee4ee69dcf0a4c2', '0b5d19e1fce4b2e6647e6b4a17760c14', '系统消息', '2', NULL, 1, 1, 'admin', '2019-02-22 18:02:08', 'admin', '2019-04-22 18:02:13');
INSERT INTO `sys_dict_item` VALUES ('b2a8b4bb2c8e66c2c4b1bb086337f393', '3486f32803bb953e7155dab3513dc68b', '正常', '0', NULL, NULL, 1, 'admin', '2022-10-18 21:46:48', 'admin', '2019-03-28 22:22:20');
INSERT INTO `sys_dict_item` VALUES ('b5f3bd5f66bb9a83fecd89228c0d93d1', '68168534ff5065a152bfab275c2136f8', '无效', '0', '无效', 1, 1, 'admin', '2019-04-26 19:21:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('b9fbe2a3602d4a27b45c100ac5328484', '78bda155fe380b1b3f175f1e88c284c6', '待提交', '1', '待提交', 1, 1, 'admin', '2019-05-09 16:32:35', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('ba27737829c6e0e582e334832703d75e', '236e8a4baff0db8c62c00dd95632834f', '同步', '1', '同步', 1, 1, 'admin', '2019-05-15 15:28:15', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('bcec04526b04307e24a005d6dcd27fd6', '880a895c98afeca9d9ac39f29e67c13e', '导入', '5', '', 5, 1, 'admin', '2019-07-22 12:06:41', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('c53da022b9912e0aed691bbec3c78473', '880a895c98afeca9d9ac39f29e67c13e', '查询', '1', '', 1, 1, 'admin', '2019-07-22 10:54:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('c5700a71ad08994d18ad1dacc37a71a9', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '否', '0', '', 1, 1, 'admin', '2019-05-22 19:29:55', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('df168368dcef46cade2aadd80100d8aa', '3d9a351be3436fbefb1307d4cfb49bf2', '男', '1', NULL, 1, 1, NULL, '2027-08-04 14:56:49', 'admin', '2019-03-23 22:44:44');
INSERT INTO `sys_dict_item` VALUES ('e6329e3a66a003819e2eb830b0ca2ea0', '4e4602b3e3686f0911384e188dc7efb4', '小于', '<', '小于', 2, 1, 'admin', '2019-04-01 16:44:15', 'admin', '2019-04-01 17:48:34');
INSERT INTO `sys_dict_item` VALUES ('e94eb7af89f1dbfa0d823580a7a6e66a', '236e8a4baff0db8c62c00dd95632834f', '不同步', '0', '不同步', 2, 1, 'admin', '2019-05-15 15:28:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f16c5706f3ae05c57a53850c64ce7c45', 'a9d9942bd0eccb6e89de92d130ec4c4a', '发送成功', '1', NULL, 2, 1, 'admin', '2019-04-12 18:19:43', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f2a7920421f3335afdf6ad2b342f6b5d', '845da5006c97754728bf48b6a10f79cc', '冻结', '2', NULL, NULL, 1, 'admin', '2019-03-18 21:46:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f37f90c496ec9841c4c326b065e00bb2', '83bfb33147013cc81640d5fd9eda030c', '登录日志', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:22:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f80a8f6838215753b05e1a5ba3346d22', '880a895c98afeca9d9ac39f29e67c13e', '删除', '4', '', 4, 1, 'admin', '2019-07-22 10:55:14', 'admin', '2019-07-22 10:55:30');
INSERT INTO `sys_dict_item` VALUES ('fe50b23ae5e68434def76f67cef35d2d', '78bda155fe380b1b3f175f1e88c284c6', '已作废', '4', '已作废', 4, 1, 'admin', '2021-09-09 16:33:43', 'admin', '2019-05-09 16:34:40');

-- ----------------------------
-- Table structure for sys_fill_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_fill_rule`;
CREATE TABLE `sys_fill_rule`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则Code',
  `rule_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则实现类',
  `rule_params` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则参数',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_sys_fill_rule_code`(`rule_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_fill_rule
-- ----------------------------
INSERT INTO `sys_fill_rule` VALUES ('1202551334738382850', '机构编码生成', 'org_num_role', 'com.tievd.cube.modules.system.extra.fillrules.OrgCodeRule', '{\"parentId\":\"c6d7cb4deeac411cb3384b1b31278596\"}', 'admin', '2019-12-09 10:37:06', 'admin', '2019-12-05 19:32:35');
INSERT INTO `sys_fill_rule` VALUES ('1202787623203065858', '分类字典编码生成', 'category_code_rule', 'com.tievd.cube.modules.system.extra.fillrules.CategoryCodeRule', '{\"pid\":\"\"}', 'admin', '2019-12-09 10:36:54', 'admin', '2019-12-06 11:11:31');
INSERT INTO `sys_fill_rule` VALUES ('1260134137920090113', '商城订单流水号', 'shop_order_num', 'com.tievd.cube.modules.system.extra.fillrules.OrderNumberRule', '{}', 'admin', '2020-07-11 11:35:00', 'admin', '2020-05-12 17:06:05');

-- ----------------------------
-- Table structure for sys_gateway_route
-- ----------------------------
DROP TABLE IF EXISTS `sys_gateway_route`;
CREATE TABLE `sys_gateway_route`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `router_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '服务名',
  `uri` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '服务地址',
  `predicates` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '断言',
  `filters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '过滤器',
  `retryable` int(11) NULL DEFAULT NULL COMMENT '是否重试:0-否 1-是',
  `strip_prefix` int(11) NULL DEFAULT NULL COMMENT '是否忽略前缀0-否 1-是',
  `persist` int(11) NULL DEFAULT NULL COMMENT '是否为保留数据:0-否 1-是',
  `show_api` int(11) NULL DEFAULT NULL COMMENT '是否在接口文档中展示:0-否 1-是',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态:0-无效 1-有效',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_gateway_route
-- ----------------------------
INSERT INTO `sys_gateway_route` VALUES ('1331051599401857026', 'cube-demo-websocket', 'cube-demo-websocket', 'lb:ws://cube-demo', '[{\"args\":[\"/vxeSocket/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-24 09:46:46', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route` VALUES ('cube-cloud-websocket', 'cube-system-websocket', 'cube-system-websocket', 'lb:ws://cube-system', '[{\"args\":[\"/websocket/**\",\"/eoaSocket/**\",\"/newsWebsocket/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route` VALUES ('cube-demo', 'cube-demo', 'cube-demo', 'lb://cube-demo', '[{\"args\":[\"/mock/**\",\"/test/**\",\"/bigscreen/template1/**\",\"/bigscreen/template2/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route` VALUES ('cube-system', 'cube-system', 'cube-system', 'lb://cube-system', '[{\"args\":[\"/sys/**\",\"/eoa/**\",\"/joa/**\",\"/online/**\",\"/bigscreen/**\",\"/jmreport/**\",\"/desform/**\",\"/process/**\",\"/act/**\",\"/plug-in/***/\",\"/druid/**\",\"/generic/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `log_type` int(11) NULL DEFAULT NULL COMMENT '日志类型（1登录日志，2操作日志）',
  `log_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '日志内容',
  `operate_type` int(11) NULL DEFAULT NULL COMMENT '操作类型',
  `userid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作用户账号',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作用户名称',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP',
  `method` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求java方法',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `cost_time` bigint(20) NULL DEFAULT NULL COMMENT '耗时',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_table_userid`(`userid`) USING BTREE,
  INDEX `index_logt_ype`(`log_type`) USING BTREE,
  INDEX `index_operate_type`(`operate_type`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '父id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件',
  `component_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件名字',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '一级菜单跳转地址',
  `menu_type` int(11) NULL DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
  `perms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单权限编码',
  `perms_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '权限策略1显示2禁用',
  `sort_no` double(8, 2) NULL DEFAULT NULL COMMENT '菜单排序',
  `always_show` tinyint(1) NULL DEFAULT NULL COMMENT '聚合子路由: 1是0否',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `is_route` tinyint(1) NULL DEFAULT 1 COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
  `is_leaf` tinyint(1) NULL DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是',
  `keep_alive` tinyint(1) NULL DEFAULT NULL COMMENT '是否缓存该页面:    1:是   0:不是',
  `hidden` int(11) NULL DEFAULT 0 COMMENT '是否隐藏路由: 0否,1是',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态 0正常 1已删除',
  `rule_flag` int(11) NULL DEFAULT 0 COMMENT '是否添加数据权限1是0否',
  `status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '按钮权限状态(0无效1有效)',
  `internal_or_external` tinyint(1) NULL DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_prem_pid`(`parent_id`) USING BTREE,
  INDEX `index_prem_is_route`(`is_route`) USING BTREE,
  INDEX `index_prem_is_leaf`(`is_leaf`) USING BTREE,
  INDEX `index_prem_sort_no`(`sort_no`) USING BTREE,
  INDEX `index_prem_del_flag`(`del_flag`) USING BTREE,
  INDEX `index_menu_type`(`menu_type`) USING BTREE,
  INDEX `index_menu_hidden`(`hidden`) USING BTREE,
  INDEX `index_menu_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('00a2a0ae65cdca5e93209cdbde97cbe6', '2e42e3835c2b44ec9f7bc26c146ee531', '成功', '/result/success', 'result/Success', NULL, NULL, 1, NULL, NULL, 1.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('05b3c82ddb2536a4a5ee1a4c46b5abef', '540a2936940846cb98114ffb0d145cb8', '用户列表', '/list/user-list', 'examples/list/UserList', NULL, NULL, 1, NULL, NULL, 3.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('08e6b9dc3c04489c8e1ff2ce6f105aa4', '', '系统监控', '/dashboard3', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 6.00, 0, 'dashboard', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-31 22:19:58', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('1166535831146504193', '2a470fc0c3954d9dbb61de6d80846549', '对象存储', '/oss/file', 'modules/oss/OSSFileList', NULL, NULL, 1, NULL, '1', 1.00, 0, '', 1, 1, 0, 0, NULL, 'admin', '2019-08-28 02:19:50', 'admin', '2019-08-28 02:20:57', 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('1170592628746878978', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理-异步', '/isystem/newPermissionList', 'system/NewPermissionList', NULL, NULL, 1, NULL, '1', 100.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-09-08 15:00:05', 'admin', '2019-12-25 09:58:18', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1174506953255182338', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '职务管理', '/isystem/position', 'system/SysPositionList', NULL, NULL, 1, NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-09-19 10:14:13', 'admin', '2019-09-19 10:15:22', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1174590283938041857', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '通讯录', '/isystem/addressList', 'system/AddressList', NULL, NULL, 1, NULL, '1', 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-09-19 15:45:21', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1192318987661234177', 'e41b69c57a941a3bbcce45032fe57605', '系统编码规则', '/isystem/fillRule', 'system/SysFillRuleList', NULL, NULL, 1, NULL, '1', 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-11-07 13:52:53', 'admin', '2020-07-10 16:55:03', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1209731624921534465', 'e41b69c57a941a3bbcce45032fe57605', '多数据源管理', '/isystem/dataSource', 'system/SysDataSourceList', NULL, NULL, 1, NULL, '1', 6.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-12-25 15:04:30', 'admin', '2020-02-23 22:43:37', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1224641973866467330', 'e41b69c57a941a3bbcce45032fe57605', '系统校验规则', '/isystem/checkRule', 'system/SysCheckRuleList', NULL, NULL, 1, NULL, '1', 5.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-11-07 13:52:53', 'admin', '2020-07-10 16:55:12', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1260928341675982849', '3f915b2769fc80648e92d04e84ca059d', '添加按钮', NULL, NULL, NULL, NULL, 2, 'user:add', '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-05-14 21:41:58', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1260929666434318338', '3f915b2769fc80648e92d04e84ca059d', '用户编辑', NULL, NULL, NULL, NULL, 2, 'user:edit', '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-05-14 21:47:14', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1260931366557696001', '3f915b2769fc80648e92d04e84ca059d', '表单性别可见', '', NULL, NULL, NULL, 2, 'user:sex', '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-05-14 21:53:59', 'admin', '2020-05-14 21:57:00', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1260933542969458689', '3f915b2769fc80648e92d04e84ca059d', '禁用生日字段', NULL, NULL, NULL, NULL, 2, 'user:form:birthday', '2', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-05-14 22:02:38', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1265162119913824258', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '路由网关', '/isystem/gatewayroute', 'system/SysGatewayRouteList', NULL, NULL, 1, NULL, '1', 0.00, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-05-26 14:05:30', 'admin', '2020-09-09 14:47:52', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1280350452934307841', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '租户管理', '/isys/tenant', 'system/TenantList', NULL, NULL, 1, NULL, '1', 10.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-07 11:58:30', 'admin', '2020-07-10 15:46:35', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('13212d3416eb690c2e1d5033166ff47a', '2e42e3835c2b44ec9f7bc26c146ee531', '失败', '/result/fail', 'result/Error', NULL, NULL, 1, NULL, NULL, 2.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('1335876951575314434', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '在线用户', '/isystem/online/user', 'system/OnlineUserList', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-12-07 17:20:59', 'admin', '2020-12-08 10:23:23', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1336133158097592321', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '地区管理', '/isystem/area', 'system/SysAreaList', NULL, NULL, 1, NULL, '1', 8.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-12-08 10:19:04', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1336135755432927234', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '行为统计', '/isystem/permission/count', 'system/PermissionCountList', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-12-08 10:29:23', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1336136043015380994', 'e41b69c57a941a3bbcce45032fe57605', '数据导出', '/isystem/data/export', 'system/DataExport', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-12-08 10:30:32', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1367a93f2c410b169faa7abcbad2f77c', '6e73eb3c26099c191bf03852ee1310a1', '基本设置', '/account/settings/BaseSetting', 'account/settings/BaseSetting', 'account-settings-base', NULL, 1, 'BaseSettings', NULL, NULL, 0, NULL, 1, 1, NULL, 1, NULL, NULL, '2018-12-26 18:58:35', 'admin', '2019-03-20 12:57:31', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('1408265706710597633', 'e41b69c57a941a3bbcce45032fe57605', '代码生成', '/codegenerator', 'modules/generateCode', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2021-06-25 11:28:03', 'admin', '2021-07-12 14:13:03', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1437700059899138049', '700b7f95165c46cc7a78bf227aa8fed3', '连接池信息', '/monitor/datasource', 'modules/monitor/ConnectionInfo', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2021-09-14 16:49:40', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('190c2b43bec6a5f7a4194a85db67d96a', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '角色管理', '/isystem/roleUserList', 'system/RoleUserList', NULL, NULL, 1, NULL, NULL, 1.20, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-04-17 15:13:56', 'admin', '2019-12-25 09:36:31', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('1a0811914300741f4e11838ff37a1d3a', '3f915b2769fc80648e92d04e84ca059d', '手机号禁用', NULL, NULL, NULL, NULL, 2, 'user:form:phone', '2', 1.00, 0, NULL, 0, 1, NULL, 0, NULL, 'admin', '2019-05-11 17:19:30', 'admin', '2019-05-11 18:00:22', 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('277bfabef7d76e89b33062b16a9a5020', 'e3c13679c73a4f829bcff2aba8fd68b1', '基础表单', '/form/base-form', 'examples/form/BasicForm', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-02-26 17:02:08', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('2a470fc0c3954d9dbb61de6d80846549', '', '常见案例', '/jeecg', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 7.00, 0, 'qrcode', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-04-02 11:46:42', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('2dbbafa22cda07fa5d169d741b81fe12', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '在线文档', '{{ window._CONFIG[\'domianURL\'] }}/doc/app.html', 'layouts/IframePageView', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-01-30 10:00:01', 'admin', '2022-05-05 10:48:58', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('2e42e3835c2b44ec9f7bc26c146ee531', '2a470fc0c3954d9dbb61de6d80846549', '结果页', '/result', 'layouts/PageView', NULL, NULL, 1, NULL, NULL, 12.00, 0, 'check-circle-o', 1, 0, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2021-05-26 16:39:06', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('3f915b2769fc80648e92d04e84ca059d', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '用户管理', '/isystem/user', 'system/UserList', NULL, NULL, 1, NULL, NULL, 1.10, 0, NULL, 1, 0, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-12-25 09:36:24', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('45c966826eeff4c99b8f8ebfe74511fc', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '部门管理', '/isystem/depart', 'system/DepartList', NULL, NULL, 1, NULL, NULL, 1.40, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-01-29 18:47:40', 'admin', '2019-12-25 09:36:47', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('4f66409ef3bbd69c1d80469d6e2a885e', '6e73eb3c26099c191bf03852ee1310a1', '账户绑定', '/account/settings/binding', 'account/settings/Binding', NULL, NULL, 1, 'BindingSettings', NULL, NULL, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-26 19:01:20', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('53a9230444d33de28aa11cc108fb1dba', '5c8042bd6c601270b2bbd9b20bccc68b', '我的消息', '/isps/userAnnouncement', 'system/UserAnnouncementList', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-04-19 10:16:00', 'admin', '2019-12-25 09:54:34', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('54dd5457a3190740005c1bfec55b1c34', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/isystem/permission', 'system/PermissionList', NULL, NULL, 1, NULL, NULL, 1.30, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-12-25 09:36:39', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('58857ff846e61794c69208e9d3a85466', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '日志管理', '/isystem/log', 'system/LogList', NULL, NULL, 1, NULL, NULL, 2.00, 0, '', 1, 0, 0, 0, NULL, NULL, '2018-12-26 10:11:18', 'admin', '2020-09-09 14:48:25', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('5c2f42277948043026b7a14692456828', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '我的部门', '/isystem/departUserList', 'system/DepartUserList', NULL, NULL, 1, NULL, NULL, 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-04-17 15:12:24', 'admin', '2019-12-25 09:35:26', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('5c8042bd6c601270b2bbd9b20bccc68b', '', '消息中心', '/message', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 6.00, 0, 'message', 1, 0, NULL, 0, NULL, 'admin', '2019-04-09 11:05:04', 'admin', '2019-04-11 19:47:54', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('65a8f489f25a345836b7f44b1181197a', 'c65321e57b7949b7a975313220de0422', '403', '/exception/403', 'exception/403', NULL, NULL, 1, NULL, NULL, 1.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('6e73eb3c26099c191bf03852ee1310a1', '717f6bee46f44a3897eca9abd6e2ec44', '个人设置', '/account/settings/BaseSetting', 'account/settings/Index', NULL, NULL, 1, NULL, NULL, 2.00, 1, NULL, 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-04-19 09:41:05', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('700b7f95165c46cc7a78bf227aa8fed3', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '性能监控', '/monitor', 'layouts/RouteView', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2019-04-02 11:34:34', 'admin', '2020-09-09 14:48:51', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('717f6bee46f44a3897eca9abd6e2ec44', '2a470fc0c3954d9dbb61de6d80846549', '个人页', '/account', 'layouts/RouteView', NULL, NULL, 1, NULL, NULL, 12.00, 0, 'user', 1, 0, 0, 1, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2021-05-26 16:39:45', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('841057b8a1bef8f6b4b20f9a618a7fa6', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '数据日志', '/sys/dataLog-list', 'system/DataLogList', NULL, NULL, 1, NULL, NULL, 2.10, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-03-11 19:26:49', 'admin', '2020-09-09 14:48:32', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('882a73768cfd7f78f3a37584f7299656', '6e73eb3c26099c191bf03852ee1310a1', '个性化设置', '/account/settings/custom', 'account/settings/Custom', NULL, NULL, 1, 'CustomSettings', NULL, NULL, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-26 19:00:46', NULL, '2018-12-26 21:13:25', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('8b3bff2eee6f1939147f5c68292a1642', '700b7f95165c46cc7a78bf227aa8fed3', '服务器信息', '/monitor/SystemInfo', 'modules/monitor/SystemInfo', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-02 11:39:19', 'admin', '2019-04-02 15:40:02', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('8d1ebd663688965f1fd86a2f0ead3416', '700b7f95165c46cc7a78bf227aa8fed3', 'Redis监控', '/monitor/redis/info', 'modules/monitor/RedisInfo', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-02 13:11:33', 'admin', '2019-05-07 15:18:54', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('944abf0a8fc22fe1f1154a389a574154', '5c8042bd6c601270b2bbd9b20bccc68b', '消息管理', '/modules/message/sysMessageList', 'modules/message/SysMessageList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-09 11:27:53', 'admin', '2019-04-09 19:31:23', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('9502685863ab87f0ad1134142788a385', '', '首页', '/dashboard/analysis', 'dashboard/Analysis', NULL, NULL, 0, NULL, NULL, 0.00, 0, 'home', 1, 1, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-29 11:04:13', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('97c8629abc7848eccdb6d77c24bb3ebb', '700b7f95165c46cc7a78bf227aa8fed3', '磁盘监控', '/monitor/Disk', 'modules/monitor/DiskMonitoring', NULL, NULL, 1, NULL, NULL, 6.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-25 14:30:06', 'admin', '2019-05-05 14:37:14', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('aedbf679b5773c1f25e9f7b10111da73', '08e6b9dc3c04489c8e1ff2ce6f105aa4', 'SQL监控', '{{ window._CONFIG[\'domianURL\'] }}/monitoring', 'layouts/IframePageView', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-01-30 09:43:22', 'admin', '2020-09-09 14:48:38', 0, 0, NULL, 1);
INSERT INTO `sys_permission` VALUES ('b1cb0a3fedf7ed0e4653cb5a229837ee', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '定时任务', '/isystem/QuartzJobList', 'system/QuartzJobList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2019-01-03 09:38:52', 'admin', '2020-09-09 14:48:16', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('b4dfc7d5dd9e8d5b6dd6d4579b1aa559', 'c65321e57b7949b7a975313220de0422', '500', '/exception/500', 'exception/500', NULL, NULL, 1, NULL, NULL, 3.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('c65321e57b7949b7a975313220de0422', '2a470fc0c3954d9dbb61de6d80846549', '异常页', '/exception', 'layouts/RouteView', NULL, NULL, 1, NULL, NULL, 12.00, 0, 'warning', 1, 0, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2021-05-26 16:39:24', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('d07a2c87a451434c99ab06296727ec4f', '700b7f95165c46cc7a78bf227aa8fed3', 'JVM信息', '/monitor/JvmInfo', 'modules/monitor/JvmInfo', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-01 23:07:48', 'admin', '2019-04-02 11:37:16', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('d2bbf9ebca5a8fa2e227af97d2da7548', 'c65321e57b7949b7a975313220de0422', '404', '/exception/404', 'exception/404', NULL, NULL, 1, NULL, NULL, 2.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('d7d6e2e4e2934f2c9385a623fd98c6f3', '', '系统管理', '/isystem', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 4.00, 0, 'setting', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-31 22:19:52', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('d86f58e7ab516d3bc6bfb1fe10585f97', '717f6bee46f44a3897eca9abd6e2ec44', '个人中心', '/account/center', 'account/center/Index', NULL, NULL, 1, NULL, NULL, 1.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('e08cb190ef230d5d4f03824198773950', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '系统通告', '/isystem/annountCement', 'system/SysAnnouncementList', NULL, NULL, 1, 'annountCement', NULL, 6.00, NULL, '', 1, 1, NULL, NULL, NULL, NULL, '2019-01-02 17:23:01', NULL, '2019-01-02 17:31:23', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('e41b69c57a941a3bbcce45032fe57605', '', '在线开发', '/online', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 5.00, 0, 'cloud', 1, 0, NULL, 0, NULL, 'admin', '2019-03-08 10:43:10', 'admin', '2019-05-11 10:36:01', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('ebb9d82ea16ad864071158e0c449d186', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '分类字典', '/isys/category', 'system/SysCategoryList', NULL, NULL, 1, NULL, '1', 5.20, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-05-29 18:48:07', 'admin', '2020-02-23 22:45:33', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('ec8d607d0156e198b11853760319c646', '6e73eb3c26099c191bf03852ee1310a1', '安全设置', '/account/settings/security', 'account/settings/Security', NULL, NULL, 1, 'SecuritySettings', NULL, NULL, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-26 18:59:52', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('f1cb187abf927c88b89470d08615f5ac', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '数据字典', '/isystem/dict', 'system/DictList', NULL, NULL, 1, NULL, NULL, 5.00, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-28 13:54:43', 'admin', '2020-02-23 22:45:25', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('f780d0d3083d849ccbdb1b1baee4911d', '5c8042bd6c601270b2bbd9b20bccc68b', '模板管理', '/modules/message/sysMessageTemplateList', 'modules/message/SysMessageTemplateList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-09 11:50:31', 'admin', '2019-04-12 10:16:34', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('fc810a2267dd183e4ef7c71cc60f4670', '700b7f95165c46cc7a78bf227aa8fed3', '请求追踪', '/monitor/HttpTrace', 'modules/monitor/HttpTrace', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-02 09:46:19', 'admin', '2019-04-02 11:37:27', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('fedfbf4420536cacc0218557d263dfea', '6e73eb3c26099c191bf03852ee1310a1', '新消息通知', '/account/settings/notification', 'account/settings/Notification', NULL, NULL, 1, 'NotificationSettings', NULL, NULL, NULL, '', 1, 1, NULL, NULL, NULL, NULL, '2018-12-26 19:02:05', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('1438041680501059586', '700b7f95165c46cc7a78bf227aa8fed3', '运行环境', '/monitor/EnvironmentInfo', 'modules/monitor/EnvironmentInfo', NULL, NULL, 1, NULL, '1', 8.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2021-09-15 15:27:09', 'admin', '2021-09-15 15:47:55', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1446311260709236738', '58857ff846e61794c69208e9d3a85466', '日志详情', NULL, NULL, NULL, NULL, 2, 'log:detail', '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'llc', '2021-10-08 11:07:30', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1555081846828613633', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '授权管理', '/isystem/ram', 'system/SysRoleKeyList', NULL, NULL, 1, NULL, '1', 7.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2022-08-04 14:42:59', NULL, NULL, 0, 0, '1', 0);

-- ----------------------------
-- Table structure for sys_permission_data_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_data_rule`;
CREATE TABLE `sys_permission_data_rule`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID',
  `permission_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单ID',
  `rule_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_column` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '字段',
  `rule_conditions` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '条件',
  `rule_value` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则值',
  `status` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限有效状态1有0否',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_fucntionid`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission_data_rule
-- ----------------------------

-- ----------------------------
-- Table structure for sys_permission_history
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_history`;
CREATE TABLE `sys_permission_history`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `permission_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单id',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission_history
-- ----------------------------

-- ----------------------------
-- Table structure for sys_position
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职务编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职务名称',
  `post_rank` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职级',
  `company_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司id',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_position
-- ----------------------------
INSERT INTO `sys_position` VALUES ('1256485574212153345', 'CEO', '总经理', '5', NULL, 'admin', '2020-05-02 15:28:00', 'admin', '2020-05-02 15:28:03', 'A01');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
  `role_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_sys_role_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'admin', '管理员', NULL, '2018-12-21 18:03:39', 'admin', '2019-05-20 11:40:26');
INSERT INTO `sys_role` VALUES ('1456085100369317890', '普通用户', 'user', '普通用户', 'admin', '2021-11-04 10:25:15', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据权限ids',
  `operate_date` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `operate_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作ip',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE,
  INDEX `index_group_role_id`(`role_id`) USING BTREE,
  INDEX `index_group_per_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('0c6b8facbb1cc874964c87a8cf01e4b1', 'f6817f48af4fb3af11b9e8bf182f618b', '841057b8a1bef8f6b4b20f9a618a7fa6', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('0c6e1075e422972083c3e854d9af7851', 'f6817f48af4fb3af11b9e8bf182f618b', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('0e1469997af2d3b97fff56a59ee29eeb', 'f6817f48af4fb3af11b9e8bf182f618b', 'e41b69c57a941a3bbcce45032fe57605', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('0f861cb988fdc639bb1ab943471f3a72', 'f6817f48af4fb3af11b9e8bf182f618b', '97c8629abc7848eccdb6d77c24bb3ebb', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1185039870491439105', 'f6817f48af4fb3af11b9e8bf182f618b', '1174506953255182338', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1185039870529187841', 'f6817f48af4fb3af11b9e8bf182f618b', '1174590283938041857', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1197431682208206850', 'f6817f48af4fb3af11b9e8bf182f618b', '1192318987661234177', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1209423580355481602', 'f6817f48af4fb3af11b9e8bf182f618b', '190c2b43bec6a5f7a4194a85db67d96a', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1231590078632955905', 'f6817f48af4fb3af11b9e8bf182f618b', '1224641973866467330', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1231590078658121729', 'f6817f48af4fb3af11b9e8bf182f618b', '1209731624921534465', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1260928399955836929', 'f6817f48af4fb3af11b9e8bf182f618b', '1260928341675982849', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1269526122208522241', 'f6817f48af4fb3af11b9e8bf182f618b', '1267412134208319489', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('126ea9faebeec2b914d6d9bef957afb6', 'f6817f48af4fb3af11b9e8bf182f618b', 'f1cb187abf927c88b89470d08615f5ac', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1281494164924653569', 'f6817f48af4fb3af11b9e8bf182f618b', '1280350452934307841', NULL, '2020-07-10 15:43:13', NULL);
INSERT INTO `sys_role_permission` VALUES ('1281494684632473602', 'f6817f48af4fb3af11b9e8bf182f618b', '1265162119913824258', NULL, '2020-07-10 15:45:16', NULL);
INSERT INTO `sys_role_permission` VALUES ('1335877076745928705', 'f6817f48af4fb3af11b9e8bf182f618b', '1335876951575314434', NULL, '2020-12-07 17:21:29', NULL);
INSERT INTO `sys_role_permission` VALUES ('1336134372344406018', 'f6817f48af4fb3af11b9e8bf182f618b', '1336133158097592321', NULL, '2020-12-08 10:23:53', NULL);
INSERT INTO `sys_role_permission` VALUES ('1336136136401559554', 'f6817f48af4fb3af11b9e8bf182f618b', '1336136043015380994', NULL, '2020-12-08 10:30:54', NULL);
INSERT INTO `sys_role_permission` VALUES ('1336136136401559555', 'f6817f48af4fb3af11b9e8bf182f618b', '1336135755432927234', NULL, '2020-12-08 10:30:54', NULL);
INSERT INTO `sys_role_permission` VALUES ('1408266208303218689', 'f6817f48af4fb3af11b9e8bf182f618b', '1408265706710597633', NULL, '2021-06-25 11:30:03', NULL);
INSERT INTO `sys_role_permission` VALUES ('1437700111170310146', 'f6817f48af4fb3af11b9e8bf182f618b', '1437700059899138049', NULL, '2021-09-14 16:49:52', NULL);
INSERT INTO `sys_role_permission` VALUES ('165acd6046a0eaf975099f46a3c898ea', 'f6817f48af4fb3af11b9e8bf182f618b', '4f66409ef3bbd69c1d80469d6e2a885e', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1664b92dff13e1575e3a929caa2fa14d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd2bbf9ebca5a8fa2e227af97d2da7548', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1e47db875601fd97723254046b5bba90', 'f6817f48af4fb3af11b9e8bf182f618b', 'baf16b7174bd821b6bab23fa9abb200d', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('25491ecbd5a9b34f09c8bc447a10ede1', 'f6817f48af4fb3af11b9e8bf182f618b', 'd07a2c87a451434c99ab06296727ec4f', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('2779cdea8367fff37db26a42c1a1f531', 'f6817f48af4fb3af11b9e8bf182f618b', 'fef097f3903caf3a3c3a6efa8de43fbb', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('2ad37346c1b83ddeebc008f6987b2227', 'f6817f48af4fb3af11b9e8bf182f618b', '8d1ebd663688965f1fd86a2f0ead3416', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('38a2e55db0960262800576e34b3af44c', 'f6817f48af4fb3af11b9e8bf182f618b', '5c2f42277948043026b7a14692456828', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('3de2a60c7e42a521fecf6fcc5cb54978', 'f6817f48af4fb3af11b9e8bf182f618b', '2d83d62bd2544b8994c8f38cf17b0ddf', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('4204f91fb61911ba8ce40afa7c02369f', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('4dab5a06acc8ef3297889872caa74747', 'f6817f48af4fb3af11b9e8bf182f618b', 'ffb423d25cc59dcd0532213c4a518261', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('4ea403fc1d19feb871c8bdd9f94a4ecc', 'f6817f48af4fb3af11b9e8bf182f618b', '2e42e3835c2b44ec9f7bc26c146ee531', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('4f254549d9498f06f4cc9b23f3e2c070', 'f6817f48af4fb3af11b9e8bf182f618b', '93d5cfb4448f11e9916698e7f462b4b6', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('504e326de3f03562cdd186748b48a8c7', 'f6817f48af4fb3af11b9e8bf182f618b', '027aee69baee98a0ed2e01806e89c891', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('5d230e6cd2935c4117f6cb9a7a749e39', 'f6817f48af4fb3af11b9e8bf182f618b', 'fc810a2267dd183e4ef7c71cc60f4670', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('5de6871fadb4fe1cdd28989da0126b07', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160a3432231af', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('5e4015a9a641cbf3fb5d28d9f885d81a', 'f6817f48af4fb3af11b9e8bf182f618b', '2dbbafa22cda07fa5d169d741b81fe12', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('66b202f8f84fe766176b3f51071836ef', 'f6817f48af4fb3af11b9e8bf182f618b', '1367a93f2c410b169faa7abcbad2f77c', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('6c74518eb6bb9a353f6a6c459c77e64b', 'f6817f48af4fb3af11b9e8bf182f618b', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('6daddafacd7eccb91309530c17c5855d', 'f6817f48af4fb3af11b9e8bf182f618b', 'edfa74d66e8ea63ea432c2910837b150', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('6fb4c2142498dd6d5b6c014ef985cb66', 'f6817f48af4fb3af11b9e8bf182f618b', '6e73eb3c26099c191bf03852ee1310a1', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('7413acf23b56c906aedb5a36fb75bd3a', 'f6817f48af4fb3af11b9e8bf182f618b', 'a4fc7b64b01a224da066bb16230f9c5a', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('76a54a8cc609754360bf9f57e7dbb2db', 'f6817f48af4fb3af11b9e8bf182f618b', 'c65321e57b7949b7a975313220de0422', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('7ca833caa5eac837b7200d8b6de8b2e3', 'f6817f48af4fb3af11b9e8bf182f618b', 'fedfbf4420536cacc0218557d263dfea', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('84eac2f113c23737128fb099d1d1da89', 'f6817f48af4fb3af11b9e8bf182f618b', '03dc3d93261dda19fc86dd7ca486c6cf', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('86060e2867a5049d8a80d9fe5d8bc28b', 'f6817f48af4fb3af11b9e8bf182f618b', '765dd244f37b804e3d00f475fd56149b', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('8b09925bdc194ab7f3559cd3a7ea0507', 'f6817f48af4fb3af11b9e8bf182f618b', 'ebb9d82ea16ad864071158e0c449d186', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('8d154c2382a8ae5c8d1b84bd38df2a93', 'f6817f48af4fb3af11b9e8bf182f618b', 'd86f58e7ab516d3bc6bfb1fe10585f97', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('9380121ca9cfee4b372194630fce150e', 'f6817f48af4fb3af11b9e8bf182f618b', '65a8f489f25a345836b7f44b1181197a', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('94911fef73a590f6824105ebf9b6cab3', 'f6817f48af4fb3af11b9e8bf182f618b', '8b3bff2eee6f1939147f5c68292a1642', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('9700d20dbc1ae3cbf7de1c810b521fe6', 'f6817f48af4fb3af11b9e8bf182f618b', 'ec8d607d0156e198b11853760319c646', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('980171fda43adfe24840959b1d048d4d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('987c23b70873bd1d6dca52f30aafd8c2', 'f6817f48af4fb3af11b9e8bf182f618b', '00a2a0ae65cdca5e93209cdbde97cbe6', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('9d980ec0489040e631a9c24a6af42934', 'f6817f48af4fb3af11b9e8bf182f618b', '05b3c82ddb2536a4a5ee1a4c46b5abef', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('a034ed7c38c996b880d3e78f586fe0ae', 'f6817f48af4fb3af11b9e8bf182f618b', 'c89018ea6286e852b424466fd92a2ffc', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('acacce4417e5d7f96a9c3be2ded5b4be', 'f6817f48af4fb3af11b9e8bf182f618b', 'f9d3f4f27653a71c52faa9fb8070fbe7', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('af60ac8fafd807ed6b6b354613b9ccbc', 'f6817f48af4fb3af11b9e8bf182f618b', '58857ff846e61794c69208e9d3a85466', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('bbec16ad016efec9ea2def38f4d3d9dc', 'f6817f48af4fb3af11b9e8bf182f618b', '13212d3416eb690c2e1d5033166ff47a', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('bea2986432079d89203da888d99b3f16', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('c90b0b01c7ca454d2a1cb7408563e696', 'f6817f48af4fb3af11b9e8bf182f618b', '882a73768cfd7f78f3a37584f7299656', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('cf1feb1bf69eafc982295ad6c9c8d698', 'f6817f48af4fb3af11b9e8bf182f618b', 'a2b11669e98c5fe54a53c3e3c4f35d14', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('cf2ef620217673e4042f695743294f01', 'f6817f48af4fb3af11b9e8bf182f618b', '717f6bee46f44a3897eca9abd6e2ec44', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('cf43895aef7fc684669483ab00ef2257', 'f6817f48af4fb3af11b9e8bf182f618b', '700b7f95165c46cc7a78bf227aa8fed3', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('d37ad568e26f46ed0feca227aa9c2ffa', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('d8a5c9079df12090e108e21be94b4fd7', 'f6817f48af4fb3af11b9e8bf182f618b', '078f9558cdeab239aecb2bda1a8ed0d1', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('dbc5dd836d45c5bc7bc94b22596ab956', 'f6817f48af4fb3af11b9e8bf182f618b', '1939e035e803a99ceecb6f5563570fb2', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('dc83bb13c0e8c930e79d28b2db26f01f', 'f6817f48af4fb3af11b9e8bf182f618b', '63b551e81c5956d5c861593d366d8c57', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('dc8fd3f79bd85bd832608b42167a1c71', 'f6817f48af4fb3af11b9e8bf182f618b', '91c23960fab49335831cf43d820b0a61', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('de82e89b8b60a3ea99be5348f565c240', 'f6817f48af4fb3af11b9e8bf182f618b', '56ca78fe0f22d815fabc793461af67b8', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('e7467726ee72235baaeb47df04a35e73', 'f6817f48af4fb3af11b9e8bf182f618b', 'e08cb190ef230d5d4f03824198773950', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('eaef4486f1c9b0408580bbfa2037eb66', 'f6817f48af4fb3af11b9e8bf182f618b', '2a470fc0c3954d9dbb61de6d80846549', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('ec846a3f85fdb6813e515be71f11b331', 'f6817f48af4fb3af11b9e8bf182f618b', '732d48f8e0abe99fe6a23d18a3171cd1', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('ecdd72fe694e6bba9c1d9fc925ee79de', 'f6817f48af4fb3af11b9e8bf182f618b', '45c966826eeff4c99b8f8ebfe74511fc', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('f177acac0276329dc66af0c9ad30558a', 'f6817f48af4fb3af11b9e8bf182f618b', 'c2c356bf4ddd29975347a7047a062440', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1438041770103975937', 'f6817f48af4fb3af11b9e8bf182f618b', '1438041680501059586', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1441998626820239362', 'f6817f48af4fb3af11b9e8bf182f618b', '5c8042bd6c601270b2bbd9b20bccc68b', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1441998626803462146', 'f6817f48af4fb3af11b9e8bf182f618b', '53a9230444d33de28aa11cc108fb1dba', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1438041770103975939', 'f6817f48af4fb3af11b9e8bf182f618b', '1446311260709236738', NULL, '2021-09-15 15:27:30', NULL);
INSERT INTO `sys_role_permission` VALUES ('1555083117631422466', 'f6817f48af4fb3af11b9e8bf182f618b', '1555081846828613633', NULL, '2022-08-04 14:48:02', NULL);
INSERT INTO `sys_role_permission` VALUES ('1559084895530074114', 'f6817f48af4fb3af11b9e8bf182f618b', 'aedbf679b5773c1f25e9f7b10111da73', NULL, NULL, NULL);



-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` int(11) NOT NULL COMMENT '租户编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '租户名称',
  `begin_date` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `status` bit(1) NULL DEFAULT NULL COMMENT '状态 1正常 0冻结',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '多租户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1, '四川天翼网络服务有限公司', NULL, NULL, 1, 'admin', '2020-07-10 15:43:32');

-- ----------------------------
-- Table structure for sys_third_account
-- ----------------------------
DROP TABLE IF EXISTS `sys_third_account`;
CREATE TABLE `sys_third_account`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '编号',
  `sys_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '第三方登录id',
  `third_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录来源',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态(1-正常,2-冻结)',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)',
  `realname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `third_user_uuid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '第三方账号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_third_account
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '机构编码',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '性别(1-正常,2-冻结)',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)',
  `third_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '第三方登录的唯一标识',
  `third_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '第三方类型',
  `work_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工号，唯一键',
  `post` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职务，关联职务表',
  `telephone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '座机号',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `user_identity` tinyint(1) NULL DEFAULT NULL COMMENT '身份（1普通成员 2上级）',
  `depart_ids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '负责部门',
  `rel_tenant_ids` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '多租户标识',
  `first_login` bit(1) NOT NULL DEFAULT 1 COMMENT '是否首次登录（0否 1是）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_username`(`username`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_phone`(`phone`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_email`(`email`) USING BTREE,
  INDEX `idx_sys_user_status`(`status`) USING BTREE,
  INDEX `idx_sys_user_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('e9ca23d68d884d4ebb19d07889727dae', 'admin', '管理员', 'cb362cfeefbf3d8d', 'RCGTeGiH', '', '2020-05-27 00:00:00', 1, 'cube@tievd.com', '18611111111', 'A01', 1, 0, NULL, NULL, '00001', 'CEO', NULL, NULL, '2020-06-21 17:54:10', 'admin', '2020-07-10 15:27:10', 2, 'c6d7cb4deeac411cb3384b1b31278596', '', 0);

-- ----------------------------
-- Table structure for sys_user_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_depart`;
CREATE TABLE `sys_user_depart`  (
  `ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户id',
  `dep_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_depart_groupk_userid`(`user_id`) USING BTREE,
  INDEX `index_depart_groupkorgid`(`dep_id`) USING BTREE,
  INDEX `index_depart_groupk_uidanddid`(`user_id`, `dep_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_depart
-- ----------------------------
INSERT INTO `sys_user_depart` VALUES ('1281490128540393474', 'e9ca23d68d884d4ebb19d07889727dae', 'c6d7cb4deeac411cb3384b1b31278596');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sur_user_id`(`user_id`) USING BTREE,
  INDEX `idx_sur_role_id`(`role_id`) USING BTREE,
  INDEX `idx_sur_user_role_id`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1281490128242597889', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');

-- ----------------------------
-- Table structure for sys_role_key
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_key`;
CREATE TABLE `sys_role_key`  (
  `id` bigint(20) NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `access_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'AK',
  `secret_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'SK',
  `enabled` bit(1) NULL DEFAULT NULL COMMENT '是否启用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'API授权管理' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;