package org.cube.plugin.filemanager.s3;

import org.cube.plugin.filemanager.IClient;
import org.cube.plugin.filemanager.models.FileServer;
import lombok.SneakyThrows;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * S3 Client 生命周期管理
 */
public class S3ClientManager implements IClient<S3Client> {
    protected final FileServer fileServer;
    private S3Client s3Client;

    public S3ClientManager(FileServer fileServer) {
        this.fileServer = fileServer;
        s3Client = defaultClient();
    }

    @Override
    @SneakyThrows
    public S3Client defaultClient() {
        if (s3Client == null) {
            AwsCredentials credentials = AwsBasicCredentials.create(fileServer.getAccessKey(), fileServer.getSecretKey());
            StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(credentials);
            URI uri = new URI(fileServer.isHttps() ? "https" : "http" + "://" + fileServer.getHostname() + ":" + fileServer.getPort());
            Region region = Region.of(fileServer.getParams().get("region"));
            s3Client = S3Client.builder()
                    .credentialsProvider(staticCredentialsProvider)
                    .endpointOverride(uri)
                    .region(region)
                    .build();
        }
        return s3Client;
    }

    @Override
    public void close() {
        s3Client.close();
        s3Client = null;
    }
}
