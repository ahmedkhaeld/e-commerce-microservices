package com.ecommerce.orderservice.product;

import com.ecommerce.orderservice.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Client component responsible for interacting with the external Product Service.
 *
 * <p>This client is used by {@link com.ecommerce.orderservice.order.OrderService} to:
 * <ul>
 *     <li>Reserve inventory for products being purchased</li>
 *     <li>Validate product availability during the order creation process</li>
 * </ul>
 *
 * <p>During order creation, the Order Service calls {@link #purchaseProducts(List)} to
 * request inventory reservation. If the Product Service returns an error or is unavailable,
 * the order creation process will fail and throw a {@link BusinessException}.
 *
 * <p>This class is a REST-based client that uses {@link RestTemplate} to make HTTP calls.
 * The URL of the Product Service is injected via configuration property {@code application.config.product-url}.
 */
@Service
@RequiredArgsConstructor
public class ProductClient {

    /**
     * Base URL of the Product Service.
     * Injected from application configuration (application.properties or application.yml).
     */
    @Value("${application.config.product-url}")
    private String productUrl;

    /**
     * RestTemplate instance used to make HTTP requests to the Product Service.
     */
    private final RestTemplate restTemplate;

    /**
     * Sends a purchase request to the Product Service in order to reserve inventory for a list of products.
     *
     * <p>This method is typically called during {@link com.ecommerce.orderservice.order.OrderService#createOrder(com.ecommerce.orderservice.order.OrderRequest)}.
     * If the Product Service responds with an error status code, this method will throw a {@link BusinessException},
     * causing the order transaction to roll back.
     *
     * @param requestBody List of {@link PurchaseRequest} objects representing products to be purchased.
     * @return A list of {@link PurchaseResponse} objects returned by the Product Service.
     * @throws BusinessException if the Product Service responds with an error or unexpected response.
     */
    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(requestBody, headers);
        ParameterizedTypeReference<List<PurchaseResponse>> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase",
                POST,
                requestEntity,
                responseType
        );

        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while processing the products purchase: " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }
}
