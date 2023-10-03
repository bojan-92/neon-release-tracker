FROM openjdk:17-jdk-slim-buster
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} release-tracker.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/release-tracker.jar"]