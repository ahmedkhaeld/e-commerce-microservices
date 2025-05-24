package com.ecommerce.customerservice.customer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
@Validated
public class Address {

    private String street;
    private String houseNumber;
    private String zipCode;
}