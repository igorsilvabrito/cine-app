package com.app.cine.controller;

import com.app.cine.dto.session_seats.SessionSeatResponse;
import com.app.cine.service.SessionSeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions/{sessionId}/seats")
public class SessionSeatController {

    private final SessionSeatService sessionSeatService;

    public SessionSeatController(SessionSeatService sessionSeatService) {
        this.sessionSeatService = sessionSeatService;
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
    public ResponseEntity<Void> reserve(
            @PathVariable Long sessionId,
            @PathVariable Long seatId
    ) {
        sessionSeatService.reserve(sessionId, seatId);
        return ResponseEntity.noContent().build();
    }
}
