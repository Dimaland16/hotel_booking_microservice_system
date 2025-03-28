package com.example.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class NotificationRequestHandler {

    private final NotificationService notificationService;

    @Autowired
    public NotificationRequestHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "notification.requests")
    public String handleNotificationRequest(Map<String, Object> message) {
        log.info("Получен запрос на уведомление из очереди 'notification.requests': {}", message);
        String email = (String) message.get("email");
        String checkIn = (String) message.get("checkIn");
        String checkOut = (String) message.get("checkOut");


        if (email == null || checkIn == null || checkOut == null) {
            log.warn("Неполные данные в запросе: email={}, checkIn={}, checkOut={}", email, checkIn, checkOut);
            throw new IllegalArgumentException("Некорректные данные в запросе: требуется email, checkIn и checkOut.");
        }

        String notificationMessage = "Вы успешно забронировали номер на " + checkIn + " - " + checkOut;

        log.info("Создание уведомления для email: {}, checkIn: {}, checkOut: {}", email, checkIn, checkOut);

        notificationService.saveNotification(
                email,
                "text",
                "Booking Confirmation",
                notificationMessage
        );
        //return email + " Вы успешно забронировали номер на " + checkIn + " - " + checkOut;
        String response = String.format("%s Вы успешно забронировали номер на %s - %s", email, checkIn, checkOut);
        log.info("Уведомление успешно обработано. Ответ для очереди 'notification.requests': {}", response);
        return response;
    }
}
