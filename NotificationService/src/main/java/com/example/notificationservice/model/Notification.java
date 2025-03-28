package com.example.notificationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient; // Email или номер телефона
    private String type;      // Тип уведомления: EMAIL, SMS, PUSH
    private String topic;   // Тема (для email)
    private String message;   // Текст уведомления

    private LocalDateTime sendAt; // Время отправки
    private boolean isSent;         // Флаг: отправлено ли уведомление

    public Notification() {}

    public Notification(String recipient, String type, String subject, String message, LocalDateTime sendAt, boolean sent) {
        this.recipient = recipient;
        this.type = type;
        this.topic = subject;
        this.message = message;
        this.sendAt = sendAt;
        this.isSent = sent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
