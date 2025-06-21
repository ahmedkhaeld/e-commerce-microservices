package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.customer.CustomerResponse;
import com.ecommerce.orderservice.order.PaymentMethod;
import com.ecommerce.orderservice.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payload for order confirmation events published to Kafka.
 *
 * <p>This record is used by {@link OrderProducer} to send order confirmation messages
 * to the Kafka topic {@code order-topic}.
 *
 * <p>The {@link com.ecommerce.orderservice.order.OrderService} constructs this payload
 * after a successful order creation and payment initiation.
 *
 * <p>Other services (such as Shipping, Notifications, or Analytics) consume this event
 * to perform additional business processes related to the completed order.
 *
 * <p>Fields included in this event:
 * <ul>
 *     <li>{@code orderReference} — The unique reference of the order</li>
 *     <li>{@code totalAmount} — The total amount of the order</li>
 *     <li>{@code paymentMethod} — The payment method used for the order</li>
 *     <li>{@code customer} — Basic customer information for the order</li>
 *     <li>{@code products} — List of purchased products (with quantities and details)</li>
 * </ul>
 *
 * <p>This event promotes loose coupling between Order Service and other dependent services.
 *
 * @param orderReference The unique reference for the order.
 * @param totalAmount The total monetary value of the order.
 * @param paymentMethod The payment method used to pay for the order.
 * @param customer Basic details about the customer placing the order.
 * @param products List of purchased products with details such as quantity and product ID.
 */
public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) { }
