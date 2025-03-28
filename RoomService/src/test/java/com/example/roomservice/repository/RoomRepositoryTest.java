package com.example.roomservice.repository;

import com.example.roomservice.model.Room;
import com.example.roomservice.model.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void testFindAllByRoomTypeId() {
        // Создаём RoomType
        RoomType roomType = new RoomType(null, "Deluxe", 2, 1, 30.0, 80.0, "Comfortable room", null);
        RoomType savedRoomType = entityManager.persist(roomType);

        // Создаём комнаты
        Room room1 = new Room(null, "101", savedRoomType);
        Room room2 = new Room(null, "102", savedRoomType);
        entityManager.persist(room1);
        entityManager.persist(room2);

        // Вызываем метод репозитория
        List<Room> rooms = roomRepository.findAllByRoomTypeId(savedRoomType.getId());

        // Проверяем результат
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().allMatch(room -> room.getRoomType().getId().equals(savedRoomType.getId())));
    }

    @Test
    void testFindAllWithRoomType() {
        // Создаём RoomType
        RoomType roomType1 = new RoomType(null, "Deluxe", 2, 1, 30.0, 80.0, "Comfortable room", null);
        RoomType roomType2 = new RoomType(null, "Standard", 4, 2, 50.0, 120.0, "Spacious room", null);
        RoomType savedRoomType1 = entityManager.persist(roomType1);
        RoomType savedRoomType2 = entityManager.persist(roomType2);

        // Создаём комнаты
        Room room1 = new Room(null, "101", savedRoomType1);
        Room room2 = new Room(null, "102", savedRoomType2);
        entityManager.persist(room1);
        entityManager.persist(room2);

        // Вызываем метод репозитория
        List<Long> roomTypeIds = List.of(savedRoomType1.getId(), savedRoomType2.getId());
        List<Room> rooms = roomRepository.findAllWithRoomType(roomTypeIds);

        // Проверяем результат
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().anyMatch(room -> room.getRoomType().getId().equals(savedRoomType1.getId())));
        assertTrue(rooms.stream().anyMatch(room -> room.getRoomType().getId().equals(savedRoomType2.getId())));
    }
}
