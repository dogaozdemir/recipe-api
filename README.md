# Recipe API
___
### Spring Boot Application

---
This project provides to manage recipes for users.

### Prerequisites

---
- Maven
- Docker

### Run & Build
There are 2 ways of run & build the application.
Test user is already inserted, set Authorization header to 1.

1-Maven:

Install maven dependencies with command below

```sh
mvn clean install
```

After that for running the application use the command below

```sh
mvn spring-boot:run
```

2:Docker

```sh
docker build -t recipe-api:1.0 .
```

```sh
docker run -d -p 8080:8080 -t recipe-api:1.0
```




### Swagger UI will be run on this url
For both API documentation and calling end-points, you can check the link below:

http://localhost:8080/swagger-ui/


### TechStacks

---
- Java 11
- Spring Boot
- Spring Data JPA
- Lombok
- Restful API
- H2 In memory database
- Maven
- JUnit 5

