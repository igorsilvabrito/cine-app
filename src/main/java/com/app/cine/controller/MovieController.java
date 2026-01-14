package com.app.cine.controller;

import com.app.cine.dto.movie.MovieCreateRequest;
import com.app.cine.dto.movie.MovieResponse;
import com.app.cine.dto.movie.MovieUpdateRequest;
import com.app.cine.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> create(@RequestBody @Valid MovieCreateRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movieService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> list() {
        return ResponseEntity.ok(movieService.listActive());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid MovieUpdateRequest dto
    ) {
        return ResponseEntity.ok(movieService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}