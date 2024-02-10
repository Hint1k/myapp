FROM openjdk:17
WORKDIR /myapp
COPY ./target/myapp-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "myapp-0.0.1-SNAPSHOT.jar"]
