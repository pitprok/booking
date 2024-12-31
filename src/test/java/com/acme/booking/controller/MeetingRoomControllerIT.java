package com.acme.booking.controller;

import com.acme.booking.BookingApplicationBaseIntegrationTest;
import com.acme.booking.controller.dto.MeetingRoomDto;
import com.acme.booking.domain.model.MeetingRoom;
import com.acme.booking.repository.MeetingRoomRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static com.acme.booking.utils.MeetingRoomUtils.DEFAULT_MEETING_ROOM_NAME;
import static com.acme.booking.utils.MeetingRoomUtils.generateMeetingRoomWithoutId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MeetingRoomControllerIT extends BookingApplicationBaseIntegrationTest {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        meetingRoomRepository.deleteAll();
    }

    @Test
    public void when_meetingRoomCreationRequestIsReceived_then_meetingRoomIsCreated() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/meeting-room/create")
                        .param("name", DEFAULT_MEETING_ROOM_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void given_anotherMeetingRoomAlreadyExists_when_meetingRoomIsCreated_then_bothMeetingRoomsAreReturned() throws Exception {
        meetingRoomRepository.save(generateMeetingRoomWithoutId("Existing Meeting Room"));

        // When & Then
        mockMvc.perform(post("/api/meeting-room/create")
                        .param("name", DEFAULT_MEETING_ROOM_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void given_meetingRoomsExist_when_allMeetingRoomsAreRequested_then_allMeetingRoomsAreReturned() throws Exception {
        // Given
        MeetingRoom expectedMeetingRoom1 = meetingRoomRepository.save(generateMeetingRoomWithoutId("Meeting Room 1"));
        MeetingRoom expectedMeetingRoom2 = meetingRoomRepository.save(generateMeetingRoomWithoutId("Meeting Room 2"));

        List<MeetingRoomDto> expectedMeetingRoomDtos = List.of(
                new MeetingRoomDto(expectedMeetingRoom1.getId(), expectedMeetingRoom1.getName()),
                new MeetingRoomDto(expectedMeetingRoom2.getId(), expectedMeetingRoom2.getName())
        );

        // When & Then
        String jsonResponse = mockMvc.perform(get("/api/meeting-room"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<MeetingRoomDto> actualMeetingRoomDtos = objectMapper.readValue(
                jsonResponse,
                new TypeReference<>() {}
        );

        assertEquals(expectedMeetingRoomDtos, actualMeetingRoomDtos);
    }

    @Test
    public void given_meetingRoomAlreadyExists_when_meetingRoomIsCreated_then_conflictErrorIsReturned() throws Exception {
        meetingRoomRepository.save(generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME));

        // When & Then
        mockMvc.perform(post("/api/meeting-room/create")
                        .param("name", DEFAULT_MEETING_ROOM_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string(String.format("A room with the name '%s' already exists", DEFAULT_MEETING_ROOM_NAME)));
    }
}