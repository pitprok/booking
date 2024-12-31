package com.acme.booking.service;


import com.acme.booking.domain.model.MeetingRoom;
import com.acme.booking.domain.service.MeetingRoomService;
import com.acme.booking.exception.ResourceAlreadyExistsException;
import com.acme.booking.exception.ResourceNotFoundException;
import com.acme.booking.repository.MeetingRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.acme.booking.utils.MeetingRoomUtils.DEFAULT_MEETING_ROOM_NAME;
import static com.acme.booking.utils.MeetingRoomUtils.generateMeetingRoomWithId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeetingRoomServiceTest {

    @InjectMocks
    private MeetingRoomService meetingRoomService;

    @Mock
    private MeetingRoomRepository meetingRoomRepository;

    @Test
    public void given_meetingRoomDoesNotExist_when_createMeetingRoom_then_returnsListOfAvailableMeetingRooms() {
        // Given
        MeetingRoom meetingRoom = generateMeetingRoomWithId(DEFAULT_MEETING_ROOM_NAME);
        MeetingRoom meetingRoom2 = generateMeetingRoomWithId(DEFAULT_MEETING_ROOM_NAME + "2");
        List <MeetingRoom> expectedMeetingRooms = List.of(meetingRoom, meetingRoom2);

        when(meetingRoomRepository.existsByNameIgnoreCase(meetingRoom.getName())).thenReturn(false);
        when(meetingRoomRepository.findAll()).thenReturn(expectedMeetingRooms);

        // When
        List<MeetingRoom> result = meetingRoomService.createMeetingRoom(DEFAULT_MEETING_ROOM_NAME);

        // Then
        assertEquals(result, expectedMeetingRooms);
    }

    @Test
    public void given_meetingRoomAlreadyExists_when_createMeetingRoom_then_throwsResourceAlreadyExistsException() {
        // Given
        when(meetingRoomRepository.existsByNameIgnoreCase(DEFAULT_MEETING_ROOM_NAME)).thenReturn(true);

        // When & Then
        assertThrows(ResourceAlreadyExistsException.class, () -> meetingRoomService.createMeetingRoom(DEFAULT_MEETING_ROOM_NAME));
    }


    @Test
    public void given_nonExistentMeetingRoomId_when_findRoomById_then_throwsResourceNotFoundException() {
        // Given
        when(meetingRoomRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> meetingRoomService.findById(UUID.randomUUID()));
    }
}