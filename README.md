# Booking Application

This project is a Java-based booking application designed to manage reservations for ACME's meeting rooms.

## Table of Contents

- [Features](#features)
- [Restrictions](#restrictions)
- [Architecture Overview](#architecture-overview)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Building the Application](#building-the-application)
    - [Running the Application](#running-the-application)
- [Usage](#usage)


## Features

- Create meeting rooms.
- Book meeting rooms.
- Search bookings by date and meeting room.
- Prevent booking conflicts with overlapping time slots.
- Ensure bookings meet predefined constraints:
    - Minimum duration of 1 hour.
    - Duration should be set to one-hour increments
    - End time must be after the start time.
    - End datetime must not be in the past
- Cancel bookings before their expiration.

## Restrictions

The application was created based on the following requirements/restrictions

- The name of each meeting room should be unique
- When booking a room the following fields are mandatory
  1. Meeting room
  2. Employee email
  3. Date
  4. Time from
  5. Time to

## Architecture Overview

The Booking Application follows a layered architecture with the following components:

1. **Controller Layer:** Manages API endpoints for booking-related operations.
2. **Facade layer** Orchestrates the service coordination and handles DTO to model mappings.
3. **Service Layer:** Contains the core business logic, such as conflict checks and validations.
4. **Repository Layer:** Interfaces with the embedded H2 database for data persistence.
5. **Database:** An H2 in-memory database is used by default for simplicity and local development.

## Technologies Used

- Java
- Spring Boot
- Maven
- Docker
- H2
- Swagger

## Getting Started

Follow these instructions to set up and run the project on your local machine.

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- Docker (optional, for containerized deployment)
- Maven 3.6.3 or higher

### Building the Application

1. **Clone the repository**:

   ```bash
   git clone https://github.com/pitprok/booking.git 
   ```

2. **Navigate to the project directory**:

   ```bash
   cd booking
   ```

3. **Package the project using Maven**:

   Run the following command to build the project's jar
   ```bash
   mvn clean package
   ```

### Running the Application

**Using Maven**:

You can start the application directly using Maven:

   ```bash
   mvn spring-boot:run
   ```

**Using Docker**:

1. **Build the Docker image**:

    To build the Docker image, run:

   ```bash
   docker build -t booking .
   ```

2. **Run the Docker container**:

    Start the container with:

   ```bash
   docker run -p 8080:8080 booking
   ```

Once the application is running, it will be accessible at http://localhost:8080.

### Usage

- **API Documentation:** Use the Swagger UI available at http://localhost:8080/swagger-ui.html to learn how to interact with the API.
- **Database access:** Open your web browser and navigate to http://localhost:8080/console to connect to the embedded H2 database
