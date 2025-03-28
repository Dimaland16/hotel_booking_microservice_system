package com.example.roomservice.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTest {

    @Test
    void testRoomConstructorAndGetters() {
        Long id = 1L;
        String roomNumber = "101";
        RoomType roomType = new RoomType();

        Room room = new Room(id, roomNumber, roomType);

        assertThat(room.getId()).isEqualTo(id);
        assertThat(room.getRoomNumber()).isEqualTo(roomNumber);
        assertThat(room.getRoomType()).isEqualTo(roomType);
    }

    @Test
    void testSetters() {
        Room room = new Room();
        Long id = 1L;
        String roomNumber = "202";
        RoomType roomType = new RoomType();

        room.setId(id);
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);

        assertThat(room.getId()).isEqualTo(id);
        assertThat(room.getRoomNumber()).isEqualTo(roomNumber);
        assertThat(room.getRoomType()).isEqualTo(roomType);
    }

    @Test
    void testNoArgsConstructor() {
        Room room = new Room();

        assertThat(room.getId()).isNull();
        assertThat(room.getRoomNumber()).isNull();
        assertThat(room.getRoomType()).isNull();
    }
}
