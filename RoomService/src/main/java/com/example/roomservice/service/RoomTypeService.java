package com.example.roomservice.service;

import com.example.roomservice.model.RoomType;
import com.example.roomservice.repository.RoomTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<RoomType> getAllRoomTypes() {
        log.info("Получение всех типов комнат.");
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        log.info("Найдено {} типов комнат.", roomTypes.size());
        return roomTypes;
    }

    public RoomType getRoomTypeById(Long id) {
        log.info("Получение типа комнаты с ID: {}", id);
        RoomType roomType = roomTypeRepository.findById(id).orElse(null);
        if (roomType == null) {
            log.warn("Тип комнаты с ID: {} не найден.", id);
        } else {
            log.info("Найден тип комнаты: {}", roomType);
        }
        return roomType;
    }

    public RoomType createRoomType(RoomType roomType) {
        log.info("Создание нового типа комнаты: {}", roomType);
        RoomType createdRoomType = roomTypeRepository.save(roomType);
        log.info("Тип комнаты создан: {}", createdRoomType);
        return createdRoomType;
    }

    public void deleteRoomType(Long id) {
        log.info("Удаление типа комнаты с ID: {}", id);
        if (roomTypeRepository.existsById(id)) {
            roomTypeRepository.deleteById(id);
            log.info("Тип комнаты с ID: {} успешно удален.", id);
        } else {
            log.warn("Тип комнаты с ID: {} не найден для удаления.", id);
        }
    }

    public RoomType updateRoomTypeById(Long id, RoomType updatedRoomType) {
        log.info("Обновление типа комнаты с ID: {}", id);

        RoomType existingRoomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Тип комнаты с ID: {} не найден.", id);
                    return new EntityNotFoundException("RoomType not found with id: " + id);
                });

        existingRoomType.setName(updatedRoomType.getName());
        existingRoomType.setMaxNumOfPeople(updatedRoomType.getMaxNumOfPeople());
        existingRoomType.setNumOfBeds(updatedRoomType.getNumOfBeds());
        existingRoomType.setArea(updatedRoomType.getArea());
        existingRoomType.setPricePerNight(updatedRoomType.getPricePerNight());
        existingRoomType.setDescription(updatedRoomType.getDescription());

        RoomType updated = roomTypeRepository.save(existingRoomType);
        log.info("Тип комнаты с ID: {} обновлен: {}", id, updated);
        return updated;
    }

    public List<RoomType> getRoomTypeByMaxNumOfPeople(int maxNumOfPeople) {
        log.info("Поиск типов комнат с количеством человек >= {}", maxNumOfPeople);
        List<RoomType> roomTypes = roomTypeRepository.findByMaxNumOfPeopleGreaterThanEqual(maxNumOfPeople);
        log.info("Найдено {} типов комнат для {} человек.", roomTypes.size(), maxNumOfPeople);
        return roomTypes;
    }
}
