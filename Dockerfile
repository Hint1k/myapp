FROM openjdk:17.0.2-jdk-slim-buster
ARG JAR_FILE=target/*.war
COPY ${JAR_FILE} myapp-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java","-jar","/myapp-0.0.1-SNAPSHOT.war"]
