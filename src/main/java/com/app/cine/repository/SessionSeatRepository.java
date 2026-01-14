package com.app.cine.repository;

import com.app.cine.entity.session.SessionSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionSeatRepository extends JpaRepository<SessionSeat, Long> {

    Optional<SessionSeat> findBySessionIdAndSeatId(Long sessionId, Long seatId);
    List<SessionSeat> findBySessionId(Long sessionId);

}
