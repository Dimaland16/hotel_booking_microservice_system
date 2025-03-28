package com.example.bookingservice.service;


import com.example.bookingservice.model.Booking;
import com.example.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final RoomServiceClient roomServiceClient;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomServiceClient roomServiceClient) {
        this.bookingRepository = bookingRepository;
        this.roomServiceClient = roomServiceClient;
    }

    public List<Long> getAvailableRoomIds(Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        List<Long> roomIds = roomServiceClient.getRoomIdsByType(roomTypeId);
        List<Long> availableRoomIds = new ArrayList<>();

        for (Long roomId : roomIds) {
            List<Booking> bookings = bookingRepository.findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(
                    roomId, checkIn, checkOut);
            if (bookings.isEmpty()) {
                availableRoomIds.add(roomId);
            }
        }
        return availableRoomIds;
    }

    public Booking createBooking(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        Booking booking = new Booking();
        booking.setRoomId(roomId);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        return bookingRepository.save(booking);
    }

}
