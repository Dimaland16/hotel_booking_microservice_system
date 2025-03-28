package com.example.bookingservice.service;


import com.example.bookingservice.model.Booking;
import com.example.bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final RabbitMQService roomServiceClient;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RabbitMQService roomServiceClient) {
        this.bookingRepository = bookingRepository;
        this.roomServiceClient = roomServiceClient;
    }

    @Transactional
    public String createRandomBooking(Long roomTypeId, LocalDate checkIn, LocalDate checkOut, String email) {
        log.info("Создание случайного бронирования для типа комнаты {} с {} по {} для email {}", roomTypeId, checkIn, checkOut, email);

        if (!roomServiceClient.checkCustomer(email)) {
            log.warn("Клиент с email {} заблокирован. Бронирование не выполнено.", email);
            return null;
        }

        List<Long> availableRoomIds = getAvailableRoomIds(roomTypeId, checkIn, checkOut);
        if (availableRoomIds.isEmpty()) {
            log.error("Нет доступных комнат для типа {} на указанные даты: {} - {}", roomTypeId, checkIn, checkOut);
            throw new IllegalStateException("No available rooms for the given type and dates.");
        }

        // Выбираем случайный номер
        int randomIndex = ThreadLocalRandom.current().nextInt(availableRoomIds.size());
        Long selectedRoomId = availableRoomIds.get(randomIndex);
        log.info("Выбрана случайная комната с ID: {}", selectedRoomId);

        createBooking(selectedRoomId, checkIn, checkOut);

        String notificationStatus = roomServiceClient.sendNotification(email, checkIn, checkOut);
        log.info("Уведомление отправлено клиенту {}. Сообщение: {}", email, notificationStatus);

        return notificationStatus;
    }

    public List<Long> getAvailableRoomIds(Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        log.info("Получение доступных комнат для типа {} с {} по {}", roomTypeId, checkIn, checkOut);

        List<Long> roomIds = roomServiceClient.getRoomIdsByType(roomTypeId);
        log.info("Найдено {} комнат для типа {}", roomIds.size(), roomTypeId);

        List<Long> availableRoomIds = CheckRoomAvailable(roomIds, checkIn, checkOut);
        log.info("Найдено {} доступных комнат для типа {} с {} по {}", availableRoomIds.size(), roomTypeId, checkIn, checkOut);

        return availableRoomIds;
    }

    public List<Long> CheckRoomAvailable(List<Long> roomIds, LocalDate checkIn, LocalDate checkOut) {
        log.info("Проверка доступности {} комнат с {} по {}", roomIds.size(), checkIn, checkOut);

        List<Long> availableRoomIds = new ArrayList<>();

        for (Long roomId : roomIds) {
            List<Booking> bookings = bookingRepository.findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(
                    roomId, checkIn, checkOut);
            if (bookings.isEmpty()) {
                availableRoomIds.add(roomId);
            }
        }
        log.info("Доступно {} комнат.", availableRoomIds.size());

        return availableRoomIds;
    }

    public Booking createBooking(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        log.info("Создание бронирования для комнаты {} с {} по {}", roomId, checkIn, checkOut);

        Booking booking = new Booking();
        booking.setRoomId(roomId);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Бронирование создано: {}", savedBooking);
        return savedBooking;
    }

    public List<Long> getAvailableRoomTypes(LocalDate checkInDate, LocalDate checkOutDate, int capacity) {
        log.info("Получение доступных типов комнат для {} человек с {} по {}", capacity, checkInDate, checkOutDate);
        List<Long> roomTypeIds = roomServiceClient.getRoomTypesByCapacity(capacity);
        log.info("Найдено {} типов комнат для указанной вместимости: {}", roomTypeIds.size(), roomTypeIds);

        Map<Long, List<Long>> typeToRoomMap = roomServiceClient.getRoomIdsByType(roomTypeIds);
        log.info("Получено {} типов с их комнатами.", typeToRoomMap.size());

        List<Long> availableRoomTypes = CheckTypeAvailable(typeToRoomMap, checkInDate, checkOutDate);
        log.info("Доступно {} типов комнат с {} по {}", availableRoomTypes.size(), checkInDate, checkOutDate);

        return availableRoomTypes;
    }

    public List<Long> CheckTypeAvailable(Map<Long, List<Long>> typeToRoomMap, LocalDate checkInDate, LocalDate checkOutDate) {
        log.info("Проверка доступности типов комнат на даты: {} - {}", checkInDate, checkOutDate);

        List<Long> availableRoomTypes = new ArrayList<>();
        for (Map.Entry<Long, List<Long>> entry : typeToRoomMap.entrySet()) {
            Long typeId = entry.getKey();
            List<Long> roomIds = entry.getValue();

            if (isTypeAvailable(roomIds, checkInDate, checkOutDate)) {
                availableRoomTypes.add(typeId);
            }
        }
        log.info("Доступные типы комнат: {}", availableRoomTypes);

        return availableRoomTypes;
    }

    private boolean isTypeAvailable(List<Long> roomIds, LocalDate checkInDate, LocalDate checkOutDate) {
        log.info("Проверка доступности типа комнаты для {} комнат с {} по {}", roomIds.size(), checkInDate, checkOutDate);

        for (Long roomId : roomIds) {
            List<Booking> bookings = bookingRepository.findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(
                    roomId, checkInDate, checkOutDate);

            if (bookings.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public List<Booking> getAllBookings() {
        log.info("Получение всех бронирований.");
        List<Booking> bookings = bookingRepository.findAll();
        log.info("Найдено {} бронирований.", bookings.size());
        return bookings;
    }

    public Booking getBookingById(Long id) {
        log.info("Получение бронирования с ID: {}", id);
        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            log.warn("Бронирование с ID: {} не найдено.", id);
        } else {
            log.info("Найдено бронирование: {}", booking);
        }

        return booking;
    }

    public Booking createBooking2(Booking booking) {
        log.info("Создание бронирования: {}", booking);
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Бронирование создано: {}", savedBooking);
        return savedBooking;
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        log.info("Обновление бронирования с ID: {}", id);

        Booking existingBooking = getBookingById(id);

        existingBooking.setRoomId(updatedBooking.getRoomId());
        existingBooking.setCheckInDate(updatedBooking.getCheckInDate());
        existingBooking.setCheckOutDate(updatedBooking.getCheckOutDate());

        Booking savedBooking = bookingRepository.save(existingBooking);
        log.info("Бронирование с ID: {} обновлено: {}", id, savedBooking);
        return savedBooking;
    }

    public void deleteBooking(Long id) {
        log.info("Удаление бронирования с ID: {}", id);
        bookingRepository.deleteById(id);
        log.info("Бронирование с ID: {} успешно удалено.", id);
    }
}
