package com.example.bookingservice.system;

import com.example.bookingservice.model.Booking;
import com.example.bookingservice.repository.BookingRepository;
import com.example.bookingservice.service.BookingService;
import com.example.bookingservice.service.RabbitMQService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceSystemTest {

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private BookingService bookingService;

    @Test
    void testEndToEndBookingFlow() {
        int maxNumOfPeople = 3;
        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(2);

        // Step 1: Get room types by capacity
        List<Long> roomTypeIds = rabbitMQService.getRoomTypesByCapacity(maxNumOfPeople);
        assertNotNull(roomTypeIds, "Room types should not be null");
        assertFalse(roomTypeIds.isEmpty(), "Room types should not be empty");

        // Step 2: Get room IDs by room type
        Map<Long, List<Long>> roomIdsByType = rabbitMQService.getRoomIdsByType(roomTypeIds);
        assertNotNull(roomIdsByType, "Room ID map should not be null");
        assertFalse(roomIdsByType.isEmpty(), "Room ID map should not be empty");

        List<Long> availableTypeIds = bookingService.CheckTypeAvailable(roomIdsByType, checkInDate, checkOutDate);
        assertFalse(availableTypeIds.isEmpty(), "No available room types for " + maxNumOfPeople + " people were found" + " on " + checkInDate + " - " + checkOutDate);

        // Step 3: Check customer email
        String email = "user@example.com";
        boolean isCustomerActive = rabbitMQService.checkCustomer(email);
        assertTrue(isCustomerActive, "Customer should be active");

        // Step 4: Select a room type and get available room IDs
        Long selectedRoomTypeId = availableTypeIds.get(0);
        List<Long> roomIds = rabbitMQService.getRoomIdsByType(selectedRoomTypeId);
        assertNotNull(roomIds, "Room IDs should not be null");
        assertFalse(roomIds.isEmpty(), "Room IDs should not be empty");

        List<Long> availableRoomIds = bookingService.CheckRoomAvailable(roomIds, checkInDate, checkOutDate);
        assertFalse(availableRoomIds.isEmpty(), "No available rooms for " + maxNumOfPeople + " people were found" + " on " + checkInDate + " - " + checkOutDate);


        // Step 5: Create a booking
        Long selectedRoomId = availableRoomIds.get(0);

        Booking savedBooking = bookingService.createBooking(selectedRoomId, checkInDate, checkOutDate);

        assertNotNull(savedBooking, "Booking should be successfully saved");
        assertEquals(selectedRoomId, savedBooking.getRoomId(), "Saved booking should match selected room");

        // Step 6: Send notification
        String notificationResponse = rabbitMQService.sendNotification(email, checkInDate, checkOutDate);
        assertNotNull(notificationResponse, "Notification response should not be null");
        assertEquals(email + " Вы успешно забронировали номер на " + checkInDate + " - " + checkOutDate, notificationResponse, "Notification should confirm the booking");
    }
}
