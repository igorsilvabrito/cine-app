package com.app.cine.service;

import com.app.cine.dto.session.SessionCreateRequest;
import com.app.cine.dto.session.SessionResponse;
import com.app.cine.entity.movies.Movie;
import com.app.cine.entity.room.Room;
import com.app.cine.entity.session.Session;
import com.app.cine.repository.MovieRepository;
import com.app.cine.repository.RoomRepository;
import com.app.cine.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public SessionService(
            SessionRepository sessionRepository,
            MovieRepository movieRepository,
            RoomRepository roomRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    public SessionResponse create(SessionCreateRequest dto) {

        if (sessionRepository.existsByRoomIdAndStartTime(
                dto.roomId(), dto.startTime())) {
            throw new RuntimeException("Já existe sessão nessa sala nesse horário");
        }

        Movie movie = movieRepository.findById(dto.movieId())
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));

        Room room = roomRepository.findById(dto.roomId())
                .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

        Session session = new Session();
        session.setMovie(movie);
        session.setRoom(room);
        session.setStartTime(dto.startTime());
        session.setPrice(dto.price());

        sessionRepository.save(session);

        return toResponse(session);
    }

    public List<SessionResponse> listActive() {
        return sessionRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private SessionResponse toResponse(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getMovie().getTitle(),
                session.getRoom().getName(),
                session.getStartTime(),
                session.getPrice()
        );
    }
}
