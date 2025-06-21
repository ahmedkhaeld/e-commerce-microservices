package com.ecommerce.orderservice.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client interface for interacting with the external Payment Service.
 *
 * <p>This client is used by {@link com.ecommerce.orderservice.order.OrderService} to initiate
 * payment processing for customer orders.
 *
 * <p>During the {@link com.ecommerce.orderservice.order.OrderService#createOrder(com.ecommerce.orderservice.order.OrderRequest)} process,
 * after reserving products and saving the order, this client sends a payment request
 * to the Payment Service.
 *
 * <p>The Payment Service is responsible for handling payment processing, authorization,
 * and recording payment results.
 *
 * <p>The base URL of the Payment Service is injected via the property {@code application.config.payment-url}.
 *
 * <p>This client uses Spring Cloud OpenFeign to simplify REST communication.
 */
@FeignClient(
        name = "payment-service",
        url = "${application.config.payment-url}"
)
public interface PaymentClient {

    /**
     * Sends a payment request to the Payment Service for a given order.
     *
     * <p>If the payment is processed successfully, the Payment Service returns the payment transaction ID
     * (represented here as an Integer).
     *
     * <p>If the Payment Service encounters an error, Feign will throw an exception
     * and the order transaction will be rolled back by {@link org.springframework.transaction.annotation.Transactional}.
     *
     * @param request The {@link PaymentRequest} containing order details and payment information.
     * @return The generated payment transaction ID.
     */
    @PostMapping
    Integer requestOrderPayment(@RequestBody PaymentRequest request);
}
