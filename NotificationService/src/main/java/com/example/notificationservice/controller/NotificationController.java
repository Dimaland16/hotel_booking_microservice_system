package com.example.notificationservice.controller;

import com.example.notificationservice.model.Notification;
import com.example.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        log.info("Получен запрос на получение всех уведомлений.");
        List<Notification> notifications = notificationService.getAllNotifications();
        return notifications;
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable Long id) {
        log.info("Получен запрос на получение уведомления с ID: {}", id);
        return notificationService.getNotificationById(id);
    }

    @PostMapping("/add")
    public Notification addNotification(@RequestBody Notification notification) {
        log.info("Получен запрос на добавление нового уведомления: {}", notification);
        return notificationService.createNotification(notification);
    }

    @PutMapping("/{id}")
    public Notification updateNotification(@PathVariable Long id, @RequestBody Notification updatedNotification) {
        log.info("Получен запрос на обновление уведомления с ID: {}", id);
        return notificationService.updateNotification(id, updatedNotification);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteNotification(@PathVariable Long id) {
        log.info("Получен запрос на удаление уведомления с ID: {}", id);
        notificationService.deleteNotification(id);
    }
}
