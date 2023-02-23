package org.cube.commons.dict;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.annotations.Dict;
import org.cube.commons.api.CommonAPI;
import org.cube.commons.base.Result;
import org.cube.modules.system.model.DictModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字典翻译组件
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-04-20
 */
@Slf4j
@Component
public class DictComponent {

    private static final List<String> BASIC_TYPE = ListUtil.of(String.class.getName(), Long.class.getName(), Integer.class.getName(), Double.class.getName(), Float.class.getName(), Boolean.class.getName());

    /**
     * 缓存所有需要翻译的对象的反射模板
     */
    private static final Map<String, TranslateTemplate> translateTemplateMap = new ConcurrentHashMap<>(64);

    @Autowired
    private CommonAPI commonAPI;
    @Autowired
    private ObjectMapper mapper;

    /**
     * 字典翻译
     *
     * @param object API响应的Result对象
     * @apiNote 通过对实体类添加注解@Dict，来标识需要的字典内容，字典分为：数据字典（value） ，表字典（value/table/text）
     */
    public void parseDictText(Object object) {
        if (object instanceof Result) {
            Result<Object> result = (Result<Object>) object;
            Object resultBody = result.getResult();
            if (resultBody != null) {
                if (resultBody instanceof IPage) { // 分页翻译
                    IPage<Object> page = (IPage<Object>) resultBody;
                    page.setRecords(translateObjects(page.getRecords()));
                } else if (resultBody instanceof List) { // 普通列表翻译
                    List<Object> list = Convert.convert(List.class, resultBody);
                    result.setResult(translateObjects(list));
                } else { // 单个对象
                    result.setResult(translateObjects(ListUtil.of(resultBody)).get(0));
                }
            }
        }
    }

    /**
     * 批量翻译
     *
     * @param objects 要输出的对象
     * @apiNote 解决多次SQL查询问题，将同样的SQL合并到一次查询
     */
    public List<Object> translateObjects(List<Object> objects) {
        // 读取字典翻译模板和要翻译的字典值
        Map<String, List<String>> fieldValuesMap = MapUtil.newHashMap(10);
        for (Object object : objects) {
            String className = object.getClass().getName();
            // 判断如果数据是基础数据类型则没有翻译的必要了
            if (BASIC_TYPE.contains(className)) {
                return objects;
            }
            ObjectNode root = mapper.valueToTree(object);
            TranslateTemplate translateTemplate = translateTemplateMap.get(className);
            if (translateTemplate != null) {
                List<FieldInfo> fieldInfoList = translateTemplate.getFieldInfoList();
                for (FieldInfo fieldInfo : fieldInfoList) {
                    JsonNode fieldValue = root.get(fieldInfo.getName());
                    String value = fieldValue == null ? null : fieldValue.asText();
                    List<String> values = fieldValuesMap.computeIfAbsent(className + ":" + fieldInfo.getName(), k -> new LinkedList<>());
                    splitValue(value, values);
                }
            } else {
                translateTemplate = new TranslateTemplate();
                List<FieldInfo> fieldInfoList = new ArrayList<>();
                for (Field field : ReflectUtil.getFieldsDirectly(object.getClass(), true)) {
                    Dict dict = field.getAnnotation(Dict.class);
                    if (dict != null) {
                        FieldInfo fieldInfo = new FieldInfo();
                        fieldInfo.setName(field.getName());
                        String code = dict.value();
                        fieldInfo.setCode(code);
                        String text = dict.text();
                        fieldInfo.setText(text);
                        String table = dict.table();
                        fieldInfo.setTable(table);
                        fieldInfoList.add(fieldInfo);
                        JsonNode fieldValue = root.get(fieldInfo.getName());
                        String value = fieldValue == null ? null : fieldValue.asText();
                        List<String> values = new LinkedList<>();
                        splitValue(value, values);
                        fieldValuesMap.put(className + ":" + field.getName(), values);
                    }
                }
                translateTemplate.setCls(object.getClass());
                translateTemplate.setFieldInfoList(fieldInfoList);
                translateTemplateMap.put(className, translateTemplate);
            }
        }
        // 把翻译好的值写入到要返回的对象中
        List<Object> returnList = new ArrayList<>();
        for (Object object : objects) {
            String className = object.getClass().getName();
            ObjectNode root = mapper.valueToTree(object);
            TranslateTemplate translateTemplate = translateTemplateMap.get(className);
            List<FieldInfo> fieldInfoList = translateTemplate.getFieldInfoList();
            for (FieldInfo fieldInfo : fieldInfoList) {
                JsonNode fieldValue = root.get(fieldInfo.getName());
                if (fieldValue == null) {
                    root.put(fieldInfo.getName() + CommonConst.DICT_TEXT_SUFFIX, "");
                    continue;
                }
                String value = fieldValue.asText();
                List<DictModel> dictModels = new ArrayList<>();
                if (StrUtil.isEmpty(fieldInfo.getTable())) {
                    dictModels = commonAPI.queryDictItemsByCode(fieldInfo.getCode());
                } else {
                    List<String> values = fieldValuesMap.get(className + ":" + fieldInfo.getName());
                    if (values.size() > 0) {
                        dictModels = commonAPI.translateDictFromTable(fieldInfo.getTable(), fieldInfo.getText(), fieldInfo.getCode(), values);
                    }
                }
                String dictText = "";
                dictModelsForeach:
                for (DictModel dictModel : dictModels) {
                    if (StrUtil.isNotEmpty(value)) {
                        if (value.contains(",")) {
                            String[] values = value.split(",");
                            for (String v : values) {
                                if (dictModel.getValue().equals(v)) {
                                    if (StrUtil.isEmpty(dictText)) {
                                        dictText = dictText.concat(dictModel.getText());
                                    } else {
                                        dictText = dictText.concat(",").concat(dictModel.getText());
                                    }
                                    break dictModelsForeach;
                                }
                            }
                        } else {
                            if (dictModel.getValue().equals(value)) {
                                dictText = dictModel.getText();
                                break;
                            }
                        }
                    }
                }
                root.put(fieldInfo.getName() + CommonConst.DICT_TEXT_SUFFIX, dictText);
            }
            returnList.add(root);
        }
        return returnList;
    }

    /**
     * 拆分多个值的value
     *
     * @param value 值
     * @param list  列表
     */
    private void splitValue(String value, List<String> list) {
        if (StrUtil.isNotEmpty(value)) {
            if (value.contains(",")) {
                String[] values = value.split(",");
                list.addAll(Arrays.asList(values));
            } else {
                list.add(value);
            }
        }
    }
}
