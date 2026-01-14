package com.app.cine.service;

import com.app.cine.dto.session_seats.SessionSeatResponse;
import com.app.cine.entity.session.SeatStatus;
import com.app.cine.entity.session.SessionSeat;
import com.app.cine.repository.SessionSeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionSeatService {

    private final SessionSeatRepository sessionSeatRepository;

    public SessionSeatService(SessionSeatRepository sessionSeatRepository) {
        this.sessionSeatRepository = sessionSeatRepository;
    }

    public List<SessionSeatResponse> listBySession(Long sessionId) {
        return sessionSeatRepository.findBySessionId(sessionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void reserve(Long sessionId, Long seatId) {

        SessionSeat sessionSeat = sessionSeatRepository
                .findBySessionIdAndSeatId(sessionId, seatId)
                .orElseThrow(() -> new RuntimeException("Cadeira não encontrada para esta sessão"));

        if (sessionSeat.getStatus() != SeatStatus.AVAILABLE) {
            throw new RuntimeException("Cadeira indisponível");
        }

        sessionSeat.setStatus(SeatStatus.RESERVED);
    }

    private SessionSeatResponse toResponse(SessionSeat ss) {
        return new SessionSeatResponse(
                ss.getSeat().getId(),
                ss.getSeat().getCode(),
                ss.getStatus().name()
        );
    }
}