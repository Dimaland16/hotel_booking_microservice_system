package com.example.roomservice.integration;

import com.example.roomservice.model.Room;
import com.example.roomservice.model.RoomType;
import com.example.roomservice.repository.RoomRepository;
import com.example.roomservice.repository.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RoomServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @BeforeEach
    void setup() {
        roomRepository.deleteAll();
        roomTypeRepository.deleteAll();

        RoomType roomType = new RoomType(null, "Deluxe", 2, 1, 25.5, 100.0, "Spacious deluxe room", null);
        roomType = roomTypeRepository.save(roomType);

        Room room1 = new Room(null, "101", roomType);
        Room room2 = new Room(null, "102", roomType);
        roomRepository.saveAll(List.of(room1, room2));
    }

    @Test
    void testGetAllRooms() {
        String url = "http://localhost:" + port + "/api/rooms";
        ResponseEntity<Room[]> response = restTemplate.getForEntity(url, Room[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void testGetRoomById() {
        Room room = roomRepository.findAll().get(0);
        String url = "http://localhost:" + port + "/api/rooms/" + room.getId();
        ResponseEntity<Room> response = restTemplate.getForEntity(url, Room.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRoomNumber()).isEqualTo("101");
    }

    @Test
    void testCreateRoom() {
        RoomType roomType = roomTypeRepository.findAll().get(0);
        Room newRoom = new Room(null, "103", roomType);

        String url = "http://localhost:" + port + "/api/rooms/add?id=" + roomType.getId();
        ResponseEntity<Room> response = restTemplate.postForEntity(url, newRoom, Room.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(roomRepository.findAll()).hasSize(3);
    }

    @Test
    void testDeleteRoom() {
        Room room = roomRepository.findAll().get(0);
        String url = "http://localhost:" + port + "/api/rooms/delete/" + room.getId();

        restTemplate.delete(url);

        assertThat(roomRepository.existsById(room.getId())).isFalse();
    }
}
