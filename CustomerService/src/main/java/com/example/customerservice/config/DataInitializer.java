package com.example.customerservice.config;

import com.example.customerservice.model.Customer;
import com.example.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Bean
    public CommandLineRunner init(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (customerRepository.count() == 0) {
                Customer admin = new Customer();
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("1"));
                admin.setRoles("ADMIN");
                customerRepository.save(admin);

                Customer user = new Customer();
                user.setEmail("user@gmail.com");
                user.setPassword(passwordEncoder.encode("2"));
                user.setRoles("USER");
                customerRepository.save(user);
            }
        };
    }
}
