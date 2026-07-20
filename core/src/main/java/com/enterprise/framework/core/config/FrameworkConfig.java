package com.enterprise.framework.core.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.DefaultValue;
import org.aeonbits.owner.Config.Key;
/**
 * Typed framework configuration backed by application property files.
 */
public interface FrameworkConfig extends Config {
    @Key("env")
    @DefaultValue("local")
    String env();

    @Key("baseUrl")
    String baseUrl();

    @Key("apiBaseUrl")
    String apiBaseUrl();

    @Key("browser")
    @DefaultValue("chromium")
    String browser();

    @Key("headless")
    @DefaultValue("true")
    boolean headless();

    @Key("remote")
    @DefaultValue("false")
    boolean remote();

    @Key("remoteEndpoint")
    @DefaultValue("")
    String remoteEndpoint();

    @Key("timeoutMs")
    @DefaultValue("30000")
    long timeoutMs();

    @Key("slowMoMs")
    @DefaultValue("0")
    long slowMoMs();

    @Key("aws.profile")
    @DefaultValue("")
    String awsProfile();

    @Key("aws.region")
    @DefaultValue("us-east-1")
    String awsRegion();

    @Key("aws.roleArn")
    @DefaultValue("")
    String awsRoleArn();

    @Key("aws.roleSessionName")
    @DefaultValue("automation-session")
    String awsRoleSessionName();

    @Key("aws.externalId")
    @DefaultValue("")
    String awsExternalId();

    @Key("aws.s3Bucket")
    @DefaultValue("")
    String awsS3Bucket();

    @Key("aws.snsTopicArn")
    @DefaultValue("")
    String awsSnsTopicArn();

    @Key("aws.sqsQueueUrl")
    @DefaultValue("")
    String awsSqsQueueUrl();
}
