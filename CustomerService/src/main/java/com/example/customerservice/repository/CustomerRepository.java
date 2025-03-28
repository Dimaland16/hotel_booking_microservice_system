package com.example.customerservice.repository;

import com.example.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository <Customer,Long> {
    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);
}
