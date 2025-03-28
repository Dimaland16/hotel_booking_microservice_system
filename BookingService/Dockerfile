# Используем базовый образ JDK
FROM openjdk:22-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar-файл в контейнер
COPY /target/BookingService-0.0.1-SNAPSHOT.jar /app/booking.jar

# Открываем порт, на котором работает микросервис
EXPOSE 8081

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "booking.jar"]
