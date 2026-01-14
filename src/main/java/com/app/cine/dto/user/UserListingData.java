package com.app.cine.dto.user;

public record UserListingData(
        Long id,
        String name,
        String email,
        Boolean active
) {}