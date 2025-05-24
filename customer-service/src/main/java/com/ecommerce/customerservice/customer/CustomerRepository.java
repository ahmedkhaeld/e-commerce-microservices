package com.ecommerce.customerservice.customer;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for Customer entities.
 * <p>
 * This interface extends the MongoRepository interface provided by Spring Data MongoDB.
 * It provides CRUD operations for Customer entities stored in a MongoDB database.
 * </p>
 * 
 * @see org.springframework.data.mongodb.repository.MongoRepository
 * @see com.ecommerce.customerservice.customer.Customer
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {

}