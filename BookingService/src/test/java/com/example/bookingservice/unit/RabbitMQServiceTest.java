package com.example.bookingservice.unit;

import com.example.bookingservice.service.RabbitMQService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RabbitMQServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQService rabbitMQService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoomIdsByType() {
        // Prepare mock response
        List<Map<String, Long>> mockedResponse = List.of(Map.of("id", 1L), Map.of("id", 2L));
        when(rabbitTemplate.convertSendAndReceive(eq("room.requests"), anyMap())).thenReturn(mockedResponse);

        // Call method
        List<Long> result = rabbitMQService.getRoomIdsByType(1L);

        // Verify result
        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));

        // Verify that RabbitTemplate was called with the correct arguments
        verify(rabbitTemplate, times(1)).convertSendAndReceive(eq("room.requests"), anyMap());
    }

    @Test
    void testGetRoomTypesByCapacity() {
        // Prepare mock response
        List<Map<String, Long>> mockedResponse = List.of(Map.of("id", 1L), Map.of("id", 2L));
        when(rabbitTemplate.convertSendAndReceive(eq("room.types.requests"), anyMap())).thenReturn(mockedResponse);

        // Call method
        List<Long> result = rabbitMQService.getRoomTypesByCapacity(2);

        // Verify result
        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));

        // Verify that RabbitTemplate was called with the correct arguments
        verify(rabbitTemplate, times(1)).convertSendAndReceive(eq("room.types.requests"), anyMap());
    }

    @Test
    void testCheckCustomer() {
        // Prepare mock response
        when(rabbitTemplate.convertSendAndReceive(eq("customer.check.email"), anyMap())).thenReturn(true);

        // Call method
        boolean result = rabbitMQService.checkCustomer("test@example.com");

        // Verify result
        assertTrue(result);

        // Verify that RabbitTemplate was called with the correct arguments
        verify(rabbitTemplate, times(1)).convertSendAndReceive(eq("customer.check.email"), anyMap());
    }

    @Test
    void testSendNotification() {
        // Prepare mock response
        when(rabbitTemplate.convertSendAndReceive(eq("notification.requests"), anyMap())).thenReturn("Notification Sent");

        // Call method
        String result = rabbitMQService.sendNotification("test@example.com", LocalDate.now(), LocalDate.now().plusDays(1));

        // Verify result
        assertEquals("Notification Sent", result);

        // Verify that RabbitTemplate was called with the correct arguments
        verify(rabbitTemplate, times(1)).convertSendAndReceive(eq("notification.requests"), anyMap());
    }

    @Test
    void testGetRoomIdsByType_MultipleRooms() {
        // Prepare mock response
        Map<String, List<Long>> mockedResponse = Map.of(
                "1", List.of(1L, 2L),
                "2", List.of(3L, 4L)
        );
        when(rabbitTemplate.convertSendAndReceive(eq("room.ids.by.type.requests"), anyMap())).thenReturn(mockedResponse);

        // Call method
        Map<Long, List<Long>> result = rabbitMQService.getRoomIdsByType(List.of(1L, 2L));

        // Verify result
        assertEquals(2, result.size());
        assertTrue(result.containsKey(1L));
        assertTrue(result.containsKey(2L));
        assertEquals(2, result.get(1L).size());
        assertEquals(2, result.get(2L).size());

        // Verify that RabbitTemplate was called with the correct arguments
        verify(rabbitTemplate, times(1)).convertSendAndReceive(eq("room.ids.by.type.requests"), anyMap());
    }
}
