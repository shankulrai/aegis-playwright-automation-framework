package com.enterprise.framework.integration;

import com.enterprise.framework.aws.AwsConfig;
import com.enterprise.framework.aws.AwsCredentialManager;
import com.enterprise.framework.aws.S3Helper;
import com.enterprise.framework.aws.SNSHelper;
import com.enterprise.framework.aws.SQSHelper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * Convenience facade for AWS service helpers.
 */
public class AwsIntegrationFacade {
    private final S3Helper s3Helper;
    private final SNSHelper snsHelper;
    private final SQSHelper sqsHelper;

    public AwsIntegrationFacade(AwsConfig config) {
        var credentials = AwsCredentialManager.resolveCredentialsProvider(config);
        this.s3Helper = new S3Helper(S3Client.builder().region(AwsCredentialManager.resolveRegion(config)).credentialsProvider(credentials).build(), config.s3Bucket());
        this.snsHelper = new SNSHelper(SnsClient.builder().region(AwsCredentialManager.resolveRegion(config)).credentialsProvider(credentials).build());
        this.sqsHelper = new SQSHelper(SqsClient.builder().region(AwsCredentialManager.resolveRegion(config)).credentialsProvider(credentials).build());
    }

    public S3Helper s3() {
        return s3Helper;
    }

    public SNSHelper sns() {
        return snsHelper;
    }

    public SQSHelper sqs() {
        return sqsHelper;
    }
}
