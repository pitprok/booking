package com.acme.booking.controller;

import com.acme.booking.BookingApplicationBaseIntegrationTest;
import com.acme.booking.controller.dto.BookingDto;
import com.acme.booking.controller.dto.CreateBookingDto;
import com.acme.booking.domain.model.Booking;
import com.acme.booking.domain.model.MeetingRoom;
import com.acme.booking.facade.mapper.BookingMapper;
import com.acme.booking.repository.BookingRepository;
import com.acme.booking.repository.MeetingRoomRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.acme.booking.utils.BookingUtils.generateBookingWithoutId;
import static com.acme.booking.utils.BookingUtils.generateCreateBookingDto;
import static com.acme.booking.utils.MeetingRoomUtils.DEFAULT_MEETING_ROOM_NAME;
import static com.acme.booking.utils.MeetingRoomUtils.generateMeetingRoomWithoutId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingApplicationControllerIT extends BookingApplicationBaseIntegrationTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BookingMapper bookingMapper;

    @BeforeEach
    public void init() {
        bookingRepository.deleteAll();
        meetingRoomRepository.deleteAll();
    }

    @Test
    void given_bookingIsValidAndMeetingRoomExists_when_creatingBooking_then_bookingIsCreated() throws Exception {
        // Given
        MeetingRoom meetingRoom = generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME);

        meetingRoom = meetingRoomRepository.save(meetingRoom);

        CreateBookingDto createBookingDto = generateCreateBookingDto(meetingRoom.getId());

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void given_anotherBookingAlreadyExistsForTheSameRoomAndDate_when_creatingBooking_then_bothBookingsAreReturned() throws Exception {
        // Given
        MeetingRoom meetingRoom = generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME);

        meetingRoom = meetingRoomRepository.save(meetingRoom);

        CreateBookingDto createBookingDto = generateCreateBookingDto(meetingRoom.getId());

        Booking booking = Booking.builder()
                .meetingRoom(meetingRoom)
                .employeeEmail(createBookingDto.getEmployeeEmail())
                .date(createBookingDto.getDate())
                .timeFrom(createBookingDto.getTimeFrom().plusHours(1))
                .timeTo(createBookingDto.getTimeTo().plusHours(1))
                .build();

        bookingRepository.save(booking);

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void given_bookingAlreadyExistsForTheSameRoomAndDateTime_when_creatingBooking_then_conflictErrorIsReturned() throws Exception {
        // Given
        MeetingRoom meetingRoom = generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME);

        meetingRoom = meetingRoomRepository.save(meetingRoom);

        CreateBookingDto createBookingDto = generateCreateBookingDto(meetingRoom.getId());

        Booking booking = bookingMapper.toModel(createBookingDto, meetingRoom);

        bookingRepository.save(booking);

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Booking overlaps with an existing booking"));
    }

    @Test
    void given_bookingEndDatetimeIsInThePast_when_creatingBooking_then_shouldReturnRelevantValidationError() throws Exception {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().minusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['Validation error']").value("Booking end datetime cannot be in the past."));
    }

    @Test
    void given_bookingDurationIsInvalid_when_creatingBooking_then_shouldReturnRelevantValidationError() throws Exception {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 30))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['Validation error']").value("The booking duration must be at least 1 hour and in 1-hour increments."));
    }

    @Test
    void given_bookingTimeToIsBeforeTimeFrom_when_creatingBooking_then_shouldReturnRelevantValidationError() throws Exception {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(11, 30))
                .timeTo(LocalTime.of(10, 0))
                .build();

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['Validation error']").value("The end time cannot be before the start time."));
    }

    @Test
    void given_bookingDateIsNull_when_creatingBooking_then_shouldReturnNullValidationErrorForTheField() throws Exception {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['date']").value("must not be null"));
    }

    @Test
    void given_bookingTimeFromIsNull_when_creatingBooking_then_shouldReturnNullValidationErrorForTheField() throws Exception {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['timeFrom']").value("must not be null"));
    }

    @Test
    void given_bookingTimeToIsNull_when_creatingBooking_then_shouldReturnNullValidationErrorForTheField() throws Exception {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .build();

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['timeTo']").value("must not be null"));
    }

    @Test
    void given_bookingMeetingRoomIdIsNull_when_creatingBooking_then_shouldReturnNullValidationErrorForTheField() throws Exception {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .employeeEmail("employee@example.com")
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();

        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['meetingRoomId']").value("must not be null"));
    }

    @Test
    void given_bookingEmployeeEmailIsNull_when_creatingBooking_then_shouldReturnNullValidationErrorForTheField() throws Exception {
        // Given
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .meetingRoomId(UUID.randomUUID())
                .date(LocalDate.now().plusDays(1))
                .timeFrom(LocalTime.of(10, 0))
                .timeTo(LocalTime.of(11, 0))
                .build();


        // When && Then
        mockMvc.perform(post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['employeeEmail']").value("must not be null"));
    }

    @Test
    void given_bookingExists_when_requestingBookingsForThatDateAndMeetingRoom_then_bookingIsReturned() throws Exception {
        // Given
        MeetingRoom meetingRoom = generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME);
        Booking booking = generateBookingWithoutId();

        meetingRoom = meetingRoomRepository.save(meetingRoom);

        booking.setMeetingRoom(meetingRoom);

        BookingDto expectedBookingDto = bookingMapper.toDto(bookingRepository.save(booking));

        // When && Then
        String jsonResponse = mockMvc.perform(get("/api/booking")
                        .param("meetingRoomId", meetingRoom.getId().toString())
                        .param("date", booking.getDate().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<BookingDto> bookings = objectMapper.readValue(
                jsonResponse,
                new TypeReference<>() {}
        );

        assertEquals(expectedBookingDto, bookings.getFirst());
    }


    @Test
    void given_bookingExists_when_deletingBooking_then_bookingIsDeleted() throws Exception {
        // Given
        MeetingRoom meetingRoom = generateMeetingRoomWithoutId(DEFAULT_MEETING_ROOM_NAME);
        Booking booking = generateBookingWithoutId();

        meetingRoom = meetingRoomRepository.save(meetingRoom);

        booking.setMeetingRoom(meetingRoom);
        bookingRepository.save(booking);

        // When
        assertEquals(1, bookingRepository.findAll().size());

        mockMvc.perform(delete("/api/booking")
                        .param("id", booking.getId().toString()))
                .andExpect(status().isOk());

        // Then
        assertEquals(0, bookingRepository.findAll().size());
    }

    @Test
    void given_bookingDoesNotExist_when_deletingBooking_then_throwNotFoundError() throws Exception {
        // When && Then
        mockMvc.perform(delete("/api/booking")
                        .param("id", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }
}