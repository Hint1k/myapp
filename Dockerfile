FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /home/app
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17.0.2-jdk-slim-buster
WORKDIR /app
RUN groupadd -g 1000 appuser && useradd -u 1000 -g appuser -s /bin/bash appuser
RUN mkdir -p /app && chown -R appuser:appuser /app
COPY --from=builder /home/app/target/myapp-0.0.1-SNAPSHOT.war myapp.war
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "myapp.war"]