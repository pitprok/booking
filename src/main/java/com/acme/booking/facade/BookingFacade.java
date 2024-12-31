package com.acme.booking.facade;

import com.acme.booking.controller.dto.BookingDto;
import com.acme.booking.controller.dto.CreateBookingDto;
import com.acme.booking.domain.model.Booking;
import com.acme.booking.domain.model.MeetingRoom;
import com.acme.booking.domain.service.BookingService;
import com.acme.booking.domain.service.MeetingRoomService;
import com.acme.booking.facade.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingFacade {

    private final BookingService bookingService;
    private final MeetingRoomService meetingRoomService;
    private final BookingMapper bookingMapper;

    /**
     * Creates a booking and returns the resulting DTOs.
     *
     * @param createBookingDto the booking to create
     * @return list of bookings with the same date and meeting room as the one that was created
     */
    public List<BookingDto> createBooking(CreateBookingDto createBookingDto) {
        MeetingRoom meetingRoom = meetingRoomService.findById(createBookingDto.getMeetingRoomId());
        Booking booking = bookingMapper.toModel(createBookingDto, meetingRoom);
        return bookingMapper.toDto(bookingService.createBooking(booking));
    }

    /**
     * Retrieves bookings for a specific meeting room on a given date.
     *
     * @param meetingRoomId the meeting room ID
     * @param date          the date of bookings
     * @return list of booking DTOs
     */
    public List<BookingDto> getBookings(UUID meetingRoomId, LocalDate date) {
        List<Booking> bookings = bookingService.getBookings(meetingRoomId, date);

        return bookingMapper.toDto(bookings);
    }

    /**
     * Deletes a booking by ID.
     *
     * @param bookingId the booking ID
     */
    public void deleteBooking(UUID bookingId) {
        bookingService.deleteBooking(bookingId);
    }
}
