package com.acme.booking.repository;

import com.acme.booking.domain.model.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
