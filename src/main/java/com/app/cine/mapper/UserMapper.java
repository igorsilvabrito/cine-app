package com.app.cine.mapper;

import com.app.cine.dto.user.RegisterRequest;
import com.app.cine.dto.user.UserListingData;
import com.app.cine.entity.user.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import com.app.cine.entity.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User toEntity(RegisterRequest dto, Profile profile) {
        User newUser = new User();
        newUser.setName(dto.name());
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setActive(Boolean.TRUE);
        newUser.getProfiles().add(profile);
        return newUser;

    }
    public UserListingData toDto(User user) {
        return new UserListingData(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getActive()
        );
    }
}
