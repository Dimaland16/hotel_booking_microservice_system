package com.example.bookingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Long> getRoomIdsByType(Long roomTypeId) {
        String queueName = "room.requests";
        Map<String, Long> message = Map.of("roomTypeId", roomTypeId);
        log.info("Отправка сообщения в очередь '{}': {}", queueName, message);

        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);
        log.info("Получен ответ из очереди '{}': {}", queueName, response);

        if (response instanceof List) {
            return ((List<Map<String, Long>>) response).stream()
                    .map(roomMap -> ((Number) roomMap.get("id")).longValue())
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Invalid response from Room Service");
    }

    public List<Long> getRoomTypesByCapacity(int maxNumOfPeople) {
        String queueName = "room.types.requests";
        Map<String, Integer> message = Map.of("maxNumOfPeople", maxNumOfPeople);
        log.info("Отправка сообщения в очередь '{}': {}", queueName, message);

        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);
        log.info("Получен ответ из очереди '{}': {}", queueName, response);


        if (response instanceof List) {
            return ((List<Map<String, Long>>) response).stream()
                    .map(roomTypeMap -> ((Number) roomTypeMap.get("id")).longValue())
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Invalid response from Room Service");
    }

    public Map<Long, List<Long>> getRoomIdsByType(List<Long> roomTypeIds) {
        String queueName = "room.ids.by.type.requests";
        Map<String, List<Long>> message = Map.of("typeIds", roomTypeIds);
        log.info("Отправка сообщения в очередь '{}': {}", queueName, message);


        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);
        log.info("Получен ответ из очереди '{}': {}", queueName, response);

        if (response instanceof Map) {
            Map<?, ?> rawMap = (Map<?, ?>) response;
            return rawMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            entry -> Long.valueOf(entry.getKey().toString()),
                            entry -> ((List<?>) entry.getValue()).stream()
                                    .map(id -> Long.valueOf(id.toString()))
                                    .collect(Collectors.toList())
                    ));
        }
        throw new RuntimeException("Invalid response from Room Service");
    }

    public boolean checkCustomer(String email) {
        String queueName = "customer.check.email";

        // Отправляем email для проверки
        Map<String, String> message = Map.of("email", email);
        log.info("Отправка сообщения в очередь '{}': {}", queueName, message);

        // Отправляем запрос и получаем ответ
        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);
        log.info("Получен ответ из очереди '{}': {}", queueName, response);

        // Если ответ True, значит пользователь активен или был создан
        if (response instanceof Boolean) {
            return (Boolean) response;
        }

        throw new RuntimeException("Invalid response from Customer Service");
    }

    public String sendNotification(String email, LocalDate checkIn, LocalDate checkOut) {
        String queueName = "notification.requests";

        // Формируем сообщение для отправки
        Map<String, Object> message = Map.of(
                "email", email,
                "checkIn", checkIn.toString(),
                "checkOut", checkOut.toString()
        );
        log.info("Отправка сообщения в очередь '{}': {}", queueName, message);

        // Отправляем сообщение в очередь и ожидаем ответа
        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);
        log.info("Получен ответ из очереди '{}': {}", queueName, response);

        // Проверяем, что ответ корректен
        if (response instanceof String) {
            return (String) response;
        }

        throw new RuntimeException("Invalid response from Notification Service");
    }
}
