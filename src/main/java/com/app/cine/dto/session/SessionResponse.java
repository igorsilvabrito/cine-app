package com.app.cine.dto.session;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionResponse(
        Long id,
        String movieTitle,
        String roomName,
        LocalDateTime startTime,
        BigDecimal price
) {}
