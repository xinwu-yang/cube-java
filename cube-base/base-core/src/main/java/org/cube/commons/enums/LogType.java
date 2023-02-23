package org.cube.commons.enums;

/**
 * 日志类型
 */
public enum LogType {

    /**
     * 登录日志
     */
    LOGIN(1),

    /**
     * 操作日志
     */
    OPERATE(2);

    private final int value;

    public int getValue() {
        return value;
    }

    LogType(int value) {
        this.value = value;
    }
}
