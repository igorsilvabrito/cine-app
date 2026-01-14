package com.app.cine.controller;

import com.app.cine.dto.room.RoomCreateRequest;
import com.app.cine.dto.room.RoomResponse;
import com.app.cine.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> create(
            @RequestBody @Valid RoomCreateRequest dto
    ) {
        return ResponseEntity.ok(roomService.create(dto));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomResponse>> list() {
        return ResponseEntity.ok(roomService.listActive());
    }
}