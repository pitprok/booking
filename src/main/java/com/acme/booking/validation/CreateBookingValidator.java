package com.acme.booking.validation;

import com.acme.booking.controller.dto.CreateBookingDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Slf4j
public class CreateBookingValidator implements ConstraintValidator<ValidBooking, CreateBookingDto> {

    @Override
    public boolean isValid(CreateBookingDto createBookingDto, ConstraintValidatorContext context) {
        if (createBookingDto == null) {
            return true;
        }

        LocalDate date = createBookingDto.getDate();
        LocalTime timeFrom = createBookingDto.getTimeFrom();
        LocalTime timeTo = createBookingDto.getTimeTo();

        if (date == null || timeFrom == null || timeTo == null) {
            return true;
        }

        LocalDateTime bookingEnd = LocalDateTime.of(date, timeTo);
        LocalDateTime now = LocalDateTime.now();

        context.disableDefaultConstraintViolation();

        if (bookingEnd.isBefore(now)) {
            log.error("Booking end time for {} is in the past: {}. Validation failed.", createBookingDto, bookingEnd);
            context.buildConstraintViolationWithTemplate("Booking end datetime cannot be in the past.")
                    .addConstraintViolation();
            return false;
        }

        if (timeTo.isBefore(timeFrom)) {
            log.error("End time {} is before start time {}. Validation failed.", timeTo, timeFrom);
            context.buildConstraintViolationWithTemplate("The end time cannot be before the start time.")
                    .addConstraintViolation();
            return false;
        }

        long durationMinutes = Duration.between(timeFrom, timeTo).toMinutes();
        if (durationMinutes < 60 || durationMinutes % 60 != 0) {
            log.error("Booking duration for {} is invalid: {} minutes. Validation failed.", createBookingDto, durationMinutes);
            context.buildConstraintViolationWithTemplate("The booking duration must be at least 1 hour and in 1-hour increments.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}