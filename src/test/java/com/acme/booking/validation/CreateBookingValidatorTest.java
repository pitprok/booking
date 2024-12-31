package com.acme.booking.validation;

import com.acme.booking.controller.dto.CreateBookingDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateBookingValidatorTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void given_bookingIsValid_when_validating_then_constraintViolationListIsEmpty() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(result.size(), 0);
    }

    @Test
    void given_bookingEndDatetimeIsInThePast_when_validating_then_shouldReturnConstraintViolation() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().minusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(1, result.size());
        assertEquals("Booking end datetime cannot be in the past.", result.getFirst().getMessage());
    }

    @Test
    void given_bookingDurationIsInvalid_when_validating_then_shouldReturnConstraintViolation() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 30))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(1, result.size());
        assertEquals("The booking duration must be at least 1 hour and in 1-hour increments.", result.getFirst().getMessage());
    }

    @Test
    void given_bookingTimeToIsBeforeTimeFrom_when_validating_then_shouldReturnConstraintViolation() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(11, 30))
                .timeTo(LocalTime.of(10, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(1, result.size());
        assertEquals("The end time cannot be before the start time.", result.getFirst().getMessage());
    }

    @Test
    void given_bookingDateIsNull_when_validating_then_shouldReturnConstraintViolation() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(1, result.size());
        assertEquals("must not be null", result.getFirst().getMessage());
    }

    @Test
    void given_bookingTimeFromIsNull_when_validating_then_shouldReturnConstraintViolation() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(1, result.size());
        assertEquals("must not be null", result.getFirst().getMessage());
    }

    @Test
    void given_bookingTimeToIsNull_when_validating_then_shouldReturnConstraintViolation() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(1, result.size());
        assertEquals("must not be null", result.getFirst().getMessage());
    }

    @Test
    void given_bookingMeetingRoomIdIsNull_when_validating_then_shouldReturnConstraintViolation() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(1, result.size());
        assertEquals("must not be null", result.getFirst().getMessage());
    }

    @Test
    void given_bookingEmployeeEmailIsNull_when_validating_then_shouldReturnConstraintViolation() {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When
        List <ConstraintViolation<CreateBookingDto>> result = validator.validate(createBookingDto).stream().toList();

        // Then
        assertEquals(1, result.size());
        assertEquals("must not be null", result.getFirst().getMessage());
    }
}
