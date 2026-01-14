package com.app.cine.repository;

import com.app.cine.entity.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    boolean existsByRoomIdAndStartTime(Long roomId, LocalDateTime startTime);

    List<Session> findByActiveTrue();
}
