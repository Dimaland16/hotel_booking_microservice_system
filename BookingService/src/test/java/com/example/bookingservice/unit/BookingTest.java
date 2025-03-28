package com.example.bookingservice.unit;

import com.example.bookingservice.model.Booking;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    void testBookingConstructorAndGetters() {
        Long id = 1L;
        Long roomId = 101L;
        LocalDate checkInDate = LocalDate.of(2023, 12, 1);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 5);

        // Creating Booking instance using constructor
        Booking booking = new Booking(id, roomId, checkInDate, checkOutDate);

        // Verifying that the fields are correctly initialized
        assertEquals(id, booking.getId());
        assertEquals(roomId, booking.getRoomId());
        assertEquals(checkInDate, booking.getCheckInDate());
        assertEquals(checkOutDate, booking.getCheckOutDate());
    }

    @Test
    void testBookingSettersAndGetters() {
        // Creating an instance using no-argument constructor
        Booking booking = new Booking();

        Long id = 1L;
        Long roomId = 101L;
        LocalDate checkInDate = LocalDate.of(2023, 12, 1);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 5);

        // Setting values using setters
        booking.setId(id);
        booking.setRoomId(roomId);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);

        // Verifying that the setters correctly set the values
        assertEquals(id, booking.getId());
        assertEquals(roomId, booking.getRoomId());
        assertEquals(checkInDate, booking.getCheckInDate());
        assertEquals(checkOutDate, booking.getCheckOutDate());
    }

    @Test
    void testBookingDefaultConstructor() {
        // Creating an instance using default constructor
        Booking booking = new Booking();

        // Verifying that fields are initialized to default values (null or 0)
        assertNull(booking.getId());
        assertNull(booking.getRoomId());
        assertNull(booking.getCheckInDate());
        assertNull(booking.getCheckOutDate());
    }
}
