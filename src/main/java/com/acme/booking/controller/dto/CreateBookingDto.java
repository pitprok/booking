package com.acme.booking.controller.dto;

import com.acme.booking.validation.ValidBooking;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@ValidBooking
public class CreateBookingDto {
    @NotNull
    private UUID meetingRoomId;
    @NotNull
    private String employeeEmail;
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime timeFrom;
    @NotNull
    private LocalTime timeTo;
}
