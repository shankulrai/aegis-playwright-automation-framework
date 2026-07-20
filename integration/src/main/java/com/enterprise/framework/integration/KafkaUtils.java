package com.enterprise.framework.integration;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Kafka helper utilities.
 */
public class KafkaUtils {
    private final Properties properties;

    public KafkaUtils(Properties properties) {
        this.properties = properties;
    }

    public AdminClient adminClient() {
        return AdminClient.create(properties);
    }

    public <K, V> Future<?> send(String topic, K key, V value) {
        try (KafkaProducer<K, V> producer = new KafkaProducer<>(properties)) {
            return producer.send(new ProducerRecord<>(topic, key, value));
        }
    }

    public <K, V> List<ConsumerRecord<K, V>> poll(KafkaConsumer<K, V> consumer, Duration timeout) {
        String topic = consumer.subscription().stream().findFirst()
            .orElseThrow(() -> new IllegalStateException("Consumer must be subscribed before polling"));
        List<ConsumerRecord<K, V>> records = new ArrayList<>();
        consumer.poll(timeout).records(topic).forEach(records::add);
        return records;
    }
}
