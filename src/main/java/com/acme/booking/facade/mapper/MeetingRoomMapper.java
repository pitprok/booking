package com.acme.booking.facade.mapper;

import com.acme.booking.controller.dto.MeetingRoomDto;
import com.acme.booking.domain.model.MeetingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MeetingRoomMapper {

    MeetingRoomDto toDto(MeetingRoom meetingRoom);

    List<MeetingRoomDto> toDto(List<MeetingRoom> meetingRoom);
}
