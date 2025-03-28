# Используем базовый образ JDK
FROM openjdk:22-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar-файл в контейнер
COPY /target/RoomService-0.0.1-SNAPSHOT.jar /app/room.jar

# Открываем порт, на котором работает микросервис
EXPOSE 8080

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "room.jar"]
