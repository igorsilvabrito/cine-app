package com.app.cine.entity.session;

import com.app.cine.entity.seats.Seat;
import com.app.cine.entity.session.Session;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "session_seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SessionSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Session session;

    @ManyToOne
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;
}
