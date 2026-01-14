package com.app.cine.dto.movie;

public record MovieResponse(
        Long id,
        String title,
        String description,
        Integer durationMinutes,
        String rating
) {}