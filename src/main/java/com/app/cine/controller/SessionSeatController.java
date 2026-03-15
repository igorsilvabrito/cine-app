package com.app.cine.controller;

import com.app.cine.dto.session_seats.SessionSeatResponse;
import com.app.cine.entity.user.User;
import com.app.cine.service.SeatHoldService;
import com.app.cine.service.SessionSeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions/{sessionId}/seats")
public class SessionSeatController {

    private final SessionSeatService sessionSeatService;
    private final SeatHoldService seatHoldService;

    public SessionSeatController(SessionSeatService sessionSeatService, SeatHoldService seatHoldService) {
        this.sessionSeatService = sessionSeatService;
        this.seatHoldService = seatHoldService;
    }

    @GetMapping
    public ResponseEntity<List<SessionSeatResponse>> list(
            @PathVariable Long sessionId
    ) {
        return ResponseEntity.ok(
                sessionSeatService.listBySession(sessionId)
        );
    }

    @PatchMapping("/{seatId}/reserve")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> reserve(
            @PathVariable Long sessionId,
            @PathVariable Long seatId
    ) {
        sessionSeatService.reserve(sessionId, seatId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{seatId}/hold")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> hold(
            @PathVariable Long sessionId,
            @PathVariable Long seatId
    ) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) auth.getPrincipal();
        Long userId = user.getId();

        boolean success = seatHoldService
                .holdSeat(sessionId, seatId, userId);

        if (!success) {
            return ResponseEntity.status(409).build(); // já está em hold
        }

        return ResponseEntity.noContent().build();
    }


}
