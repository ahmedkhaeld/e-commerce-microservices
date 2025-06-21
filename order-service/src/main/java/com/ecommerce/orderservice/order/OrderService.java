package com.ecommerce.orderservice.order;

import com.ecommerce.orderservice.customer.CustomerClient;
import com.ecommerce.orderservice.exception.BusinessException;
import com.ecommerce.orderservice.kafka.OrderConfirmation;
import com.ecommerce.orderservice.kafka.OrderProducer;
import com.ecommerce.orderservice.orderline.OrderLineRequest;
import com.ecommerce.orderservice.orderline.OrderLineService;
import com.ecommerce.orderservice.payment.PaymentClient;
import com.ecommerce.orderservice.payment.PaymentRequest;
import com.ecommerce.orderservice.product.ProductClient;
import com.ecommerce.orderservice.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing customer orders.
 *
 * This service handles the full business process of creating an order, including:
 * <ul>
 *     <li>Validating the customer</li>
 *     <li>Purchasing products from the product service</li>
 *     <li>Persisting the order and order lines</li>
 *     <li>Requesting payment from the payment service</li>
 *     <li>Sending an order confirmation via Kafka</li>
 * </ul>
 *
 * Additionally, it provides methods to query orders from the system.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    /** Repository for persisting and querying Order entities. */
    private final OrderRepository repository;

    /** Mapper for converting between Order domain entities and DTOs. */
    private final OrderMapper mapper;

    /** Client for querying customer information from the Customer service. */
    private final CustomerClient customerClient;

    /** Client for requesting payment from the Payment service. */
    private final PaymentClient paymentClient;

    /** Client for purchasing product stock from the Product service. */
    private final ProductClient productClient;

    /** Service for creating and managing OrderLines (order items). */
    private final OrderLineService orderLineService;

    /** Kafka producer for sending order confirmation events. */
    private final OrderProducer orderProducer;

    /**
     * Creates a new customer order.
     * <br>
     * The following steps occur in a single transaction:
     * <ol>
     *     <li>Validate that the customer exists</li>
     *     <li>Reserve product inventory (purchase)</li>
     *     <li>Persist the Order entity</li>
     *     <li>Persist each OrderLine (product/quantity)</li>
     *     <li>Initiate payment via the Payment service</li>
     *     <li>Send an order confirmation event via Kafka</li>
     * </ol>
     *
     * @param request The request object containing order details.
     * @return The generated Order ID.
     * @throws BusinessException if the customer does not exist.
     */
    @Transactional
    public Integer createOrder(OrderRequest request) {
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        var purchasedProducts = productClient.purchaseProducts(request.products());

        var order = this.repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );

        paymentClient.requestOrderPayment(paymentRequest);

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    /**
     * Retrieves all customer orders in the system.
     *
     * @return A list of OrderResponse objects representing each order.
     */
    public List<OrderResponse> findAllOrders() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific customer order by ID.
     *
     * @param id The unique ID of the order to retrieve.
     * @return The corresponding OrderResponse.
     * @throws EntityNotFoundException if no order with the given ID is found.
     */
    public OrderResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}
