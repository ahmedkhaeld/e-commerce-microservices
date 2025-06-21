package com.ecommerce.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka topic configuration for the Order Service.
 *
 * <p>This configuration class defines the Kafka topic {@code order-topic},
 * which is used by {@link com.ecommerce.orderservice.kafka.OrderProducer}
 * to publish {@link com.ecommerce.orderservice.kafka.OrderConfirmation} events.
 *
 * <p>The {@link NewTopic} bean ensures that the topic is created automatically
 * by Spring Kafka (if auto-topic creation is enabled in the Kafka broker),
 * or validated at application startup.
 *
 * <p>This promotes consistency in topic naming across services, and avoids
 * runtime errors caused by missing topics.
 *
 * <p>The {@code order-topic} is a central part of the order event flow,
 * enabling other services (e.g., Shipping Service, Notification Service)
 * to consume order-related events.
 */
@Configuration
public class KafkaOrderTopicConfig {

    /**
     * Defines the Kafka topic {@code order-topic}.
     *
     * <p>This topic is used to publish {@link com.ecommerce.orderservice.kafka.OrderConfirmation} events.
     *
     * <p>The topic is created using {@link TopicBuilder}, which allows specifying
     * additional settings (such as number of partitions, replication factor) if needed.
     *
     * @return A {@link NewTopic} bean representing the {@code order-topic}.
     */
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name("order-topic")
                .build();
    }
}
