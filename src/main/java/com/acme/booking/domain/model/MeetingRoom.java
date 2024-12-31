package com.acme.booking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
    indexes = {
            @Index(name = "ix_meeting_room_name", columnList = "name")
    }
)
public class MeetingRoom {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;

    public MeetingRoom(String name) {
        this.name = name;
    }
}