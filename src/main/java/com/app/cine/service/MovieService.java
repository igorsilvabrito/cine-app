package com.app.cine.service;

import com.app.cine.dto.movie.MovieCreateRequest;
import com.app.cine.dto.movie.MovieResponse;
import com.app.cine.dto.movie.MovieUpdateRequest;
import com.app.cine.entity.movies.Movie;
import com.app.cine.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieResponse create(MovieCreateRequest dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.title());
        movie.setDescription(dto.description());
        movie.setDurationMinutes(dto.durationMinutes());
        movie.setRating(dto.rating());
        movie.setActive(true);

        movieRepository.save(movie);
        return toResponse(movie);
    }

    public List<MovieResponse> listActive() {
        return movieRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MovieResponse update(Long id, MovieUpdateRequest dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));

        movie.setTitle(dto.title());
        movie.setDescription(dto.description());
        movie.setDurationMinutes(dto.durationMinutes());
        movie.setRating(dto.rating());

        movieRepository.save(movie);
        return toResponse(movie);
    }

    public void delete(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));

        movie.setActive(false);
        movieRepository.save(movie);
    }

    private MovieResponse toResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationMinutes(),
                movie.getRating()
        );
    }
}