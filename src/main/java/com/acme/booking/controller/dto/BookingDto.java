package com.acme.booking.controller.dto;

import com.acme.booking.validation.ValidBooking;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@ValidBooking
public class BookingDto extends CreateBookingDto {
    private UUID id;
}
