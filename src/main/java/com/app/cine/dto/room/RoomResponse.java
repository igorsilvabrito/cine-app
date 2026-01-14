package com.app.cine.dto.room;

public record RoomResponse(
        Long id,
        String name,
        Integer rows,
        Integer columns
) {}