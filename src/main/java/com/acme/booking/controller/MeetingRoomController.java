package com.acme.booking.controller;

import com.acme.booking.controller.dto.MeetingRoomDto;
import com.acme.booking.facade.MeetingRoomFacade;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/meeting-room")
public class MeetingRoomController {

    private final MeetingRoomFacade meetingRoomFacade;

    /**
     * Creates a new meeting room
     *
     * @param name the name of the meeting room to create
     * @return a list of all meeting rooms
     */
    @Operation(summary = "Create meeting room", description = "Creates a new meeting room and returns a list of all meeting rooms.")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public List<MeetingRoomDto> createMeetingRoom(@RequestParam String name) {
        return meetingRoomFacade.createMeetingRoom(name);
    }

    /**
     * Retrieves all meeting rooms.
     *
     * @return a list of all meeting rooms
     */
    @Operation(summary = "Get all meeting rooms", description = "Returns a list of all meeting rooms.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MeetingRoomDto> getAll() {
        return meetingRoomFacade.getAll();
    }
}
