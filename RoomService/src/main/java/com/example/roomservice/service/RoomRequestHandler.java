package com.example.roomservice.service;

import com.example.roomservice.model.Room;
import com.example.roomservice.model.RoomType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomRequestHandler {

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomRequestHandler(RoomService roomService, RoomTypeService roomTypeService) {
        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
    }

    @RabbitListener(queues = "room.requests")
    public List<Map<String, Long>> handleRoomRequest(Map<String, Long> request) {
        log.info("Получено сообщение из очереди 'room.requests': {}", request);

        Long roomTypeId = ((Number) request.get("roomTypeId")).longValue();

        List<Room> rooms = roomService.getRoomsByType(roomTypeId);
        log.info("Найдено {} комнат для типа ID: {}", rooms.size(), roomTypeId);

        List<Map<String, Long>> response = rooms.stream()
                .map(room -> Map.of("id", room.getId()))
                .toList();

        log.info("Ответ для очереди 'room.requests': {}", response);
        return response;
    }

    @RabbitListener(queues = "room.types.requests", ackMode = "AUTO")
    public List<Map<String, Long>> handleRoomTypesRequest(Map<String, Object> request) {
        log.info("Получено сообщение из очереди 'room.types.requests': {}", request);

        int capacity = ((Number) request.get("maxNumOfPeople")).intValue();
        List<RoomType> roomTypes = roomTypeService.getRoomTypeByMaxNumOfPeople(capacity);
        log.info("Найдено {} типов комнат для вместимости >= {}", roomTypes.size(), capacity);

        List<Map<String, Long>> response = roomTypes.stream()
                .map(roomType -> Map.of("id", roomType.getId()))
                .toList();

        log.info("Ответ для очереди 'room.types.requests': {}", response);
        return response;
    }

    @RabbitListener(queues = "room.ids.by.type.requests")
    public Map<Long, List<Long>> handleRoomIdsByTypeRequest(Map<String, List<Long>> request) {
        log.info("Получено сообщение из очереди 'room.ids.by.type.requests': {}", request);

        List<Long> typeIds = request.get("typeIds");

        List<Room> rooms = roomService.findAllWithRoomType(typeIds);
        log.info("Найдено {} комнат для типов ID: {}", rooms.size(), typeIds);

        Map<Long, List<Long>> response = rooms.stream()
                .collect(Collectors.groupingBy(
                        room -> Long.valueOf(room.getRoomType().getId().toString()), // Убедитесь, что ID типа комнаты Long
                        Collectors.mapping(room -> Long.valueOf(room.getId().toString()), Collectors.toList()) // Преобразование ID комнаты
                ));

        log.info("Ответ для очереди 'room.ids.by.type.requests': {}", response);
        return response;
    }
}
