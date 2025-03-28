package com.example.bookingservice.unit;

import com.example.bookingservice.controller.BookingController;
import com.example.bookingservice.model.Booking;
import com.example.bookingservice.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookingController.class)
@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAvailableRoomsTypes() throws Exception {
        List<Long> roomTypes = List.of(1L, 2L);
        LocalDate checkIn = LocalDate.of(2023, 12, 1);
        LocalDate checkOut = LocalDate.of(2023, 12, 5);

        Mockito.when(bookingService.getAvailableRoomTypes(checkIn, checkOut, 2)).thenReturn(roomTypes);

        mockMvc.perform(get("/api/bookings/rooms-types")
                        .param("maxNumOfPeople", "2")
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0]", is(1)))
                .andExpect(jsonPath("$[1]", is(2)));
    }

    @Test
    void testGetAvailableRooms() throws Exception {
        Long roomTypeId = 1L;
        LocalDate checkIn = LocalDate.of(2023, 12, 1);
        LocalDate checkOut = LocalDate.of(2023, 12, 5);
        String email = "test@example.com";
        String expectedMessage = "Booking created successfully";

        Mockito.when(bookingService.createRandomBooking(roomTypeId, checkIn, checkOut, email)).thenReturn(expectedMessage);

        mockMvc.perform(get("/api/bookings/book")
                        .param("roomTypeId", roomTypeId.toString())
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString())
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    void testGetAllBookings() throws Exception {
        Booking booking1 = new Booking(1L, 1L, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 5));
        Booking booking2 = new Booking(2L, 2L, LocalDate.of(2023, 12, 6), LocalDate.of(2023, 12, 10));

        List<Booking> bookings = List.of(booking1, booking2);
        Mockito.when(bookingService.getAllBookings()).thenReturn(bookings);

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void testGetBookingById() throws Exception {
        Long id = 1L;
        Booking booking = new Booking(id, 1L, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 5));

        Mockito.when(bookingService.getBookingById(id)).thenReturn(booking);

        mockMvc.perform(get("/api/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.checkInDate", is("2023-12-01")))
                .andExpect(jsonPath("$.checkOutDate", is("2023-12-05")));
    }

    @Test
    void testAddBooking() throws Exception {
        Booking booking = new Booking(null, 1L, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 5));
        Booking savedBooking = new Booking(1L, 1L, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 5));

        Mockito.when(bookingService.createBooking2(Mockito.any(Booking.class))).thenReturn(savedBooking);

        mockMvc.perform(post("/api/bookings/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.checkInDate", is("2023-12-01")))
                .andExpect(jsonPath("$.checkOutDate", is("2023-12-05")));
    }

    @Test
    void testUpdateBooking() throws Exception {
        Long id = 1L;
        Booking updatedBooking = new Booking(id, 1L, LocalDate.of(2023, 12, 6), LocalDate.of(2023, 12, 10));

        Mockito.when(bookingService.updateBooking(Mockito.eq(id), Mockito.any(Booking.class))).thenReturn(updatedBooking);

        mockMvc.perform(put("/api/bookings/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.checkInDate", is("2023-12-06")))
                .andExpect(jsonPath("$.checkOutDate", is("2023-12-10")));
    }

    @Test
    void testDeleteBooking() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(bookingService).deleteBooking(id);

        mockMvc.perform(delete("/api/bookings/delete/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(bookingService, Mockito.times(1)).deleteBooking(id);
    }
}