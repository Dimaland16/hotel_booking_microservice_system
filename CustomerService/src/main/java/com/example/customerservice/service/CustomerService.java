package com.example.customerservice.service;

import com.example.customerservice.model.Customer;
import com.example.customerservice.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        log.info("Получение всех клиентов.");
        List<Customer> customers = customerRepository.findAll();
        log.info("Найдено {} клиентов.", customers.size());
        return customers;
    }

    public Customer getCustomerById(Long id) {
        log.info("Получение клиента с ID: {}", id);
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            log.warn("Клиент с ID: {} не найден.", id);
        } else {
            log.info("Найден клиент: {}", customer);
        }
        return customer;
    }

    public Customer createCustomer(Customer customer) {
        log.info("Создание нового клиента: {}", customer);
        if (customerRepository.existsByEmail(customer.getEmail())) {
            log.warn("Email уже существует: {}", customer.getEmail());
            throw new RuntimeException("Email already exists: " + customer.getEmail());
        }
        Customer createdCustomer = customerRepository.save(customer);
        log.info("Клиент создан: {}", createdCustomer);
        return createdCustomer;
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        log.info("Обновление клиента с ID: {}", id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Клиент с ID: {} не найден.", id);
                    return new EntityNotFoundException("Customer not found with id: " + id);
                });

        log.info("Обновление информации клиента: {}", updatedCustomer);
        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setPassword(passwordEncoder.encode(updatedCustomer.getPassword()));
        existingCustomer.setRoles(updatedCustomer.getRoles());
        existingCustomer.setActive(updatedCustomer.isActive());

        Customer updated = customerRepository.save(existingCustomer);
        log.info("Клиент с ID: {} успешно обновлен: {}", id, updated);
        return updated;
    }

    public void deleteCustomer(Long id) {
        log.info("Удаление клиента с ID: {}", id);
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            log.info("Клиент с ID: {} успешно удален.", id);
        } else {
            log.warn("Клиент с ID: {} не найден для удаления.", id);
        }
    }

    public Optional<Customer> findByEmail(String email) {
        log.info("Поиск клиента по email: {}", email);
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            log.info("Клиент найден: {}", customer.get());
        } else {
            log.warn("Клиент с email: {} не найден.", email);
        }
        return customer;
    }
}
