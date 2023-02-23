package org.cube.commons.enums;

/**
 * 操作日志类型
 */
public enum OperateLogType {

    /**
     * 操作日志类型： 查询
     */
    SELECT(1),

    /**
     * 操作日志类型： 添加
     */
    ADD(2),

    /**
     * 操作日志类型： 更新
     */
    UPDATE(3),

    /**
     * 操作日志类型： 删除
     */
    DELETE(4),

    /**
     * 操作日志类型： 导入
     */
    IMPORT(5),

    /**
     * 操作日志类型： 导出
     */
    EXPORT(6),

    /**
     * 自定义
     */
    OTHER(7);

    private final int value;

    public int getValue() {
        return value;
    }

    OperateLogType(int value) {
        this.value = value;
    }
}
