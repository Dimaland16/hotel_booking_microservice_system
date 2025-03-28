package com.example.roomservice.service;

import com.example.roomservice.model.Room;
import com.example.roomservice.model.RoomType;
import com.example.roomservice.repository.RoomRepository;
import com.example.roomservice.repository.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void testGetAllRooms() {
        // Arrange
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(2L);

        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));

        // Act
        List<Room> rooms = roomService.getAllRooms();

        // Assert
        assertEquals(2, rooms.size());
        verify(roomRepository).findAll();
    }

    @Test
    void testGetRoomByIdExists() {
        // Arrange
        Long roomId = 1L;
        Room room = new Room();
        room.setId(roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Act
        Room result = roomService.getRoomById(roomId);

        // Assert
        assertNotNull(result);
        assertEquals(roomId, result.getId());
        verify(roomRepository).findById(roomId);
    }

    @Test
    void testGetRoomByIdNotFound() {
        // Arrange
        Long roomId = 1L;

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act
        Room result = roomService.getRoomById(roomId);

        // Assert
        assertNull(result);
        verify(roomRepository).findById(roomId);
    }

    @Test
    void testCreateRoom() {
        // Arrange
        Long typeId = 1L;
        RoomType roomType = new RoomType();
        roomType.setId(typeId);

        Room room = new Room();
        room.setId(1L);

        when(roomTypeRepository.findById(typeId)).thenReturn(Optional.of(roomType));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        Room result = roomService.createRoom(room, typeId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(typeId, result.getRoomType().getId());
        verify(roomTypeRepository).findById(typeId);
        verify(roomRepository).save(room);
    }

    @Test
    void testDeleteRoomByIdExists() {
        // Arrange
        Long roomId = 1L;

        when(roomRepository.existsById(roomId)).thenReturn(true);

        // Act
        roomService.deleteRoomById(roomId);

        // Assert
        verify(roomRepository).existsById(roomId);
        verify(roomRepository).deleteById(roomId);
    }

    @Test
    void testDeleteRoomByIdNotFound() {
        // Arrange
        Long roomId = 1L;

        when(roomRepository.existsById(roomId)).thenReturn(false);

        // Act
        roomService.deleteRoomById(roomId);

        // Assert
        verify(roomRepository).existsById(roomId);
        verify(roomRepository, never()).deleteById(roomId);
    }

    @Test
    void testUpdateRoomById() {
        // Arrange
        Long roomId = 1L;
        Room existingRoom = new Room();
        existingRoom.setId(roomId);
        existingRoom.setRoomNumber("101");

        Room updatedRoom = new Room();
        updatedRoom.setRoomNumber("102");

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(existingRoom);

        // Act
        Room result = roomService.updateRoomById(roomId, updatedRoom);

        // Assert
        assertNotNull(result);
        assertEquals("102", result.getRoomNumber());
        verify(roomRepository).findById(roomId);
        verify(roomRepository).save(existingRoom);
    }

    @Test
    void testGetRoomsByType() {
        // Arrange
        Long typeId = 1L;
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(2L);

        when(roomRepository.findAllByRoomTypeId(typeId)).thenReturn(List.of(room1, room2));

        // Act
        List<Room> rooms = roomService.getRoomsByType(typeId);

        // Assert
        assertEquals(2, rooms.size());
        verify(roomRepository).findAllByRoomTypeId(typeId);
    }

    @Test
    void testFindAllWithRoomType() {
        // Arrange
        List<Long> typeIds = List.of(1L, 2L);
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(2L);

        when(roomRepository.findAllWithRoomType(typeIds)).thenReturn(List.of(room1, room2));

        // Act
        List<Room> rooms = roomService.findAllWithRoomType(typeIds);

        // Assert
        assertEquals(2, rooms.size());
        verify(roomRepository).findAllWithRoomType(typeIds);
    }
}

