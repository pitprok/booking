package com.acme.booking.domain.service;


import com.acme.booking.domain.model.Booking;
import com.acme.booking.exception.BookingOverlapException;
import com.acme.booking.exception.PastBookingException;
import com.acme.booking.exception.ResourceNotFoundException;
import com.acme.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;

    /**
     * Creates a booking and checks for overlap with other bookings.
     *
     * @param booking the booking to create
     * @return list of bookings for the same room and date
     */
    public List<Booking> createBooking(Booking booking) {
        checkForBookingOverlap(booking);
        bookingRepository.save(booking);
        log.info("Booking created successfully: {}", booking);
        return getBookings(booking.getMeetingRoom().getId(), booking.getDate());
    }

    /**
     * Retrieves bookings for a specific meeting room on a given date.
     *
     * @param meetingRoomId the meeting room ID
     * @param date          the date of bookings
     * @return list of bookings
     */
    public List<Booking> getBookings(UUID meetingRoomId, LocalDate date) {
        return bookingRepository.findByMeetingRoomIdAndDate(meetingRoomId, date);
    }

    /**
     * Deletes a booking by ID, ensuring it isn't in the past.
     *
     * @param bookingId the booking ID
     * @throws PastBookingException If the booking end datetime is in the past
     * @throws ResourceNotFoundException If the booking does not exist
     */
    public void deleteBooking(UUID bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            log.error("There is no booking with id {}", bookingId);
            throw new ResourceNotFoundException("There is no booking with id " + bookingId);
        }
        Booking booking = bookingOpt.get();

        if (bookingIsInThePast(booking)){
            log.error("Attempt to cancel a booking in the past with ID: {}", bookingId);
            throw new PastBookingException("A booking that has already ended cannot be canceled");
        }

        bookingRepository.deleteById(bookingId);
        log.info("Successfully deleted booking with ID: {}", bookingId);
    }

    /**
     * Checks if a booking is in the past. It uses the date and end time of the booking to do so.
     *
     * @param booking the booking to check
     * @return true if the booking is in the past, false otherwise
     */
    private boolean bookingIsInThePast(Booking booking) {
        LocalDateTime bookingDateTime = LocalDateTime.of(booking.getDate(), booking.getTimeTo());
        return bookingDateTime.isBefore(now());
    }

    /**
     * Validates if the booking overlaps with existing ones.
     *
     * @param booking the booking to check
     * @throws BookingOverlapException If the booking overlaps with an existing booking
     */
    private void checkForBookingOverlap(Booking booking) {
        if (bookingRepository.existsByMeetingRoomIdAndDateAndTimeOverlap(booking)) {
            log.error("Booking overlap detected for booking: {}", booking);
            throw new BookingOverlapException("Booking overlaps with an existing booking");
        }
    }
}