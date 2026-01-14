package com.app.cine.controller;

import com.app.cine.dto.session.SessionCreateRequest;
import com.app.cine.dto.session.SessionResponse;
import com.app.cine.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionResponse> create(
            @RequestBody @Valid SessionCreateRequest dto
    ) {
        return ResponseEntity.ok(sessionService.create(dto));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SessionResponse>> list() {
        return ResponseEntity.ok(sessionService.listActive());
    }
}
