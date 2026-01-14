package com.app.cine.repository;

import com.app.cine.entity.user.Profile;
import com.app.cine.entity.user.ProfileName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByName(ProfileName profileName);
}
