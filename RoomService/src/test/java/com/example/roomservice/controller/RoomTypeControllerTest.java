package com.example.roomservice.controller;

import com.example.roomservice.model.RoomType;
import com.example.roomservice.service.RoomTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(RoomTypeController.class)
@ExtendWith(SpringExtension.class)
class RoomTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomTypeService roomTypeService;

    @Test
    void testGetAllRoomTypes() throws Exception {
        List<RoomType> roomTypes = List.of(
                new RoomType(1L, "Standard", 2, 1, 20.5, 50.0, "Standard room", null),
                new RoomType(2L, "Deluxe", 4, 2, 40.0, 100.0, "Deluxe room", null)
        );

        Mockito.when(roomTypeService.getAllRoomTypes()).thenReturn(roomTypes);

        mockMvc.perform(get("/api/room-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Standard")))
                .andExpect(jsonPath("$[1].name", is("Deluxe")));
    }

    @Test
    void testGetRoomTypeById() throws Exception {
        RoomType roomType = new RoomType(1L, "Standard", 2, 1, 20.5, 50.0, "Standard room", null);

        Mockito.when(roomTypeService.getRoomTypeById(1L)).thenReturn(roomType);

        mockMvc.perform(get("/api/room-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Standard")))
                .andExpect(jsonPath("$.maxNumOfPeople", is(2)));
    }

    @Test
    void testAddRoomType() throws Exception {
        RoomType roomType = new RoomType(null, "Deluxe", 4, 2, 40.0, 100.0, "Deluxe room", null);
        RoomType createdRoomType = new RoomType(2L, "Deluxe", 4, 2, 40.0, 100.0, "Deluxe room", null);

        Mockito.when(roomTypeService.createRoomType(Mockito.any(RoomType.class))).thenReturn(createdRoomType);

        mockMvc.perform(post("/api/room-types/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Deluxe",
                                    "maxNumOfPeople": 4,
                                    "numOfBeds": 2,
                                    "area": 40.0,
                                    "pricePerNight": 100.0,
                                    "description": "Deluxe room"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Deluxe")));
    }

    @Test
    void testUpdateRoomTypeById() throws Exception {
        RoomType updatedRoomType = new RoomType(1L, "Updated Standard", 3, 1, 22.0, 55.0, "Updated room", null);

        Mockito.when(roomTypeService.updateRoomTypeById(Mockito.eq(1L), Mockito.any(RoomType.class)))
                .thenReturn(updatedRoomType);

        mockMvc.perform(put("/api/room-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated Standard",
                                    "maxNumOfPeople": 3,
                                    "numOfBeds": 1,
                                    "area": 22.0,
                                    "pricePerNight": 55.0,
                                    "description": "Updated room"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Standard")))
                .andExpect(jsonPath("$.maxNumOfPeople", is(3)));
    }

    @Test
    void testDeleteRoomTypeById() throws Exception {
        Mockito.doNothing().when(roomTypeService).deleteRoomType(1L);

        mockMvc.perform(delete("/api/room-types/delete/1"))
                .andExpect(status().isOk());

        Mockito.verify(roomTypeService, Mockito.times(1)).deleteRoomType(1L);
    }

    @Test
    void testGetRoomTypesByMaxNumOfPeople() throws Exception {
        List<RoomType> roomTypes = List.of(
                new RoomType(1L, "Standard", 2, 1, 20.5, 50.0, "Standard room", null)
        );

        Mockito.when(roomTypeService.getRoomTypeByMaxNumOfPeople(2)).thenReturn(roomTypes);

        mockMvc.perform(get("/api/room-types/searchByNum")
                        .param("numbers", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is("Standard")));
    }
}

