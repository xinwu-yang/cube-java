ALTER TABLE `sys_depart` 
ADD COLUMN `area_id`  bigint(20) NULL COMMENT '地区ID' AFTER `description`,
ADD COLUMN `gb_code`  varchar(64) NULL COMMENT '国标编码' AFTER `area_id`;

-- ----------------------------
-- Table structure for sys_permission_history
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_history`;
CREATE TABLE `sys_permission_history`
(
    `id`            bigint                                 NOT NULL COMMENT '主键id',
    `permission_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单id',
    `user_id`       varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `create_time`   datetime                               NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- 添加两个新菜单
-- ----------------------------
INSERT INTO sys_permission(id, parent_id, name, perms, perms_type, icon, component, component_name, url, redirect,
                           sort_no, menu_type, description, create_by, del_flag, rule_flag, create_time, update_by,
                           update_time, status)
VALUES ('7b52783b4e8bf4253f0a93c87e8e1db8', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '行为统计', null, '1', null,
        'system/PermissionCountList', null, '/permission/count', null, 1.0, 1, null, 'admin', 0, 0,
        '2020-10-28 10:53:13', null, null, '1'),
       ('a74587a4288d054867e4389cb8c9d11a', 'e41b69c57a941a3bbcce45032fe57605', '数据导出', null, '1', null,
        'system/DataExport', null, '/data/export/sql', null, 1.0, 1, null, 'admin', 0, 0, '2020-10-19 06:18:32', null,
        null, '1');

-- ----------------------------
-- 授权给Admin
-- ----------------------------
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('e7dc8eddae6ee4c867fd430e75225bca', 'f6817f48af4fb3af11b9e8bf182f618b', '7b52783b4e8bf4253f0a93c87e8e1db8',
        NULL, '2020-11-03 11:36:09', '25.30.14.35'),
       ('abd574f45f4b64d7e9f267102802e3ec', 'f6817f48af4fb3af11b9e8bf182f618b', 'a74587a4288d054867e4389cb8c9d11a',
        NULL, '2020-11-03 11:36:09', '25.30.14.35');