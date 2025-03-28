package com.example.bookingservice.controller;

import com.example.bookingservice.model.Booking;
import com.example.bookingservice.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/rooms-types")
    public List<Long> getAvailableRoomsTypes(
            @RequestParam int maxNumOfPeople,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        log.info("Получен запрос на получение доступных типов комнат для {} человек с {} по {}", maxNumOfPeople, checkIn, checkOut);
        return bookingService.getAvailableRoomTypes(checkIn, checkOut,maxNumOfPeople);
    }

    @GetMapping("/book")
    public String getAvailableRooms(
            @RequestParam Long roomTypeId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String email) {
        log.info("Получен запрос на создание случайного бронирования для типа комнаты {} с {} по {} для email {}", roomTypeId, checkIn, checkOut, email);
        return bookingService.createRandomBooking(roomTypeId, checkIn, checkOut, email);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        log.info("Получен запрос на получение всех бронирований.");
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        log.info("Получен запрос на получение бронирования с ID: {}", id);
        return bookingService.getBookingById(id);
    }

    @PostMapping("/add")
    public Booking addBooking(@RequestBody Booking booking) {
        log.info("Получен запрос на добавление бронирования: {}", booking);
        return bookingService.createBooking2(booking);
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        log.info("Получен запрос на обновление бронирования с ID: {}. Новые данные: {}", id, updatedBooking);
        return bookingService.updateBooking(id, updatedBooking);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBooking(@PathVariable Long id) {
        log.info("Получен запрос на удаление бронирования с ID: {}", id);
        bookingService.deleteBooking(id);
    }

    @PostMapping
    public Booking createBooking(@RequestParam Long roomId,
                                 @RequestParam LocalDate checkIn,
                                 @RequestParam LocalDate checkOut) {
        log.info("Получен запрос на создание бронирования для комнаты {} с {} по {}", roomId, checkIn, checkOut);
        return bookingService.createBooking(roomId, checkIn, checkOut);
    }
}
