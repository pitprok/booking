package com.acme.booking.service;

import com.acme.booking.domain.model.Booking;
import com.acme.booking.domain.service.BookingService;
import com.acme.booking.exception.BookingOverlapException;
import com.acme.booking.exception.PastBookingException;
import com.acme.booking.exception.ResourceNotFoundException;
import com.acme.booking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.acme.booking.utils.BookingUtils.generateBookingWithId;
import static com.acme.booking.utils.BookingUtils.generateBookingWithoutId;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void given_newBookingDoesntOverlapWithExistingBooking_when_createBooking_then_bookingIsCreated() {
        // Given
        Booking booking = generateBookingWithoutId();

        when(bookingRepository.existsByMeetingRoomIdAndDateAndTimeOverlap(booking)).thenReturn(false);

        // When && Then
        assertDoesNotThrow(() -> bookingService.createBooking(booking));
        verify(bookingRepository, times(1)).save(booking);
        verify(bookingRepository, times(1)).findByMeetingRoomIdAndDate(booking.getMeetingRoom().getId(), booking.getDate());
    }

    @Test
    void given_newBookingOverlapsWithExistingBooking_when_createBooking_then_throwBookingOverlapException() {
        // Given
        Booking booking = generateBookingWithoutId();

        when(bookingRepository.existsByMeetingRoomIdAndDateAndTimeOverlap(booking)).thenReturn(true);

        // When && Then
        assertThrows(BookingOverlapException.class, () -> bookingService.createBooking(booking));
    }


    @Test
    void given_bookingExistsAndBookingEndTimeIsNotInThePast_when_deleteBooking_then_bookingIsDeleted() {
        // Given
        Booking booking = generateBookingWithId();

        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).deleteById(any(UUID.class));

        // When && Then
        assertDoesNotThrow(() -> bookingService.deleteBooking(booking.getId()));
        verify(bookingRepository, times(1)).deleteById(booking.getId());
    }

    @Test
    void given_bookingExistsAndBookingEndTimeIsInThePast_when_deleteBooking_then_throwPastBookingException() {
        // Given
        Booking booking = generateBookingWithId().toBuilder()
                .date(LocalDate.now().minusDays(1))
                .build();

        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(booking));

        // When && Then
        assertThrows(PastBookingException.class, () -> bookingService.deleteBooking(UUID.randomUUID()));
    }

    @Test
    void given_bookingDoesNotExist_when_deleteBooking_then_throwResourceNotFoundException() {
        // When && Then
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.deleteBooking(UUID.randomUUID()));
    }
}