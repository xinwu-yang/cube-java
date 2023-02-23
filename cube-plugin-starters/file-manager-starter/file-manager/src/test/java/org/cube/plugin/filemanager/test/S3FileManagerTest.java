package org.cube.plugin.filemanager.test;

import cn.hutool.core.io.file.FileReader;
import org.cube.plugin.filemanager.IBucketManager;
import org.cube.plugin.filemanager.IFileManager;
import org.cube.plugin.filemanager.IPreSignedManager;
import org.cube.plugin.filemanager.IStsManager;
import org.cube.plugin.filemanager.models.FileServer;
import org.cube.plugin.filemanager.models.PreSignedDownloadParam;
import org.cube.plugin.filemanager.models.SignStsCredentialsParam;
import org.cube.plugin.filemanager.models.StsCredentials;
import org.cube.plugin.filemanager.s3.S3BucketManager;
import org.cube.plugin.filemanager.s3.S3FileManager;
import org.cube.plugin.filemanager.s3.S3PreSignedManager;
import org.cube.plugin.filemanager.sts.DefaultStsManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sts.StsClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class S3FileManagerTest {
    private IFileManager<S3Client> fileManager;
    private IPreSignedManager<S3Presigner> preSignedManager;
    private IBucketManager<S3Client> bucketManager;
    private IStsManager<StsClient> stsManager;

    @BeforeEach
    public void before() {
        Map<String, String> params = new HashMap<>();
        params.put("bucket", "capture");
        params.put("region", "zh-sc-cd");
        FileServer minioServer = FileServer.builder().hostname("bucket.cdfuhang.info").port(19800).accessKey("minadmin").secretKey("tievd@minio!@#").isHttps(false).params(params).build();
        fileManager = new S3FileManager(minioServer);
        preSignedManager = new S3PreSignedManager(minioServer);
        bucketManager = new S3BucketManager(minioServer);
        stsManager = new DefaultStsManager(minioServer);
    }

    @AfterEach
    public void after() throws Exception {
        fileManager.close();
        preSignedManager.close();
        bucketManager.close();
        stsManager.close();
    }

    @Test
    public void upload() {
        fileManager.upload("vis_api_group.sql", new File("C:\\Users\\xinwuy\\Documents\\vis_api_group.sql"));
    }

    @Test
    public void uploadBytes() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("bucket", "task-file-buket");
        params.put("region", "zh-sc-cd");
        FileReader fileReader = new FileReader("C:\\Users\\xinwuy\\Documents\\vis_api_group.sql");
        fileManager.upload("1_bytes_vis_api_group.sql", fileReader.readBytes(), params);
    }

    @Test
    public void download() throws FileNotFoundException {
        Map<String, String> params = new HashMap<>();
        params.put("bucket", "org/cube/plugin/filemanager/test");
        fileManager.download("App/HBuilderX.2.8.13.dmg", new FileOutputStream("/Users/xinwuy/Downloads/HBuilderX.2.8.13.test-1.dmg"), params);
    }

    @Test
    public void preSignedUpload() {
        File file = new File("/Users/xinwuy/Downloads/HBuilderX.2.8.13.20200927.dmg");
        String url = preSignedManager.preSignedUpload("App/HBuilderX.2.8.14.dmg", "org/cube/plugin/filemanager/test", file.length(), Duration.ofMinutes(10));
        System.out.println("url = " + url);
        // Http Put上传文件
//        URL uploadUrl = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();
//        connection.setDoOutput(true);
//        connection.setRequestMethod("PUT");
//        OutputStream os = connection.getOutputStream();
//        os.write(FileUtils.readFileToByteArray(file));
//        os.close();
//        int statusCode = connection.getResponseCode();
//        System.out.println("HTTP response code: " + connection.getResponseCode());
//        assert statusCode == 200;
    }

    @Test
    public void preSignedDownload() {
        String url = preSignedManager.preSignedDownload("1.jpg", "big-screen", Duration.ofMinutes(10));
        System.out.println("url = " + url);
    }

    @Test
    public void preSignedDownloadParam() {
        PreSignedDownloadParam param = PreSignedDownloadParam.builder().key("/2021110915/16364419323918796376.webp").bucket("big-screen").duration(Duration.ofMinutes(10)).contentType("image/webp").build();
        String url = preSignedManager.preSignedDownload(param);
        System.out.println("url = " + url);
    }

    @Test
    public void createBucket() {
        bucketManager.createBucket("big-screen");
    }

    @Test
    public void testSts() {
        String roleArn = "arn:aws:s3:::*";
        String roleSession = "any-session";
        String policy = "{\n" + "    \"Version\": \"2012-10-17\",\n" + "    \"Statement\": [\n" + "        {\n" + "            \"Effect\": \"Allow\",\n" + "            \"Action\": [\n" + "                \"s3:PutObject\"\n" + "            ],\n" + "            \"Resource\": [\n" + "                \"arn:aws:s3:::*\"\n" + "            ]\n" + "        }\n" + "    ]\n" + "}";

        SignStsCredentialsParam signStsCredentialsParam = new SignStsCredentialsParam();
        signStsCredentialsParam.setRoleArn(roleArn);
        signStsCredentialsParam.setRoleSession(roleSession);
        signStsCredentialsParam.setPolicy(policy);
        signStsCredentialsParam.setDurationSeconds(7 * 24 * 60 * 60);

        StsCredentials stsCredentials = stsManager.signStsCredentials(signStsCredentialsParam);
        if (stsCredentials != null) {
            System.out.println("AK: " + stsCredentials.getAccessKeyId());
            System.out.println("SK: " + stsCredentials.getSecretAccessKey());
            System.out.println("ST: " + stsCredentials.getSessionToken());
            System.out.println("EX: " + stsCredentials.getExpiration());
        }
    }
}
