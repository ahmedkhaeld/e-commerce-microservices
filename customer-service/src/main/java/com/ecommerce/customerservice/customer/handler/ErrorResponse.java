package com.ecommerce.customerservice.customer.handler;

import java.util.Map;

/**
 * Represents an error response containing validation errors.
 * <p>
 * This record is used to encapsulate error details in a structured format, specifically for validation errors
 * encountered in the application. It provides a map of field names to error messages, allowing clients to
 * understand which fields failed validation and why.
 * </p>
 * <p>
 * The use of a record simplifies the creation of immutable data objects, ensuring that the error details
 * remain consistent once created.
 * </p>
 */
public record ErrorResponse(Map<String, String> errors) {

}