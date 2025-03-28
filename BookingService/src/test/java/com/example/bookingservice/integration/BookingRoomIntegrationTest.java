package com.example.bookingservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingRoomIntegrationTest {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    BookingRoomIntegrationTest(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Test
    void testGetRoomTypesByCapacity() {
        // Подготовка данных
        String queueName = "room.types.requests";
        int capacity = 3;

        // Отправка запроса через RabbitMQ
        Object response = rabbitTemplate.convertSendAndReceive(queueName, Map.of("maxNumOfPeople", capacity));

        // Проверка результата
        assertNotNull(response);
        assertInstanceOf(List.class, response);

        List<Long> roomTypeIds = ((List<Map<String, Long>>) response).stream()
                .map(roomTypeMap -> ((Number) roomTypeMap.get("id")).longValue())
                .toList();

        assertEquals(2, roomTypeIds.size());
        assertTrue(roomTypeIds.contains(1L));
        assertTrue(roomTypeIds.contains(2L));
    }

    // Мокируем RoomService, чтобы он возвращал фиктивный ответ
    @RabbitListener(queues = "room.types.requests")
    public List<Map<String, Long>> mockRoomTypesService(Map<String, Integer> request) {
        // Проверяем параметры запроса
        assertNotNull(request);
        assertEquals(3, request.get("maxNumOfPeople"));

        // Возвращаем фиктивные данные
        return List.of(
                Map.of("id", 1L),
                Map.of("id", 2L)
        );
    }

    @Test
    void testGetRoomIdsByType() {
        // Подготовка данных
        String queueName = "room.requests";
        Long roomTypeId = 101L;

        // Отправка запроса через RabbitMQ
        Map<String, Long> message = Map.of("roomTypeId", roomTypeId);
        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);

        // Проверка результата
        assertNotNull(response);
        assertInstanceOf(List.class, response);

        List<Long> roomIds = ((List<Map<String, Long>>) response).stream()
                .map(roomMap -> ((Number) roomMap.get("id")).longValue())
                .toList();

        assertEquals(3, roomIds.size());
        assertTrue(roomIds.contains(1L));
        assertTrue(roomIds.contains(2L));
        assertTrue(roomIds.contains(3L));
    }

    // Мокируем RoomService, чтобы он возвращал фиктивный ответ
    @RabbitListener(queues = "room.requests")
    public List<Map<String, Long>> mockRoomService(Map<String, Long> request) {
        // Проверяем параметры запроса
        assertNotNull(request);
        assertEquals(101L, request.get("roomTypeId"));

        // Возвращаем фиктивные данные
        return List.of(
                Map.of("id", 1L),
                Map.of("id", 2L),
                Map.of("id", 3L)
        );
    }

    @Test
    void testGetMapRoomIdsByType() {
        // Подготовка данных
        String queueName = "room.ids.by.type.requests";
        List<Long> roomTypeIds = List.of(1L, 2L);

        // Отправка запроса через RabbitMQ
        Map<String, List<Long>> message = Map.of("typeIds", roomTypeIds);
        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);

        // Проверка результата
        assertNotNull(response);
        assertInstanceOf(Map.class, response);

        Map<Long, List<Long>> roomTypeIdsMap = convertResponseToMap(response);

        // Проверка, что размер мапы соответствует ожиданиям
        assertEquals(2, roomTypeIdsMap.size());

        // Проверка, что мапа содержит ключи 1L и 2L
        assertTrue(roomTypeIdsMap.containsKey(1L));
        assertTrue(roomTypeIdsMap.containsKey(2L));

        // Проверка значений по ключам
        assertIterableEquals(List.of(101L, 102L), roomTypeIdsMap.get(1L));
        assertIterableEquals(List.of(103L, 104L), roomTypeIdsMap.get(2L));
    }

    // Преобразование ответа в Map<Long, List<Long>>
    private Map<Long, List<Long>> convertResponseToMap(Object response) {
        if (response instanceof Map) {
            Map<?, ?> rawMap = (Map<?, ?>) response;
            return rawMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            entry -> Long.valueOf(entry.getKey().toString()), // Преобразуем ключ в Long
                            entry -> ((List<?>) entry.getValue()).stream()
                                    .map(id -> Long.valueOf(id.toString())) // Преобразуем элементы списка в Long
                                    .collect(Collectors.toList())
                    ));
        }
        throw new IllegalArgumentException("Invalid response type");
    }

    // Мокируем RoomService, чтобы он возвращал фиктивный ответ
    @RabbitListener(queues = "room.ids.by.type.requests")
    public Map<Long, List<Long>> mockMapRoomService(Map<String, List<Long>> request) {
        // Проверяем параметры запроса
        assertNotNull(request);
        assertTrue(request.containsKey("typeIds"));
        assertEquals(2, request.get("typeIds").size());

        // Возвращаем фиктивные данные
        return Map.of(
                1L, List.of(101L, 102L),
                2L, List.of(103L, 104L)
        );
    }

    @Test
    void testCheckCustomer() {
        // Подготовка данных
        String queueName = "customer.check.email";
        String email = "test@example.com";

        // Отправка запроса через RabbitMQ
        Map<String, String> message = Map.of("email", email);
        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);

        // Проверка результата
        assertNotNull(response);
        assertInstanceOf(Boolean.class, response);

        // Ожидаем, что ответ будет true (пользователь существует или был создан)
        assertTrue((Boolean) response);
    }

    // Мокируем CustomerService, чтобы он возвращал фиктивный ответ
    @RabbitListener(queues = "customer.check.email")
    public Boolean mockCustomerService(Map<String, String> request) {
        // Проверяем параметры запроса
        assertNotNull(request);
        assertEquals("test@example.com", request.get("email"));

        // Возвращаем фиктивные данные: имитация того, что пользователь существует
        return true;  // Можно вернуть true или false в зависимости от теста
    }

    @Test
    void testSendNotification() {
        // Подготовка данных
        String queueName = "notification.requests";
        String email = "test@example.com";
        LocalDate checkIn = LocalDate.of(2024, 12, 10);
        LocalDate checkOut = LocalDate.of(2024, 12, 15);

        // Отправка запроса через RabbitMQ
        Map<String, Object> message = Map.of(
                "email", email,
                "checkIn", checkIn.toString(),
                "checkOut", checkOut.toString()
        );
        Object response = rabbitTemplate.convertSendAndReceive(queueName, message);

        // Проверка результата
        assertNotNull(response);
        assertInstanceOf(String.class, response);

        // Ожидаем, что ответ будет строкой, например "Notification sent"
        assertEquals("Notification sent", response);
    }

    // Мокируем NotificationService, чтобы он возвращал фиктивный ответ
    @RabbitListener(queues = "notification.requests")
    public String mockNotificationService(Map<String, Object> request) {
        // Проверяем параметры запроса
        assertNotNull(request);
         assertEquals("test@example.com", request.get("email"));
        assertEquals("2024-12-10", request.get("checkIn"));
        assertEquals("2024-12-15", request.get("checkOut"));

        // Возвращаем фиктивные данные: имитация успешной отправки уведомления
        return "Notification sent";  // Можно вернуть любое сообщение, соответствующее ответу
    }
}
