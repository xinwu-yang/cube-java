### sql-export
使用了mybatis-plus的annotation接口动态生成SQL语句，目前只支持MySQL（5.7+）

##### Quick Start:
1. 在Maven的pom.xml配置公司的私服，并引入sql-export依赖
```xml
<repositories>
    <!-- 公司私服-->
    <repository>
        <id>tievd</id>
        <url>http://125.71.201.6:8081/content/groups/public</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.tievd.cube.commons</groupId>
    <artifactId>sql-export</artifactId>
    <version>1.1.4</version>
</dependency>
```
2. SQL语句生成
```java
@Data
@TableName("sys_area")
public class SysArea implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long parentId;
    private String name;
    private String gbCode;
    private String postCode;
    private Integer level;
    private String path;
    private Boolean hasChildren;
    @TableLogic
    private Integer delFlag;
    private Date createTime;
}

SysArea sysArea = new SysArea();
sysArea.setId(1L);
sysArea.setName("成都市");
sysArea.setParentId(0L);
sysArea.setPostCode("6100041");
sysArea.setGbCode("GB2312");
sysArea.setLevel(1);
sysArea.setPath("A01");
sysArea.setHasChildren(false);
sysArea.setDelFlag(0);
sysArea.setCreateTime(new Date());

SQLExporter exporter = new MySQLExporter();
// 生成insert语句
exporter.insert(sysArea);
// 批量生成insert语句
exporter.inserts(sysArea);
// 生成delete语句
exporter.delete(sysArea);
// 批量生成delete语句
exporter.deletes(sysArea);
// 生成忽略外键语句
exporter.foreignKeyChecks(true);
// 生成注释
exporter.comment("Hello World!");
```

3. 注解说明
```java
// 生成sql的表名，默认是实体名的下划线模式，注解中的配置优先
@TableName("sys-area")
// 定义主键，如果主键是自增则生成的sql语句中不会有主键字段
@TableId(type = IdType.AUTO)
// 定义字段的名称，默认是属性的下划线模式，注解中的配置优先，支持忽略字段
@TableField("del_flag")
// 忽略字段，则不会生成这个字段到SQL中
@TableField(exist = false)
```

##### MySQL
1. 时间类型目前只支持java.util.Date