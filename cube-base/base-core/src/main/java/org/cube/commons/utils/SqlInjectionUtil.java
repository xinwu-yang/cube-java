package org.cube.commons.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * sql注入处理工具类
 *
 * @author zhoujf
 */
@Slf4j
public class SqlInjectionUtil {
    private final static List<String> XSS_LIST = ListUtil.of(" and ", " or ", "select ", "insert ", "delete ", "update ", "exec ", "drop ", "count ", "chr ", "mid ", "master ", "truncate ", "char ", "declare ", ";", "+");

    private final static List<String> TABLE_DICT_XSS_LIST = ListUtil.sub(XSS_LIST, 3, XSS_LIST.size());

    /**
     * SQL注入过滤处理
     */
    public static void filterContent(String... values) {
        filterContentWithKeywords(XSS_LIST, values);
    }

    /**
     * 表字典SQL注入过滤处理
     */
    public static void filterContentForTableDict(String... values) {
        filterContentWithKeywords(TABLE_DICT_XSS_LIST, values);
    }

    /**
     * 基础过滤方法
     *
     * @param keywords 关键字列表
     * @param values   SQL语句片段
     */
    public static void filterContentWithKeywords(List<String> keywords, String... values) {
        for (String value : values) {
            if (StrUtil.isEmpty(value)) {
                continue;
            }
            // 统一转为小写
            value = value.toLowerCase();
            for (String xss : keywords) {
                if (value.contains(xss)) {
                    log.error("请注意，存在SQL注入关键词---> {}", xss);
                    log.error("请注意，值可能存在SQL注入风险!---> {}", value);
                    throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
                }
            }
        }
    }
}
