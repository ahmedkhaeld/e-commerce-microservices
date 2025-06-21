package com.ecommerce.orderservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * Kafka producer responsible for publishing order confirmation events.
 *
 * <p>This producer is used by {@link com.ecommerce.orderservice.order.OrderService}
 * after successfully creating an order and initiating payment.
 *
 * <p>The event {@link OrderConfirmation} is published to the Kafka topic {@code order-topic}
 * so that other services (such as Notification Service, Shipping Service, Analytics, etc.)
 * can react to the new order.
 *
 * <p>The use of Kafka provides loose coupling between services, enabling scalable
 * asynchronous communication.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    /**
     * KafkaTemplate used to send messages of type {@link OrderConfirmation}.
     */
    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    /**
     * Sends an {@link OrderConfirmation} event to the Kafka topic {@code order-topic}.
     *
     * <p>This method is typically called at the end of
     * {@link com.ecommerce.orderservice.order.OrderService#createOrder(com.ecommerce.orderservice.order.OrderRequest)},
     * after the order has been saved and the payment request has been made.
     *
     * <p>The event allows other services to asynchronously handle post-order processing,
     * such as sending confirmation emails or starting order fulfillment.
     *
     * @param orderConfirmation The order confirmation event payload.
     */
    public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {
        log.info("Sending order confirmation");

        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)
                .setHeader(TOPIC, "order-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
