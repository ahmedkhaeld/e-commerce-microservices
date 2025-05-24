package com.ecommerce.customerservice.customer;

import com.ecommerce.customerservice.customer.exception.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing customer operations.
 * <p>
 * This class provides methods to create, update, find, and delete customer entities.
 * It acts as a bridge between the controller layer and the repository layer, handling business logic
 * and data transformation using the {@link CustomerMapper}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    /**
     * Creates a new customer entity from a {@link CustomerRequest}.
     * <p>
     * This method maps the incoming request to a {@link Customer} entity using the {@link CustomerMapper},
     * saves it to the repository, and returns the generated customer ID.
     * </p>
     *
     * @param request the customer request containing data for the new customer
     * @return the ID of the newly created customer
     */
    public String createCustomer(CustomerRequest request) {
        var customer = this.repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    /**
     * Updates an existing customer entity with data from a {@link CustomerRequest}.
     * <p>
     * This method finds the customer by ID, merges the new data into the existing entity,
     * and saves the updated entity back to the repository. Throws a {@link CustomerNotFoundException}
     * if the customer does not exist.
     * </p>
     *
     * @param request the customer request containing updated data
     * @throws CustomerNotFoundException if no customer is found with the provided ID
     */
    public void updateCustomer(CustomerRequest request) {
        var customer = this.repository.findById(request.id()).orElseThrow(() -> new CustomerNotFoundException(String.format("Cannot update customer:: No customer found with the provided ID: %s", request.id())));
        mergeCustomer(customer, request);
        this.repository.save(customer);
    }

    /**
     * Merges data from a {@link CustomerRequest} into an existing {@link Customer} entity.
     * <p>
     * This private method updates the fields of the customer entity with non-blank values from the request.
     * </p>
     *
     * @param customer the existing customer entity to be updated
     * @param request the customer request containing new data
     */
    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    /**
     * Retrieves all customer entities and maps them to {@link CustomerResponse} objects.
     * <p>
     * This method fetches all customers from the repository, transforms them using the {@link CustomerMapper},
     * and returns a list of customer responses.
     * </p>
     *
     * @return a list of all customers as {@link CustomerResponse} objects
     */
    public List<CustomerResponse> findAllCustomers() {
        return this.repository.findAll().stream().map(this.mapper::fromCustomer).collect(Collectors.toList());
    }

    /**
     * Finds a customer by ID and maps it to a {@link CustomerResponse} object.
     * <p>
     * This method retrieves a customer from the repository by ID, transforms it using the {@link CustomerMapper},
     * and returns the customer response. Throws a {@link CustomerNotFoundException} if the customer does not exist.
     * </p>
     *
     * @param id the ID of the customer to find
     * @return the customer as a {@link CustomerResponse} object
     * @throws CustomerNotFoundException if no customer is found with the provided ID
     */
    public CustomerResponse findById(String id) {
        return this.repository.findById(id).map(mapper::fromCustomer).orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with the provided ID: %s", id)));
    }

    /**
     * Checks if a customer exists by ID.
     * <p>
     * This method queries the repository to determine if a customer with the given ID exists.
     * </p>
     *
     * @param id the ID of the customer to check
     * @return true if the customer exists, false otherwise
     */
    public boolean existsById(String id) {
        return this.repository.findById(id).isPresent();
    }

    /**
     * Deletes a customer by ID.
     * <p>
     * This method removes the customer entity with the specified ID from the repository.
     * </p>
     *
     * @param id the ID of the customer to delete
     */
    public void deleteCustomer(String id) {
        this.repository.deleteById(id);
    }
}