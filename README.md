# Spring Boot Security Create/View App

This project is a Spring Boot application secured with Spring Security (HTTP Basic) and connected to MySQL database `spring_securitydb`.

## Features

- Create item (`POST /api/items`)
- View all items (`GET /api/items`)
- Secured API endpoints using Spring Security

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Security
- Spring Data JPA
- MySQL Connector
- Maven

## Database Configuration

Update `src/main/resources/application.properties` with your MySQL credentials:

- `spring.datasource.username`
- `spring.datasource.password`

The app is configured to use:

- Database: `spring_securitydb`
- URL: `jdbc:mysql://localhost:3306/spring_securitydb`

## Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

Default security credentials:

- Username: `appuser`
- Password: `app12345`

## Example API Calls

Create item:

```bash
curl -u appuser:app12345 -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"First Item\",\"description\":\"Created item\"}"
```

View all items:

```bash
curl -u appuser:app12345 http://localhost:8080/api/items
```

## Git Setup and Push to Repository

Initialize git (if not already initialized):

```bash
git init
git add .
git commit -m "Initial Spring Boot security create/view app"
```

Add remote and push:

```bash
git remote add origin <your-repository-url>
git branch -M main
git push -u origin main
```
