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
    private final SeatHoldService seatHoldService;

    public SessionSeatService(SessionSeatRepository sessionSeatRepository, SeatHoldService seatHoldService) {
        this.sessionSeatRepository = sessionSeatRepository;
        this.seatHoldService = seatHoldService;
    }

    public List<SessionSeatResponse> listBySession(Long sessionId) {
        return sessionSeatRepository.findBySessionId(sessionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void reserve(Long sessionId, Long seatId, Long userId) {

        SessionSeat seat = sessionSeatRepository
                .findBySessionIdAndSeatId(sessionId, seatId)
                .orElseThrow(() -> new RuntimeException("Cadeira não encontrada"));

        if (seatHoldService.isHeld(sessionId, seatId)) {
            throw new RuntimeException("Cadeira em processo de reserva");
        }

        boolean held = seatHoldService.holdSeat(sessionId, seatId, userId);

        if (!held) {
            throw new RuntimeException("Cadeira já reservada");
        }

        seat.setStatus(SeatStatus.HELD);
        sessionSeatRepository.save(seat);
    }


    private SessionSeatResponse toResponse(SessionSeat ss) {
        return new SessionSeatResponse(
                ss.getSeat().getId(),
                ss.getSeat().getCode(),
                ss.getStatus().name()
        );
    }
}