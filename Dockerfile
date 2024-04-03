FROM openjdk:17.0.2-jdk-slim-buster
ENV PORT 8080
EXPOSE 8080
ARG JAR_FILE=/target/myapp-0.0.1-SNAPSHOT.war
COPY ${JAR_FILE} myapp.war
ENTRYPOINT ["java","-jar","/myapp.war"]