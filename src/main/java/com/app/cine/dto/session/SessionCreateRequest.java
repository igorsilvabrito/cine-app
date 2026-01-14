package com.app.cine.dto.session;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionCreateRequest(
        @NotNull Long movieId,
        @NotNull Long roomId,
        @NotNull LocalDateTime startTime,
        @NotNull BigDecimal price
) {}
