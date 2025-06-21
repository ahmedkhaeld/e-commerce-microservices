package com.ecommerce.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring configuration class that defines a {@link RestTemplate} bean.
 *
 * <p>The {@link RestTemplate} is used for making synchronous HTTP requests
 * to external services.
 *
 * <p>In the Order Service, it is currently used by {@link com.ecommerce.orderservice.product.ProductClient}
 * to interact with the external Product Service when reserving products during the order process.
 *
 * <p>Defining the {@link RestTemplate} as a bean allows it to be injected and shared across components,
 * following Spring's dependency injection best practices.
 *
 * <p>In future versions, this could be replaced with Spring WebClient (reactive) for better scalability,
 * but for simple use cases, {@link RestTemplate} remains a valid choice.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Defines a {@link RestTemplate} bean for use in the Order Service.
     *
     * @return a new {@link RestTemplate} instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
