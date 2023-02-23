package org.cube.plugin.sqlexport.utils;

public class NameStrategy {
    private final static String UNDERLINE = "_";

    /**
     * 驼峰命名转下划线命名
     *
     * @param variable 名称
     * @return 下划线命名
     */
    public static String toUnderline(String variable) {
        StringBuilder sb = new StringBuilder(variable);
        int temp = 0;//定位
        if (!variable.contains(UNDERLINE)) {
            for (int i = 0; i < variable.length(); i++) {
                if (Character.isUpperCase(variable.charAt(i)) && i != 0) {
                    sb.insert(i + temp, UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 属性转其get方法名称
     *
     * @param variable 属性名称
     * @return get方法名称
     */
    public static String toGetterName(String variable) {
        return "get" + Character.toUpperCase(variable.charAt(0)) + variable.substring(1);
    }
}
