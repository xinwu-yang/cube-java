package org.cube.commons.mybatisplus;

/**
 * 查询链接规则
 *
 * @author Sunjianlei
 */
public enum MatchTypeEnum {

    AND("AND"),
    OR("OR");

    private String value;

    MatchTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MatchTypeEnum getByValue(Object value) {
        if (value == null) {
            return null;
        }
        return getByValue(value.toString());
    }

    public static MatchTypeEnum getByValue(String value) {
        if (value == null) {
            return null;
        }
        for (MatchTypeEnum val : values()) {
            if (val.getValue().equalsIgnoreCase(value)) {
                return val;
            }
        }
        return null;
    }
}
