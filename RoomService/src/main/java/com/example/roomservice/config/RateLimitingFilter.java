package com.example.roomservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        int requests = requestCounts.getOrDefault(clientIp, 0) + 1;

        int MAX_REQUESTS_PER_MINUTE = 10;
        if (requests > MAX_REQUESTS_PER_MINUTE) {
            // Отправляем ошибку, если лимит превышен
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return;
        }

        // Обновляем счетчик запросов для текущего IP
        requestCounts.put(clientIp, requests);

        // Продолжаем выполнение цепочки фильтров
        filterChain.doFilter(request, response);
    }

    // Сбрасываем счетчики запросов
    @Scheduled(fixedRate = 30000)
    public void resetCounts() {
        requestCounts.clear();
    }
}