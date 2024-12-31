package com.acme.booking.domain.service;

import com.acme.booking.domain.model.MeetingRoom;
import com.acme.booking.exception.ResourceAlreadyExistsException;
import com.acme.booking.exception.ResourceNotFoundException;
import com.acme.booking.repository.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;

    /**
     * Creates a new meeting room if the name is unique and returns the updated list of meeting rooms.
     *
     * @param name the name of the meeting room to create
     * @return a list of all meeting rooms after the new room is created
     * @throws ResourceAlreadyExistsException if a room with the given name already exists
     */
    public List<MeetingRoom> createMeetingRoom(String name) {
        if (meetingRoomRepository.existsByNameIgnoreCase(name)){
            log.error("A room with the name '{}' already exists", name);
            throw new ResourceAlreadyExistsException(String.format("A room with the name '%s' already exists", name));
        }
        meetingRoomRepository.save(new MeetingRoom(name));
        log.info("Meeting room '{}' created successfully", name);
        return getAll();
    }

    /**
     * Retrieves all meeting rooms.
     *
     * @return a list of meeting rooms
     */
    public List<MeetingRoom> getAll() {
        return meetingRoomRepository.findAll();
    }

    /**
     * Finds a meeting room by its ID.
     *
     * @param id the UUID of the meeting room to find
     * @return the MeetingRoom object if found
     * @throws ResourceNotFoundException if the meeting room with the given ID is not found
     */
    public MeetingRoom findById(UUID id) {
        log.info("Finding meeting room with ID: {}", id);
        return meetingRoomRepository.findById(id)
                .orElseThrow(() ->{
                    log.error("The meeting room with ID '{}' was not found", id);
                    return new ResourceNotFoundException(String.format("The meeting room with ID '%s' was not found", id));
                });
    }
}