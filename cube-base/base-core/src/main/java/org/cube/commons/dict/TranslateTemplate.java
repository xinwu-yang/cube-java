package org.cube.commons.dict;

import lombok.Data;

import java.util.List;

@Data
public class TranslateTemplate {

    /**
     * 需要翻译的类
     */
    private Class<?> cls;

    /**
     * 需要字典翻译的属性
     */
    private List<FieldInfo> fieldInfoList;
}
