package com.enterprise.framework.aws;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sns.model.Topic;

import java.util.Map;

/**
 * SNS helper for publishing and subscription operations.
 */
public class SNSHelper {
    private final SnsClient client;

    public SNSHelper(SnsClient client) {
        this.client = client;
    }

    public String publish(String topicArn, String subject, String message, Map<String, String> attributes) {
        Map<String, MessageAttributeValue> messageAttributes = attributes.entrySet().stream()
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                entry -> MessageAttributeValue.builder().dataType("String").stringValue(entry.getValue()).build()
            ));
        PublishRequest request = PublishRequest.builder()
            .topicArn(topicArn)
            .subject(subject)
            .message(message)
            .messageAttributes(messageAttributes)
            .build();
        return client.publish(request).messageId();
    }

    public Topic topicDetails(String topicArn) {
        return client.listTopics().topics().stream()
            .filter(topic -> topic.topicArn().equals(topicArn))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topicArn));
    }

    public SubscribeResponse subscribeEndpoint(String topicArn, String protocol, String endpoint) {
        return client.subscribe(SubscribeRequest.builder()
            .topicArn(topicArn)
            .protocol(protocol)
            .endpoint(endpoint)
            .build());
    }
}
