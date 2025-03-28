package com.example.notificationservice.service;


import com.example.notificationservice.model.Notification;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications() {
        log.info("Получение всех уведомлений.");
        List<Notification> notifications = notificationRepository.findAll();
        log.info("Найдено {} уведомлений.", notifications.size());
        return notifications;
    }

    public Notification getNotificationById(Long id) {
        log.info("Получение уведомления с ID: {}", id);
        return notificationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Уведомление с ID: {} не найдено.", id);
                    return new RuntimeException("Notification not found with ID: " + id);
                });
    }

    public Notification createNotification(Notification notification) {
        log.info("Создание нового уведомления: {}", notification);
        Notification createdNotification = notificationRepository.save(notification);
        log.info("Уведомление успешно создано: {}", createdNotification);
        return createdNotification;
    }

    public Notification updateNotification(Long id, Notification updatedNotification) {
        log.info("Обновление уведомления с ID: {}", id);
        Notification existingNotification = getNotificationById(id);

        existingNotification.setRecipient(updatedNotification.getRecipient());
        existingNotification.setType(updatedNotification.getType());
        existingNotification.setTopic(updatedNotification.getTopic());
        existingNotification.setMessage(updatedNotification.getMessage());
        existingNotification.setSendAt(updatedNotification.getSendAt());
        existingNotification.setSent(updatedNotification.isSent());

        Notification updated = notificationRepository.save(existingNotification);
        log.info("Уведомление с ID: {} успешно обновлено: {}", id, updated);
        return updated;
    }

    public void deleteNotification(Long id) {
        log.info("Удаление уведомления с ID: {}", id);
        Notification existingNotification = getNotificationById(id);
        notificationRepository.delete(existingNotification);
        log.info("Уведомление с ID: {} успешно удалено.", id);
    }

    public void saveNotification(String recipient, String type, String topic, String message) {
        log.info("Сохранение уведомления. Получатель: {}, Тип: {}, Тема: {}", recipient, type, topic);
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(type);
        notification.setTopic(topic);
        notification.setMessage(message);
        notification.setSendAt(LocalDateTime.now());
        notification.setSent(true);

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Уведомление успешно сохранено: {}", savedNotification);
    }

}
