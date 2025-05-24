package com.ecommerce.customerservice.customer;

import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link CustomerRequest} and {@link Customer} entities,
 * and between {@link Customer} entities and {@link CustomerResponse} objects.
 * <p>
 * This class provides methods to map data from incoming requests to domain entities and vice versa,
 * facilitating the transformation of data between different layers of the application.
 * </p>
 */
@Component
public class CustomerMapper {

    /**
     * Converts a {@link CustomerRequest} object to a {@link Customer} entity.
     * <p>
     * This method takes a {@link CustomerRequest} as input and maps its fields to a new {@link Customer} entity.
     * If the input request is null, the method returns null.
     * </p>
     *
     * @param request the customer request object containing data to be mapped
     * @return a {@link Customer} entity with data mapped from the request, or null if the request is null
     */
    public Customer toCustomer(CustomerRequest request) {
        if (request == null) {
            return null;
        }
        return Customer.builder()
                .id(request.id())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .address(request.address())
                .build();
    }

    /**
     * Converts a {@link Customer} entity to a {@link CustomerResponse} object.
     * <p>
     * This method takes a {@link Customer} entity as input and maps its fields to a new {@link CustomerResponse} object.
     * If the input customer is null, the method returns null.
     * </p>
     *
     * @param customer the customer entity containing data to be mapped
     * @return a {@link CustomerResponse} object with data mapped from the customer entity, or null if the customer is null
     */
    public CustomerResponse fromCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}