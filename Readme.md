# My Finances API

This API is intended to manage personal finances. It contains account setup, account management, categories, and
transactions history.

## Resources

This API was developed using spring boot. To set up the environment on your computer you need:

- [Java JDK 17](https://www.oracle.com/ca-en/java/technologies/downloads/#java17)
- [Intellij](https://www.jetbrains.com/idea/) (Community edition)
- [Postgresql](https://www.postgresql.org/)

## Main Dependencies Used in this project

- [Spring Boot](https://spring.io/)
    - [Spring Data JPA](https://spring.io/projects/spring-data-jpa/)
    - [Spring Data Redis](https://spring.io/projects/spring-data-redis/)
    - [Spring Security](https://spring.io/projects/spring-security/)
- [Lombok](https://projectlombok.org/)
- [Slf4j](https://www.slf4j.org/)

## Getting Started

- Clone this repository from [My Finances API](https://github.com/JeisonEccel/my-finances-api)
- Load the projects to your IDE
- Copy or create `application.yml` in the project root folder and change information as needed
- Download and install all dependencies
    - If you use Intellij with Maven the dependencies will be downloaded when loading the project
    - Some IDEs may require to manually downloading Lombok
- Make sure you have postgres installed and running
- Setup required environment variables
- Run `bin/reset-database.sh` to drop/create database
- Go to `src/main/java/com/jeisoneccel/my_finances/MyFinancesApplication` and run the main class

## Consuming the API

### Register User

To register a new user, they need the following body as `application/json`:

```
{
    "name": "string",
    "email": "string",
    "password": "string"
}
```

### Login

When user logs in, they need the following body as `application/json`:

```
{
    "username": "string",
    "password": "string"
}
```

Only users with login can modify data in this api.