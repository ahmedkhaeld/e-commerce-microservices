package com.ecommerce.orderservice.payment;


import com.ecommerce.orderservice.customer.CustomerResponse;
import com.ecommerce.orderservice.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}