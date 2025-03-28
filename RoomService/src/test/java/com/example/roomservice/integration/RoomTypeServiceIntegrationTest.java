package com.example.roomservice.integration;

import com.example.roomservice.model.RoomType;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RoomTypeServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @BeforeEach
    void setup() {
        roomTypeRepository.deleteAll();
        RoomType deluxe = new RoomType(null, "Deluxe", 2, 1, 25.5, 100.0, "Spacious deluxe room", null);
        RoomType suite = new RoomType(null, "Suite", 4, 2, 50.0, 200.0, "Luxurious suite", null);
        roomTypeRepository.saveAll(List.of(deluxe, suite));
    }

    @Test
    void testGetAllRoomTypes() {
        String url = "http://localhost:" + port + "/api/room-types";
        ResponseEntity<RoomType[]> response = restTemplate.getForEntity(url, RoomType[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void testGetRoomTypesByMaxNumOfPeople() {
        String url = "http://localhost:" + port + "/api/room-types?numbers=2";
        ResponseEntity<RoomType[]> response = restTemplate.getForEntity(url, RoomType[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getName()).isEqualTo("Deluxe");
        assertThat(response.getBody()[1].getName()).isEqualTo("Suite");
    }

    @Test
    void testCreateRoomType() {
        RoomType newRoomType = new RoomType(null, "Single", 1, 1, 15.0, 50.0, "Compact single room", null);
        String url = "http://localhost:" + port + "/api/room-types/add";
        ResponseEntity<RoomType> response = restTemplate.postForEntity(url, newRoomType, RoomType.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(roomTypeRepository.findAll()).hasSize(3);
    }

    @Test
    void testDeleteRoomType() {
        RoomType roomType = roomTypeRepository.findAll().get(0);
        String url = "http://localhost:" + port + "/api/room-types/delete/" + roomType.getId();

        restTemplate.delete(url);

        assertThat(roomTypeRepository.existsById(roomType.getId())).isFalse();
    }
}