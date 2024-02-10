FROM openjdk:8
ADD target/myapp.jar myapp.jar
ENTRYPOINT ["java", "-jar","myapp.jar"]
EXPOSE 8080