package org.cube.plugin.filemanager.s3;

import org.cube.plugin.filemanager.IFileManager;
import org.cube.plugin.filemanager.models.FileServer;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;

public class S3FileManager extends S3ClientManager implements IFileManager<S3Client> {

    public S3FileManager(FileServer fileServer) {
        super(fileServer);
    }

    @Override
    public void upload(String key, File file, Map<String, String> otherParams) {
        S3Client client = this.defaultClient();
        checkParams(otherParams);
        Path filePath = file.toPath();
        String bucket = otherParams.get("bucket");
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentLength(file.length())
                .build();
        client.putObject(putObjectRequest, filePath);
    }

    @Override
    public void upload(String key, File file) {
        upload(key, file, this.fileServer.getParams());
    }

    @Override
    public void upload(String key, byte[] bytes, Map<String, String> otherParams) {
        S3Client client = this.defaultClient();
        checkParams(otherParams);
        String bucket = otherParams.get("bucket");
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentLength((long) bytes.length)
                .build();
        client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
    }

    @Override
    public void upload(String key, byte[] bytes) {
        upload(key, bytes, this.fileServer.getParams());
    }

    @Override
    public void download(String key, OutputStream outputStream, Map<String, String> otherParams) {
        S3Client client = this.defaultClient();
        checkParams(otherParams);
        String bucket = otherParams.get("bucket");
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        client.getObject(getObjectRequest, ResponseTransformer.toOutputStream(outputStream));
    }

    private void checkParams(Map<String, String> params) {
        String bucket = params.get("bucket");
        if (bucket == null || "".equals(bucket)) {
            throw new IllegalArgumentException("bucket is required");
        }
    }
}
