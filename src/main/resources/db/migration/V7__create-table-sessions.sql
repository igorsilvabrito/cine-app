CREATE TABLE sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    movie_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,

    start_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,

    active BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_session_movie
        FOREIGN KEY (movie_id) REFERENCES movies(id),

    CONSTRAINT fk_session_room
        FOREIGN KEY (room_id) REFERENCES rooms(id),

    UNIQUE (room_id, start_time)
);