package com.acme.booking.utils;

import com.acme.booking.domain.model.MeetingRoom;

import java.util.UUID;

public class MeetingRoomUtils {

    public static String DEFAULT_MEETING_ROOM_NAME = "Meeting Room";

    public static MeetingRoom generateMeetingRoomWithId(String name){
        return generateMeetingRoomWithoutId(name).toBuilder().id(UUID.randomUUID()).build();
    }

    public static MeetingRoom generateMeetingRoomWithoutId(String name){
        return MeetingRoom.builder().name(name).build();
    }
}
