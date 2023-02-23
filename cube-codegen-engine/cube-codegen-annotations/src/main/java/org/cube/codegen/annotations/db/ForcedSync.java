package org.cube.codegen.annotations.db;

import java.lang.annotation.*;

/**
 * 强制同步数据库
 * 删除老表创建新表
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ForcedSync {
}
