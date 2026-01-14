CREATE TABLE users_profiles (
    user_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, profile_id),

    CONSTRAINT fk_users_profiles_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_users_profiles_profile
        FOREIGN KEY (profile_id) REFERENCES profiles(id)
        ON DELETE CASCADE
);