package com.acme.booking.facade;

import com.acme.booking.controller.dto.MeetingRoomDto;
import com.acme.booking.domain.model.MeetingRoom;
import com.acme.booking.domain.service.MeetingRoomService;
import com.acme.booking.facade.mapper.MeetingRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetingRoomFacade {

    private final MeetingRoomService meetingRoomService;
    private final MeetingRoomMapper meetingRoomMapper;

    /**
     * Creates a new meeting room with the given name and returns the updated list of meeting rooms.
     *
     * @param name the name of the meeting room to create
     * @return a list of all meeting rooms
     */
    public List<MeetingRoomDto> createMeetingRoom(String name) {
        List<MeetingRoom> meetingRooms = meetingRoomService.createMeetingRoom(name);
        return meetingRoomMapper.toDto(meetingRooms);
    }

    /**
     * Retrieves all meeting rooms
     *
     * @return a list of all meeting rooms
     */
    public List<MeetingRoomDto> getAll() {
        return meetingRoomMapper.toDto(meetingRoomService.getAll());
    }
}
