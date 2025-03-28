package com.example.bookingservice.unit;


import com.example.bookingservice.model.Booking;
import com.example.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        booking1 = new Booking(null, 1L, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 5));
        booking2 = new Booking(null, 1L, LocalDate.of(2023, 12, 6), LocalDate.of(2023, 12, 10));

        // Save the bookings to the repository (this persists them in an in-memory DB during the test)
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
    }

    @Test
    void testFindByRoomIdAndCheckOutDateAfterAndCheckInDateBefore() {
        // Given: We have two bookings in the repository
        Long roomId = 1L;
        LocalDate startDate = LocalDate.of(2023, 12, 2);
        LocalDate endDate = LocalDate.of(2023, 12, 7);

        // When: Calling the repository method
        List<Booking> result = bookingRepository.findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(roomId, startDate, endDate);

        // Then: Verify that the correct booking(s) are returned
        assertEquals(1, result.size()); // Only booking2 should match
        assertEquals(booking2.getId(), result.get(0).getId());
    }

    @Test
    void testNoBookingsFound() {
        // Given: A roomId that doesn't overlap with any existing bookings
        Long roomId = 1L;
        LocalDate startDate = LocalDate.of(2023, 12, 11);
        LocalDate endDate = LocalDate.of(2023, 12, 15);

        // When: Calling the repository method
        List<Booking> result = bookingRepository.findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(roomId, startDate, endDate);

        // Then: No bookings should be found
        assertEquals(0, result.size());
    }
}

