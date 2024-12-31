package com.acme.booking.configuration;

import com.acme.booking.domain.model.Booking;
import com.acme.booking.domain.model.MeetingRoom;
import com.acme.booking.repository.BookingRepository;
import com.acme.booking.repository.MeetingRoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(MeetingRoomRepository meetingRoomRepository, BookingRepository bookingRepository) {
        return args -> {
            MeetingRoom polaris = meetingRoomRepository.save(new MeetingRoom("Polaris"));
            MeetingRoom titan = meetingRoomRepository.save(new MeetingRoom("Titan"));
            meetingRoomRepository.save(new MeetingRoom("Andromeda"));
            meetingRoomRepository.save(new MeetingRoom("Sirius"));
            meetingRoomRepository.save(new MeetingRoom("Cassiopeia"));
            meetingRoomRepository.save(new MeetingRoom("Milky Way"));

            bookingRepository.save(generateBooking(polaris, "2026-02-01", "12:30:00","13:30:00"));
            bookingRepository.save(generateBooking(polaris, "2026-02-01", "13:30:00", "14:30:00"));
            bookingRepository.save(generateBooking(titan, "2026-02-01", "12:30:00", "13:30:00"));
            bookingRepository.save(generateBooking(polaris, "2026-02-02", "13:30:00", "14:30:00"));
            bookingRepository.save(generateBooking(titan, "2026-02-02", "13:30:00", "14:30:00"));
        };
    }

    private Booking generateBooking(MeetingRoom meetingRoom, String date, String timeFrom, String timeTo) {
        return new Booking(null, meetingRoom, "employee@example.com", LocalDate.parse(date), LocalTime.parse(timeFrom), LocalTime.parse(timeTo));
    }
}