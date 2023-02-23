package org.cube.commons.dict;

import cn.hutool.core.util.StrUtil;
import org.cube.commons.api.CommonAPI;
import org.cube.commons.annotations.Dict;
import org.cube.plugin.easyexcel.dict.IDictTranslator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * EasyExcel 字典翻译
 *
 * @author 杨欣武
 * @version 2.4.3
 * @since 2022-05-19
 */
public class DictTranslator implements IDictTranslator {

    @Autowired
    private CommonAPI commonAPI;

    @Override
    public String valueToId(String value, Dict dict) {
        String id;
        String table = dict.table();
        String code = dict.value();
        if (StrUtil.isEmpty(table)) {
            // 数据字典
            id = commonAPI.translateDictKey(code, value);
        } else {
            // 表字典
            String text = dict.text();
            id = commonAPI.translateDictKeyFromTable(table, text, code, value);
        }
        return id;
    }

    @Override
    public String idToValue(String id, Dict dict) {
        String value;
        String table = dict.table();
        String code = dict.value();
        if (StrUtil.isEmpty(table)) {
            // 数据字典
            value = commonAPI.translateDict(code, id);
        } else {
            // 表字典
            String text = dict.text();
            value = commonAPI.translateDictFromTable(table, text, code, id);
        }
        return value;
    }
}
