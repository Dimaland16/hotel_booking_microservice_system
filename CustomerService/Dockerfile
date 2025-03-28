# Используем базовый образ JDK
FROM openjdk:22-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar-файл в контейнер
COPY /target/CustomerService-0.0.1-SNAPSHOT.jar /app/cust.jar

# Открываем порт, на котором работает микросервис
EXPOSE 8082

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "cust.jar"]
