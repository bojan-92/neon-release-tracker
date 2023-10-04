#FROM openjdk:17-jdk-slim-buster
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} release-tracker.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/release-tracker.jar"]
FROM maven:3.8.5-openjdk-17

WORKDIR /neon-release-tracker
COPY . .
RUN mvn clean install -DskipTests

CMD mvn spring-boot:run