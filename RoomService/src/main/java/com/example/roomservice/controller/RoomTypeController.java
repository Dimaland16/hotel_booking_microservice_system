package com.example.roomservice.controller;

import com.example.roomservice.model.RoomType;
import com.example.roomservice.service.RoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @Operation(summary = "Получить все типы комнаты")
    @GetMapping
    public List<RoomType> getAllRoomTypes() {
        log.info("Получен запрос на получение всех типов комнат.");
        return roomTypeService.getAllRoomTypes();
    }

    @GetMapping("/{id}")
    public RoomType getRoomTypeById(@PathVariable Long id) {
        log.info("Получен запрос на получение типа комнаты с ID: {}", id);
        return roomTypeService.getRoomTypeById(id);
    }

    @PostMapping("/add")
    public RoomType addRoomType(@Valid @RequestBody RoomType roomType) {
        log.info("Получен запрос на добавление нового типа комнаты: {}", roomType);
        return roomTypeService.createRoomType(roomType);
    }

    @PutMapping("/{id}")
    public RoomType updateRoomTypeById(@PathVariable Long id, @Valid @RequestBody RoomType updatedRoomType) {
        log.info("Получен запрос на обновление типа комнаты с ID: {}", id);
        return roomTypeService.updateRoomTypeById(id, updatedRoomType);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRoomTypeById(@PathVariable Long id) {
        log.info("Получен запрос на удаление типа комнаты с ID: {}", id);
        roomTypeService.deleteRoomType(id);
    }

    @GetMapping("/searchByNum")
    public List<RoomType> getRoomTypes(@RequestParam("numbers") int maxNumOfPeople) {
        log.info("Получен запрос на поиск типов комнат для {} человек.", maxNumOfPeople);
        return roomTypeService.getRoomTypeByMaxNumOfPeople(maxNumOfPeople);
    }
}
