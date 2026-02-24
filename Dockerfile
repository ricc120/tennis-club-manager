# Usa un'immagine base leggera con Java 17 (versione Alpine)
FROM eclipse-temurin:17-jdk-alpine

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Copia il file JAR generato da Maven nella directory /app del container
COPY target/*.jar app.jar

# Esponi la porta 8080 (quella di default per Spring Boot)
EXPOSE 8080

# Definisci il comando di avvio dell'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
