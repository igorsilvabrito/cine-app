package com.app.cine.dto.room;


public record RoomCreateRequest(
        String name,
        Integer rows,
        Integer columns
) {}