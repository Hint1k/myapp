FROM openjdk:17.0.2-jdk-slim-buster
ENV PORT 8080
EXPOSE 8080
ARG JAR_FILE=target/*.war
COPY ${JAR_FILE} myapp-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java","-jar","/myapp-0.0.1-SNAPSHOT.war"]
