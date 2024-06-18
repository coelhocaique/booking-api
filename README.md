# Booking API

## Technologies

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Spring 2.7.18](https://spring.io/blog/2023/11/23/spring-boot-2-7-18-available-now)
* [H2 Database](https://h2database.com/html/main.html)

## Introduction

This API allows you to perform bookings for a certain period of time for your desired property, and also add some blocks for certain periods in which the property will be unavailable to be booked.   

You can also cancel and rebook your bookings.

## Database tables

Database tables are created automatically during startup, see `resources/schema.sql` for reference.

For sake of simplicity, we already have some `properties` pre-populated by `resources/data.sql` script, and they can be listed using `/properties` endpoint. 

## Run

Run locally with gradle:

```
./gradlew bootRun

```
Or run with [docker](https://docs.docker.com/get-docker/):

```
cd docker
docker-compose up
```

## Checkout how to use the API

Once API is running, you can access the [Swagger Documentation](http://localhost:8081/swagger-ui/index.html)

[Docker hub repository](https://hub.docker.com/r/coelhocaique/booking-api/tags)