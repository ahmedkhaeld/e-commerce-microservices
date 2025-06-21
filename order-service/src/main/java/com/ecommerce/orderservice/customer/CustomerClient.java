package com.ecommerce.orderservice.customer;

import com.ecommerce.orderservice.order.OrderService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Feign client interface for interacting with the external Customer Service.
 *
 * <p>This client is used by {@link com.ecommerce.orderservice.order.OrderService} during the
 * {@link com.ecommerce.orderservice.order.OrderService#createOrder(com.ecommerce.orderservice.order.OrderRequest)}
 * process to retrieve and validate customer information.
 *
 * <p>Before an order can be created, the {@link OrderService} must ensure that the customer ID is valid
 * and that the customer exists in the system. If no customer is found, order creation will fail
 * with a {@link com.ecommerce.orderservice.exception.BusinessException}.
 *
 * <p>The base URL of the Customer Service is injected via the property {@code application.config.customer-url}.
 *
 * <p>This client uses Spring Cloud OpenFeign to simplify REST communication with the Customer Service.
 */
@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}"
)
public interface CustomerClient {

    /**
     * Retrieves customer information by customer ID.
     *
     * <p>If the customer exists, this method returns an {@link Optional} containing the {@link CustomerResponse}.
     * If the customer does not exist, it returns {@link Optional#empty()}.
     *
     * <p>This method is used to validate that the customer placing an order exists and is known
     * to the system.
     *
     * @param customerId The unique identifier of the customer.
     * @return An Optional containing {@link CustomerResponse} if the customer exists,
     *         or an empty Optional if not found.
     */
    @GetMapping("/{customer-id}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);
}
