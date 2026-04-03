# STAGE 1: Build dell'applicazione
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copia il file pom.xml e scarica le dipendenze (cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia il codice sorgente e crea il pacchetto .jar
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Esecuzione dell'applicazione
# Usa un'immagine base leggera con Java 17 (versione Alpine)
FROM eclipse-temurin:17-jdk-alpine

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Copiamo il file .jar dallo stage di build
COPY --from=build /app/target/*.jar app.jar

# Esponi la porta 8080 (quella di default per Spring Boot)
EXPOSE 8080

# Definisci il comando di avvio dell'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
