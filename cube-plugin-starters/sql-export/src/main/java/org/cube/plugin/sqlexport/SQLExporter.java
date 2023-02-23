package org.cube.plugin.sqlexport;

import java.util.List;

public interface SQLExporter {
    /**
     * 导出单条 Insert SQL
     *
     * @param obj entity
     * @return SQL
     */
    String insert(Object obj);

    /**
     * 批量导出 Insert SQL
     *
     * @param objs entities
     * @return SQL's
     */
    <T> List<String> inserts(List<T> objs);

    /**
     * 导出单条delete语句
     *
     * @param obj entity
     * @return SQL
     */
    String delete(Object obj);

    /**
     * 批量导出delete语句
     *
     * @param objs entities
     * @return SQL's
     */
    <T> List<String> deletes(List<T> objs);

    /**
     * 设置外键忽略
     *
     * @param value true/false 忽略和恢复
     * @return sql
     */
    String foreignKeyChecks(boolean value);

    /**
     * 单行注释
     *
     * @param content 正文
     * @return comment
     */
    String comment(String content);
}
