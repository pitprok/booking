package com.acme.booking.controller;

import com.acme.booking.controller.dto.BookingDto;
import com.acme.booking.controller.dto.CreateBookingDto;
import com.acme.booking.facade.BookingFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingFacade bookingFacade;

    /**
     * Creates a new booking.
     *
     * @param createBookingDto the booking data
     * @return list of bookings with the same date and meeting room as the one that was created
     */
    @Operation(summary = "Create Booking", description = "Creates a new booking and returns a list of bookings with the same date and meeting room as the one that was created")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public List<BookingDto> createBooking(@Valid @RequestBody CreateBookingDto createBookingDto) {
        log.info("Creating booking with data: {}", createBookingDto);
        return bookingFacade.createBooking(createBookingDto);

    }

    /**
     * Retrieves bookings for a specific meeting room on a given date.
     *
     * @param meetingRoomId the meeting room ID
     * @param date          the date of bookings
     * @return list of bookings
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all bookings for meeting room/date", description = "Retrieves bookings for a specific meeting room on a given date.")
    public List<BookingDto> getBookingsForMeetingRoomAndDate(@RequestParam UUID meetingRoomId, @RequestParam LocalDate date) {
        log.debug("Fetching bookings for meeting room ID: {} and date: {}", meetingRoomId, date);
        return bookingFacade.getBookings(meetingRoomId, date);
    }

    /**
     * Deletes a booking by ID.
     *
     * @param id the booking ID
     */
    @Operation(summary = "Delete booking", description = "Deletes a booking by ID.")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteBooking(@RequestParam UUID id) {
        log.info("Deleting booking with ID: {}", id);
        bookingFacade.deleteBooking(id);
    }
}