package com.acme.booking.repository;


import com.acme.booking.domain.model.MeetingRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static com.acme.booking.utils.MeetingRoomUtils.DEFAULT_MEETING_ROOM_NAME;
import static com.acme.booking.utils.MeetingRoomUtils.generateMeetingRoomWithoutId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MeetingRoomRepositoryIT {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @BeforeEach
    public void init() {
        meetingRoomRepository.deleteAll();
    }

    @Test
    public void given_aRoomWithASpecificNameAlreadyExists_when_creatingARoomWithTheSameName_then_exceptionIsThrown() {
        // Given
        MeetingRoom room1 = generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME);
        meetingRoomRepository.save(room1);

        // When & Then
        MeetingRoom room2 = generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME);
        assertThrows(DataIntegrityViolationException.class, () -> meetingRoomRepository.save(room2));
    }

    @Test
    public void given_aRoomExists_when_checkingIfItExists_then_trueIsReturned() {
        // Given
        MeetingRoom room = generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME);
        meetingRoomRepository.save(room);

        // When
        boolean exists = meetingRoomRepository.existsByName(DEFAULT_MEETING_ROOM_NAME);

        // Then
        assertThat(exists).isTrue();
    }
}