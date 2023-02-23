package org.cube.plugin.filemanager.s3;

import org.cube.plugin.filemanager.IBucketManager;
import org.cube.plugin.filemanager.models.FileServer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

public class S3BucketManager extends S3ClientManager implements IBucketManager<S3Client> {

    public S3BucketManager(FileServer fileServer) {
        super(fileServer);
    }

    @Override
    public void createBucket(String name) {
        CreateBucketConfiguration createBucketConfiguration = CreateBucketConfiguration.builder()
                .locationConstraint("default")
                .build();
        S3Client client = this.defaultClient();
        CreateBucketRequest createBucketRequest = CreateBucketRequest
                .builder()
                .createBucketConfiguration(createBucketConfiguration)
                .bucket(name)
                .build();
        client.createBucket(createBucketRequest);
    }
}
