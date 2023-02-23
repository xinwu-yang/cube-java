/*
 魔方基础数据库脚本-达梦数据库8
 
 Release Date: 2022-12-05
 Version: 2.6.4
*/

CREATE TABLE "CUBE"."sys_user_role"
(
 "id" VARCHAR(32) NOT NULL,
 "user_id" VARCHAR(32) NULL,
 "role_id" VARCHAR(32) NULL
);
CREATE TABLE "CUBE"."sys_user_depart"
(
 "ID" VARCHAR(32) NOT NULL,
 "user_id" VARCHAR(32) NULL,
 "dep_id" VARCHAR(32) NULL
);
CREATE TABLE "CUBE"."sys_user"
(
 "id" VARCHAR(32) NOT NULL,
 "username" VARCHAR(100) NULL,
 "realname" VARCHAR(100) NULL,
 "password" VARCHAR(255) NULL,
 "salt" VARCHAR(45) NULL,
 "avatar" VARCHAR(255) NULL,
 "birthday" TIMESTAMP(0) NULL,
 "sex" TINYINT NULL,
 "email" VARCHAR(45) NULL,
 "phone" VARCHAR(45) NULL,
 "org_code" VARCHAR(64) NULL,
 "status" TINYINT NULL,
 "del_flag" TINYINT DEFAULT 0
 NULL,
 "third_id" VARCHAR(100) NULL,
 "third_type" VARCHAR(100) NULL,
 "work_no" VARCHAR(100) NULL,
 "post" VARCHAR(100) NULL,
 "telephone" VARCHAR(45) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "user_identity" TINYINT NULL,
 "depart_ids" CLOB NULL,
 "rel_tenant_ids" VARCHAR(100) NULL,
 "first_login" BIT DEFAULT '1'
 NOT NULL
);
CREATE TABLE "CUBE"."sys_third_account"
(
 "id" VARCHAR(32) NOT NULL,
 "sys_user_id" VARCHAR(32) NULL,
 "third_type" VARCHAR(255) NULL,
 "avatar" VARCHAR(255) NULL,
 "status" TINYINT NULL,
 "del_flag" TINYINT DEFAULT 0
 NULL,
 "realname" VARCHAR(100) NULL,
 "third_user_uuid" VARCHAR(100) NULL
);
CREATE TABLE "CUBE"."sys_tenant"
(
 "id" INT NOT NULL,
 "name" VARCHAR(100) NULL,
 "begin_date" TIMESTAMP(0) NULL,
 "end_date" TIMESTAMP(0) NULL,
 "status" BIT NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_role_permission"
(
 "id" VARCHAR(32) NOT NULL,
 "role_id" VARCHAR(32) NULL,
 "permission_id" VARCHAR(32) NULL,
 "data_rule_ids" VARCHAR(1000) NULL,
 "operate_date" TIMESTAMP(0) NULL,
 "operate_ip" VARCHAR(20) NULL
);
CREATE TABLE "CUBE"."sys_role_key"
(
 "id" BIGINT NOT NULL,
 "username" VARCHAR(100) NULL,
 "access_key" VARCHAR(32) NULL,
 "secret_key" VARCHAR(32) NULL,
 "enabled" BIT NULL,
 "create_time" TIMESTAMP(0) NULL,
 "create_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL
);
CREATE TABLE "CUBE"."sys_role"
(
 "id" VARCHAR(32) NOT NULL,
 "role_name" VARCHAR(200) NULL,
 "role_code" VARCHAR(100) NOT NULL,
 "description" VARCHAR(255) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_position"
(
 "id" VARCHAR(32) NOT NULL,
 "code" VARCHAR(100) NULL,
 "name" VARCHAR(100) NULL,
 "post_rank" VARCHAR(2) NULL,
 "company_id" VARCHAR(255) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "sys_org_code" VARCHAR(50) NULL
);
CREATE TABLE "CUBE"."sys_permission_history"
(
 "id" BIGINT NOT NULL,
 "permission_id" VARCHAR(64) NOT NULL,
 "user_id" VARCHAR(64) NOT NULL,
 "create_time" TIMESTAMP(0) NOT NULL
);
CREATE TABLE "CUBE"."sys_permission_data_rule"
(
 "id" VARCHAR(32) NOT NULL,
 "permission_id" VARCHAR(32) NULL,
 "rule_name" VARCHAR(50) NULL,
 "rule_column" VARCHAR(50) NULL,
 "rule_conditions" VARCHAR(50) NULL,
 "rule_value" VARCHAR(300) NULL,
 "status" VARCHAR(3) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_permission"
(
 "id" VARCHAR(32) NOT NULL,
 "parent_id" VARCHAR(32) NULL,
 "name" VARCHAR(100) NULL,
 "url" VARCHAR(255) NULL,
 "component" VARCHAR(255) NULL,
 "component_name" VARCHAR(100) NULL,
 "redirect" VARCHAR(255) NULL,
 "menu_type" INT NULL,
 "perms" VARCHAR(255) NULL,
 "perms_type" VARCHAR(10) DEFAULT '0'
 NULL,
 "sort_no" DOUBLE NULL,
 "always_show" TINYINT NULL,
 "icon" VARCHAR(100) NULL,
 "is_route" TINYINT DEFAULT 1
 NULL,
 "is_leaf" TINYINT NULL,
 "keep_alive" TINYINT NULL,
 "hidden" INT DEFAULT 0
 NULL,
 "description" VARCHAR(255) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "del_flag" TINYINT DEFAULT 0
 NULL,
 "rule_flag" INT DEFAULT 0
 NULL,
 "status" VARCHAR(2) NULL,
 "internal_or_external" TINYINT NULL
);
CREATE TABLE "CUBE"."sys_log"
(
 "id" VARCHAR(32) NOT NULL,
 "log_type" INT NULL,
 "log_content" VARCHAR(1000) NULL,
 "operate_type" INT NULL,
 "userid" VARCHAR(32) NULL,
 "username" VARCHAR(100) NULL,
 "ip" VARCHAR(100) NULL,
 "method" VARCHAR(500) NULL,
 "request_param" CLOB NULL,
 "cost_time" BIGINT NULL,
 "create_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_gateway_route"
(
 "id" VARCHAR(36) NOT NULL,
 "router_id" VARCHAR(50) NULL,
 "name" VARCHAR(32) NULL,
 "uri" VARCHAR(32) NULL,
 "predicates" TEXT NULL,
 "filters" TEXT NULL,
 "retryable" INT NULL,
 "strip_prefix" INT NULL,
 "persist" INT NULL,
 "show_api" INT NULL,
 "status" INT NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "sys_org_code" VARCHAR(64) NULL
);
CREATE TABLE "CUBE"."sys_fill_rule"
(
 "id" VARCHAR(32) NOT NULL,
 "rule_name" VARCHAR(100) NULL,
 "rule_code" VARCHAR(100) NULL,
 "rule_class" VARCHAR(100) NULL,
 "rule_params" VARCHAR(200) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_dict_item"
(
 "id" VARCHAR(32) NOT NULL,
 "dict_id" VARCHAR(32) NULL,
 "item_text" VARCHAR(100) NOT NULL,
 "item_value" VARCHAR(100) NOT NULL,
 "description" VARCHAR(255) NULL,
 "sort_order" INT NULL,
 "status" INT NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_dict"
(
 "id" VARCHAR(32) NOT NULL,
 "dict_name" VARCHAR(100) NOT NULL,
 "dict_code" VARCHAR(100) NOT NULL,
 "description" VARCHAR(255) NULL,
 "del_flag" TINYINT DEFAULT 0
 NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "type" BIGINT DEFAULT 0
 NULL
);
CREATE TABLE "CUBE"."sys_depart_role_user"
(
 "id" VARCHAR(32) NOT NULL,
 "user_id" VARCHAR(32) NULL,
 "drole_id" VARCHAR(32) NULL
);
CREATE TABLE "CUBE"."sys_depart_role_permission"
(
 "id" VARCHAR(32) NOT NULL,
 "depart_id" VARCHAR(32) NULL,
 "role_id" VARCHAR(32) NULL,
 "permission_id" VARCHAR(32) NULL,
 "data_rule_ids" VARCHAR(1000) NULL,
 "operate_date" TIMESTAMP(0) NULL,
 "operate_ip" VARCHAR(20) NULL
);
CREATE TABLE "CUBE"."sys_depart_role"
(
 "id" VARCHAR(32) NOT NULL,
 "depart_id" VARCHAR(32) NULL,
 "role_name" VARCHAR(200) NULL,
 "role_code" VARCHAR(100) NULL,
 "description" VARCHAR(255) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_depart_permission"
(
 "id" VARCHAR(32) NOT NULL,
 "depart_id" VARCHAR(32) NULL,
 "permission_id" VARCHAR(32) NULL,
 "data_rule_ids" VARCHAR(1000) NULL
);
CREATE TABLE "CUBE"."sys_depart"
(
 "id" VARCHAR(32) NOT NULL,
 "parent_id" VARCHAR(32) NULL,
 "depart_name" VARCHAR(100) NOT NULL,
 "depart_name_en" VARCHAR(500) NULL,
 "depart_name_abbr" VARCHAR(500) NULL,
 "depart_order" INT DEFAULT 0
 NULL,
 "description" VARCHAR(500) NULL,
 "area_id" BIGINT NULL,
 "gb_code" VARCHAR(64) NULL,
 "org_category" VARCHAR(10) DEFAULT '1'
 NOT NULL,
 "org_type" VARCHAR(10) NULL,
 "org_code" VARCHAR(64) NOT NULL,
 "mobile" VARCHAR(32) NULL,
 "fax" VARCHAR(32) NULL,
 "address" VARCHAR(100) NULL,
 "memo" VARCHAR(500) NULL,
 "status" VARCHAR(1) NULL,
 "del_flag" TINYINT DEFAULT 0
 NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_data_source"
(
 "id" VARCHAR(36) NOT NULL,
 "code" VARCHAR(100) NULL,
 "name" VARCHAR(100) NULL,
 "remark" VARCHAR(200) NULL,
 "db_type" VARCHAR(10) NULL,
 "db_driver" VARCHAR(100) NULL,
 "db_url" VARCHAR(500) NULL,
 "db_name" VARCHAR(100) NULL,
 "db_username" VARCHAR(100) NULL,
 "db_password" VARCHAR(100) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "sys_org_code" VARCHAR(64) NULL
);
CREATE TABLE "CUBE"."sys_data_log"
(
 "id" VARCHAR(32) NOT NULL,
 "data_table" VARCHAR(32) NULL,
 "data_id" VARCHAR(32) NULL,
 "data_content" TEXT NULL,
 "data_version" INT NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_check_rule"
(
 "id" VARCHAR(32) NOT NULL,
 "rule_name" VARCHAR(100) NULL,
 "rule_code" VARCHAR(100) NULL,
 "rule_json" VARCHAR(1024) NULL,
 "rule_description" VARCHAR(200) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_category"
(
 "id" VARCHAR(36) NOT NULL,
 "pid" VARCHAR(36) NULL,
 "name" VARCHAR(100) NULL,
 "code" VARCHAR(100) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "sys_org_code" VARCHAR(64) NULL,
 "has_child" VARCHAR(3) NULL
);
CREATE TABLE "CUBE"."sys_area"
(
 "id" BIGINT NOT NULL,
 "parent_id" BIGINT NULL,
 "name" VARCHAR(255) NULL,
 "gb_code" VARCHAR(255) NULL,
 "post_code" VARCHAR(255) NULL,
 "level" INT NULL,
 "path" VARCHAR(255) NULL,
 "del_flag" TINYINT DEFAULT 0
 NULL,
 "create_time" TIMESTAMP(0) NULL,
 "has_children" BIT NULL
);
CREATE TABLE "CUBE"."sys_announcement_send"
(
 "id" VARCHAR(32) NULL,
 "annt_id" VARCHAR(32) NULL,
 "user_id" VARCHAR(32) NULL,
 "read_flag" VARCHAR(10) NULL,
 "read_time" TIMESTAMP(0) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL
);
CREATE TABLE "CUBE"."sys_announcement"
(
 "id" VARCHAR(32) NOT NULL,
 "title" VARCHAR(100) NULL,
 "msg_content" TEXT NULL,
 "start_time" TIMESTAMP(0) NULL,
 "end_time" TIMESTAMP(0) NULL,
 "sender" VARCHAR(100) NULL,
 "priority" VARCHAR(255) NULL,
 "msg_category" VARCHAR(10) DEFAULT '2'
 NOT NULL,
 "msg_type" VARCHAR(10) NULL,
 "send_status" VARCHAR(10) NULL,
 "send_time" TIMESTAMP(0) NULL,
 "cancel_time" TIMESTAMP(0) NULL,
 "del_flag" TINYINT DEFAULT 0
 NULL,
 "bus_type" VARCHAR(20) NULL,
 "bus_id" VARCHAR(50) NULL,
 "open_type" VARCHAR(20) NULL,
 "open_page" VARCHAR(255) NULL,
 "create_by" VARCHAR(100) NULL,
 "create_time" TIMESTAMP(0) NULL,
 "update_by" VARCHAR(100) NULL,
 "update_time" TIMESTAMP(0) NULL,
 "user_ids" TEXT NULL,
 "msg_abstract" TEXT NULL
);
INSERT INTO "CUBE"."sys_check_rule"("id","rule_name","rule_code","rule_json","rule_description","create_by","create_time","update_by","update_time") VALUES('1224980593992388610','通用编码规则','common','[{"digits":"1","pattern":"^[a-z|A-Z]$","message":"第一位只能是字母"},{"digits":"*","pattern":"^[0-9|a-z|A-Z|_]{0,}$","message":"只能填写数字、大小写字母、下划线"},{"digits":"*","pattern":"^.{3,}$","message":"最少输入3位数"},{"digits":"*","pattern":"^.{3,12}$","message":"最多输入12位数"}]','规则：1、首位只能是字母；2、只能填写数字、大小写字母、下划线；3、最少3位数，最多12位数。','admin','2020-02-07 11:25:48','admin','2020-02-05 16:58:27');
INSERT INTO "CUBE"."sys_check_rule"("id","rule_name","rule_code","rule_json","rule_description","create_by","create_time","update_by","update_time") VALUES('1225001845524004866','负责的功能测试','test','[{"digits":"*","pattern":"^.{3,12}$","message":"只能输入3-12位字符"},{"digits":"3","pattern":"^\\d{3}$","message":"前3位必须是数字"},{"digits":"*","pattern":"^[^pP]*$","message":"不能输入P"},{"digits":"4","pattern":"^@{4}$","message":"第4-7位必须都为 @"},{"digits":"2","pattern":"^#=$","message":"第8-9位必须是 #="},{"digits":"1","pattern":"^O$","message":"第10位必须为大写的O"},{"digits":"*","pattern":"^.*。$","message":"必须以。结尾"}]','包含长度校验、特殊字符校验等','admin','2020-02-07 11:57:31','admin','2020-02-05 18:22:54');

INSERT INTO "CUBE"."sys_data_source"("id","code","name","remark","db_type","db_driver","db_url","db_name","db_username","db_password","create_by","create_time","update_by","update_time","sys_org_code") VALUES('1209779538310004737','local_mysql','MySQL5.7','本地数据库MySQL5.7','1','com.mysql.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/cube?characterEncoding=UTF-8&useUnicode=true&useSSL=false','roots','root','f5b6775e8d1749483f2320627de0e706','admin','2019-12-25 18:14:53','admin','2020-07-10 16:54:42','A01');

INSERT INTO "CUBE"."sys_depart"("id","parent_id","depart_name","depart_name_en","depart_name_abbr","depart_order","description","area_id","gb_code","org_category","org_type","org_code","mobile","fax","address","memo","status","del_flag","create_by","create_time","update_by","update_time") VALUES('c6d7cb4deeac411cb3384b1b31278596','','四川天翼网络服务有限公司',null,null,0,null,null,null,'1','1','A01',null,null,null,null,null,0,'admin','2019-02-11 14:21:51','admin','2020-05-02 18:21:27');

INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('0b5d19e1fce4b2e6647e6b4a17760c14','通告类型','msg_category','消息类型1:通知公告2:系统消息',0,'admin','2019-04-22 18:01:35',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1174509082208395266','职务职级','position_rank','职务表职级字典',0,'admin','2019-09-19 10:22:41',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1174511106530525185','机构类型','org_category','机构类型 1公司，2部门 3岗位',0,'admin','2019-09-19 10:30:43',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1199517671259906049','紧急程度','urgent_level','日程计划紧急程度',0,'admin','2019-11-27 10:37:53',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1199518099888414722','日程计划类型','eoa_plan_type','',0,'admin','2019-11-27 10:39:36',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1199520177767587841','分类栏目类型','eoa_cms_menu_type','',0,'admin','2019-11-27 10:47:51','admin','2019-11-27 10:49:35',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1199525215290306561','日程计划状态','eoa_plan_status','',0,'admin','2019-11-27 11:07:52','admin','2019-11-27 11:10:11',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1209733563293962241','数据库类型','database_type','',0,'admin','2019-12-25 15:12:12',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1250687930947620866','定时任务状态','quartz_status','',0,'admin','2020-04-16 15:30:14','',null,null);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('1280401766745718786','租户状态','tenant_status','租户状态',0,'admin','2020-07-07 15:22:25',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('236e8a4baff0db8c62c00dd95632834f','同步工作流引擎','activiti_sync','同步工作流引擎',0,'admin','2019-05-15 15:27:33',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('2e02df51611a4b9632828ab7e5338f00','权限策略','perms_type','权限策略',0,'admin','2019-04-26 18:26:55',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('2f0320997ade5dd147c90130f7218c3e','推送类别','msg_type','',0,'admin','2019-03-17 21:21:32','admin','2019-03-26 19:57:45',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('3486f32803bb953e7155dab3513dc68b','删除状态','del_flag',null,0,'admin','2019-01-18 21:46:26','admin','2019-03-30 11:17:11',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('3d9a351be3436fbefb1307d4cfb49bf2','性别','sex',null,0,null,'2019-01-04 14:56:32','admin','2019-03-30 11:28:27',1);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('4274efc2292239b6f000b153f50823ff','全局权限策略','global_perms_type','全局权限策略',0,'admin','2019-05-10 17:54:05',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('4c753b5293304e7a445fd2741b46529d','字典状态','dict_item_status',null,0,'admin','2020-06-18 23:18:42','admin','2019-03-30 19:33:52',1);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('4d7fec1a7799a436d26d02325eff295e','优先级','priority','优先级',0,'admin','2019-03-16 17:03:34','admin','2019-04-16 17:39:23',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('4e4602b3e3686f0911384e188dc7efb4','条件规则','rule_conditions','',0,'admin','2019-04-01 10:15:03','admin','2019-04-01 10:30:47',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('4f69be5f507accea8d5df5f11346181a','发送消息类型','msgType',null,0,'admin','2019-04-11 14:27:09',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('68168534ff5065a152bfab275c2136f8','有效无效状态','valid_status','有效无效状态',0,'admin','2020-09-26 19:21:14','admin','2019-04-26 19:21:23',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('78bda155fe380b1b3f175f1e88c284c6','流程状态','bpm_status','流程状态',0,'admin','2019-05-09 16:31:52',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('83bfb33147013cc81640d5fd9eda030c','日志类型','log_type',null,0,'admin','2019-03-18 23:22:19',null,null,1);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('845da5006c97754728bf48b6a10f79cc','状态','status',null,0,'admin','2019-03-18 21:45:25','admin','2019-03-18 21:58:25',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('880a895c98afeca9d9ac39f29e67c13e','操作类型','operate_type','操作类型',0,'admin','2019-07-22 10:54:29',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('8dfe32e2d29ea9430a988b3b558bf233','发布状态','send_status','发布状态',0,'admin','2019-04-16 17:40:42',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('a7adbcd86c37f7dbc9b66945c82ef9e6','1是0否','yn','',0,'admin','2019-05-22 19:29:29',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('a9d9942bd0eccb6e89de92d130ec4c4a','消息发送状态','msgSendStatus',null,0,'admin','2019-04-12 18:18:17',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('ac2f7c0c5c5775fcea7e2387bcb22f01','菜单类型','menu_type',null,0,'admin','2020-12-18 23:24:32','admin','2019-04-01 15:27:06',1);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('c36169beb12de8a71c8683ee7c28a503','部门状态','depart_status',null,0,'admin','2019-03-18 21:59:51',null,null,0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('d6e1152968b02d69ff358c75b48a6ee1','流程类型','bpm_process_type',null,0,'admin','2021-02-22 19:26:54','admin','2019-03-30 18:14:44',0);
INSERT INTO "CUBE"."sys_dict"("id","dict_name","dict_code","description","del_flag","create_by","create_time","update_by","update_time","type") VALUES('fc6cd58fde2e8481db10d3a1e68ce70c','用户状态','user_status',null,0,'admin','2019-03-18 21:57:25','admin','2019-03-18 23:11:58',1);

INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('0072d115e07c875d76c9b022e2179128','4d7fec1a7799a436d26d02325eff295e','低','L','低',3,1,'admin','2019-04-16 17:04:59',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('05a2e732ce7b00aa52141ecc3e330b4e','3486f32803bb953e7155dab3513dc68b','已删除','1',null,null,1,'admin','2025-10-18 21:46:56','admin','2019-03-28 22:23:20');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('0c9532916f5cd722017b46bc4d953e41','2f0320997ade5dd147c90130f7218c3e','指定用户','USER',null,null,1,'admin','2019-03-17 21:22:19','admin','2019-03-17 21:22:28');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1030a2652608f5eac3b49d70458b8532','2e02df51611a4b9632828ab7e5338f00','禁用','2','禁用',2,1,'admin','2021-03-26 18:27:28','admin','2019-04-26 18:39:11');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1174509082208395266','1174511106530525185','岗位','3','岗位',1,1,'admin','2019-09-19 10:31:16','',null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1174509601047994369','1174509082208395266','员级','1','',1,1,'admin','2019-09-19 10:24:45','admin','2019-09-23 11:46:39');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1174509667297026049','1174509082208395266','助级','2','',2,1,'admin','2019-09-19 10:25:01','admin','2019-09-23 11:46:47');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1174509713568587777','1174509082208395266','中级','3','',3,1,'admin','2019-09-19 10:25:12','admin','2019-09-23 11:46:56');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1174509788361416705','1174509082208395266','副高级','4','',4,1,'admin','2019-09-19 10:25:30','admin','2019-09-23 11:47:06');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1174509835803189250','1174509082208395266','正高级','5','',5,1,'admin','2019-09-19 10:25:41','admin','2019-09-23 11:47:12');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1174511197735665665','1174511106530525185','公司','1','公司',1,1,'admin','2019-09-19 10:31:05',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1174511244036587521','1174511106530525185','部门','2','部门',1,1,'admin','2019-09-19 10:31:16',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199517884758368257','1199517671259906049','一般','1','',1,1,'admin','2019-11-27 10:38:44',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199517914017832962','1199517671259906049','重要','2','',1,1,'admin','2019-11-27 10:38:51',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199517941339529217','1199517671259906049','紧急','3','',1,1,'admin','2019-11-27 10:38:58',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199518186144276482','1199518099888414722','日常记录','1','',1,1,'admin','2019-11-27 10:39:56',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199518214858481666','1199518099888414722','本周工作','2','',1,1,'admin','2019-11-27 10:40:03',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199518235943247874','1199518099888414722','下周计划','3','',1,1,'admin','2019-11-27 10:40:08',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199520817285701634','1199520177767587841','列表','1','',1,1,'admin','2019-11-27 10:50:24',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199520835035996161','1199520177767587841','链接','2','',1,1,'admin','2019-11-27 10:50:28',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199525468672405505','1199525215290306561','未开始','0','',1,1,'admin','2019-11-27 11:08:52',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199525490575060993','1199525215290306561','进行中','1','',1,1,'admin','2019-11-27 11:08:58',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199525506429530114','1199525215290306561','已完成','2','',1,1,'admin','2019-11-27 11:09:02','admin','2019-11-27 11:10:02');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1199607547704647681','4f69be5f507accea8d5df5f11346181a','系统','4','',1,1,'admin','2019-11-27 16:35:02','admin','2019-11-27 19:37:46');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1209733775114702850','1209733563293962241','MySQL','1','',1,1,'admin','2019-12-25 15:13:02',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1209733839933476865','1209733563293962241','Oracle','2','',1,1,'admin','2019-12-25 15:13:18',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1209733903020003330','1209733563293962241','SQLServer','3','',1,1,'admin','2019-12-25 15:13:33',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1234371726545010689','4e4602b3e3686f0911384e188dc7efb4','左模糊','LEFT_LIKE','左模糊',7,1,'admin','2020-03-02 14:55:27',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1234371809495760898','4e4602b3e3686f0911384e188dc7efb4','右模糊','RIGHT_LIKE','右模糊',7,1,'admin','2020-03-02 14:55:47',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1250688147579228161','1250687930947620866','正常','0','',1,1,'admin','2020-04-16 15:31:05','',null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1250688201064992770','1250687930947620866','停止','-1','',1,1,'admin','2020-04-16 15:31:18','',null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1280401815068295170','1280401766745718786','正常','1','',1,1,'admin','2020-07-07 15:22:36',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('1280401847607705602','1280401766745718786','冻结','0','',1,1,'admin','2020-07-07 15:22:44',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('147c48ff4b51545032a9119d13f3222a','d6e1152968b02d69ff358c75b48a6ee1','测试流程','test',null,1,1,'admin','2019-03-22 19:27:05',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('222705e11ef0264d4214affff1fb4ff9','4f69be5f507accea8d5df5f11346181a','短信','1','',1,1,'admin','2023-02-28 10:50:36','admin','2019-04-28 10:58:11');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('23a5bb76004ed0e39414e928c4cde155','4e4602b3e3686f0911384e188dc7efb4','不等于','!=','不等于',3,1,'admin','2019-04-01 16:46:15','admin','2019-04-01 17:48:40');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('25847e9cb661a7c711f9998452dc09e6','4e4602b3e3686f0911384e188dc7efb4','小于等于','<=','小于等于',6,1,'admin','2019-04-01 16:44:34','admin','2019-04-01 17:49:10');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('2d51376643f220afdeb6d216a8ac2c01','68168534ff5065a152bfab275c2136f8','有效','1','有效',2,1,'admin','2019-04-26 19:22:01',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('308c8aadf0c37ecdde188b97ca9833f5','8dfe32e2d29ea9430a988b3b558bf233','已发布','1','已发布',2,1,'admin','2019-04-16 17:41:24',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('333e6b2196e01ef9a5f76d74e86a6e33','8dfe32e2d29ea9430a988b3b558bf233','未发布','0','未发布',1,1,'admin','2019-04-16 17:41:12',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('33bc9d9f753cf7dc40e70461e50fdc54','a9d9942bd0eccb6e89de92d130ec4c4a','发送失败','2',null,3,1,'admin','2019-04-12 18:20:02',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('3fbc03d6c994ae06d083751248037c0e','78bda155fe380b1b3f175f1e88c284c6','已完成','3','已完成',3,1,'admin','2019-05-09 16:33:25',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('41d7aaa40c9b61756ffb1f28da5ead8e','0b5d19e1fce4b2e6647e6b4a17760c14','通知公告','1',null,1,1,'admin','2019-04-22 18:01:57',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('41fa1e9571505d643aea87aeb83d4d76','4e4602b3e3686f0911384e188dc7efb4','等于','=','等于',4,1,'admin','2019-04-01 16:45:24','admin','2019-04-01 17:49:00');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('43d2295b8610adce9510ff196a49c6e9','845da5006c97754728bf48b6a10f79cc','正常','1',null,null,1,'admin','2019-03-18 21:45:51',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('4f05fb5376f4c61502c5105f52e4dd2b','83bfb33147013cc81640d5fd9eda030c','操作日志','2',null,null,1,'admin','2019-03-18 23:22:49',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('50223341bfb5ba30bf6319789d8d17fe','d6e1152968b02d69ff358c75b48a6ee1','业务办理','business',null,3,1,'admin','2023-04-22 19:28:05','admin','2019-03-22 23:24:39');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('51222413e5906cdaf160bb5c86fb827c','a7adbcd86c37f7dbc9b66945c82ef9e6','是','1','',1,1,'admin','2019-05-22 19:29:45',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('538fca35afe004972c5f3947c039e766','2e02df51611a4b9632828ab7e5338f00','显示','1','显示',1,1,'admin','2025-03-26 18:27:13','admin','2019-04-26 18:39:07');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('5584c21993bde231bbde2b966f2633ac','4e4602b3e3686f0911384e188dc7efb4','自定义SQL表达式','USE_SQL_RULES','自定义SQL表达式',9,1,'admin','2019-04-01 10:45:24','admin','2019-04-01 17:49:27');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('5b65a88f076b32e8e69d19bbaadb52d5','2f0320997ade5dd147c90130f7218c3e','全体用户','ALL',null,null,1,'admin','2020-10-17 21:22:43','admin','2019-03-28 22:17:09');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('5d833f69296f691843ccdd0c91212b6b','880a895c98afeca9d9ac39f29e67c13e','修改','3','',3,1,'admin','2019-07-22 10:55:07','admin','2019-07-22 10:55:41');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('5d84a8634c8fdfe96275385075b105c9','3d9a351be3436fbefb1307d4cfb49bf2','女','2',null,2,1,null,'2019-01-04 14:56:56',null,'2019-01-04 17:38:12');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('66c952ae2c3701a993e7db58f3baf55e','4e4602b3e3686f0911384e188dc7efb4','大于','>','大于',1,1,'admin','2019-04-01 10:45:46','admin','2019-04-01 17:48:29');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('69cacf64e244100289ddd4aa9fa3b915','a9d9942bd0eccb6e89de92d130ec4c4a','未发送','0',null,1,1,'admin','2019-04-12 18:19:23',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('6a7a9e1403a7943aba69e54ebeff9762','4f69be5f507accea8d5df5f11346181a','邮件','2','',2,1,'admin','2031-02-28 10:50:44','admin','2019-04-28 10:59:03');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('6d404fd2d82311fbc87722cd302a28bc','4e4602b3e3686f0911384e188dc7efb4','模糊','LIKE','模糊',7,1,'admin','2019-04-01 16:46:02','admin','2019-04-01 17:49:20');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('6d4e26e78e1a09699182e08516c49fc4','4d7fec1a7799a436d26d02325eff295e','高','H','高',1,1,'admin','2019-04-16 17:04:24',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('71b924faa93805c5c1579f12e001c809','d6e1152968b02d69ff358c75b48a6ee1','OA办公','oa',null,2,1,'admin','2021-03-22 19:27:17','admin','2019-03-22 23:24:36');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('75b260d7db45a39fc7f21badeabdb0ed','c36169beb12de8a71c8683ee7c28a503','不启用','0',null,null,1,'admin','2019-03-18 23:29:41','admin','2019-03-18 23:29:54');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('7688469db4a3eba61e6e35578dc7c2e5','c36169beb12de8a71c8683ee7c28a503','启用','1',null,null,1,'admin','2019-03-18 23:29:28',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('78ea6cadac457967a4b1c4eb7aaa418c','fc6cd58fde2e8481db10d3a1e68ce70c','正常','1',null,null,1,'admin','2019-03-18 23:30:28',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('7ccf7b80c70ee002eceb3116854b75cb','ac2f7c0c5c5775fcea7e2387bcb22f01','按钮权限','2',null,null,1,'admin','2019-03-18 23:25:40',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('81fb2bb0e838dc68b43f96cc309f8257','fc6cd58fde2e8481db10d3a1e68ce70c','冻结','2',null,null,1,'admin','2019-03-18 23:30:37',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('83250269359855501ec4e9c0b7e21596','4274efc2292239b6f000b153f50823ff','可见/可访问(授权后可见/可访问)','1','',1,1,'admin','2019-05-10 17:54:51',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('84778d7e928bc843ad4756db1322301f','4e4602b3e3686f0911384e188dc7efb4','大于等于','>=','大于等于',5,1,'admin','2019-04-01 10:46:02','admin','2019-04-01 17:49:05');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('84dfc178dd61b95a72900fcdd624c471','78bda155fe380b1b3f175f1e88c284c6','处理中','2','处理中',2,1,'admin','2019-05-09 16:33:01',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('86f19c7e0a73a0bae451021ac05b99dd','ac2f7c0c5c5775fcea7e2387bcb22f01','子菜单','1',null,null,1,'admin','2019-03-18 23:25:27',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('8bccb963e1cd9e8d42482c54cc609ca2','4f69be5f507accea8d5df5f11346181a','微信','3',null,3,1,'admin','2021-05-11 14:29:12','admin','2019-04-11 14:29:31');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('8c618902365ca681ebbbe1e28f11a548','4c753b5293304e7a445fd2741b46529d','启用','1','',0,1,'admin','2020-07-18 23:19:27','admin','2019-05-17 14:51:18');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('8cdf08045056671efd10677b8456c999','4274efc2292239b6f000b153f50823ff','可编辑(未授权时禁用)','2','',2,1,'admin','2019-05-10 17:55:38',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('8ff48e657a7c5090d4f2a59b37d1b878','4d7fec1a7799a436d26d02325eff295e','中','M','中',2,1,'admin','2019-04-16 17:04:40',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('948923658baa330319e59b2213cda97c','880a895c98afeca9d9ac39f29e67c13e','添加','2','',2,1,'admin','2019-07-22 10:54:59','admin','2019-07-22 10:55:36');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('9a96c4a4e4c5c9b4e4d0cbf6eb3243cc','4c753b5293304e7a445fd2741b46529d','不启用','0',null,1,1,'admin','2019-03-18 23:19:53',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('a1e7d1ca507cff4a480c8caba7c1339e','880a895c98afeca9d9ac39f29e67c13e','导出','6','',6,1,'admin','2019-07-22 12:06:50',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('a2be752dd4ec980afaec1efd1fb589af','8dfe32e2d29ea9430a988b3b558bf233','已撤销','2','已撤销',3,1,'admin','2019-04-16 17:41:39',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('aa0d8a8042a18715a17f0a888d360aa4','ac2f7c0c5c5775fcea7e2387bcb22f01','一级菜单','0',null,null,1,'admin','2019-03-18 23:24:52',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('adcf2a1fe93bb99a84833043f475fe0b','4e4602b3e3686f0911384e188dc7efb4','包含','IN','包含',8,1,'admin','2019-04-01 16:45:47','admin','2019-04-01 17:49:24');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('b029a41a851465332ee4ee69dcf0a4c2','0b5d19e1fce4b2e6647e6b4a17760c14','系统消息','2',null,1,1,'admin','2019-02-22 18:02:08','admin','2019-04-22 18:02:13');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('b2a8b4bb2c8e66c2c4b1bb086337f393','3486f32803bb953e7155dab3513dc68b','正常','0',null,null,1,'admin','2022-10-18 21:46:48','admin','2019-03-28 22:22:20');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('b5f3bd5f66bb9a83fecd89228c0d93d1','68168534ff5065a152bfab275c2136f8','无效','0','无效',1,1,'admin','2019-04-26 19:21:49',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('b9fbe2a3602d4a27b45c100ac5328484','78bda155fe380b1b3f175f1e88c284c6','待提交','1','待提交',1,1,'admin','2019-05-09 16:32:35',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('ba27737829c6e0e582e334832703d75e','236e8a4baff0db8c62c00dd95632834f','同步','1','同步',1,1,'admin','2019-05-15 15:28:15',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('bcec04526b04307e24a005d6dcd27fd6','880a895c98afeca9d9ac39f29e67c13e','导入','5','',5,1,'admin','2019-07-22 12:06:41',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('c53da022b9912e0aed691bbec3c78473','880a895c98afeca9d9ac39f29e67c13e','查询','1','',1,1,'admin','2019-07-22 10:54:51',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('c5700a71ad08994d18ad1dacc37a71a9','a7adbcd86c37f7dbc9b66945c82ef9e6','否','0','',1,1,'admin','2019-05-22 19:29:55',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('df168368dcef46cade2aadd80100d8aa','3d9a351be3436fbefb1307d4cfb49bf2','男','1',null,1,1,null,'2027-08-04 14:56:49','admin','2019-03-23 22:44:44');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('e6329e3a66a003819e2eb830b0ca2ea0','4e4602b3e3686f0911384e188dc7efb4','小于','<','小于',2,1,'admin','2019-04-01 16:44:15','admin','2019-04-01 17:48:34');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('e94eb7af89f1dbfa0d823580a7a6e66a','236e8a4baff0db8c62c00dd95632834f','不同步','0','不同步',2,1,'admin','2019-05-15 15:28:28',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('f16c5706f3ae05c57a53850c64ce7c45','a9d9942bd0eccb6e89de92d130ec4c4a','发送成功','1',null,2,1,'admin','2019-04-12 18:19:43',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('f2a7920421f3335afdf6ad2b342f6b5d','845da5006c97754728bf48b6a10f79cc','冻结','2',null,null,1,'admin','2019-03-18 21:46:02',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('f37f90c496ec9841c4c326b065e00bb2','83bfb33147013cc81640d5fd9eda030c','登录日志','1',null,null,1,'admin','2019-03-18 23:22:37',null,null);
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('f80a8f6838215753b05e1a5ba3346d22','880a895c98afeca9d9ac39f29e67c13e','删除','4','',4,1,'admin','2019-07-22 10:55:14','admin','2019-07-22 10:55:30');
INSERT INTO "CUBE"."sys_dict_item"("id","dict_id","item_text","item_value","description","sort_order","status","create_by","create_time","update_by","update_time") VALUES('fe50b23ae5e68434def76f67cef35d2d','78bda155fe380b1b3f175f1e88c284c6','已作废','4','已作废',4,1,'admin','2021-09-09 16:33:43','admin','2019-05-09 16:34:40');

INSERT INTO "CUBE"."sys_fill_rule"("id","rule_name","rule_code","rule_class","rule_params","create_by","create_time","update_by","update_time") VALUES('1202551334738382850','机构编码生成','org_num_role','com.tievd.cube.modules.system.extra.fillrules.OrgCodeRule','{"parentId":"c6d7cb4deeac411cb3384b1b31278596"}','admin','2019-12-09 10:37:06','admin','2019-12-05 19:32:35');
INSERT INTO "CUBE"."sys_fill_rule"("id","rule_name","rule_code","rule_class","rule_params","create_by","create_time","update_by","update_time") VALUES('1202787623203065858','分类字典编码生成','category_code_rule','com.tievd.cube.modules.system.extra.fillrules.CategoryCodeRule','{"pid":""}','admin','2019-12-09 10:36:54','admin','2019-12-06 11:11:31');
INSERT INTO "CUBE"."sys_fill_rule"("id","rule_name","rule_code","rule_class","rule_params","create_by","create_time","update_by","update_time") VALUES('1260134137920090113','商城订单流水号','shop_order_num','com.tievd.cube.modules.system.extra.fillrules.OrderNumberRule','{}','admin','2020-07-11 11:35:00','admin','2020-05-12 17:06:05');

INSERT INTO "CUBE"."sys_gateway_route"("id","router_id","name","uri","predicates","filters","retryable","strip_prefix","persist","show_api","status","create_by","create_time","update_by","update_time","sys_org_code") VALUES('1331051599401857026','cube-demo-websocket','cube-demo-websocket','lb:ws://cube-demo','[{"args":["/vxeSocket/**"],"name":"Path"}]','[]',null,null,null,null,1,'admin','2020-11-24 09:46:46',null,null,null);
INSERT INTO "CUBE"."sys_gateway_route"("id","router_id","name","uri","predicates","filters","retryable","strip_prefix","persist","show_api","status","create_by","create_time","update_by","update_time","sys_org_code") VALUES('cube-cloud-websocket','cube-system-websocket','cube-system-websocket','lb:ws://cube-system','[{"args":["/websocket/**","/eoaSocket/**","/newsWebsocket/**"],"name":"Path"}]','[]',null,null,null,null,1,'admin','2020-11-16 19:41:51',null,null,null);
INSERT INTO "CUBE"."sys_gateway_route"("id","router_id","name","uri","predicates","filters","retryable","strip_prefix","persist","show_api","status","create_by","create_time","update_by","update_time","sys_org_code") VALUES('cube-demo','cube-demo','cube-demo','lb://cube-demo','[{"args":["/mock/**","/test/**","/bigscreen/template1/**","/bigscreen/template2/**"],"name":"Path"}]','[]',null,null,null,null,1,'admin','2020-11-16 19:41:51',null,null,null);
INSERT INTO "CUBE"."sys_gateway_route"("id","router_id","name","uri","predicates","filters","retryable","strip_prefix","persist","show_api","status","create_by","create_time","update_by","update_time","sys_org_code") VALUES('cube-system','cube-system','cube-system','lb://cube-system','[{"args":["/sys/**","/eoa/**","/joa/**","/online/**","/bigscreen/**","/jmreport/**","/desform/**","/process/**","/act/**","/plug-in/***/","/druid/**","/generic/**"],"name":"Path"}]','[]',null,null,null,null,1,'admin','2020-11-16 19:41:51',null,null,null);

INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('00a2a0ae65cdca5e93209cdbde97cbe6','2e42e3835c2b44ec9f7bc26c146ee531','成功','/result/success','result/Success',null,null,1,null,null,1.0,null,null,1,1,null,null,null,null,'2018-12-25 20:34:38',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('05b3c82ddb2536a4a5ee1a4c46b5abef','540a2936940846cb98114ffb0d145cb8','用户列表','/list/user-list','examples/list/UserList',null,null,1,null,null,3.0,null,null,1,1,null,null,null,null,'2018-12-25 20:34:38',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('08e6b9dc3c04489c8e1ff2ce6f105aa4','','系统监控','/dashboard3','layouts/RouteView',null,null,0,null,null,6.0,0,'dashboard',1,0,null,0,null,null,'2018-12-25 20:34:38','admin','2019-03-31 22:19:58',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1166535831146504193','2a470fc0c3954d9dbb61de6d80846549','对象存储','/oss/file','modules/oss/OSSFileList',null,null,1,null,'1',1.0,0,'',1,1,0,0,null,'admin','2019-08-28 02:19:50','admin','2019-08-28 02:20:57',0,0,'1',null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1170592628746878978','d7d6e2e4e2934f2c9385a623fd98c6f3','菜单管理-异步','/isystem/newPermissionList','system/NewPermissionList',null,null,1,null,'1',100.0,0,null,1,1,0,0,null,'admin','2019-09-08 15:00:05','admin','2019-12-25 09:58:18',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1174506953255182338','d7d6e2e4e2934f2c9385a623fd98c6f3','职务管理','/isystem/position','system/SysPositionList',null,null,1,null,'1',2.0,0,null,1,1,0,0,null,'admin','2019-09-19 10:14:13','admin','2019-09-19 10:15:22',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1174590283938041857','d7d6e2e4e2934f2c9385a623fd98c6f3','通讯录','/isystem/addressList','system/AddressList',null,null,1,null,'1',3.0,0,null,1,1,0,0,null,'admin','2019-09-19 15:45:21',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1192318987661234177','e41b69c57a941a3bbcce45032fe57605','系统编码规则','/isystem/fillRule','system/SysFillRuleList',null,null,1,null,'1',3.0,0,null,1,1,0,0,null,'admin','2019-11-07 13:52:53','admin','2020-07-10 16:55:03',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1209731624921534465','e41b69c57a941a3bbcce45032fe57605','多数据源管理','/isystem/dataSource','system/SysDataSourceList',null,null,1,null,'1',6.0,0,null,1,1,0,0,null,'admin','2019-12-25 15:04:30','admin','2020-02-23 22:43:37',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1224641973866467330','e41b69c57a941a3bbcce45032fe57605','系统校验规则','/isystem/checkRule','system/SysCheckRuleList',null,null,1,null,'1',5.0,0,null,1,1,0,0,null,'admin','2019-11-07 13:52:53','admin','2020-07-10 16:55:12',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1260928341675982849','3f915b2769fc80648e92d04e84ca059d','添加按钮',null,null,null,null,2,'user:add','1',1.0,0,null,1,1,0,0,null,'admin','2020-05-14 21:41:58',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1260929666434318338','3f915b2769fc80648e92d04e84ca059d','用户编辑',null,null,null,null,2,'user:edit','1',1.0,0,null,1,1,0,0,null,'admin','2020-05-14 21:47:14',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1260931366557696001','3f915b2769fc80648e92d04e84ca059d','表单性别可见','',null,null,null,2,'user:sex','1',1.0,0,null,1,1,0,0,null,'admin','2020-05-14 21:53:59','admin','2020-05-14 21:57:00',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1260933542969458689','3f915b2769fc80648e92d04e84ca059d','禁用生日字段',null,null,null,null,2,'user:form:birthday','2',1.0,0,null,1,1,0,0,null,'admin','2020-05-14 22:02:38',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1265162119913824258','08e6b9dc3c04489c8e1ff2ce6f105aa4','路由网关','/isystem/gatewayroute','system/SysGatewayRouteList',null,null,1,null,'1',0.0,0,null,1,1,0,0,null,null,'2020-05-26 14:05:30','admin','2020-09-09 14:47:52',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1280350452934307841','d7d6e2e4e2934f2c9385a623fd98c6f3','租户管理','/isys/tenant','system/TenantList',null,null,1,null,'1',10.0,0,null,1,1,0,0,null,'admin','2020-07-07 11:58:30','admin','2020-07-10 15:46:35',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('13212d3416eb690c2e1d5033166ff47a','2e42e3835c2b44ec9f7bc26c146ee531','失败','/result/fail','result/Error',null,null,1,null,null,2.0,null,null,1,1,null,null,null,null,'2018-12-25 20:34:38',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1335876951575314434','08e6b9dc3c04489c8e1ff2ce6f105aa4','在线用户','/isystem/online/user','system/OnlineUserList',null,null,1,null,'1',1.0,0,null,1,1,0,0,null,'admin','2020-12-07 17:20:59','admin','2020-12-08 10:23:23',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1336133158097592321','d7d6e2e4e2934f2c9385a623fd98c6f3','地区管理','/isystem/area','system/SysAreaList',null,null,1,null,'1',8.0,0,null,1,1,0,0,null,'admin','2020-12-08 10:19:04',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1336135755432927234','08e6b9dc3c04489c8e1ff2ce6f105aa4','行为统计','/isystem/permission/count','system/PermissionCountList',null,null,1,null,'1',1.0,0,null,1,1,0,0,null,'admin','2020-12-08 10:29:23',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1336136043015380994','e41b69c57a941a3bbcce45032fe57605','数据导出','/isystem/data/export','system/DataExport',null,null,1,null,'1',1.0,0,null,1,1,0,0,null,'admin','2020-12-08 10:30:32',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1367a93f2c410b169faa7abcbad2f77c','6e73eb3c26099c191bf03852ee1310a1','基本设置','/account/settings/BaseSetting','account/settings/BaseSetting','account-settings-base',null,1,'BaseSettings',null,null,0,null,1,1,null,1,null,null,'2018-12-26 18:58:35','admin','2019-03-20 12:57:31',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1408265706710597633','e41b69c57a941a3bbcce45032fe57605','代码生成','/codegenerator','modules/generateCode',null,null,1,null,'1',1.0,0,null,1,1,0,0,null,'admin','2021-06-25 11:28:03','admin','2021-07-12 14:13:03',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1437700059899138049','700b7f95165c46cc7a78bf227aa8fed3','连接池信息','/monitor/datasource','modules/monitor/ConnectionInfo',null,null,1,null,'1',1.0,0,null,1,1,0,0,null,'admin','2021-09-14 16:49:40',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1438041680501059586','700b7f95165c46cc7a78bf227aa8fed3','运行环境','/monitor/EnvironmentInfo','modules/monitor/EnvironmentInfo',null,null,1,null,'1',8.0,0,null,1,1,0,0,null,'admin','2021-09-15 15:27:09','admin','2021-09-15 15:47:55',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1446311260709236738','58857ff846e61794c69208e9d3a85466','日志详情',null,null,null,null,2,'log:detail','1',1.0,0,null,1,1,0,0,null,'llc','2021-10-08 11:07:30',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1555081846828613633','d7d6e2e4e2934f2c9385a623fd98c6f3','授权管理','/isystem/ram','system/SysRoleKeyList',null,null,1,null,'1',7.0,0,null,1,1,0,0,null,'admin','2022-08-04 14:42:59',null,null,0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('190c2b43bec6a5f7a4194a85db67d96a','d7d6e2e4e2934f2c9385a623fd98c6f3','角色管理','/isystem/roleUserList','system/RoleUserList',null,null,1,null,null,1.2,0,null,1,1,0,0,null,'admin','2019-04-17 15:13:56','admin','2019-12-25 09:36:31',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('1a0811914300741f4e11838ff37a1d3a','3f915b2769fc80648e92d04e84ca059d','手机号禁用',null,null,null,null,2,'user:form:phone','2',1.0,0,null,0,1,null,0,null,'admin','2019-05-11 17:19:30','admin','2019-05-11 18:00:22',0,0,'1',null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('277bfabef7d76e89b33062b16a9a5020','e3c13679c73a4f829bcff2aba8fd68b1','基础表单','/form/base-form','examples/form/BasicForm',null,null,1,null,null,1.0,0,null,1,0,null,0,null,null,'2018-12-25 20:34:38','admin','2019-02-26 17:02:08',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('2a470fc0c3954d9dbb61de6d80846549','','常见案例','/jeecg','layouts/RouteView',null,null,0,null,null,7.0,0,'qrcode',1,0,null,0,null,null,'2018-12-25 20:34:38','admin','2019-04-02 11:46:42',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('2dbbafa22cda07fa5d169d741b81fe12','08e6b9dc3c04489c8e1ff2ce6f105aa4','在线文档','{{ window._CONFIG[''domianURL''] }}/doc/app.html','layouts/IframePageView',null,null,1,null,null,4.0,0,null,1,1,0,0,null,'admin','2019-01-30 10:00:01','admin','2022-05-05 10:48:58',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('2e42e3835c2b44ec9f7bc26c146ee531','2a470fc0c3954d9dbb61de6d80846549','结果页','/result','layouts/PageView',null,null,1,null,null,12.0,0,'check-circle-o',1,0,0,0,null,null,'2018-12-25 20:34:38','admin','2021-05-26 16:39:06',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('3f915b2769fc80648e92d04e84ca059d','d7d6e2e4e2934f2c9385a623fd98c6f3','用户管理','/isystem/user','system/UserList',null,null,1,null,null,1.1,0,null,1,0,0,0,null,null,'2018-12-25 20:34:38','admin','2019-12-25 09:36:24',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('45c966826eeff4c99b8f8ebfe74511fc','d7d6e2e4e2934f2c9385a623fd98c6f3','部门管理','/isystem/depart','system/DepartList',null,null,1,null,null,1.4,0,null,1,1,0,0,null,'admin','2019-01-29 18:47:40','admin','2019-12-25 09:36:47',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('4f66409ef3bbd69c1d80469d6e2a885e','6e73eb3c26099c191bf03852ee1310a1','账户绑定','/account/settings/binding','account/settings/Binding',null,null,1,'BindingSettings',null,null,null,null,1,1,null,null,null,null,'2018-12-26 19:01:20',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('53a9230444d33de28aa11cc108fb1dba','5c8042bd6c601270b2bbd9b20bccc68b','我的消息','/isps/userAnnouncement','system/UserAnnouncementList',null,null,1,null,null,3.0,0,null,1,1,0,0,null,'admin','2019-04-19 10:16:00','admin','2019-12-25 09:54:34',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('54dd5457a3190740005c1bfec55b1c34','d7d6e2e4e2934f2c9385a623fd98c6f3','菜单管理','/isystem/permission','system/PermissionList',null,null,1,null,null,1.3,0,null,1,1,0,0,null,null,'2018-12-25 20:34:38','admin','2019-12-25 09:36:39',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('58857ff846e61794c69208e9d3a85466','08e6b9dc3c04489c8e1ff2ce6f105aa4','日志管理','/isystem/log','system/LogList',null,null,1,null,null,2.0,0,'',1,0,0,0,null,null,'2018-12-26 10:11:18','admin','2020-09-09 14:48:25',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('5c2f42277948043026b7a14692456828','d7d6e2e4e2934f2c9385a623fd98c6f3','我的部门','/isystem/departUserList','system/DepartUserList',null,null,1,null,null,2.0,0,null,1,1,0,0,null,'admin','2019-04-17 15:12:24','admin','2019-12-25 09:35:26',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('5c8042bd6c601270b2bbd9b20bccc68b','','消息中心','/message','layouts/RouteView',null,null,0,null,null,6.0,0,'message',1,0,null,0,null,'admin','2019-04-09 11:05:04','admin','2019-04-11 19:47:54',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('65a8f489f25a345836b7f44b1181197a','c65321e57b7949b7a975313220de0422','403','/exception/403','exception/403',null,null,1,null,null,1.0,null,null,1,1,null,null,null,null,'2018-12-25 20:34:38',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('6e73eb3c26099c191bf03852ee1310a1','717f6bee46f44a3897eca9abd6e2ec44','个人设置','/account/settings/BaseSetting','account/settings/Index',null,null,1,null,null,2.0,1,null,1,0,null,0,null,null,'2018-12-25 20:34:38','admin','2019-04-19 09:41:05',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('700b7f95165c46cc7a78bf227aa8fed3','08e6b9dc3c04489c8e1ff2ce6f105aa4','性能监控','/monitor','layouts/RouteView',null,null,1,null,null,3.0,0,null,1,0,0,0,null,'admin','2019-04-02 11:34:34','admin','2020-09-09 14:48:51',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('717f6bee46f44a3897eca9abd6e2ec44','2a470fc0c3954d9dbb61de6d80846549','个人页','/account','layouts/RouteView',null,null,1,null,null,12.0,0,'user',1,0,0,1,null,null,'2018-12-25 20:34:38','admin','2021-05-26 16:39:45',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('841057b8a1bef8f6b4b20f9a618a7fa6','08e6b9dc3c04489c8e1ff2ce6f105aa4','数据日志','/sys/dataLog-list','system/DataLogList',null,null,1,null,null,2.1,0,null,1,1,0,0,null,'admin','2019-03-11 19:26:49','admin','2020-09-09 14:48:32',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('882a73768cfd7f78f3a37584f7299656','6e73eb3c26099c191bf03852ee1310a1','个性化设置','/account/settings/custom','account/settings/Custom',null,null,1,'CustomSettings',null,null,null,null,1,1,null,null,null,null,'2018-12-26 19:00:46',null,'2018-12-26 21:13:25',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('8b3bff2eee6f1939147f5c68292a1642','700b7f95165c46cc7a78bf227aa8fed3','服务器信息','/monitor/SystemInfo','modules/monitor/SystemInfo',null,null,1,null,null,4.0,0,null,1,1,null,0,null,'admin','2019-04-02 11:39:19','admin','2019-04-02 15:40:02',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('8d1ebd663688965f1fd86a2f0ead3416','700b7f95165c46cc7a78bf227aa8fed3','Redis监控','/monitor/redis/info','modules/monitor/RedisInfo',null,null,1,null,null,1.0,0,null,1,1,null,0,null,'admin','2019-04-02 13:11:33','admin','2019-05-07 15:18:54',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('944abf0a8fc22fe1f1154a389a574154','5c8042bd6c601270b2bbd9b20bccc68b','消息管理','/modules/message/sysMessageList','modules/message/SysMessageList',null,null,1,null,null,1.0,0,null,1,1,null,0,null,'admin','2019-04-09 11:27:53','admin','2019-04-09 19:31:23',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('9502685863ab87f0ad1134142788a385','','首页','/dashboard/analysis','dashboard/Analysis',null,null,0,null,null,0.0,0,'home',1,1,null,0,null,null,'2018-12-25 20:34:38','admin','2019-03-29 11:04:13',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('97c8629abc7848eccdb6d77c24bb3ebb','700b7f95165c46cc7a78bf227aa8fed3','磁盘监控','/monitor/Disk','modules/monitor/DiskMonitoring',null,null,1,null,null,6.0,0,null,1,1,null,0,null,'admin','2019-04-25 14:30:06','admin','2019-05-05 14:37:14',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('aedbf679b5773c1f25e9f7b10111da73','08e6b9dc3c04489c8e1ff2ce6f105aa4','SQL监控','{{ window._CONFIG[''domianURL''] }}/druid/','layouts/IframePageView',null,null,1,null,null,3.0,0,null,1,1,0,0,null,'admin','2019-01-30 09:43:22','admin','2020-09-09 14:48:38',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('b1cb0a3fedf7ed0e4653cb5a229837ee','08e6b9dc3c04489c8e1ff2ce6f105aa4','定时任务','/isystem/QuartzJobList','system/QuartzJobList',null,null,1,null,null,1.0,0,null,1,1,0,0,null,null,'2019-01-03 09:38:52','admin','2020-09-09 14:48:16',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('b4dfc7d5dd9e8d5b6dd6d4579b1aa559','c65321e57b7949b7a975313220de0422','500','/exception/500','exception/500',null,null,1,null,null,3.0,null,null,1,1,null,null,null,null,'2018-12-25 20:34:38',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('c65321e57b7949b7a975313220de0422','2a470fc0c3954d9dbb61de6d80846549','异常页','/exception','layouts/RouteView',null,null,1,null,null,12.0,0,'warning',1,0,0,0,null,null,'2018-12-25 20:34:38','admin','2021-05-26 16:39:24',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('d07a2c87a451434c99ab06296727ec4f','700b7f95165c46cc7a78bf227aa8fed3','JVM信息','/monitor/JvmInfo','modules/monitor/JvmInfo',null,null,1,null,null,4.0,0,null,1,1,null,0,null,'admin','2019-04-01 23:07:48','admin','2019-04-02 11:37:16',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('d2bbf9ebca5a8fa2e227af97d2da7548','c65321e57b7949b7a975313220de0422','404','/exception/404','exception/404',null,null,1,null,null,2.0,null,null,1,1,null,null,null,null,'2018-12-25 20:34:38',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('d7d6e2e4e2934f2c9385a623fd98c6f3','','系统管理','/isystem','layouts/RouteView',null,null,0,null,null,4.0,0,'setting',1,0,null,0,null,null,'2018-12-25 20:34:38','admin','2019-03-31 22:19:52',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('d86f58e7ab516d3bc6bfb1fe10585f97','717f6bee46f44a3897eca9abd6e2ec44','个人中心','/account/center','account/center/Index',null,null,1,null,null,1.0,null,null,1,1,null,null,null,null,'2018-12-25 20:34:38',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('e08cb190ef230d5d4f03824198773950','d7d6e2e4e2934f2c9385a623fd98c6f3','系统通告','/isystem/annountCement','system/SysAnnouncementList',null,null,1,'annountCement',null,6.0,null,'',1,1,null,null,null,null,'2019-01-02 17:23:01',null,'2019-01-02 17:31:23',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('e41b69c57a941a3bbcce45032fe57605','','在线开发','/online','layouts/RouteView',null,null,0,null,null,5.0,0,'cloud',1,0,null,0,null,'admin','2019-03-08 10:43:10','admin','2019-05-11 10:36:01',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('ebb9d82ea16ad864071158e0c449d186','d7d6e2e4e2934f2c9385a623fd98c6f3','分类字典','/isys/category','system/SysCategoryList',null,null,1,null,'1',5.2,0,null,1,1,0,0,null,'admin','2019-05-29 18:48:07','admin','2020-02-23 22:45:33',0,0,'1',0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('ec8d607d0156e198b11853760319c646','6e73eb3c26099c191bf03852ee1310a1','安全设置','/account/settings/security','account/settings/Security',null,null,1,'SecuritySettings',null,null,null,null,1,1,null,null,null,null,'2018-12-26 18:59:52',null,null,0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('f1cb187abf927c88b89470d08615f5ac','d7d6e2e4e2934f2c9385a623fd98c6f3','数据字典','/isystem/dict','system/DictList',null,null,1,null,null,5.0,0,null,1,1,0,0,null,null,'2018-12-28 13:54:43','admin','2020-02-23 22:45:25',0,0,null,0);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('f780d0d3083d849ccbdb1b1baee4911d','5c8042bd6c601270b2bbd9b20bccc68b','模板管理','/modules/message/sysMessageTemplateList','modules/message/SysMessageTemplateList',null,null,1,null,null,1.0,0,null,1,1,null,0,null,'admin','2019-04-09 11:50:31','admin','2019-04-12 10:16:34',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('fc810a2267dd183e4ef7c71cc60f4670','700b7f95165c46cc7a78bf227aa8fed3','请求追踪','/monitor/HttpTrace','modules/monitor/HttpTrace',null,null,1,null,null,4.0,0,null,1,1,null,0,null,'admin','2019-04-02 09:46:19','admin','2019-04-02 11:37:27',0,0,null,null);
INSERT INTO "CUBE"."sys_permission"("id","parent_id","name","url","component","component_name","redirect","menu_type","perms","perms_type","sort_no","always_show","icon","is_route","is_leaf","keep_alive","hidden","description","create_by","create_time","update_by","update_time","del_flag","rule_flag","status","internal_or_external") VALUES('fedfbf4420536cacc0218557d263dfea','6e73eb3c26099c191bf03852ee1310a1','新消息通知','/account/settings/notification','account/settings/Notification',null,null,1,'NotificationSettings',null,null,null,'',1,1,null,null,null,null,'2018-12-26 19:02:05',null,null,0,0,null,null);

INSERT INTO "CUBE"."sys_position"("id","code","name","post_rank","company_id","create_by","create_time","update_by","update_time","sys_org_code") VALUES('1256485574212153345','CEO','总经理','5',null,'admin','2020-05-02 15:28:00','admin','2020-05-02 15:28:03','A01');

INSERT INTO "CUBE"."sys_role"("id","role_name","role_code","description","create_by","create_time","update_by","update_time") VALUES('1456085100369317890','普通用户','user','普通用户','admin','2021-11-04 10:25:15',null,null);
INSERT INTO "CUBE"."sys_role"("id","role_name","role_code","description","create_by","create_time","update_by","update_time") VALUES('f6817f48af4fb3af11b9e8bf182f618b','管理员','admin','管理员',null,'2018-12-21 18:03:39','admin','2019-05-20 11:40:26');

INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('0c6b8facbb1cc874964c87a8cf01e4b1','f6817f48af4fb3af11b9e8bf182f618b','841057b8a1bef8f6b4b20f9a618a7fa6',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('0c6e1075e422972083c3e854d9af7851','f6817f48af4fb3af11b9e8bf182f618b','08e6b9dc3c04489c8e1ff2ce6f105aa4',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('0e1469997af2d3b97fff56a59ee29eeb','f6817f48af4fb3af11b9e8bf182f618b','e41b69c57a941a3bbcce45032fe57605',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('0f861cb988fdc639bb1ab943471f3a72','f6817f48af4fb3af11b9e8bf182f618b','97c8629abc7848eccdb6d77c24bb3ebb',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1185039870491439105','f6817f48af4fb3af11b9e8bf182f618b','1174506953255182338',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1185039870529187841','f6817f48af4fb3af11b9e8bf182f618b','1174590283938041857',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1197431682208206850','f6817f48af4fb3af11b9e8bf182f618b','1192318987661234177',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1209423580355481602','f6817f48af4fb3af11b9e8bf182f618b','190c2b43bec6a5f7a4194a85db67d96a',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1231590078632955905','f6817f48af4fb3af11b9e8bf182f618b','1224641973866467330',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1231590078658121729','f6817f48af4fb3af11b9e8bf182f618b','1209731624921534465',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1260928399955836929','f6817f48af4fb3af11b9e8bf182f618b','1260928341675982849',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1269526122208522241','f6817f48af4fb3af11b9e8bf182f618b','1267412134208319489',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('126ea9faebeec2b914d6d9bef957afb6','f6817f48af4fb3af11b9e8bf182f618b','f1cb187abf927c88b89470d08615f5ac',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1281494164924653569','f6817f48af4fb3af11b9e8bf182f618b','1280350452934307841',null,'2020-07-10 15:43:13',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1281494684632473602','f6817f48af4fb3af11b9e8bf182f618b','1265162119913824258',null,'2020-07-10 15:45:16',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1335877076745928705','f6817f48af4fb3af11b9e8bf182f618b','1335876951575314434',null,'2020-12-07 17:21:29',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1336134372344406018','f6817f48af4fb3af11b9e8bf182f618b','1336133158097592321',null,'2020-12-08 10:23:53',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1336136136401559554','f6817f48af4fb3af11b9e8bf182f618b','1336136043015380994',null,'2020-12-08 10:30:54',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1336136136401559555','f6817f48af4fb3af11b9e8bf182f618b','1336135755432927234',null,'2020-12-08 10:30:54',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1408266208303218689','f6817f48af4fb3af11b9e8bf182f618b','1408265706710597633',null,'2021-06-25 11:30:03',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1437700111170310146','f6817f48af4fb3af11b9e8bf182f618b','1437700059899138049',null,'2021-09-14 16:49:52',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1438041770103975937','f6817f48af4fb3af11b9e8bf182f618b','1438041680501059586',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1438041770103975939','f6817f48af4fb3af11b9e8bf182f618b','1446311260709236738',null,'2021-09-15 15:27:30',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1441998626803462146','f6817f48af4fb3af11b9e8bf182f618b','53a9230444d33de28aa11cc108fb1dba',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1441998626820239362','f6817f48af4fb3af11b9e8bf182f618b','5c8042bd6c601270b2bbd9b20bccc68b',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1555083117631422466','f6817f48af4fb3af11b9e8bf182f618b','1555081846828613633',null,'2022-08-04 14:48:02',null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('165acd6046a0eaf975099f46a3c898ea','f6817f48af4fb3af11b9e8bf182f618b','4f66409ef3bbd69c1d80469d6e2a885e',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1664b92dff13e1575e3a929caa2fa14d','f6817f48af4fb3af11b9e8bf182f618b','d2bbf9ebca5a8fa2e227af97d2da7548',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('1e47db875601fd97723254046b5bba90','f6817f48af4fb3af11b9e8bf182f618b','baf16b7174bd821b6bab23fa9abb200d',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('25491ecbd5a9b34f09c8bc447a10ede1','f6817f48af4fb3af11b9e8bf182f618b','d07a2c87a451434c99ab06296727ec4f',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('2779cdea8367fff37db26a42c1a1f531','f6817f48af4fb3af11b9e8bf182f618b','fef097f3903caf3a3c3a6efa8de43fbb',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('2ad37346c1b83ddeebc008f6987b2227','f6817f48af4fb3af11b9e8bf182f618b','8d1ebd663688965f1fd86a2f0ead3416',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('38a2e55db0960262800576e34b3af44c','f6817f48af4fb3af11b9e8bf182f618b','5c2f42277948043026b7a14692456828',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('3de2a60c7e42a521fecf6fcc5cb54978','f6817f48af4fb3af11b9e8bf182f618b','2d83d62bd2544b8994c8f38cf17b0ddf',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('4204f91fb61911ba8ce40afa7c02369f','f6817f48af4fb3af11b9e8bf182f618b','3f915b2769fc80648e92d04e84ca059d',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('4dab5a06acc8ef3297889872caa74747','f6817f48af4fb3af11b9e8bf182f618b','ffb423d25cc59dcd0532213c4a518261',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('4ea403fc1d19feb871c8bdd9f94a4ecc','f6817f48af4fb3af11b9e8bf182f618b','2e42e3835c2b44ec9f7bc26c146ee531',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('4f254549d9498f06f4cc9b23f3e2c070','f6817f48af4fb3af11b9e8bf182f618b','93d5cfb4448f11e9916698e7f462b4b6',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('504e326de3f03562cdd186748b48a8c7','f6817f48af4fb3af11b9e8bf182f618b','027aee69baee98a0ed2e01806e89c891',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('5d230e6cd2935c4117f6cb9a7a749e39','f6817f48af4fb3af11b9e8bf182f618b','fc810a2267dd183e4ef7c71cc60f4670',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('5de6871fadb4fe1cdd28989da0126b07','f6817f48af4fb3af11b9e8bf182f618b','a400e4f4d54f79bf5ce160a3432231af',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('5e4015a9a641cbf3fb5d28d9f885d81a','f6817f48af4fb3af11b9e8bf182f618b','2dbbafa22cda07fa5d169d741b81fe12',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('66b202f8f84fe766176b3f51071836ef','f6817f48af4fb3af11b9e8bf182f618b','1367a93f2c410b169faa7abcbad2f77c',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('6c74518eb6bb9a353f6a6c459c77e64b','f6817f48af4fb3af11b9e8bf182f618b','b4dfc7d5dd9e8d5b6dd6d4579b1aa559',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('6daddafacd7eccb91309530c17c5855d','f6817f48af4fb3af11b9e8bf182f618b','edfa74d66e8ea63ea432c2910837b150',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('6fb4c2142498dd6d5b6c014ef985cb66','f6817f48af4fb3af11b9e8bf182f618b','6e73eb3c26099c191bf03852ee1310a1',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('7413acf23b56c906aedb5a36fb75bd3a','f6817f48af4fb3af11b9e8bf182f618b','a4fc7b64b01a224da066bb16230f9c5a',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('76a54a8cc609754360bf9f57e7dbb2db','f6817f48af4fb3af11b9e8bf182f618b','c65321e57b7949b7a975313220de0422',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('7ca833caa5eac837b7200d8b6de8b2e3','f6817f48af4fb3af11b9e8bf182f618b','fedfbf4420536cacc0218557d263dfea',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('84eac2f113c23737128fb099d1d1da89','f6817f48af4fb3af11b9e8bf182f618b','03dc3d93261dda19fc86dd7ca486c6cf',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('86060e2867a5049d8a80d9fe5d8bc28b','f6817f48af4fb3af11b9e8bf182f618b','765dd244f37b804e3d00f475fd56149b',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('8b09925bdc194ab7f3559cd3a7ea0507','f6817f48af4fb3af11b9e8bf182f618b','ebb9d82ea16ad864071158e0c449d186',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('8d154c2382a8ae5c8d1b84bd38df2a93','f6817f48af4fb3af11b9e8bf182f618b','d86f58e7ab516d3bc6bfb1fe10585f97',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('9380121ca9cfee4b372194630fce150e','f6817f48af4fb3af11b9e8bf182f618b','65a8f489f25a345836b7f44b1181197a',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('94911fef73a590f6824105ebf9b6cab3','f6817f48af4fb3af11b9e8bf182f618b','8b3bff2eee6f1939147f5c68292a1642',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('9700d20dbc1ae3cbf7de1c810b521fe6','f6817f48af4fb3af11b9e8bf182f618b','ec8d607d0156e198b11853760319c646',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('980171fda43adfe24840959b1d048d4d','f6817f48af4fb3af11b9e8bf182f618b','d7d6e2e4e2934f2c9385a623fd98c6f3',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('987c23b70873bd1d6dca52f30aafd8c2','f6817f48af4fb3af11b9e8bf182f618b','00a2a0ae65cdca5e93209cdbde97cbe6',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('9d980ec0489040e631a9c24a6af42934','f6817f48af4fb3af11b9e8bf182f618b','05b3c82ddb2536a4a5ee1a4c46b5abef',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('a034ed7c38c996b880d3e78f586fe0ae','f6817f48af4fb3af11b9e8bf182f618b','c89018ea6286e852b424466fd92a2ffc',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('acacce4417e5d7f96a9c3be2ded5b4be','f6817f48af4fb3af11b9e8bf182f618b','f9d3f4f27653a71c52faa9fb8070fbe7',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('af60ac8fafd807ed6b6b354613b9ccbc','f6817f48af4fb3af11b9e8bf182f618b','58857ff846e61794c69208e9d3a85466',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('bbec16ad016efec9ea2def38f4d3d9dc','f6817f48af4fb3af11b9e8bf182f618b','13212d3416eb690c2e1d5033166ff47a',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('bea2986432079d89203da888d99b3f16','f6817f48af4fb3af11b9e8bf182f618b','54dd5457a3190740005c1bfec55b1c34',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('c90b0b01c7ca454d2a1cb7408563e696','f6817f48af4fb3af11b9e8bf182f618b','882a73768cfd7f78f3a37584f7299656',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('cf1feb1bf69eafc982295ad6c9c8d698','f6817f48af4fb3af11b9e8bf182f618b','a2b11669e98c5fe54a53c3e3c4f35d14',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('cf2ef620217673e4042f695743294f01','f6817f48af4fb3af11b9e8bf182f618b','717f6bee46f44a3897eca9abd6e2ec44',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('cf43895aef7fc684669483ab00ef2257','f6817f48af4fb3af11b9e8bf182f618b','700b7f95165c46cc7a78bf227aa8fed3',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('d37ad568e26f46ed0feca227aa9c2ffa','f6817f48af4fb3af11b9e8bf182f618b','9502685863ab87f0ad1134142788a385',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('d8a5c9079df12090e108e21be94b4fd7','f6817f48af4fb3af11b9e8bf182f618b','078f9558cdeab239aecb2bda1a8ed0d1',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('dbc5dd836d45c5bc7bc94b22596ab956','f6817f48af4fb3af11b9e8bf182f618b','1939e035e803a99ceecb6f5563570fb2',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('dc83bb13c0e8c930e79d28b2db26f01f','f6817f48af4fb3af11b9e8bf182f618b','63b551e81c5956d5c861593d366d8c57',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('dc8fd3f79bd85bd832608b42167a1c71','f6817f48af4fb3af11b9e8bf182f618b','91c23960fab49335831cf43d820b0a61',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('de82e89b8b60a3ea99be5348f565c240','f6817f48af4fb3af11b9e8bf182f618b','56ca78fe0f22d815fabc793461af67b8',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('e7467726ee72235baaeb47df04a35e73','f6817f48af4fb3af11b9e8bf182f618b','e08cb190ef230d5d4f03824198773950',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('eaef4486f1c9b0408580bbfa2037eb66','f6817f48af4fb3af11b9e8bf182f618b','2a470fc0c3954d9dbb61de6d80846549',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('ec846a3f85fdb6813e515be71f11b331','f6817f48af4fb3af11b9e8bf182f618b','732d48f8e0abe99fe6a23d18a3171cd1',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('ecdd72fe694e6bba9c1d9fc925ee79de','f6817f48af4fb3af11b9e8bf182f618b','45c966826eeff4c99b8f8ebfe74511fc',null,null,null);
INSERT INTO "CUBE"."sys_role_permission"("id","role_id","permission_id","data_rule_ids","operate_date","operate_ip") VALUES('f177acac0276329dc66af0c9ad30558a','f6817f48af4fb3af11b9e8bf182f618b','c2c356bf4ddd29975347a7047a062440',null,null,null);

INSERT INTO "CUBE"."sys_tenant"("id","name","begin_date","end_date","status","create_by","create_time") VALUES(1,'四川天翼网络服务有限公司',null,null,1,'admin','2020-07-10 15:43:32');

INSERT INTO "CUBE"."sys_user"("id","username","realname","password","salt","avatar","birthday","sex","email","phone","org_code","status","del_flag","third_id","third_type","work_no","post","telephone","create_by","create_time","update_by","update_time","user_identity","depart_ids","rel_tenant_ids","first_login") VALUES('e9ca23d68d884d4ebb19d07889727dae','admin','管理员','cb362cfeefbf3d8d','RCGTeGiH','','2020-05-27 00:00:00',1,'cube@tievd.com','18611111111','A01',1,0,null,null,'00001','CEO',null,null,'2020-06-21 17:54:10','admin','2020-07-10 15:27:10',2,'c6d7cb4deeac411cb3384b1b31278596','',0);

INSERT INTO "CUBE"."sys_user_depart"("ID","user_id","dep_id") VALUES('1281490128540393474','e9ca23d68d884d4ebb19d07889727dae','c6d7cb4deeac411cb3384b1b31278596');

INSERT INTO "CUBE"."sys_user_role"("id","user_id","role_id") VALUES('1281490128242597889','e9ca23d68d884d4ebb19d07889727dae','f6817f48af4fb3af11b9e8bf182f618b');

ALTER TABLE "CUBE"."sys_user_role" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_user_depart" ADD CONSTRAINT  PRIMARY KEY("ID") ;

ALTER TABLE "CUBE"."sys_user" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_user" ADD CONSTRAINT "uniq_sys_user_username" UNIQUE("username") ;

ALTER TABLE "CUBE"."sys_user" ADD CONSTRAINT "uniq_sys_user_phone" UNIQUE("phone") ;

ALTER TABLE "CUBE"."sys_user" ADD CONSTRAINT "uniq_sys_user_email" UNIQUE("email") ;

ALTER TABLE "CUBE"."sys_third_account" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_tenant" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_role_permission" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_role_key" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_role" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_role" ADD CONSTRAINT "uniq_sys_role_role_code" UNIQUE("role_code") ;

ALTER TABLE "CUBE"."sys_position" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_position" ADD CONSTRAINT "uniq_code" UNIQUE("code") ;

ALTER TABLE "CUBE"."sys_permission_history" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_permission_data_rule" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_permission" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_log" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_gateway_route" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_fill_rule" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_fill_rule" ADD CONSTRAINT "uni_sys_fill_rule_code" UNIQUE("rule_code") ;

ALTER TABLE "CUBE"."sys_dict_item" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_dict" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_dict" ADD CONSTRAINT "indextable_dict_code" UNIQUE("dict_code") ;

ALTER TABLE "CUBE"."sys_depart_role_user" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_depart_role_permission" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_depart_role" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_depart_permission" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_depart" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_depart" ADD CONSTRAINT "uniq_depart_org_code" UNIQUE("org_code") ;

ALTER TABLE "CUBE"."sys_data_source" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_data_source" ADD CONSTRAINT "sys_data_source_code_uni" UNIQUE("code") ;

ALTER TABLE "CUBE"."sys_data_log" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_check_rule" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_check_rule" ADD CONSTRAINT "uni_sys_check_rule_code" UNIQUE("rule_code") ;

ALTER TABLE "CUBE"."sys_category" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_category" ADD CONSTRAINT "index_code" UNIQUE("code") ;

ALTER TABLE "CUBE"."sys_area" ADD CONSTRAINT  PRIMARY KEY("id") ;

ALTER TABLE "CUBE"."sys_announcement" ADD CONSTRAINT  PRIMARY KEY("id") ;

CREATE INDEX "idx_sur_role_id"
ON "CUBE"."sys_user_role"("role_id");

CREATE INDEX "idx_sur_user_role_id"
ON "CUBE"."sys_user_role"("user_id","role_id");

CREATE INDEX "idx_sur_user_id"
ON "CUBE"."sys_user_role"("user_id");

COMMENT ON TABLE "CUBE"."sys_user_role" IS '用户角色表';

COMMENT ON COLUMN "CUBE"."sys_user_role"."id" IS '主键id';

COMMENT ON COLUMN "CUBE"."sys_user_role"."user_id" IS '用户id';

COMMENT ON COLUMN "CUBE"."sys_user_role"."role_id" IS '角色id';

CREATE INDEX "index_depart_groupk_userid"
ON "CUBE"."sys_user_depart"("user_id");

CREATE INDEX "index_depart_groupkorgid"
ON "CUBE"."sys_user_depart"("dep_id");

CREATE INDEX "index_depart_groupk_uidanddid"
ON "CUBE"."sys_user_depart"("user_id","dep_id");

COMMENT ON COLUMN "CUBE"."sys_user_depart"."ID" IS 'id';

COMMENT ON COLUMN "CUBE"."sys_user_depart"."user_id" IS '用户id';

COMMENT ON COLUMN "CUBE"."sys_user_depart"."dep_id" IS '部门id';

CREATE INDEX "idx_sys_user_status"
ON "CUBE"."sys_user"("status");

CREATE INDEX "idx_sys_user_del_flag"
ON "CUBE"."sys_user"("del_flag");

COMMENT ON TABLE "CUBE"."sys_user" IS '用户表';

COMMENT ON COLUMN "CUBE"."sys_user"."id" IS '主键id';

COMMENT ON COLUMN "CUBE"."sys_user"."username" IS '登录账号';

COMMENT ON COLUMN "CUBE"."sys_user"."realname" IS '真实姓名';

COMMENT ON COLUMN "CUBE"."sys_user"."password" IS '密码';

COMMENT ON COLUMN "CUBE"."sys_user"."salt" IS 'md5密码盐';

COMMENT ON COLUMN "CUBE"."sys_user"."avatar" IS '头像';

COMMENT ON COLUMN "CUBE"."sys_user"."birthday" IS '生日';

COMMENT ON COLUMN "CUBE"."sys_user"."sex" IS '性别(0-默认未知,1-男,2-女)';

COMMENT ON COLUMN "CUBE"."sys_user"."email" IS '电子邮件';

COMMENT ON COLUMN "CUBE"."sys_user"."phone" IS '电话';

COMMENT ON COLUMN "CUBE"."sys_user"."org_code" IS '机构编码';

COMMENT ON COLUMN "CUBE"."sys_user"."status" IS '性别(1-正常,2-冻结)';

COMMENT ON COLUMN "CUBE"."sys_user"."del_flag" IS '删除状态(0-正常,1-已删除)';

COMMENT ON COLUMN "CUBE"."sys_user"."third_id" IS '第三方登录的唯一标识';

COMMENT ON COLUMN "CUBE"."sys_user"."third_type" IS '第三方类型';

COMMENT ON COLUMN "CUBE"."sys_user"."work_no" IS '工号，唯一键';

COMMENT ON COLUMN "CUBE"."sys_user"."post" IS '职务，关联职务表';

COMMENT ON COLUMN "CUBE"."sys_user"."telephone" IS '座机号';

COMMENT ON COLUMN "CUBE"."sys_user"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_user"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_user"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_user"."update_time" IS '更新时间';

COMMENT ON COLUMN "CUBE"."sys_user"."user_identity" IS '身份（1普通成员 2上级）';

COMMENT ON COLUMN "CUBE"."sys_user"."depart_ids" IS '负责部门';

COMMENT ON COLUMN "CUBE"."sys_user"."rel_tenant_ids" IS '多租户标识';

COMMENT ON COLUMN "CUBE"."sys_user"."first_login" IS '是否首次登录（0否 1是）';

COMMENT ON COLUMN "CUBE"."sys_third_account"."id" IS '编号';

COMMENT ON COLUMN "CUBE"."sys_third_account"."sys_user_id" IS '第三方登录id';

COMMENT ON COLUMN "CUBE"."sys_third_account"."third_type" IS '登录来源';

COMMENT ON COLUMN "CUBE"."sys_third_account"."avatar" IS '头像';

COMMENT ON COLUMN "CUBE"."sys_third_account"."status" IS '状态(1-正常,2-冻结)';

COMMENT ON COLUMN "CUBE"."sys_third_account"."del_flag" IS '删除状态(0-正常,1-已删除)';

COMMENT ON COLUMN "CUBE"."sys_third_account"."realname" IS '真实姓名';

COMMENT ON COLUMN "CUBE"."sys_third_account"."third_user_uuid" IS '第三方账号';

COMMENT ON TABLE "CUBE"."sys_tenant" IS '多租户信息表';

COMMENT ON COLUMN "CUBE"."sys_tenant"."id" IS '租户编码';

COMMENT ON COLUMN "CUBE"."sys_tenant"."name" IS '租户名称';

COMMENT ON COLUMN "CUBE"."sys_tenant"."begin_date" IS '开始时间';

COMMENT ON COLUMN "CUBE"."sys_tenant"."end_date" IS '结束时间';

COMMENT ON COLUMN "CUBE"."sys_tenant"."status" IS '状态 1正常 0冻结';

COMMENT ON COLUMN "CUBE"."sys_tenant"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_tenant"."create_time" IS '创建时间';

CREATE INDEX "index_group_role_id"
ON "CUBE"."sys_role_permission"("role_id");

CREATE INDEX "index_group_per_id"
ON "CUBE"."sys_role_permission"("permission_id");

CREATE INDEX "index_group_role_per_id"
ON "CUBE"."sys_role_permission"("role_id","permission_id");

COMMENT ON TABLE "CUBE"."sys_role_permission" IS '角色权限表';

COMMENT ON COLUMN "CUBE"."sys_role_permission"."role_id" IS '角色id';

COMMENT ON COLUMN "CUBE"."sys_role_permission"."permission_id" IS '权限id';

COMMENT ON COLUMN "CUBE"."sys_role_permission"."data_rule_ids" IS '数据权限ids';

COMMENT ON COLUMN "CUBE"."sys_role_permission"."operate_date" IS '操作时间';

COMMENT ON COLUMN "CUBE"."sys_role_permission"."operate_ip" IS '操作ip';

COMMENT ON TABLE "CUBE"."sys_role_key" IS 'API授权管理';

COMMENT ON COLUMN "CUBE"."sys_role_key"."username" IS '用户名';

COMMENT ON COLUMN "CUBE"."sys_role_key"."access_key" IS 'AK';

COMMENT ON COLUMN "CUBE"."sys_role_key"."secret_key" IS 'SK';

COMMENT ON COLUMN "CUBE"."sys_role_key"."enabled" IS '是否启用';

COMMENT ON COLUMN "CUBE"."sys_role_key"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_role_key"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_role_key"."update_time" IS '更新时间';

COMMENT ON COLUMN "CUBE"."sys_role_key"."update_by" IS '更新人';

COMMENT ON TABLE "CUBE"."sys_role" IS '角色表';

COMMENT ON COLUMN "CUBE"."sys_role"."id" IS '主键id';

COMMENT ON COLUMN "CUBE"."sys_role"."role_name" IS '角色名称';

COMMENT ON COLUMN "CUBE"."sys_role"."role_code" IS '角色编码';

COMMENT ON COLUMN "CUBE"."sys_role"."description" IS '描述';

COMMENT ON COLUMN "CUBE"."sys_role"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_role"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_role"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_role"."update_time" IS '更新时间';

COMMENT ON COLUMN "CUBE"."sys_position"."code" IS '职务编码';

COMMENT ON COLUMN "CUBE"."sys_position"."name" IS '职务名称';

COMMENT ON COLUMN "CUBE"."sys_position"."post_rank" IS '职级';

COMMENT ON COLUMN "CUBE"."sys_position"."company_id" IS '公司id';

COMMENT ON COLUMN "CUBE"."sys_position"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_position"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_position"."update_by" IS '修改人';

COMMENT ON COLUMN "CUBE"."sys_position"."update_time" IS '修改时间';

COMMENT ON COLUMN "CUBE"."sys_position"."sys_org_code" IS '组织机构编码';

COMMENT ON COLUMN "CUBE"."sys_permission_history"."id" IS '主键id';

COMMENT ON COLUMN "CUBE"."sys_permission_history"."permission_id" IS '菜单id';

COMMENT ON COLUMN "CUBE"."sys_permission_history"."user_id" IS '用户id';

COMMENT ON COLUMN "CUBE"."sys_permission_history"."create_time" IS '创建时间';

CREATE INDEX "index_fucntionid"
ON "CUBE"."sys_permission_data_rule"("permission_id");

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."id" IS 'ID';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."permission_id" IS '菜单ID';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."rule_name" IS '规则名称';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."rule_column" IS '字段';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."rule_conditions" IS '条件';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."rule_value" IS '规则值';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."status" IS '权限有效状态1有0否';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."update_by" IS '修改人';

COMMENT ON COLUMN "CUBE"."sys_permission_data_rule"."update_time" IS '修改时间';

CREATE INDEX "index_menu_type"
ON "CUBE"."sys_permission"("menu_type");

CREATE INDEX "index_prem_sort_no"
ON "CUBE"."sys_permission"("sort_no");

CREATE INDEX "index_menu_hidden"
ON "CUBE"."sys_permission"("hidden");

CREATE INDEX "index_prem_is_route"
ON "CUBE"."sys_permission"("is_route");

CREATE INDEX "index_prem_del_flag"
ON "CUBE"."sys_permission"("del_flag");

CREATE INDEX "index_menu_status"
ON "CUBE"."sys_permission"("status");

CREATE INDEX "index_prem_pid"
ON "CUBE"."sys_permission"("parent_id");

CREATE INDEX "index_prem_is_leaf"
ON "CUBE"."sys_permission"("is_leaf");

COMMENT ON TABLE "CUBE"."sys_permission" IS '菜单权限表';

COMMENT ON COLUMN "CUBE"."sys_permission"."id" IS '主键id';

COMMENT ON COLUMN "CUBE"."sys_permission"."parent_id" IS '父id';

COMMENT ON COLUMN "CUBE"."sys_permission"."name" IS '菜单标题';

COMMENT ON COLUMN "CUBE"."sys_permission"."url" IS '路径';

COMMENT ON COLUMN "CUBE"."sys_permission"."component" IS '组件';

COMMENT ON COLUMN "CUBE"."sys_permission"."component_name" IS '组件名字';

COMMENT ON COLUMN "CUBE"."sys_permission"."redirect" IS '一级菜单跳转地址';

COMMENT ON COLUMN "CUBE"."sys_permission"."menu_type" IS '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)';

COMMENT ON COLUMN "CUBE"."sys_permission"."perms" IS '菜单权限编码';

COMMENT ON COLUMN "CUBE"."sys_permission"."perms_type" IS '权限策略1显示2禁用';

COMMENT ON COLUMN "CUBE"."sys_permission"."sort_no" IS '菜单排序';

COMMENT ON COLUMN "CUBE"."sys_permission"."always_show" IS '聚合子路由: 1是0否';

COMMENT ON COLUMN "CUBE"."sys_permission"."icon" IS '菜单图标';

COMMENT ON COLUMN "CUBE"."sys_permission"."is_route" IS '是否路由菜单: 0:不是  1:是（默认值1）';

COMMENT ON COLUMN "CUBE"."sys_permission"."is_leaf" IS '是否叶子节点:    1:是   0:不是';

COMMENT ON COLUMN "CUBE"."sys_permission"."keep_alive" IS '是否缓存该页面:    1:是   0:不是';

COMMENT ON COLUMN "CUBE"."sys_permission"."hidden" IS '是否隐藏路由: 0否,1是';

COMMENT ON COLUMN "CUBE"."sys_permission"."description" IS '描述';

COMMENT ON COLUMN "CUBE"."sys_permission"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_permission"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_permission"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_permission"."update_time" IS '更新时间';

COMMENT ON COLUMN "CUBE"."sys_permission"."del_flag" IS '删除状态 0正常 1已删除';

COMMENT ON COLUMN "CUBE"."sys_permission"."rule_flag" IS '是否添加数据权限1是0否';

COMMENT ON COLUMN "CUBE"."sys_permission"."status" IS '按钮权限状态(0无效1有效)';

COMMENT ON COLUMN "CUBE"."sys_permission"."internal_or_external" IS '外链菜单打开方式 0/内部打开 1/外部打开';

CREATE INDEX "index_operate_type"
ON "CUBE"."sys_log"("operate_type");

CREATE INDEX "index_table_userid"
ON "CUBE"."sys_log"("userid");

CREATE INDEX "index_logt_ype"
ON "CUBE"."sys_log"("log_type");

COMMENT ON TABLE "CUBE"."sys_log" IS '系统日志表';

COMMENT ON COLUMN "CUBE"."sys_log"."log_type" IS '日志类型（1登录日志，2操作日志）';

COMMENT ON COLUMN "CUBE"."sys_log"."log_content" IS '日志内容';

COMMENT ON COLUMN "CUBE"."sys_log"."operate_type" IS '操作类型';

COMMENT ON COLUMN "CUBE"."sys_log"."userid" IS '操作用户账号';

COMMENT ON COLUMN "CUBE"."sys_log"."username" IS '操作用户名称';

COMMENT ON COLUMN "CUBE"."sys_log"."ip" IS 'IP';

COMMENT ON COLUMN "CUBE"."sys_log"."method" IS '请求java方法';

COMMENT ON COLUMN "CUBE"."sys_log"."request_param" IS '请求参数';

COMMENT ON COLUMN "CUBE"."sys_log"."cost_time" IS '耗时';

COMMENT ON COLUMN "CUBE"."sys_log"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."router_id" IS '路由ID';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."name" IS '服务名';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."uri" IS '服务地址';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."predicates" IS '断言';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."filters" IS '过滤器';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."retryable" IS '是否重试:0-否 1-是';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."strip_prefix" IS '是否忽略前缀0-否 1-是';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."persist" IS '是否为保留数据:0-否 1-是';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."show_api" IS '是否在接口文档中展示:0-否 1-是';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."status" IS '状态:0-无效 1-有效';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."create_time" IS '创建日期';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."update_time" IS '更新日期';

COMMENT ON COLUMN "CUBE"."sys_gateway_route"."sys_org_code" IS '所属部门';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."id" IS '主键ID';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."rule_name" IS '规则名称';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."rule_code" IS '规则Code';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."rule_class" IS '规则实现类';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."rule_params" IS '规则参数';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."update_by" IS '修改人';

COMMENT ON COLUMN "CUBE"."sys_fill_rule"."update_time" IS '修改时间';

CREATE INDEX "index_table_dict_id"
ON "CUBE"."sys_dict_item"("dict_id");

CREATE INDEX "index_table_dict_status"
ON "CUBE"."sys_dict_item"("status");

CREATE INDEX "index_table_sort_order"
ON "CUBE"."sys_dict_item"("sort_order");

COMMENT ON COLUMN "CUBE"."sys_dict_item"."dict_id" IS '字典id';

COMMENT ON COLUMN "CUBE"."sys_dict_item"."item_text" IS '字典项文本';

COMMENT ON COLUMN "CUBE"."sys_dict_item"."item_value" IS '字典项值';

COMMENT ON COLUMN "CUBE"."sys_dict_item"."description" IS '描述';

COMMENT ON COLUMN "CUBE"."sys_dict_item"."sort_order" IS '排序';

COMMENT ON COLUMN "CUBE"."sys_dict_item"."status" IS '状态（1启用 0不启用）';

ALTER TABLE "CUBE"."sys_dict" ADD CHECK("type" >= 0) ENABLE ;

COMMENT ON COLUMN "CUBE"."sys_dict"."dict_name" IS '字典名称';

COMMENT ON COLUMN "CUBE"."sys_dict"."dict_code" IS '字典编码';

COMMENT ON COLUMN "CUBE"."sys_dict"."description" IS '描述';

COMMENT ON COLUMN "CUBE"."sys_dict"."del_flag" IS '删除状态';

COMMENT ON COLUMN "CUBE"."sys_dict"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_dict"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_dict"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_dict"."update_time" IS '更新时间';

COMMENT ON COLUMN "CUBE"."sys_dict"."type" IS '字典类型0为string,1为number';

COMMENT ON TABLE "CUBE"."sys_depart_role_user" IS '部门角色用户表';

COMMENT ON COLUMN "CUBE"."sys_depart_role_user"."id" IS '主键id';

COMMENT ON COLUMN "CUBE"."sys_depart_role_user"."user_id" IS '用户id';

COMMENT ON COLUMN "CUBE"."sys_depart_role_user"."drole_id" IS '角色id';

COMMENT ON TABLE "CUBE"."sys_depart_role_permission" IS '部门角色权限表';

COMMENT ON COLUMN "CUBE"."sys_depart_role_permission"."depart_id" IS '部门id';

COMMENT ON COLUMN "CUBE"."sys_depart_role_permission"."role_id" IS '角色id';

COMMENT ON COLUMN "CUBE"."sys_depart_role_permission"."permission_id" IS '权限id';

COMMENT ON COLUMN "CUBE"."sys_depart_role_permission"."data_rule_ids" IS '数据权限ids';

COMMENT ON COLUMN "CUBE"."sys_depart_role_permission"."operate_date" IS '操作时间';

COMMENT ON COLUMN "CUBE"."sys_depart_role_permission"."operate_ip" IS '操作ip';

COMMENT ON TABLE "CUBE"."sys_depart_role" IS '部门角色表';

COMMENT ON COLUMN "CUBE"."sys_depart_role"."depart_id" IS '部门id';

COMMENT ON COLUMN "CUBE"."sys_depart_role"."role_name" IS '部门角色名称';

COMMENT ON COLUMN "CUBE"."sys_depart_role"."role_code" IS '部门角色编码';

COMMENT ON COLUMN "CUBE"."sys_depart_role"."description" IS '描述';

COMMENT ON COLUMN "CUBE"."sys_depart_role"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_depart_role"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_depart_role"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_depart_role"."update_time" IS '更新时间';

COMMENT ON TABLE "CUBE"."sys_depart_permission" IS '部门权限表';

COMMENT ON COLUMN "CUBE"."sys_depart_permission"."depart_id" IS '部门id';

COMMENT ON COLUMN "CUBE"."sys_depart_permission"."permission_id" IS '权限id';

COMMENT ON COLUMN "CUBE"."sys_depart_permission"."data_rule_ids" IS '数据规则id';

CREATE INDEX "index_depart_depart_order"
ON "CUBE"."sys_depart"("depart_order");

CREATE INDEX "index_depart_parent_id"
ON "CUBE"."sys_depart"("parent_id");

COMMENT ON TABLE "CUBE"."sys_depart" IS '组织机构表';

COMMENT ON COLUMN "CUBE"."sys_depart"."id" IS 'ID';

COMMENT ON COLUMN "CUBE"."sys_depart"."parent_id" IS '父机构ID';

COMMENT ON COLUMN "CUBE"."sys_depart"."depart_name" IS '机构/部门名称';

COMMENT ON COLUMN "CUBE"."sys_depart"."depart_name_en" IS '英文名';

COMMENT ON COLUMN "CUBE"."sys_depart"."depart_name_abbr" IS '缩写';

COMMENT ON COLUMN "CUBE"."sys_depart"."depart_order" IS '排序';

COMMENT ON COLUMN "CUBE"."sys_depart"."description" IS '描述';

COMMENT ON COLUMN "CUBE"."sys_depart"."area_id" IS '地区Id';

COMMENT ON COLUMN "CUBE"."sys_depart"."gb_code" IS '国标编码';

COMMENT ON COLUMN "CUBE"."sys_depart"."org_category" IS '机构类别 1组织机构，2岗位';

COMMENT ON COLUMN "CUBE"."sys_depart"."org_type" IS '机构类型 1一级部门 2子部门';

COMMENT ON COLUMN "CUBE"."sys_depart"."org_code" IS '机构编码';

COMMENT ON COLUMN "CUBE"."sys_depart"."mobile" IS '手机号';

COMMENT ON COLUMN "CUBE"."sys_depart"."fax" IS '传真';

COMMENT ON COLUMN "CUBE"."sys_depart"."address" IS '地址';

COMMENT ON COLUMN "CUBE"."sys_depart"."memo" IS '备注';

COMMENT ON COLUMN "CUBE"."sys_depart"."status" IS '状态（1启用，0不启用）';

COMMENT ON COLUMN "CUBE"."sys_depart"."del_flag" IS '删除状态（0，正常，1已删除）';

COMMENT ON COLUMN "CUBE"."sys_depart"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_depart"."create_time" IS '创建日期';

COMMENT ON COLUMN "CUBE"."sys_depart"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_depart"."update_time" IS '更新日期';

COMMENT ON COLUMN "CUBE"."sys_data_source"."code" IS '数据源编码';

COMMENT ON COLUMN "CUBE"."sys_data_source"."name" IS '数据源名称';

COMMENT ON COLUMN "CUBE"."sys_data_source"."remark" IS '备注';

COMMENT ON COLUMN "CUBE"."sys_data_source"."db_type" IS '数据库类型';

COMMENT ON COLUMN "CUBE"."sys_data_source"."db_driver" IS '驱动类';

COMMENT ON COLUMN "CUBE"."sys_data_source"."db_url" IS '数据源地址';

COMMENT ON COLUMN "CUBE"."sys_data_source"."db_name" IS '数据库名称';

COMMENT ON COLUMN "CUBE"."sys_data_source"."db_username" IS '用户名';

COMMENT ON COLUMN "CUBE"."sys_data_source"."db_password" IS '密码';

COMMENT ON COLUMN "CUBE"."sys_data_source"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_data_source"."create_time" IS '创建日期';

COMMENT ON COLUMN "CUBE"."sys_data_source"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_data_source"."update_time" IS '更新日期';

COMMENT ON COLUMN "CUBE"."sys_data_source"."sys_org_code" IS '所属部门';

CREATE INDEX "sindex"
ON "CUBE"."sys_data_log"("data_table","data_id");

COMMENT ON COLUMN "CUBE"."sys_data_log"."id" IS 'id';

COMMENT ON COLUMN "CUBE"."sys_data_log"."data_table" IS '表名';

COMMENT ON COLUMN "CUBE"."sys_data_log"."data_id" IS '数据ID';

COMMENT ON COLUMN "CUBE"."sys_data_log"."data_content" IS '数据内容';

COMMENT ON COLUMN "CUBE"."sys_data_log"."data_version" IS '版本号';

COMMENT ON COLUMN "CUBE"."sys_data_log"."create_by" IS '创建人登录名称';

COMMENT ON COLUMN "CUBE"."sys_data_log"."create_time" IS '创建日期';

COMMENT ON COLUMN "CUBE"."sys_data_log"."update_by" IS '更新人登录名称';

COMMENT ON COLUMN "CUBE"."sys_data_log"."update_time" IS '更新日期';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."id" IS '主键id';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."rule_name" IS '规则名称';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."rule_code" IS '规则Code';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."rule_json" IS '规则JSON';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."rule_description" IS '规则描述';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_check_rule"."update_time" IS '更新时间';

COMMENT ON COLUMN "CUBE"."sys_category"."pid" IS '父级节点';

COMMENT ON COLUMN "CUBE"."sys_category"."name" IS '类型名称';

COMMENT ON COLUMN "CUBE"."sys_category"."code" IS '类型编码';

COMMENT ON COLUMN "CUBE"."sys_category"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_category"."create_time" IS '创建日期';

COMMENT ON COLUMN "CUBE"."sys_category"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_category"."update_time" IS '更新日期';

COMMENT ON COLUMN "CUBE"."sys_category"."sys_org_code" IS '所属部门';

COMMENT ON COLUMN "CUBE"."sys_category"."has_child" IS '是否有子节点';

COMMENT ON COLUMN "CUBE"."sys_area"."id" IS '主键-分布式id-雪花算法';

COMMENT ON COLUMN "CUBE"."sys_area"."parent_id" IS '分布式id-雪花算法';

COMMENT ON COLUMN "CUBE"."sys_area"."level" IS '菜单等级';

COMMENT ON COLUMN "CUBE"."sys_area"."path" IS '全路径';

COMMENT ON COLUMN "CUBE"."sys_area"."del_flag" IS '是否删除';

COMMENT ON COLUMN "CUBE"."sys_area"."create_time" IS '创建时间';

COMMENT ON TABLE "CUBE"."sys_announcement_send" IS '用户通告阅读标记表';

COMMENT ON COLUMN "CUBE"."sys_announcement_send"."annt_id" IS '通告ID';

COMMENT ON COLUMN "CUBE"."sys_announcement_send"."user_id" IS '用户id';

COMMENT ON COLUMN "CUBE"."sys_announcement_send"."read_flag" IS '阅读状态（0未读，1已读）';

COMMENT ON COLUMN "CUBE"."sys_announcement_send"."read_time" IS '阅读时间';

COMMENT ON COLUMN "CUBE"."sys_announcement_send"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_announcement_send"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_announcement_send"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_announcement_send"."update_time" IS '更新时间';

COMMENT ON TABLE "CUBE"."sys_announcement" IS '系统通告表';

COMMENT ON COLUMN "CUBE"."sys_announcement"."title" IS '标题';

COMMENT ON COLUMN "CUBE"."sys_announcement"."msg_content" IS '内容';

COMMENT ON COLUMN "CUBE"."sys_announcement"."start_time" IS '开始时间';

COMMENT ON COLUMN "CUBE"."sys_announcement"."end_time" IS '结束时间';

COMMENT ON COLUMN "CUBE"."sys_announcement"."sender" IS '发布人';

COMMENT ON COLUMN "CUBE"."sys_announcement"."priority" IS '优先级（L低，M中，H高）';

COMMENT ON COLUMN "CUBE"."sys_announcement"."msg_category" IS '消息类型1:通知公告2:系统消息';

COMMENT ON COLUMN "CUBE"."sys_announcement"."msg_type" IS '通告对象类型（USER:指定用户，ALL:全体用户）';

COMMENT ON COLUMN "CUBE"."sys_announcement"."send_status" IS '发布状态（0未发布，1已发布，2已撤销）';

COMMENT ON COLUMN "CUBE"."sys_announcement"."send_time" IS '发布时间';

COMMENT ON COLUMN "CUBE"."sys_announcement"."cancel_time" IS '撤销时间';

COMMENT ON COLUMN "CUBE"."sys_announcement"."del_flag" IS '删除状态（0，正常，1已删除）';

COMMENT ON COLUMN "CUBE"."sys_announcement"."bus_type" IS '业务类型(email:邮件 bpm:流程)';

COMMENT ON COLUMN "CUBE"."sys_announcement"."bus_id" IS '业务id';

COMMENT ON COLUMN "CUBE"."sys_announcement"."open_type" IS '打开方式(组件：component 路由：url)';

COMMENT ON COLUMN "CUBE"."sys_announcement"."open_page" IS '组件/路由 地址';

COMMENT ON COLUMN "CUBE"."sys_announcement"."create_by" IS '创建人';

COMMENT ON COLUMN "CUBE"."sys_announcement"."create_time" IS '创建时间';

COMMENT ON COLUMN "CUBE"."sys_announcement"."update_by" IS '更新人';

COMMENT ON COLUMN "CUBE"."sys_announcement"."update_time" IS '更新时间';

COMMENT ON COLUMN "CUBE"."sys_announcement"."user_ids" IS '指定用户';

COMMENT ON COLUMN "CUBE"."sys_announcement"."msg_abstract" IS '摘要';