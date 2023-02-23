package org.cube.plugin.filemanager.sts;

import org.cube.plugin.filemanager.IStsManager;
import org.cube.plugin.filemanager.models.FileServer;
import org.cube.plugin.filemanager.models.SignStsCredentialsParam;
import org.cube.plugin.filemanager.models.StsCredentials;
import lombok.Data;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

import java.net.URI;

/**
 * 支持S3协议的STS健全
 */
@Data
public class DefaultStsManager implements IStsManager<StsClient> {
    private FileServer fileServer;
    private StsClient stsClient;

    public DefaultStsManager(FileServer fileServer) {
        this.fileServer = fileServer;
        defaultClient();
    }

    @Override
    public StsClient defaultClient() {
        AwsCredentials credentials = AwsBasicCredentials.create(fileServer.getAccessKey(), fileServer.getSecretKey());
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(credentials);
        URI uri = URI.create(fileServer.isHttps() ? "https" : "http" + "://" + fileServer.getHostname() + ":" + fileServer.getPort());
        Region region = Region.of(fileServer.getParams().get("region"));
        stsClient = StsClient.builder()
                .credentialsProvider(staticCredentialsProvider)
                .endpointOverride(uri)
                .region(region)
                .build();
        return stsClient;
    }

    @Override
    public StsCredentials signStsCredentials(SignStsCredentialsParam param) {
        StsCredentials stsCredentials = null;
        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn(param.getRoleArn())
                .roleSessionName(param.getRoleSession())
                .policy(param.getPolicy())
                .durationSeconds(param.getDurationSeconds())
                .build();

        AssumeRoleResponse assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest);
        if (assumeRoleResponse != null && assumeRoleResponse.credentials() != null) {
            Credentials credentials = assumeRoleResponse.credentials();
            stsCredentials = new StsCredentials();
            stsCredentials.setAccessKeyId(credentials.accessKeyId());
            stsCredentials.setSecretAccessKey(credentials.secretAccessKey());
            stsCredentials.setSessionToken(credentials.sessionToken());
            stsCredentials.setExpiration(credentials.expiration());
        }
        return stsCredentials;
    }

    @Override
    public void close() {
        stsClient.close();
        stsClient = null;
    }
}
