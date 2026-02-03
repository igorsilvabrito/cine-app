package com.app.cine.service;

import com.app.cine.dto.session_seats.SessionSeatResponse;
import com.app.cine.entity.session.SeatStatus;
import com.app.cine.entity.session.SessionSeat;
import com.app.cine.entity.user.User;
import com.app.cine.repository.SessionSeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionSeatService {

    private final SessionSeatRepository sessionSeatRepository;
    private final SeatHoldService seatHoldService;

    public SessionSeatService(
            SessionSeatRepository sessionSeatRepository,
            SeatHoldService seatHoldService
    ) {
        this.sessionSeatRepository = sessionSeatRepository;
        this.seatHoldService = seatHoldService;
    }

    public List<SessionSeatResponse> listBySession(Long sessionId) {
        return sessionSeatRepository.findBySessionId(sessionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void reserve(Long sessionId, Long seatId) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) auth.getPrincipal();
        Long userId = user.getId();

        if (!seatHoldService.isHeld(sessionId, seatId)) {
            throw new RuntimeException("Assento não está em hold");
        }

        String holder = seatHoldService.getHolder(sessionId, seatId);

        if (!holder.equals(userId.toString())) {
            throw new RuntimeException("Assento está segurado por outro usuário");
        }

        SessionSeat sessionSeat = sessionSeatRepository
                .findBySessionIdAndSeatId(sessionId, seatId)
                .orElseThrow(() ->
                        new RuntimeException("Cadeira não encontrada")
                );

        if (sessionSeat.getStatus() != SeatStatus.AVAILABLE) {
            throw new RuntimeException("Cadeira indisponível");
        }

        sessionSeat.setStatus(SeatStatus.RESERVED);

        seatHoldService.releaseSeat(sessionId, seatId);
    }



    private SessionSeatResponse toResponse(SessionSeat ss) {
        return new SessionSeatResponse(
                ss.getSeat().getId(),
                ss.getSeat().getCode(),
                ss.getStatus().name()
        );
    }
}