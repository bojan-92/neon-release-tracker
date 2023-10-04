# neon-release-tracker
## Built With
 - [Maven](https://maven.apache.org/) - Dependency Management
 - [Lombok](https://projectlombok.org/) - for Intellij IDEA you have to install Lombok plugin !
 - [JDK-17](https://www.oracle.com/java/technologies/downloads/#java17) - Java™ Platform, Standard Edition Development Kit
 - [MongoDB](https://www.mongodb.com/) - NoSQL database systems
 - [git](https://git-scm.com/) - Version control system
 - [Spring Boot 3](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications
 - [Rest Assured](https://rest-assured.io/) - Java library for testing Restful Web services.
 - [JUnit](https://junit.org/junit5/) - Testing framework for Java
 - [MongoDB Testcontainers](https://java.testcontainers.org/modules/databases/mongodb/) - Testcontainers is a library that provides throwaway container instances in a test runtime in order to facilitate integration tests with real dependencies
 - [Docker](https://www.docker.com/) - Open source containerization platform
 - [Docker Compose](https://docs.docker.com/compose/) - Tool for defining and running multi-container Docker applications
 - [OpenAPI 3.0](https://swagger.io/specification/) - The OpenAPI Specification (OAS) defines a standard, language-agnostic interface to HTTP APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code

## External Tools Used
 - [Postman](https://www.postman.com/) - API Development Environment

## How to run the application
It is a docker based application. Running below command in project's directory builds 2 running containers, neon-release-tracker and mongodb:<br /><br />
```
docker-compose up
```
## How to use
This application is Restful and it follows OpenAPI specification in API documentation. Thanks to SwaggerUI, you can see endpoints documentation in a graphical user interface and try their functionality and see the response. After running the containers, you can access the application links:<br /><br />
 - Application Rest service: http://localhost:8080/v1/releases
 - SwaggerUI: http://localhost:8080/swagger-ui.html

## Tests
Tests are runnable using maven command in project directory:<br /><br />
```
mvn test
```
