CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    code VARCHAR(10) NOT NULL,

    room_id BIGINT NOT NULL,

    CONSTRAINT fk_seats_room
        FOREIGN KEY (room_id)
        REFERENCES rooms(id)
        ON DELETE CASCADE
);