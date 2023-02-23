package org.cube.plugin.easyexcel.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.cube.commons.annotations.Dict;
import org.cube.plugin.easyexcel.EasyExcel;
import org.cube.plugin.easyexcel.annotations.Check;
import org.cube.plugin.easyexcel.annotations.Excel;
import org.cube.plugin.easyexcel.annotations.Ignore;
import org.cube.plugin.easyexcel.dict.IDictTranslator;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.plugin.easyexcel.model.ImportMessage;
import org.cube.plugin.easyexcel.model.ImportReslt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * EasyExcel POI实现
 */
@Data
@AllArgsConstructor
public class POIEasyExcel implements EasyExcel {

    // 是否启用支持XLSX
    private boolean supportXlsx;

    @Override
    @SneakyThrows
    public <T> List<T> read(Class<T> cls, ImportExcel importExcel, IDictTranslator dictTranslator) {
        List<T> data = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(importExcel.getInputStream());
        Sheet sheet = workbook.getSheetAt(importExcel.getSheetIndex());
        int lastRowNum = sheet.getLastRowNum() + 1;
        // 得到标题行
        Row titleCellRangeRow = sheet.getRow(importExcel.getStartRow() - 1);
        // 得到标题索引
        Map<String, Integer> titleIndex = new HashMap<>();
        for (int i = 0; i < titleCellRangeRow.getLastCellNum(); i++) {
            titleIndex.put(titleCellRangeRow.getCell(i).getStringCellValue(), i);
        }
        Field[] fields = cls.getDeclaredFields();
        // 排除static字段
        List<Field> unStaticFieldList = ListUtil.toList(fields).stream().filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
        // 遍历数据
        for (int i = importExcel.getStartRow(); i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            T obj = cls.newInstance();
            for (Field field : unStaticFieldList) {

                String name = field.getName();
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null) {
                    name = excel.value();
                }
                Integer idx = titleIndex.get(name);
                if (idx == null) {
                    continue;
                }
                String strValue;
                Cell cell = row.getCell(idx);
                CellType cellType = cell.getCellType();
                // 单元格未填写的情况下是null
                if (cellType == null) {
                    continue;
                }
                if (CellType.NUMERIC.equals(cellType)) {
                    strValue = Convert.convert(String.class, cell.getNumericCellValue());
                } else {
                    strValue = row.getCell(idx).getStringCellValue();
                }
                if (StrUtil.isBlank(strValue)) {
                    continue;
                }
                String fieldTypeName = field.getType().getSimpleName().toLowerCase();
                // 字典转换
                Dict dict = field.getAnnotation(Dict.class);
                if (dictTranslator != null && dict != null) {
                    strValue = dictTranslator.valueToId(strValue, dict);
                }
                Object value;
                if (fieldTypeName.contains("int")) {
                    value = Integer.valueOf(strValue);
                } else if (fieldTypeName.contains("long")) {
                    value = Long.valueOf(strValue);
                } else if (fieldTypeName.contains("bool")) {
                    value = Boolean.valueOf(strValue);
                } else if (fieldTypeName.contains("date")) {
                    DateTime dateTime = DateUtil.parse(strValue);
                    value = dateTime.toJdkDate();
                } else if (fieldTypeName.contains("double")) {
                    value = Double.valueOf(strValue);
                } else if (field.getType().isEnum()) {
                    Class fieldClass = field.getType();
                    value = Enum.valueOf(fieldClass, strValue);
                } else {
                    value = strValue;
                }
                field.setAccessible(true);
                field.set(obj, value);
            }
            data.add(obj);
        }
        return data;
    }


    @Override
    @SneakyThrows
    public <T> ImportReslt<T> importExcel(Class<T> cls, ImportExcel importExcel, IDictTranslator dictTranslator) {
        ImportReslt importReslt = new ImportReslt();
        ImportMessage errorMessage = new ImportMessage();
        Workbook workbook = WorkbookFactory.create(importExcel.getInputStream());
        Sheet sheet = workbook.getSheetAt(importExcel.getSheetIndex());
        int lastRowNum = sheet.getLastRowNum() + 1;
        // 得到标题行
        Row titleCellRangeRow = sheet.getRow(importExcel.getStartRow() - 1);
        // 得到标题索引
        Map<String, Integer> titleIndex = new HashMap<>();
        for (int i = importExcel.getStartLine(); i < titleCellRangeRow.getLastCellNum(); i++) {
            titleIndex.put(titleCellRangeRow.getCell(i).getStringCellValue(), i);
        }
        Field[] fields = cls.getDeclaredFields();
        // 排除static字段
        List<Field> unStaticFieldList = ListUtil.toList(fields).stream().filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
        // 遍历数据
        for (int i = importExcel.getStartRow(); i < lastRowNum; i++) {
            errorMessage.setRow(i+1);
            Row row = sheet.getRow(i);
            T obj = cls.newInstance();
            for (Field field : unStaticFieldList) {

                String name = field.getName();
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null) {
                    name = excel.value();
                }
                Integer idx = titleIndex.get(name);

                if (idx == null) {
                    continue;
                }
                if (row ==null){
                    errorMessage.setMsg("无数据");
                    importReslt.setSuccessful(false);
                }
                String strValue;
                Cell cell = row.getCell(idx);
                CellType cellType = cell.getCellType();
                // 单元格未填写的情况下是null
                if (cellType == null) {
                    continue;
                }
                if (CellType.NUMERIC.equals(cellType)) {
                    strValue = Convert.convert(String.class, cell.getNumericCellValue());
                } else {
                    strValue = row.getCell(idx).getStringCellValue();
                }
                if (StrUtil.isBlank(strValue)) {
                    continue;
                }
                //字段约束性验证
                errorMessage.setIndex(idx+1);
                Boolean aBoolean = checkFiled(field, strValue, errorMessage);
                if (!aBoolean){
                    importReslt.setImportMessage(errorMessage);
                    importReslt.setSuccessful(false);
                    return importReslt;
                }
                String fieldTypeName = field.getType().getSimpleName().toLowerCase();
                // 字典转换
                Dict dict = field.getAnnotation(Dict.class);
                if (dictTranslator != null && dict != null) {
                    strValue = dictTranslator.valueToId(strValue, dict);
                }
                Object value;
                try {
                    if (fieldTypeName.contains("int")) {
                        value = Integer.valueOf(strValue);
                    } else if (fieldTypeName.contains("long")) {
                        value = Long.valueOf(strValue);
                    } else if (fieldTypeName.contains("bool")) {
                        value = Boolean.valueOf(strValue);
                    } else if (fieldTypeName.contains("date")) {
                        DateTime dateTime = DateUtil.parse(strValue);
                        value = dateTime.toJdkDate();
                    } else if (fieldTypeName.contains("double")) {
                        value = Double.valueOf(strValue);
                    } else if (field.getType().isEnum()) {
                        Class fieldClass = field.getType();
                        value = Enum.valueOf(fieldClass, strValue);
                    } else {
                        value = strValue;
                    }
                    field.setAccessible(true);
                    field.set(obj, value);

                }catch (NumberFormatException e){
                    errorMessage.setMsg("输入的值类型不正确");
                    importReslt.setSuccessful(false);
                }catch (Exception e){
                    errorMessage.setMsg("导入异常");
                    importReslt.setSuccessful(false);
                }
                importReslt.setImportMessage(errorMessage);
                if (!importReslt.getSuccessful()){
                    return importReslt;
                }
            }

            importReslt.getData().add(obj);
        }

        if (importReslt.getSuccessful()){
            importReslt.setImportMessage(null);
        }
        return importReslt;
    }

    private Boolean checkFiled( Field field, String strValue, ImportMessage errorMessage) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Check check = field.getAnnotation(Check.class);
        if (check != null){
            Class[] validated = check.validated();
            String message = check.message();
            for (int i1 = 0; i1 < validated.length; i1++) {
                Class aClass = validated[i1];
                Object object = aClass.newInstance();
                Method initialize = aClass.getMethod("initialize",String.class);
                String str = (String) initialize.invoke(object, strValue);
                Method isValid = aClass.getMethod("isValid",String.class);
                Boolean invoke = (Boolean) isValid.invoke(object, str);
                if (!invoke){
                    errorMessage.setMsg(message);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String readToJson(Class<?> cls, ImportExcel excel, IDictTranslator dictHandler) {
        List<?> list = read(cls, excel, dictHandler);
        return JSONUtil.toJsonStr(list);
    }

    @Override
    @SneakyThrows
    public String readToJson(ImportExcel excel) {
        JSONArray data = JSONUtil.createArray();
        Workbook workbook = WorkbookFactory.create(excel.getInputStream());
        Sheet sheet = workbook.getSheetAt(excel.getSheetIndex());
        // 得到所有行
        int lastRowNum = sheet.getLastRowNum();
        // 得到标题行
        Row titleCellRangeRow = sheet.getRow(excel.getStartRow() - 1);
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < titleCellRangeRow.getLastCellNum(); i++) {
            titles.add(titleCellRangeRow.getCell(i).getStringCellValue());
        }
        // 遍历数据
        for (int i = excel.getStartRow(); i < lastRowNum; i++) {
            JSONObject obj = JSONUtil.createObj();
            Row row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                CellType cellType = cell.getCellType();
                if (CellType.NUMERIC.equals(cellType)) {
                    obj.set(titles.get(j), row.getCell(j).getNumericCellValue());
                } else {
                    obj.set(titles.get(j), row.getCell(j).getStringCellValue());
                }
            }

            data.add(obj);
        }
        return data.toString();
    }

    @Override
    @SneakyThrows
    public void export(List<?> list, OutputStream outputStream, IDictTranslator dictTranslator) {
        int dataSize = list.size();
        if (dataSize > 0) {
            Field[] fields = list.get(0).getClass().getDeclaredFields();
            try (Workbook workbook = WorkbookFactory.create(supportXlsx)) {
                Sheet sheet = workbook.createSheet("Data");
                Font font = workbook.createFont();
                font.setBold(true);
                font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
                font.setFontHeightInPoints((short) 14);
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFont(font);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                // 排除static字段
                List<Field> unStaticFieldList = ListUtil.toList(fields).stream().filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
                int k=0;
                for (int i = 0; i < unStaticFieldList.size();i++) {
                    Field field = unStaticFieldList.get(i);
                    Ignore annotation = field.getAnnotation(Ignore.class);
                    if (annotation != null){
                        continue;
                    }
                    String name = field.getName();
                    Excel excel = field.getAnnotation(Excel.class);
                    if (excel != null) {
                        name = excel.value();
                    }
                    // 写Header
                    Row header = sheet.getRow(0);
                    if (header == null) {
                        header = sheet.createRow(0);
                    }
                    Cell headerCell = header.getCell(k);
                    if (headerCell == null) {
                        headerCell = header.createCell(k);
                    }
                    headerCell.setCellStyle(cellStyle);
                    headerCell.setCellValue(name);
                    // 字典转换
                    Dict dict = field.getAnnotation(Dict.class);
                    // 按列写数据
                    for (int j = 0; j < dataSize; j++) {
                        Row row = sheet.getRow(1 + j);
                        if (row == null) {
                            row = sheet.createRow(1 + j);
                        }
                        Cell cell = row.getCell(k);
                        if (cell == null) {
                            cell = row.createCell(k);
                        }
                        field.setAccessible(true);
                        String cellData = " ";
                        Object value = field.get(list.get(j));
                        if (ObjectUtil.isNotEmpty(value)) {
                            if (value instanceof Date) {
                                cellData = DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss");
                            } else if (value instanceof List) {
                                cellData = JSONUtil.toJsonStr(value);
                            } else {
                                cellData = String.valueOf(value);
                            }
                            if (dictTranslator != null && dict != null && StrUtil.isNotEmpty(cellData)) {
                                cellData = dictTranslator.idToValue(cellData, dict);
                            }
                        }
                        cell.setCellValue(cellData);
                    }
                    sheet.autoSizeColumn(k);
                    k++;
                }
                // 写到输出流
                workbook.write(outputStream);
            }
        }
    }

    @Override
    public boolean supportXlsx() {
        return supportXlsx;
    }

    @Override
    public String getExtension() {
        if (supportXlsx) {
            return ".xlsx";
        }
        return ".xls";
    }
}
