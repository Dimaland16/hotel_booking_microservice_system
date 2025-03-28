/*
package com.example.customerservice.controller;

import com.example.customerservice.model.Customer;
import com.example.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestHeader("Authorization") String authHeader) {
        // Разбираем заголовок Basic Auth (например, "Basic dXNlcm5hbWU6cGFzc3dvcmQ=")
        String[] credentials = decodeBasicAuth(authHeader);

        // Проверяем пользователя
        Customer user = customerService.authenticate(credentials[0], credentials[1]);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Возвращаем роль или просто OK
        return ResponseEntity.ok(user.getRoles());
    }

    private String[] decodeBasicAuth(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        String base64Credentials = authHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        return credentials.split(":", 2); // [username, password]
    }
}
*/
