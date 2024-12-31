package com.acme.booking.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MeetingRoomDto {
    private UUID id;
    @NotNull
    private String name;
}
