## magic-map-spring-boot-starter

### 版本: 1.0.6

### 功能点:

```
✅地图通用点API
✅地图通用区域API
✅地图通用轨迹API
✅坐标系转换工具类
✅新增map-api作为可选方案之一
✅支持SQL存储API配置
✅聚合查询API
⬜地图区域绘制工具
⬜天网视频点位查询API
⬜警力资源查询API
⬜报警事件查询API
⬜人员轨迹查询API
⬜车辆轨迹查询API
```

### Quick Start:

#### 1.引入依赖

```xml
<dependency>
    <groupId>com.tievd.cube.starter</groupId>
    <artifactId>magic-map-spring-boot-starter</artifactId>
    <version>${latest}</version>
</dependency>
```

#### 2.配置

```yaml
# 可选配置
magic-api:
  web: /magic/web
  resource:
    # 配置文件存储位置。当以classpath开头时，为只读模式
    # mac用户请改为可读写的目录
    # 如果不想存到文件中，可以参考配置将接口信息存到数据库、Redis中（或自定义）
    location: D:\\data\\magic-api
    
    # 如果是SQL存储API
    type: database # 配置存储在数据库中
    tableName: magic_map_api # 数据库中的表名
    prefix: /magic-map/1.0.4 # key前缀
    readonly: false # 是否是只读模式
  banner: false
# 必要配置
cube:
  magic-map:
    # 相对路径从classpath读取，绝对路径从系统目录读取
    query-dir: C:\\Users\\xinwuy\\magic-sql.xml
```

#### 3.导入magic-api接口数据包

##### 3.1 通过SQL导入（推荐）

下载并执行[SQL](http://125.71.201.11:9004/cube-commons/magic-map-spring-boot-starter/raw/branch/master/sql/magic_map_api.sql)

配置：**magic-api.resource.prefix**=/magic-map/${对应使用的版本}

##### 3.2 通过文件导入

```shell
git clone http://125.71.201.11:9004/cube-commons/magic-map-api.git -b 对应版本号
```

配置：**magic-api.resource.location** 指向这个下载目录

#### 4.配置SQL

- 新建XML文件格式如下，并在yml中配置 **cube.magic-map.query-dir** XML地址

> 相对路径从classpath读取，绝对路径从系统目录读取

```xml
<?xml version="1.0" encoding="UTF-8"?>
<queries>
    <query name="selectPoint">
        select id, point_name as name, busId as businessCode, lat, lng from sys_map_point ?{username,where username like ${username}}
    </query>
    <query name="selectArea">
        select id, area_name as name, busId as businessCode, c as geoJson from sys_map_area ?{name,where name like ${name}}
    </query>
</queries>
```

- SQL查询需要满足一定的数据结构，不然数据查询出来有问题，具体参考：

```java
public class MapPoint implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 业务唯一编码，如：设备id、序列号之类的
     */
    private String businessCode;

    /**
     * 经度
     */
    private String lat;

    /**
     * 纬度
     */
    private String lng;

    /**
     * 坐标系类型，如：百度、高德、ArcGis、腾讯、PGis
     */
    private String coordinateType;

    /**
     * 所属地区
     */
    private String areaId;

    /**
     * 内置类型
     */
    private Integer type;

    /**
     * 点图标
     */
    private String iconUrl;

    /**
     * 拓展字段
     */
    private JSONObject extra;
}
```

```java
public class MapArea implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 业务唯一编码，如：设备id、序列号之类的
     */
    private String businessCode;

    /**
     * GEOJson数据
     */
    private String geoJson;

    /**
     * 拓展字段
     */
    private JSONObject extra;
}
```

```java
public class MapTrack implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 业务唯一编码，如：设备id、序列号之类的
     */
    private String businessCode;

    /**
     * 坐标系类型，如：百度、高德、ArcGis、腾讯、PGis
     */
    private String coordinateType;

    /**
     * 所属地区
     */
    private String areaId;

    /**
     * 内置类型
     */
    private Integer type;

    /**
     * 轨迹点
     */
    List<MapTrackPoint> trackPoints;

    /**
     * 拓展字段
     */
    private JSONObject extra;
}

public class MapTrackPoint implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 轨迹id
     */
    private String trackId;

    /**
     * 经度
     */
    private String lat;

    /**
     * 纬度
     */
    private String lng;

    /**
     * 上报时间
     */
    private Date time;

    /**
     * 拓展字段
     */
    private JSONObject extra;
}
```

#### 5.调用接口

```
# 资源点相关API
/map/point/search 资源点搜索 | GET | 参数：query 必填 查询名称 | 其他业务参数根据SQL中参数名对应直接透传 | 返回值：List<MapPoint>

# 区域相关API
/map/area/search 区域搜索 | GET | 参数：query 必填 查询名称 | 其他业务参数根据SQL中参数名对应直接透传 | 返回值：List<MapArea>

# 轨迹相关API
/map/track/search 轨迹搜索 | GET | 参数：query 必填 查询名称、 trackPointQuery 非必填 轨迹点子查询名称 | 其他业务参数根据SQL中参数名对应直接透传 | 返回值：List<MapTrack>

# 全局配置相关API
/map/config/query/refresh 刷新SQL配置 | GET
```

> 如果使用map-api，则需要将上述url中的map替换为map-api，两者功能是一致的

#### 6.自定义数据后处理

- 资源点后处理，新增类并实现 **com.tievd.cube.starter.magicmap.handler.IDataHandler<MapPoint/MapArea/MapTrack/MapTrackPoint>** 接口
- 注解 **@Component**，注入到Spring容器
- 注解 **@Order** 可配置后处理顺序
- query 函数非必须实现，默认全局处理，指定query则重写函数填写查询名称

```java
@Component
public class PointHandler implements IDataHandler<MapPoint> {

    @Override
    public void handle(List<MapPoint> list) {
        // 待处理数据 list
    }

    @Override
    // 此函数非必须重写，不重写默认全局处理
    public String query() {
        return "selectPoints";
    }
}
```

#### 7.指定Query参数预处理

- 新增类并实现 **com.tievd.cube.starter.magicmap.handler.IParamsHandler** 接口。
- 同一查询支持多次处理，注解 **@Order** 可配置后处理顺序。
- query 函数非必须实现，默认全局处理，指定query则重写函数填写查询名称
- 如果是map-api需要注入MapApiInterceptor到容器中，并实现上述接口方法**handle(request, response)**

```java
@Component
public class DemoParamHandler implements IParamsHandler {

    @Autowired
    private MagicResourceService magicResourceService;

    // magic-api 重写此方法
    @Override
    public void handle(ApiInfo info, MagicScriptContext context, HttpServletRequest request, HttpServletResponse response) {
        // 得到参数列表
        List<Parameter> parameters = info.getParameters();

        // 得到请求接口路径
        String magicReqPath = magicResourceService.getGroupPath(info.getGroupId()) + info.getPath();

        // 以上信息也可以从原生 HttpServletRequest 获取
        request.getParameter("key");

        // 设置自定义参数映射到SQL参数
        context.set("key", "value");
    }
    
    // map-api 重写此方法
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        // 以上信息也可以从原生 HttpServletRequest 获取
        request.getParameter("key");
        // 设置自定义参数映射到SQL参数
        request.setAttribute("customKey", "customValue");
    }
    
    @Override
    // 此函数非必须重写，不重写默认全局处理
    public String query() {
        // 要处理的查询名称，匹配上才处理
        return "selectPoints";
    }
}
```

#### 8.坐标系转换

提供转换工具类 **com.tievd.cube.starter.magicmap.util.CoordinateUtil**

支持：

- WGS84：谷歌地图 OMS ArcGis Bing
- 火星坐标系：高德地图 腾讯地图
- 百度坐标系：百度地图

```
// 世界标准地理坐标(WGS - 84) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>
wgs84ToGcj02(LatLng location)

// 中国国测局地理坐标（GCJ-02） 转换成 世界标准地理坐标（WGS-84）
gcj02ToWgs84(LatLng location)

// 世界标准地理坐标(WGS - 84) 转换成 百度地理坐标（BD-09)
wgs84ToBd09(LatLng location)

// 中国国测局地理坐标（GCJ-02）<火星坐标> 转换成 百度地理坐标（BD-09)
gcj02ToBd09(LatLng location)

// 百度地理坐标（BD-09) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>
bd09ToGcj02(LatLng location)

// 百度地理坐标（BD-09) 转换成 世界标准地理坐标（WGS-84）
bd09ToWgs84(LatLng location)
```

#### 9.坐标点聚合
本模块提供了三种基本的坐标点聚合算法：基于方格和距离的点聚合，基于网格的点聚合，基于组织机构的聚合。

实现聚合算法的模块版本为：1.0.6

9.1 基于方格和距离的点聚合算法

这是一种当前最为通用的聚合算法，大部分的地图（如百度、Google、高德等）都采用了此算法。
该算法把坐标点扩展成一个方格，如果不同方格间有重叠区域，则这些方格可以聚合成一个点，因为同一个方格可能和多个其它方格有重叠，再通过墨卡托投影算法计算该点到不同的聚合点距离，从而聚合到距离最小的点上。


9.2 基于网格的点聚合

把地图可视化区域划分为一个nXm的网格，同一个网格中的所有点聚合到一个点，再计算同一网格中的点的质心，来确定聚合点的位置。

这种算法的优势是速度更快，它不再需要计算坐标点之间的距离。但是该算法可能导致两个距离很近的点，因为被划分到不同的网格而不能聚合在一起的情况。

9.3 基于组织机构的聚合

这种聚合取决于具体数据结构，点数据中必须有组织机构字段。比如说，点数据中含有省、市、区字段，这样即可以按省、按省市、按省市区等多级聚合。
注意：本聚合算法返回结果中的聚合点位置是没有意义的。

9.4 使用点聚合算法的方法

使用本模块提供的点聚合算法，可以分为三步：

（1）创建聚合算法对象，根据不同的算法，创建不同的聚合算法对象；
（2）向算法对象中添加坐标点；
（3）调用聚合函数，获取聚合结果。

下面以使用基于方格和距离的点聚合算法为例进行代码示例说明：

```
	
	// 记录聚合开始时间
	System.out.println("start dist cluster at:" + System.currentTimeMillis());

	// 创建基于方格和距离的点聚合算法对象，如果使用基于网络的点聚合算法，类名为：
	GridBasedAlgorithm
	IClusterAlgorithm<LatLngClusterItem> algorithm = new NonHierarchicalDistanceBasedAlgorithm<LatLngClusterItem>();
	// 向算法对象中添加坐标点
	algorithm.addItems(list);
	// 调用聚合函数，获取聚合结果
	Set<? extends ICluster> set = algorithm.getClusters(zoom, null, null);
	// 记录聚合结束时间
	System.out.println("end cluster at:" + System.currentTimeMillis());
```

9.5 调用参数说明

| 参数名 | 参数类型 | 是否必选 |说明|
| ------ | ------ | ------ | ------ |
| zoom | Double | 否 |地图级别，适用于基于方格和距离的算法和基于网格的算法|
| swLat | Double | 否 |地图可视区域西南点纬度，适用于基于网格的算法|
| swLng | Double | 否 |地图可视区域西南点经度，适用于基于网格的算法|
| neLat | Double | 否 |地图可视区域东北点纬度，适用于基于网格的算法|
| neLng | Double | 否 |地图可视区域东北点经度，适用于基于网格的算法|
|clusterFields|List<String>|否|聚合的组织机构字段名列表，适用于基于组织机构的聚合|

9.5 在查询结果中聚合

模块已经提供了坐标点搜索接口:/map-api/{type}/search，来实现点位的搜索，我们扩展了该接口，可以在搜索之后再聚合，然后返回结果。

使用方法为：/map-api/cluster/search，参数在搜索的基础上增加以下参数：

| 参数名 | 参数类型 | 是否必选 |说明|
| ------ | ------ | ------ | ------ |
| clusterType | String | 是 |指定算法，grid为基于网络的聚合算法；griddistance为基于方格和距离的算法；organization为基于组织机构的聚合算法；|

其它参数与9.4节说明相同。