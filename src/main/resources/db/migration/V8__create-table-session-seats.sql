CREATE TABLE session_seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT fk_session FOREIGN KEY (session_id)
        REFERENCES sessions(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_seat FOREIGN KEY (seat_id)
        REFERENCES seats(id)
        ON DELETE CASCADE,
    UNIQUE KEY unique_session_seat (session_id, seat_id)
);