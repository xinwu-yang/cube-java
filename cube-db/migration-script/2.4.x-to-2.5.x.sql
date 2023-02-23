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

INSERT INTO `sys_permission` VALUES ('1555081846828613633', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '授权管理', '/isystem/ram', 'system/SysRoleKeyList', NULL, NULL, 1, NULL, '1', 7.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2022-08-04 14:42:59', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_role_permission` VALUES ('1555083117631422466', 'f6817f48af4fb3af11b9e8bf182f618b', '1555081846828613633', NULL, '2022-08-04 14:48:02', NULL);