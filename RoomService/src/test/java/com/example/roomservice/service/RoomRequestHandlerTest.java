package com.example.roomservice.service;

import com.example.roomservice.model.Room;
import com.example.roomservice.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomRequestHandlerTest {

    @Mock
    private RoomService roomService;

    @Mock
    private RoomTypeService roomTypeService;

    @InjectMocks
    private RoomRequestHandler roomRequestHandler;

    @Test
    void testHandleRoomRequest() {
        // Arrange
        Long roomTypeId = 1L;
        Map<String, Long> request = Map.of("roomTypeId", roomTypeId);

        Room room1 = new Room();
        room1.setId(1L);

        Room room2 = new Room();
        room2.setId(2L);

        List<Room> rooms = List.of(room1, room2);
        when(roomService.getRoomsByType(roomTypeId)).thenReturn(rooms);

        // Act
        List<Map<String, Long>> response = roomRequestHandler.handleRoomRequest(request);

        // Assert
        assertEquals(2, response.size());
        assertEquals(Map.of("id", 1L), response.get(0));
        assertEquals(Map.of("id", 2L), response.get(1));
        verify(roomService).getRoomsByType(roomTypeId);
    }

    @Test
    void testHandleRoomTypesRequest() {
        // Arrange
        int capacity = 4;
        Map<String, Object> request = Map.of("maxNumOfPeople", capacity);

        RoomType roomType1 = new RoomType();
        roomType1.setId(1L);

        RoomType roomType2 = new RoomType();
        roomType2.setId(2L);

        List<RoomType> roomTypes = List.of(roomType1, roomType2);
        when(roomTypeService.getRoomTypeByMaxNumOfPeople(capacity)).thenReturn(roomTypes);

        // Act
        List<Map<String, Long>> response = roomRequestHandler.handleRoomTypesRequest(request);

        // Assert
        assertEquals(2, response.size());
        assertEquals(Map.of("id", 1L), response.get(0));
        assertEquals(Map.of("id", 2L), response.get(1));
        verify(roomTypeService).getRoomTypeByMaxNumOfPeople(capacity);
    }

    @Test
    void testHandleRoomIdsByTypeRequest() {
        // Arrange
        List<Long> typeIds = List.of(1L, 2L);
        Map<String, List<Long>> request = Map.of("typeIds", typeIds);

        RoomType roomType1 = new RoomType();
        roomType1.setId(1L);

        RoomType roomType2 = new RoomType();
        roomType2.setId(2L);

        Room room1 = new Room();
        room1.setId(1L);
        room1.setRoomType(roomType1);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setRoomType(roomType2);

        Room room3 = new Room();
        room3.setId(3L);
        room3.setRoomType(roomType1);

        List<Room> rooms = List.of(room1, room2, room3);
        when(roomService.findAllWithRoomType(typeIds)).thenReturn(rooms);

        // Act
        Map<Long, List<Long>> response = roomRequestHandler.handleRoomIdsByTypeRequest(request);

        // Assert
        assertEquals(2, response.size());
        assertEquals(List.of(1L, 3L), response.get(1L));
        assertEquals(List.of(2L), response.get(2L));
        verify(roomService).findAllWithRoomType(typeIds);
    }
}


