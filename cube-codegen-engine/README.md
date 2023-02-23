# 魔方代码生成器

### 项目结构说明

```text
├─cube-codegen-engine（父POM： 项目依赖、modules组织）
│  ├─cube-codegen-annotations API注解等
│  ├─cube-codegen-core 核心实现
│  ├─cube-codegen-web Http API
│  ├─cube-codegen-dbSync 数据库结构同步
```

```xml

<dependency>
    <groupId>com.tievd.cube.codegen</groupId>
    <artifactId>cube-codegen-web</artifactId>
    <version>${latest-version}</version>
</dependency>
```

### 注解说明：

#### 样式/注释/文档说明类型注解
```
// 类注解
@SubTables // 子表关联 注解在主表
@Form // 表单定义 DBSync 描述会添加数据库comment
@QueryFields // 查询条件定义
@QueryField // 查询条件定义
@Groups // 分组定义
@Group // 分组定义
@JavaPackage // 定义包名 一对多下跟随主表

// 字段注解
@FormField // 属性定义
@ForeignKey // 子表外键定义 注解在子表外键
@ComponentParam // 定义component的参数
```

#### 数据库相关注解

```
// 类注解
@ForcedSync // 强制同步: 删除老表重新建表

// 字段注解
@DBField // 数据库字段定义
@DefaultValue // 定义字段数据库默认值
```

#### MyBatis-Plus注解

```
@TableName // 重写表名
@TableId // 定义主键和主键生成策略
@TableField // 定义字段名称
@TableLogic // 定义逻辑删除字段
```

### DBSync配置文件(必须)

```properties
## resources目录下添加
## db.setting文件
url=jdbc:mysql://25.30.15.85/cube?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
user=root
pass=chengxun
```

### 代码示例

案例说明: 
> 一个简单的周报系统，每周创建一个周报，每个周报有多个计划项，每周完跟踪每个计划项的完成情况。

```java
package com.tievd.cube.weekplan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tievd.cube.codegen.annotations.*;
import com.tievd.cube.codegen.annotations.db.DBField;
import com.tievd.cube.codegen.annotations.db.ForcedSync;
import org.cube.codegen.core.models.ComponentParamKey;
import org.cube.codegen.core.models.ComponentType;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 周报
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2021/12/31
 */
@Data
@ForcedSync
@TableName("week_report")
@SubTables(WeekPlan.class)
@Form(isGroup = true, description = "周报")
@QueryFields({@QueryField(label = "标题", value = "title", component = ComponentType.STRING)})
@Groups({@Group(id = 1, name = "基础信息"), @Group(id = 2, name = "周报信息")})
public class WeekReport implements Serializable {

    @TableId
    private Long id;

    @DBField(allowNullValue = false)
    @FormField(title = "标题", groupId = 1)
    private String title;

    @DBField(type = "longtext")
    @FormField(title = "领导批注", groupId = 1, component = ComponentType.EDITOR)
    private String description;

    @DBField(length = 64)
    @FormField(title = "所属部门", groupId = 1, component = ComponentType.SELECT_DEPART, componentParams = @ComponentParam(key = ComponentParamKey.DICT_CODE, value = "sys_depart,depart_name,id"))
    @Dict(table = "sys_depart", value = "id", text = "depart_name")
    private String departId;

    @FormField(title = "周开始", groupId = 2, component = ComponentType.DATE)
    private Date weekBegin;

    @FormField(title = "周结束", groupId = 2, component = ComponentType.DATE)
    private Date weekEnd;

    @TableLogic
    @FormField(title = "是否删除")
    @DBField(type = "int", length = 1)
    private Integer delFlag;

    @FormField(title = "创建时间")
    private Date createTime;

    @FormField(title = "创建人")
    private String createBy;

    @FormField(title = "更新时间")
    private Date updateTime;

    @FormField(title = "更新人")
    private String updateBy;

    @FormField(title = "部门编码")
    private String sysOrgCode;
}
```

```java
package com.tievd.cube.weekplan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tievd.cube.codegen.annotations.ComponentParam;
import com.tievd.cube.codegen.annotations.ForeignKey;
import com.tievd.cube.codegen.annotations.Form;
import com.tievd.cube.codegen.annotations.FormField;
import com.tievd.cube.codegen.annotations.db.DBField;
import com.tievd.cube.codegen.annotations.db.ForcedSync;
import org.cube.codegen.core.models.ComponentParamKey;
import com.tievd.cube.codegen.models.ComponentType;
import com.tievd.cube.codegen.models.RelationType;
import org.cube.commons.annotations.Dict;
import lombok.Data;

import java.io.Serializable;

/**
 * 周计划
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2021/12/31
 */
@Data
@ForcedSync
@TableName("week_plan")
@Form(description = "计划项")
public class WeekPlan implements Serializable {

    @TableId
    private Long id;

    @DBField(allowNullValue = false)
    @FormField(title = "计划标题")
    private String title;

    @DBField(type = "longtext")
    @FormField(title = "计划描述", groupId = 1, component = ComponentType.EDITOR)
    private String description;

    @FormField(title = "计划类型", component = ComponentType.DICT_SELECT_TAG, componentParams = {@ComponentParam(key = ComponentParamKey.DICT_CODE, value = "week_plan_type")})
    @Dict("week_plan_type")
    private String type;

    @FormField(title = "所属周报", showInForm = false, showInList = false)
    @ForeignKey(value = WeekReport.class, relationType = RelationType.ONE_TO_MANY)
    private Long weekReportId;
}
```
