package com.app.cine.dto.session_seats;

public record SessionSeatResponse(
        Long id,
        String seatCode,
        String status
) {}
