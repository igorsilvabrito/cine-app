CREATE TABLE tickets (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         session_id BIGINT NOT NULL,
                         session_seat_id BIGINT NOT NULL,
                         price DECIMAL(10,2) NOT NULL,
                         purchased_at DATETIME NOT NULL,

                         CONSTRAINT fk_ticket_user FOREIGN KEY (user_id)
                             REFERENCES users(id),

                         CONSTRAINT fk_ticket_session FOREIGN KEY (session_id)
                             REFERENCES sessions(id),

                         CONSTRAINT fk_ticket_session_seat FOREIGN KEY (session_seat_id)
                             REFERENCES session_seats(id),

                         UNIQUE KEY uk_ticket_seat (session_seat_id)
);
