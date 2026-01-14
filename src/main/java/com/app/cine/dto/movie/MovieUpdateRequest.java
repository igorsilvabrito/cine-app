package com.app.cine.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovieUpdateRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull Integer durationMinutes,
        @NotBlank String rating
) {}