package com.example.bookingservice.controller;

import com.example.bookingservice.model.Booking;
import com.example.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/available-rooms")
    public List<Long> getAvailableRooms(
            @RequestParam Long roomTypeId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        return bookingService.getAvailableRoomIds(roomTypeId, checkIn, checkOut);
    }

    @PostMapping
    public Booking createBooking(@RequestParam Long roomId,
                                 @RequestParam LocalDate checkIn,
                                 @RequestParam LocalDate checkOut) {
        return bookingService.createBooking(roomId, checkIn, checkOut);
    }
}
