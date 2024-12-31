package com.acme.booking.repository;

import com.acme.booking.domain.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByMeetingRoomIdAndDate(UUID meetingRoomId, LocalDate date);

    /**
     * Checks if a booking exists for the specified meeting room, date, and time range that overlaps with another booking.
     *
     * <p>This query performs the following checks:
     * <ul>
     *     <li>Counts the number of bookings (`b`) that satisfy the following conditions:</li>
     *     <ul>
     *         <li>The meeting room ID matches the ID of the meeting room in the given {@link Booking} object.</li>
     *         <li>The date matches the date of the given {@link Booking} object.</li>
     *         <li>The time range of the given {@link Booking} object overlaps with the time range of an existing booking.</li>
     *     </ul>
     * </ul>
     *
     * @param booking the booking object containing the meeting room, date, and time range to check for overlap
     * @return {@code true} if an overlapping booking exists, otherwise {@code false}
     */
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.meetingRoom.id = :#{#booking.meetingRoom.id} " +
            "AND b.date = :#{#booking.date} " +
            "AND (:#{#booking.timeFrom} < b.timeTo AND :#{#booking.timeTo} > b.timeFrom)")
    boolean existsByMeetingRoomIdAndDateAndTimeOverlap(Booking booking);
}
