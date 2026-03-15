package com.app.cine.controller;

import com.app.cine.entity.user.User;
import com.app.cine.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions/{sessionId}/seats/{seatId}/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> purchase(
            @PathVariable Long sessionId,
            @PathVariable Long seatId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        ticketService.purchase(sessionId, seatId, user);

        return ResponseEntity.noContent().build();
    }
}
