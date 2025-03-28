package com.example.bookingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoomServiceClient {

    private final RestTemplate restTemplate;

    public RoomServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Long> getRoomIdsByType(Long roomTypeId) {
        String url = "http://localhost:8080/api/rooms/by-type/" + roomTypeId;
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return Objects.requireNonNull(response.getBody()).stream()
                .map(roomMap -> ((Number) roomMap.get("id")).longValue())
                .collect(Collectors.toList());
    }
}
