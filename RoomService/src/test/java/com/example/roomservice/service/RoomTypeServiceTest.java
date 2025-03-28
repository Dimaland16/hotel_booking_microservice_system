package com.example.roomservice.service;


import com.example.roomservice.model.RoomType;
import com.example.roomservice.repository.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomTypeServiceTest {

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @InjectMocks
    private RoomTypeService roomTypeService;

    @Test
    void testGetAllRoomTypes() {
        // Arrange
        RoomType type1 = new RoomType();
        type1.setId(1L);
        RoomType type2 = new RoomType();
        type2.setId(2L);

        when(roomTypeRepository.findAll()).thenReturn(List.of(type1, type2));

        // Act
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypes();

        // Assert
        assertEquals(2, roomTypes.size());
        verify(roomTypeRepository).findAll();
    }

    @Test
    void testGetRoomTypeByIdExists() {
        // Arrange
        Long typeId = 1L;
        RoomType roomType = new RoomType();
        roomType.setId(typeId);

        when(roomTypeRepository.findById(typeId)).thenReturn(Optional.of(roomType));

        // Act
        RoomType result = roomTypeService.getRoomTypeById(typeId);

        // Assert
        assertNotNull(result);
        assertEquals(typeId, result.getId());
        verify(roomTypeRepository).findById(typeId);
    }

    @Test
    void testGetRoomTypeByIdNotFound() {
        // Arrange
        Long typeId = 1L;

        when(roomTypeRepository.findById(typeId)).thenReturn(Optional.empty());

        // Act
        RoomType result = roomTypeService.getRoomTypeById(typeId);

        // Assert
        assertNull(result);
        verify(roomTypeRepository).findById(typeId);
    }

    @Test
    void testCreateRoomType() {
        // Arrange
        RoomType roomType = new RoomType();
        roomType.setName("Deluxe");
        roomType.setId(1L);

        when(roomTypeRepository.save(roomType)).thenReturn(roomType);

        // Act
        RoomType result = roomTypeService.createRoomType(roomType);

        // Assert
        assertNotNull(result);
        assertEquals("Deluxe", result.getName());
        verify(roomTypeRepository).save(roomType);
    }

    @Test
    void testDeleteRoomTypeExists() {
        // Arrange
        Long typeId = 1L;

        when(roomTypeRepository.existsById(typeId)).thenReturn(true);

        // Act
        roomTypeService.deleteRoomType(typeId);

        // Assert
        verify(roomTypeRepository).existsById(typeId);
        verify(roomTypeRepository).deleteById(typeId);
    }

    @Test
    void testDeleteRoomTypeNotFound() {
        // Arrange
        Long typeId = 1L;

        when(roomTypeRepository.existsById(typeId)).thenReturn(false);

        // Act
        roomTypeService.deleteRoomType(typeId);

        // Assert
        verify(roomTypeRepository).existsById(typeId);
        verify(roomTypeRepository, never()).deleteById(typeId);
    }

    @Test
    void testUpdateRoomTypeById() {
        // Arrange
        Long typeId = 1L;
        RoomType existingType = new RoomType();
        existingType.setId(typeId);
        existingType.setName("Standard");

        RoomType updatedType = new RoomType();
        updatedType.setName("Premium");
        updatedType.setMaxNumOfPeople(4);

        when(roomTypeRepository.findById(typeId)).thenReturn(Optional.of(existingType));
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(existingType);

        // Act
        RoomType result = roomTypeService.updateRoomTypeById(typeId, updatedType);

        // Assert
        assertNotNull(result);
        assertEquals("Premium", result.getName());
        assertEquals(4, result.getMaxNumOfPeople());
        verify(roomTypeRepository).findById(typeId);
        verify(roomTypeRepository).save(existingType);
    }

    @Test
    void testGetRoomTypeByMaxNumOfPeople() {
        // Arrange
        int maxNumOfPeople = 2;
        RoomType type1 = new RoomType();
        type1.setId(1L);
        RoomType type2 = new RoomType();
        type2.setId(2L);

        when(roomTypeRepository.findByMaxNumOfPeopleGreaterThanEqual(maxNumOfPeople))
                .thenReturn(List.of(type1, type2));

        // Act
        List<RoomType> roomTypes = roomTypeService.getRoomTypeByMaxNumOfPeople(maxNumOfPeople);

        // Assert
        assertEquals(2, roomTypes.size());
        verify(roomTypeRepository).findByMaxNumOfPeopleGreaterThanEqual(maxNumOfPeople);
    }
}
