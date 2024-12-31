package com.acme.booking.utils;

import com.acme.booking.controller.dto.CreateBookingDto;
import com.acme.booking.domain.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static com.acme.booking.utils.MeetingRoomUtils.DEFAULT_MEETING_ROOM_NAME;
import static com.acme.booking.utils.MeetingRoomUtils.generateMeetingRoomWithId;

public class BookingUtils {

    public static Booking generateBookingWithId() {
        return generateBookingWithoutId().toBuilder().id(UUID.randomUUID()).build();
    }

    public static Booking generateBookingWithoutId() {
        return Booking.builder()
                .meetingRoom(generateMeetingRoomWithId(DEFAULT_MEETING_ROOM_NAME))
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();
    }

    public static CreateBookingDto generateCreateBookingDto(UUID id){
        return CreateBookingDto.builder()
                .meetingRoomId(id)
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();
    }


}
