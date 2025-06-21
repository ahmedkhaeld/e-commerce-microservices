package com.ecommerce.productservice.product;

import com.ecommerce.productservice.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing product operations in the e-commerce system.
 * <p>
 * This service provides comprehensive product management functionality including
 * creation, retrieval, and purchase operations. It handles business logic for
 * product inventory management and ensures data consistency through transactional
 * operations.
 * </p>
 * <p>
 * The service integrates with the persistence layer through ProductRepository
 * and uses ProductMapper for object transformations between DTOs and entities.
 * </p>
 *
 * @author E-commerce Development Team
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    /**
     * Creates a new product in the system.
     * <p>
     * This method converts the product request DTO to a product entity,
     * persists it to the database, and returns the generated product ID.
     * </p>
     *
     * @param request the product creation request containing product details
     * @return the unique identifier of the newly created product
     * @throws IllegalArgumentException if the request is null or contains invalid data
     */
    public Integer createProduct(ProductRequest request) {
        var product = mapper.toProduct(request);
        return repository.save(product).getId();
    }

    /**
     * Retrieves a product by its unique identifier.
     * <p>
     * This method fetches a product from the database using the provided ID
     * and converts it to a response DTO for client consumption.
     * </p>
     *
     * @param id the unique identifier of the product to retrieve
     * @return the product response DTO containing product details
     * @throws EntityNotFoundException if no product exists with the given ID
     */
    public ProductResponse findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID:: " + id));
    }

    /**
     * Retrieves all products from the system.
     * <p>
     * This method fetches all available products from the database and
     * converts them to response DTOs for client consumption.
     * </p>
     *
     * @return a list of product response DTOs containing all products
     */
    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }

    /**
     * Processes multiple product purchases in a single atomic transaction.
     * <p>
     * This method handles bulk product purchases by validating product availability,
     * checking stock quantities, updating inventory levels, and creating purchase
     * records. The entire operation is wrapped in a transaction to ensure data
     * consistency.
     * </p>
     * <p>
     * The method performs the following operations:
     * <ul>
     *   <li>Validates that all requested products exist in the system</li>
     *   <li>Checks that sufficient stock is available for each product</li>
     *   <li>Updates inventory quantities by reducing available stock</li>
     *   <li>Creates purchase response records for successful transactions</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Transaction Behavior:</strong> If any product is unavailable or
     * has insufficient stock, the entire transaction is rolled back and no
     * changes are persisted to the database.
     * </p>
     *
     * @param request a list of product purchase requests, each containing
     *                product ID and desired quantity
     * @return a list of product purchase responses containing details of
     *         successfully purchased products
     * @throws ProductPurchaseException if one or more products don't exist
     *                                 or if insufficient stock is available
     *                                 for any requested product
     * @throws IllegalArgumentException if the request list is null or empty
     */
    @Transactional(rollbackFor = ProductPurchaseException.class)
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        // Extract product IDs from purchase requests
        var productIds = request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        // Fetch requested products from database ordered by ID
        var storedProducts = repository.findAllByIdInOrderById(productIds);

        // Validate that all requested products exist
        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }

        // Sort requests by product ID to match database order
        var sortedRequest = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        // Process each product purchase
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);

            // Validate sufficient stock availability
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException(
                        "Insufficient stock quantity for product with ID:: " + productRequest.productId()
                );
            }

            // Update inventory quantity
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);

            // Create purchase response record
            purchasedProducts.add(mapper.toproductPurchaseResponse(product, productRequest.quantity()));
        }

        return purchasedProducts;
    }
}