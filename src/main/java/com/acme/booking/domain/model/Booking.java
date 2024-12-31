package com.acme.booking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "meeting_room_id", nullable = false)
    private MeetingRoom meetingRoom;
    @Column(nullable = false)
    private String employeeEmail;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime timeFrom;
    @Column(nullable = false)
    private LocalTime timeTo;
}