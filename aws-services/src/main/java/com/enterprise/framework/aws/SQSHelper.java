package com.enterprise.framework.aws;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * SQS helper for message polling and management.
 */
public class SQSHelper {
    private final SqsClient client;

    public SQSHelper(SqsClient client) {
        this.client = client;
    }

    public List<Message> pollMessages(String queueUrl, int maxMessages, int waitTimeSeconds) {
        return client.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxMessages)
                .waitTimeSeconds(waitTimeSeconds)
                .attributeNamesWithStrings("All")
                .messageAttributeNames("All")
                .build())
            .messages();
    }

    public List<Message> readMessages(String queueUrl) {
        return pollMessages(queueUrl, 10, 1);
    }

    public void deleteMessage(String queueUrl, Message message) {
        client.deleteMessage(DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(message.receiptHandle())
            .build());
    }

    public Map<String, String> retrieveMessageAttributes(Message message) {
        return message.messageAttributes().entrySet().stream()
            .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stringValue()));
    }

    public List<Message> waitAndRetryPolling(String queueUrl, Duration timeout, Duration interval) {
        long deadline = System.nanoTime() + timeout.toNanos();
        while (System.nanoTime() < deadline) {
            List<Message> messages = readMessages(queueUrl);
            if (!messages.isEmpty()) {
                return messages;
            }
            sleep(interval);
        }
        return List.of();
    }

    private static void sleep(Duration interval) {
        try {
            Thread.sleep(interval.toMillis());
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Polling interrupted", interruptedException);
        }
    }
}
