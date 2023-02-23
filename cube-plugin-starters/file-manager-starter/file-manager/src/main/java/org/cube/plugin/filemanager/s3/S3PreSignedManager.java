package org.cube.plugin.filemanager.s3;

import org.cube.plugin.filemanager.IPreSignedManager;
import org.cube.plugin.filemanager.models.FileServer;
import org.cube.plugin.filemanager.models.PreSignedDownloadParam;
import lombok.SneakyThrows;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.time.Duration;

public class S3PreSignedManager implements IPreSignedManager<S3Presigner> {
    private final FileServer fileServer;
    private S3Presigner preSigner;

    public S3PreSignedManager(FileServer fileServer) {
        this.fileServer = fileServer;
        preSigner = defaultClient();
    }

    @Override
    @SneakyThrows
    public S3Presigner defaultClient() {
        if (preSigner == null) {
            AwsCredentials credentials = AwsBasicCredentials.create(fileServer.getAccessKey(), fileServer.getSecretKey());
            StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(credentials);
            URI uri = new URI(fileServer.isHttps() ? "https" : "http" + "://" + fileServer.getHostname() + ":" + fileServer.getPort());
            preSigner = S3Presigner.builder().credentialsProvider(staticCredentialsProvider).endpointOverride(uri).region(Region.of(fileServer.getParams().get("region"))).build();
        }
        return preSigner;
    }

    @Override
    public void close() {
        preSigner.close();
        preSigner = null;
    }

    @Override
    @SneakyThrows
    public String preSignedUpload(String key, String bucket, long fileSize, Duration duration) {
        PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucket).key(key).contentLength(fileSize).build();
        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder().signatureDuration(duration).putObjectRequest(objectRequest).build();
        PresignedPutObjectRequest preSignedRequest = preSigner.presignPutObject(preSignRequest);
        return preSignedRequest.url().toString();
    }

    @Override
    public String preSignedDownload(String key, Duration duration) {
        return preSignedDownload(key, fileServer.getParams().get("bucket"), duration);
    }

    @Override
    public String preSignedDownload(String key, String bucket, Duration duration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder().signatureDuration(duration).getObjectRequest(getObjectRequest).build();
        PresignedGetObjectRequest preSignedRequest = preSigner.presignGetObject(getObjectPresignRequest);
        return preSignedRequest.url().toString();
    }

    @Override
    public String preSignedDownload(PreSignedDownloadParam param) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(param.getBucket()).key(param.getKey()).responseContentType(param.getContentType()).build();
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder().signatureDuration(param.getDuration()).getObjectRequest(getObjectRequest).build();
        PresignedGetObjectRequest preSignedRequest = preSigner.presignGetObject(getObjectPresignRequest);
        return preSignedRequest.url().toString();
    }
}
