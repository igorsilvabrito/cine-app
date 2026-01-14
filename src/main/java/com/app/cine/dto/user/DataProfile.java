package com.app.cine.dto.user;


import com.app.cine.entity.user.ProfileName;
import jakarta.validation.constraints.NotNull;

public record DataProfile(@NotNull ProfileName profileName) {
}
