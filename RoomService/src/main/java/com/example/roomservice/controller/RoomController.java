package com.example.roomservice.controller;

import com.example.roomservice.model.Room;
import com.example.roomservice.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Получить все комнаты")
    @GetMapping
    public List<Room> getAllRooms() {
        log.info("Получен запрос на получение всех комнат.");
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        log.info("Получен запрос на получение комнаты с ID: {}", id);
        return roomService.getRoomById(id);
    }

    @PostMapping("/add")
    public Room addRoom(@Valid @RequestBody Room room, @RequestParam("id") Long id) {
        log.info("Получен запрос на добавление комнаты: {} с RoomTypeID: {}", room, id);
        return roomService.createRoom(room, id);
    }

    @PutMapping("/{id}")
    public Room updateRoomById(@PathVariable Long id, @Valid @RequestBody Room updatedRoom) {
        log.info("Получен запрос на обновление комнаты с ID: {}", id);
        return roomService.updateRoomById(id, updatedRoom);
    }

    @DeleteMapping("delete/{id}")
    public void deleteRoom(@PathVariable Long id) {
        log.info("Получен запрос на удаление комнаты с ID: {}", id);
        roomService.deleteRoomById(id);
    }

    @GetMapping("/by-type/{roomTypeId}")
    public List<Room> getRoomsByType(@PathVariable Long roomTypeId) {
        log.info("Получен запрос на получение комнат по типу: {}", roomTypeId);
        return roomService.getRoomsByType(roomTypeId);
    }
}
