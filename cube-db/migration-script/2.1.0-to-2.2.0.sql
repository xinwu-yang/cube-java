ALTER TABLE `cube`.`sys_announcement` 
CHANGE COLUMN `titile` `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题' AFTER `id`;