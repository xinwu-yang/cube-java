### file-manager-starter

##### 版本: 1.2.2

##### 功能点:

- 整合了FTP和S3协议的Java SDK，支持文件的上传和下载，S3协议现在支持Ceph和MinIO
- 支持S3 bucket操作
- 支持预签名上传下载
- 支持STS鉴权

##### Quick Start:

1. 在Maven的pom.xml配置公司的私服，并引入file-manager-spring-boot-starter依赖

```xml
<repositories>
    <!-- 公司私服-->
    <repository>
        <id>tievd</id>
        <url>http://125.71.201.14:8999/repository/maven-public/</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.tievd.cube.starter</groupId>
    <artifactId>file-manager-spring-boot-starter</artifactId>
    <version>1.2.2</version>
</dependency>

<!--为了不引入大量依赖，FTP和S3的依赖都需要自己引入，如果只用到了其中之一，就只用引用一个就行了-->
<!--FTP-->
<dependency>
    <groupId>commons-net</groupId>
    <artifactId>commons-net</artifactId>
    <version>3.8.0</version>
</dependency>
<!--S3 和 STS 的基础依赖 -->
<dependency>
    <groupId>commons-logging</groupId>
    <artifactId>commons-logging</artifactId>
    <version>1.2</version>
</dependency>
<!-- S3 -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.17.273</version>
</dependency>
<!--STS-->
<!--要包含S3依赖-->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>sts</artifactId>
    <version>2.17.273</version>
</dependency>
```

2. 配置

```yaml
cube:
  file-manager:
    protocol: ftp/s3
    hostname: 25.30.10.224
    port: 21
    access-key: test/AK
    secret-key: test/SK
    #s3需要配置
    https: false
    #配置默认桶和地区
    params:
      bucket: test
      region: cn-sc-cd
```

3. 注入依赖

```
// s3协议还支持预签名上传和下载
@Autowired
private IFileManager fileManager;
@Autowired
private IPreSignedManager preSignedManager;
// bucket创建API
@Autowired
private IBucketManager bucketManager;
// STS鉴权
@Autowired
private IStsManager stsManager;
```

```
// 代码中调用API
// 上传
// @param path        文件路径
// @param fileName    文件名称
// @param file        文件
// @param otherParams 其他参数
fileManager.upload("App/HBuilderX.2.8.13.dmg",new File("/Users/xinwuy/Downloads/HBuilderX.2.8.13.20200927.dmg"),otherParams);
// 上传到默认桶
fileManager.upload("App/HBuilderX.2.8.13.dmg",new File("/Users/xinwuy/Downloads/HBuilderX.2.8.13.20200927.dmg"));
// @param otherParams 其他参数
fileManager.upload("App/HBuilderX.2.8.13.dmg",FileUtils.readFileToByteArray(new File("/Users/xinwuy/Downloads/HBuilderX.2.8.13.20200927.dmg")),otherParams);
// 上传到默认桶
fileManager.upload("App/HBuilderX.2.8.13.dmg",FileUtils.readFileToByteArray(new File("/Users/xinwuy/Downloads/HBuilderX.2.8.13.20200927.dmg")));

// 下载
// @param path         文件路径
// @param fileName     文件名称
// @param outputStream 输出流
// @param otherParams  其他参数
fileManager.download("App/HBuilderX.2.8.13.dmg",new FileOutputStream("/Users/xinwuy/Downloads/HBuilderX.2.8.13.test-1.dmg"),params);

// 生成授权上传URL，使用Http Put上传文件
// @param path     文件路径
// @param fileName 文件名称
// @param bucket   存储桶
// @param fileSize 文件大小
// @param duration 签名过期时间
String url = preSignedManager.preSignedUpload("App/HBuilderX.2.8.14.dmg","test",file.length(),Duration.ofMinutes(10));

// Http Put上传文件
URL uploadUrl = new URL(url);
HttpURLConnection connection = (HttpURLConnection)uploadUrl.openConnection();
connection.setDoOutput(true);
connection.setRequestMethod("PUT");
OutputStream os = connection.getOutputStream();
os.write(FileUtils.readFileToByteArray(file));
os.close();
int statusCode = connection.getResponseCode();
System.out.println("HTTP response code: " + connection.getResponseCode());

// 生成S3文件的下载链接（默认桶）
// @param path     文件路径
// @param fileName 文件名称
// @param bucket   存储桶
// @param duration 签名过期时间
String url = preSignedManager.preSignedDownload("App/HBuilderX.2.8.14.dmg", Duration.ofMinutes(10));

// 生成S3文件的下载链接
// @param path     文件路径
// @param fileName 文件名称
// @param duration 签名过期时间
String url = preSignedManager.preSignedDownload("App/HBuilderX.2.8.14.dmg", "test", Duration.ofMinutes(10));

// 生成S3文件的下载链接
// @param PreSignedDownloadParam 下载参数
PreSignedDownloadParam param = PreSignedDownloadParam.builder()
        .key("/2021110915/16364419323918796376.webp")
        .bucket("big-screen")
        .duration(Duration.ofMinutes(10))
        .contentType("video/mpeg4")
        .build();
String url = preSignedManager.preSignedDownload(param);

// @param name bucketName
bucketManager.createBucket("test1");

// STS 授权
SignStsCredentialsParam signStsCredentialsParam = new SignStsCredentialsParam();
signStsCredentialsParam.setRoleArn(roleArn);
signStsCredentialsParam.setRoleSession(roleSession);
signStsCredentialsParam.setPolicy(policy);

StsCredentials stsCredentials = stsManager.signStsCredentials(signStsCredentialsParam);
```