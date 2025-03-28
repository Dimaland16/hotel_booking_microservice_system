package com.example.roomservice.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTypeTest {

    @Test
    void testRoomTypeConstructorAndGetters() {
        Long id = 1L;
        String name = "Deluxe";
        int maxNumOfPeople = 3;
        int numOfBeds = 2;
        double area = 45.5;
        double pricePerNight = 120.0;
        String description = "A luxurious room with a beautiful view.";
        List<Room> rooms = List.of(
                new Room(1L, "101", null),
                new Room(2L, "102", null)
        );

        RoomType roomType = new RoomType(id, name, maxNumOfPeople, numOfBeds, area, pricePerNight, description, rooms);

        assertThat(roomType.getId()).isEqualTo(id);
        assertThat(roomType.getName()).isEqualTo(name);
        assertThat(roomType.getMaxNumOfPeople()).isEqualTo(maxNumOfPeople);
        assertThat(roomType.getNumOfBeds()).isEqualTo(numOfBeds);
        assertThat(roomType.getArea()).isEqualTo(area);
        assertThat(roomType.getPricePerNight()).isEqualTo(pricePerNight);
        assertThat(roomType.getDescription()).isEqualTo(description);
        assertThat(roomType.getRooms()).isEqualTo(rooms);
    }

    @Test
    void testSetters() {
        RoomType roomType = new RoomType();
        Long id = 2L;
        String name = "Standard";
        int maxNumOfPeople = 2;
        int numOfBeds = 1;
        double area = 30.0;
        double pricePerNight = 80.0;
        String description = "A cozy room for two.";
        List<Room> rooms = List.of(
                new Room(3L, "201", null)
        );

        roomType.setId(id);
        roomType.setName(name);
        roomType.setMaxNumOfPeople(maxNumOfPeople);
        roomType.setNumOfBeds(numOfBeds);
        roomType.setArea(area);
        roomType.setPricePerNight(pricePerNight);
        roomType.setDescription(description);
        roomType.setRooms(rooms);

        assertThat(roomType.getId()).isEqualTo(id);
        assertThat(roomType.getName()).isEqualTo(name);
        assertThat(roomType.getMaxNumOfPeople()).isEqualTo(maxNumOfPeople);
        assertThat(roomType.getNumOfBeds()).isEqualTo(numOfBeds);
        assertThat(roomType.getArea()).isEqualTo(area);
        assertThat(roomType.getPricePerNight()).isEqualTo(pricePerNight);
        assertThat(roomType.getDescription()).isEqualTo(description);
        assertThat(roomType.getRooms()).isEqualTo(rooms);
    }

    @Test
    void testNoArgsConstructor() {
        RoomType roomType = new RoomType();

        assertThat(roomType.getId()).isNull();
        assertThat(roomType.getName()).isNull();
        assertThat(roomType.getMaxNumOfPeople()).isZero();
        assertThat(roomType.getNumOfBeds()).isZero();
        assertThat(roomType.getArea()).isZero();
        assertThat(roomType.getPricePerNight()).isZero();
        assertThat(roomType.getDescription()).isNull();
        assertThat(roomType.getRooms()).isNull();
    }
}
