package com.example.customerservice.service;

import com.example.customerservice.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerRequestHandler {

    private final CustomerService customerService;

    @Autowired
    public CustomerRequestHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RabbitListener(queues = "customer.check.email")
    public boolean checkCustomerEmail(Map<String, String> message) {
        String email = message.get("email");
        log.info("Получено сообщение из очереди 'customer.check.email' для проверки email: {}", email);

        Optional<Customer> customerOptional = customerService.findByEmail(email);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            log.info("Клиент с email: {} найден. Статус активности: {}", email, customer.isActive());
            return customer.isActive();
        }
        log.warn("Клиент с email: {} не найден. Создание нового клиента.", email);

        Customer newCustomer = new Customer();
        newCustomer.setEmail(email);
        newCustomer.setActive(true);
        customerService.createCustomer(newCustomer);

        log.info("Новый клиент создан: {}", newCustomer);
        return true;
    }
}
