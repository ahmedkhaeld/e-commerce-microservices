package com.ecommerce.orderservice.orderline;

public record OrderLineResponse(
        Integer id,
        double quantity
) { }