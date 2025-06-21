package com.ecommerce.productservice.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {

}