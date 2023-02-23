package org.cube.plugin.easyexcel.dict;


import org.cube.commons.annotations.Dict;

/**
 * 处理字典翻译
 */
public interface IDictTranslator {

    /**
     * 值转id
     *
     * @param value 值
     * @param dict  字典转换映射
     * @return id
     */
    String valueToId(String value, Dict dict);

    /**
     * id转值
     *
     * @param id   id
     * @param dict 字典转换映射
     * @return 值
     */
    String idToValue(String id, Dict dict);
}
