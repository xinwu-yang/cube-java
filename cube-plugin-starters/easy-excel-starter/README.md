### easy-excel

##### 版本: 1.2.0

##### 功能点:

- 支持xls(POI)和xlsx（POI-OOXML）
- 支持读取Excel为Java对象
- 支持读取Excel内容为Json（Excel结构）
- 支持读取Excel内容为Json（Java 结构）
- 支持导出Excel
- 支持字典的转换，导入导出均可

##### Quick Start:

1. 在Maven的pom.xml配置公司的私服，引入依赖

```xml
<repositories>
    <!-- 公司私服-->
    <repository>
        <id>tievd</id>
        <url>http://125.71.201.14:8999/repository/maven-public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.tievd.cube</groupId>
        <artifactId>easy-excel-spring-boot-starter</artifactId>
        <version>${version}</version>
    </dependency>
    <!-- 默认 -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi</artifactId>
        <version>5.2.1</version>
    </dependency>
    <!-- 可选 -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.1</version>
    </dependency>
</dependencies>
```

2. 调用

```
// 注解
// 字典转换配置，支持数据字典和表字典
@Dict
// 导入导出字段映射的列名
@Excel

// 表字典
@Excel("职务") // 列名
@Dict(table = "sys_position", text = "name", value = "code")
private String post;

// 数据字典
@Dict("sex")
@Excel("性别") // 列名
private Integer sex;

// 导入参数设置
ImportExcel excel = new ImportExcel();
// 读取sheet下标
excel.setSheetIndex(0);
// 从第几行开始读取（包含标题）
excel.setStartRow(1);
// excel输入流
excel.setInputStream(new FileInputStream("C:\\Users\\xinwuy\\Desktop\\kafka-out.xlsx"));

// 引用API
@Autowired
EasyExcel easyExcel;

// 读取Excel为Java对象
// @Param Class 要转化的实体类
// @Param excel 读取参数
// @Param dictTranslator 字典翻译实现类
List<Glances> glancesList = easyExcel.read(Glances.class, excel, dictTranslator);

// 读取Excel为Json字符串（Java结构）
// @Param Class 要转化的实体类（JSON）
// @Param excel 读取参数
// @Param dictTranslator 字典翻译实现类
String data = easyExcel.readToJson(Glances.class, excel, dictTranslator);

// 读取Excel为Json字符串
// @Param excel 读取参数
String data = easyExcel.readToJson(excel);

// 导出Excel
// @Param List<T> 要写入的数据
// @Param outputStream 输出流
// @Param dictTranslator 字典翻译实现类
easyExcel.export(glancesList, outputStream, dictTranslator);
```

3. 字典转换

```java
// 字典转换
// 实现 IDictTranslator 接口，默认cube-system已经实现
package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.tievd.cube.commons.annotations.Dict;
import org.cube.plugin.easyexcel.dict.IDictTranslator;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictTranslatorImpl implements IDictTranslator {

    @Autowired
    private ISysBaseAPI sysBaseApi;

    @Override
    public String valueToId(String value, Dict dict) {
        String id;
        String table = dict.table();
        String code = dict.value();
        if (StrUtil.isEmpty(table)) {
            // 数据字典
            id = sysBaseApi.translateDictKey(code, value);
        } else {
            // 表字典
            String text = dict.text();
            id = sysBaseApi.translateDictKeyFromTable(table, text, code, value);
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
            value = sysBaseApi.translateDict(code, id);
        } else {
            // 表字典
            String text = dict.text();
            value = sysBaseApi.translateDictFromTable(table, text, code, id);
        }
        return value;
    }
}

```