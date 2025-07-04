package com.ecommerce.customerservice.customer;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Setter
@Document
public class Customer {

    @Id
    private String id;

    private String firstname;
    private String lastname;
    private String email;
    private Address address;
}