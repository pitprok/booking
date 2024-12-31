package com.acme.booking.facade.mapper;

import com.acme.booking.controller.dto.BookingDto;
import com.acme.booking.controller.dto.CreateBookingDto;
import com.acme.booking.domain.model.Booking;
import com.acme.booking.domain.model.MeetingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BookingMapper {

    @Mapping(target = "meetingRoom", source = "meetingRoom")
    @Mapping(target = "id", ignore = true)
    Booking toModel(CreateBookingDto bookingDto, MeetingRoom meetingRoom);

    @Mapping(target = "meetingRoomId", source = "meetingRoom.id")
    BookingDto toDto(Booking booking);
    List<BookingDto> toDto(List<Booking> bookings);
}
