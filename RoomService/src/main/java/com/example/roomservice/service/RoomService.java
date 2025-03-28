package com.example.roomservice.service;

import com.example.roomservice.model.Room;
import com.example.roomservice.model.RoomType;
import com.example.roomservice.repository.RoomRepository;
import com.example.roomservice.repository.RoomTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<Room> getAllRooms() {
        log.info("Получение всех комнат.");
        List<Room> rooms = roomRepository.findAll();
        log.info("Найдено {} комнат.", rooms.size());
        return rooms;
    }

    public Room getRoomById(Long id) {
        log.info("Получение комнаты с ID: {}", id);
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            log.warn("Комната с ID: {} не найдена.", id);
        } else {
            log.info("Найдена комната: {}", room);
        }
        return room;
    }

    public Room createRoom(Room room, Long typeId) {
        log.info("Создание новой комнаты: {} с типом ID: {}", room, typeId);
        RoomType roomType = roomTypeRepository.findById(typeId).orElse(null);
        if (roomType == null) {
            log.warn("Тип комнаты с ID: {} не найден. Создание комнаты отменено.", typeId);
        }
        room.setRoomType(roomType);
        Room createdRoom = roomRepository.save(room);
        log.info("Комната создана: {}", createdRoom);
        return createdRoom;
    }

    public void deleteRoomById(Long id) {
        log.info("Удаление комнаты с ID: {}", id);
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            log.info("Комната с ID: {} успешно удалена.", id);
        } else {
            log.warn("Комната с ID: {} не найдена для удаления.", id);
        }
    }

    public Room updateRoomById(Long id, Room updatedRoom) {
        log.info("Обновление комнаты с ID: {}", id);

        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Комната с ID: {} не найдена.", id);
                    return new EntityNotFoundException("Room not found with id: " + id);
                });

        log.info("Обновляем номер комнаты с {} на {}.", existingRoom.getRoomNumber(), updatedRoom.getRoomNumber());
        existingRoom.setRoomNumber(updatedRoom.getRoomNumber());

        if (updatedRoom.getRoomType() != null) {
            log.info("Обновление типа комнаты для ID: {}", updatedRoom.getRoomType().getId());
            RoomType roomType = roomTypeRepository.findById(updatedRoom.getRoomType().getId())
                    .orElseThrow(() -> {
                        log.error("Тип комнаты с ID: {} не найден.", updatedRoom.getRoomType().getId());
                        return new EntityNotFoundException("RoomType not found with id: " + updatedRoom.getRoomType().getId());
                    });
            existingRoom.setRoomType(roomType);
        }

        Room updated = roomRepository.save(existingRoom);
        log.info("Комната с ID: {} успешно обновлена: {}", id, updated);
        return updated;
    }

    public List<Room> getRoomsByType(Long roomTypeId) {
        log.info("Получение комнат с типом ID: {}", roomTypeId);
        List<Room> rooms = roomRepository.findAllByRoomTypeId(roomTypeId);
        log.info("Найдено {} комнат с типом ID: {}", rooms.size(), roomTypeId);
        return rooms;
    }

    public List<Room> findAllWithRoomType(List<Long> roomTypeList) {
        log.info("Получение всех комнат с указанными типами: {}", roomTypeList);
        List<Room> rooms = roomRepository.findAllWithRoomType(roomTypeList);
        log.info("Найдено {} комнат с указанными типами.", rooms.size());
        return rooms;
    }

}
