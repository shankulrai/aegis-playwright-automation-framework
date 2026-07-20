package com.enterprise.framework.aws;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;

import java.util.Map;

/**
 * Resolves AWS credentials with explicit precedence.
 */
public final class AwsCredentialManager {
    private AwsCredentialManager() {
    }

    public static AwsCredentialsProvider resolveCredentialsProvider(AwsConfig config) {
        if (config.profile() != null && !config.profile().isBlank()) {
            return ProfileCredentialsProvider.create(config.profile());
        }
        if (hasEnvCredentials()) {
            return EnvironmentVariableCredentialsProvider.create();
        }
        if (config.roleArn() != null && !config.roleArn().isBlank()) {
            StsClient stsClient = StsClient.builder()
                .region(Region.of(config.region()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
            AssumeRoleRequest.Builder request = AssumeRoleRequest.builder()
                .roleArn(config.roleArn())
                .roleSessionName(config.roleSessionName());
            if (config.externalId() != null && !config.externalId().isBlank()) {
                request.externalId(config.externalId());
            }
            return StsAssumeRoleCredentialsProvider.builder()
                .stsClient(stsClient)
                .refreshRequest(request.build())
                .build();
        }
        return DefaultCredentialsProvider.create();
    }

    public static Region resolveRegion(AwsConfig config) {
        return Region.of(config.region());
    }

    public static S3Client buildS3Client(AwsConfig config) {
        return S3Client.builder()
            .region(resolveRegion(config))
            .credentialsProvider(resolveCredentialsProvider(config))
            .build();
    }

    public static SnsClient buildSnsClient(AwsConfig config) {
        return SnsClient.builder()
            .region(resolveRegion(config))
            .credentialsProvider(resolveCredentialsProvider(config))
            .build();
    }

    public static SqsClient buildSqsClient(AwsConfig config) {
        return SqsClient.builder()
            .region(resolveRegion(config))
            .credentialsProvider(resolveCredentialsProvider(config))
            .build();
    }

    private static boolean hasEnvCredentials() {
        Map<String, String> env = System.getenv();
        return notBlank(env.get("AWS_ACCESS_KEY_ID")) && notBlank(env.get("AWS_SECRET_ACCESS_KEY"));
    }

    private static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}
