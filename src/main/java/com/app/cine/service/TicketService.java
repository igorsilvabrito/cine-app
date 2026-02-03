package com.app.cine.service;

import com.app.cine.entity.session.SeatStatus;
import com.app.cine.entity.session.SessionSeat;
import com.app.cine.entity.ticket.Ticket;
import com.app.cine.entity.user.User;
import com.app.cine.repository.SessionSeatRepository;
import com.app.cine.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SessionSeatRepository sessionSeatRepository;
    private final SeatHoldService seatHoldService;

    public TicketService(
            TicketRepository ticketRepository,
            SessionSeatRepository sessionSeatRepository,
            SeatHoldService seatHoldService
    ) {
        this.ticketRepository = ticketRepository;
        this.sessionSeatRepository = sessionSeatRepository;
        this.seatHoldService = seatHoldService;
    }

    @Transactional
    public void purchase(Long sessionId, Long seatId, User user) {

        if (!seatHoldService.isHeld(sessionId, seatId)) {
            throw new RuntimeException("Assento não está em hold");
        }

        String holder = seatHoldService.getHolder(sessionId, seatId);

        if (!holder.equals(user.getId().toString())) {
            throw new RuntimeException("Assento pertence a outro usuário");
        }

        SessionSeat sessionSeat = sessionSeatRepository
                .findBySessionIdAndSeatId(sessionId, seatId)
                .orElseThrow(() -> new RuntimeException("Cadeira não encontrada"));

        if (sessionSeat.getStatus() != SeatStatus.RESERVED) {
            throw new RuntimeException("Assento não está reservado");
        }

        sessionSeat.setStatus(SeatStatus.RESERVED);

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setSession(sessionSeat.getSession());
        ticket.setSessionSeat(sessionSeat);
        ticket.setPrice(sessionSeat.getSession().getPrice());
        ticket.setPurchasedAt(LocalDateTime.now());

        ticketRepository.save(ticket);

        seatHoldService.releaseSeat(sessionId, seatId);
    }
}
