package com.example.roomservice.controller;

import com.example.roomservice.model.Room;
import com.example.roomservice.model.RoomType;
import com.example.roomservice.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllRooms() throws Exception {
        RoomType roomType = new RoomType();
        Room room1 = new Room(1L, "101", roomType);
        Room room2 = new Room(2L, "102", roomType);
        List<Room> rooms = Arrays.asList(room1, room2);

        Mockito.when(roomService.getAllRooms()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].roomNumber", is("101")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].roomNumber", is("102")));
    }

    @Test
    void testGetRoomById() throws Exception {
        RoomType roomType = new RoomType();
        Room room = new Room(1L, "101", roomType);

        Mockito.when(roomService.getRoomById(1L)).thenReturn(room);

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.roomNumber", is("101")));
    }

    @Test
    void testAddRoom() throws Exception {
        RoomType roomType = new RoomType();
        Room room = new Room(null, "103", roomType);
        Room savedRoom = new Room(3L, "103", roomType);

        Mockito.when(roomService.createRoom(Mockito.any(Room.class), Mockito.eq(1L))).thenReturn(savedRoom);

        mockMvc.perform(post("/api/rooms/add?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.roomNumber", is("103")));
    }

    @Test
    void testUpdateRoomById() throws Exception {
        RoomType roomType = new RoomType();
        Room updatedRoom = new Room(1L, "105", roomType);

        Mockito.when(roomService.updateRoomById(Mockito.eq(1L), Mockito.any(Room.class))).thenReturn(updatedRoom);

        mockMvc.perform(put("/api/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRoom)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.roomNumber", is("105")));
    }

    @Test
    void testDeleteRoom() throws Exception {
        Mockito.doNothing().when(roomService).deleteRoomById(1L);

        mockMvc.perform(delete("/api/rooms/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRoomsByType() throws Exception {
        RoomType roomType = new RoomType();
        Room room1 = new Room(1L, "101", roomType);
        Room room2 = new Room(2L, "102", roomType);
        List<Room> rooms = Arrays.asList(room1, room2);

        Mockito.when(roomService.getRoomsByType(1L)).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms/by-type/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].roomNumber", is("101")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].roomNumber", is("102")));
    }
}
