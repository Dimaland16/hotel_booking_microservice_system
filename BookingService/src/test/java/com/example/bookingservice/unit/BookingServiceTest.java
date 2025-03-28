package com.example.bookingservice.unit;

import com.example.bookingservice.model.Booking;
import com.example.bookingservice.repository.BookingRepository;
import com.example.bookingservice.service.BookingService;
import com.example.bookingservice.service.RabbitMQService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RabbitMQService roomServiceClient;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRandomBooking() {
        Long roomTypeId = 1L;
        LocalDate checkIn = LocalDate.of(2023, 12, 1);
        LocalDate checkOut = LocalDate.of(2023, 12, 5);
        String email = "test@example.com";

        // Mocking RabbitMQService client to return expected results
        when(roomServiceClient.checkCustomer(email)).thenReturn(true);
        when(roomServiceClient.getRoomIdsByType(roomTypeId)).thenReturn(Arrays.asList(101L, 102L));
        when(roomServiceClient.sendNotification(eq(email), eq(checkIn), eq(checkOut))).thenReturn("Notification sent");

        // Mocking bookingRepository to simulate saving booking
        Booking savedBooking = new Booking(1L, 101L, checkIn, checkOut);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        String result = bookingService.createRandomBooking(roomTypeId, checkIn, checkOut, email);

        assertNotNull(result);
        assertEquals("Notification sent", result);

        // Verify interactions
        verify(roomServiceClient, times(1)).checkCustomer(email);
        verify(roomServiceClient, times(1)).getRoomIdsByType(roomTypeId);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testGetAvailableRoomIds() {
        Long roomTypeId = 1L;
        LocalDate checkIn = LocalDate.of(2023, 12, 1);
        LocalDate checkOut = LocalDate.of(2023, 12, 5);

        // Mocking roomServiceClient to return room IDs
        when(roomServiceClient.getRoomIdsByType(roomTypeId)).thenReturn(Arrays.asList(101L, 102L));

        // Mocking bookingRepository to simulate room availability check
        when(bookingRepository.findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(101L, checkIn, checkOut))
                .thenReturn(List.of());
        when(bookingRepository.findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(102L, checkIn, checkOut))
                .thenReturn(List.of());

        List<Long> availableRooms = bookingService.getAvailableRoomIds(roomTypeId, checkIn, checkOut);

        assertEquals(2, availableRooms.size());
        assertTrue(availableRooms.contains(101L));
        assertTrue(availableRooms.contains(102L));

        // Verify the repository interaction
        verify(bookingRepository, times(1)).findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(101L, checkIn, checkOut);
        verify(bookingRepository, times(1)).findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(102L, checkIn, checkOut);
    }

    @Test
    void testCreateBooking() {
        Long roomId = 101L;
        LocalDate checkIn = LocalDate.of(2023, 12, 1);
        LocalDate checkOut = LocalDate.of(2023, 12, 5);

        Booking booking = new Booking(null, roomId, checkIn, checkOut);
        Booking savedBooking = new Booking(1L, roomId, checkIn, checkOut);

        // Mocking bookingRepository to simulate saving booking
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        Booking result = bookingService.createBooking(roomId, checkIn, checkOut);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(roomId, result.getRoomId());
        assertEquals(checkIn, result.getCheckInDate());
        assertEquals(checkOut, result.getCheckOutDate());

        // Verify repository interaction
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testGetAllBookings() {
        Booking booking1 = new Booking(1L, 101L, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 5));
        Booking booking2 = new Booking(2L, 102L, LocalDate.of(2023, 12, 6), LocalDate.of(2023, 12, 10));

        // Mocking repository to return a list of bookings
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        List<Booking> bookings = bookingService.getAllBookings();

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertTrue(bookings.contains(booking1));
        assertTrue(bookings.contains(booking2));

        // Verify repository interaction
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testUpdateBooking() {
        Long id = 1L;
        Booking existingBooking = new Booking(id, 101L, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 5));
        Booking updatedBooking = new Booking(id, 102L, LocalDate.of(2023, 12, 6), LocalDate.of(2023, 12, 10));

        // Mocking repository to return existing booking and simulate update
        when(bookingRepository.findById(id)).thenReturn(java.util.Optional.of(existingBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);

        Booking result = bookingService.updateBooking(id, updatedBooking);

        assertNotNull(result);
        assertEquals(updatedBooking.getRoomId(), result.getRoomId());
        assertEquals(updatedBooking.getCheckInDate(), result.getCheckInDate());
        assertEquals(updatedBooking.getCheckOutDate(), result.getCheckOutDate());

        // Verify repository interaction
        verify(bookingRepository, times(1)).findById(id);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testDeleteBooking() {
        Long id = 1L;

        // Mocking repository to simulate booking deletion
        doNothing().when(bookingRepository).deleteById(id);

        bookingService.deleteBooking(id);

        // Verify repository interaction
        verify(bookingRepository, times(1)).deleteById(id);
    }
}
