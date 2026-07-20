package com.enterprise.framework.aws;

import com.enterprise.framework.core.config.ConfigManager;

/**
 * AWS runtime configuration.
 */
public record AwsConfig(String region,
                        String profile,
                        String roleArn,
                        String roleSessionName,
                        String externalId,
                        String s3Bucket,
                        String snsTopicArn,
                        String sqsQueueUrl) {

    public static AwsConfig from(ConfigManager configManager) {
        return new AwsConfig(
            configManager.awsRegion(),
            configManager.awsProfile(),
            configManager.awsRoleArn(),
            configManager.awsRoleSessionName(),
            configManager.awsExternalId(),
            configManager.awsS3Bucket(),
            configManager.awsSnsTopicArn(),
            configManager.awsSqsQueueUrl()
        );
    }
}
