package com.ecommerce.orderservice.order;

/**
 * Enumeration of the supported payment methods for an order.
 * <br>
 * This allows only valid, predefined payment types to be used,
 * improving type safety and reducing errors.
 */
public enum PaymentMethod {

    /** Payment via PayPal */
    PAYPAL,

    /** Payment via Credit Card (generic) */
    CREDIT_CARD,

    /** Payment specifically via VISA card */
    VISA,

    /** Payment specifically via MasterCard */
    MASTER_CARD,

    /** Payment using Bitcoin cryptocurrency */
    BITCOIN
}
