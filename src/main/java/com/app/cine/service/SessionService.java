package com.app.cine.service;

import com.app.cine.dto.session.SessionCreateRequest;
import com.app.cine.dto.session.SessionResponse;
import com.app.cine.entity.movies.Movie;
import com.app.cine.entity.room.Room;
import com.app.cine.entity.seats.Seat;
import com.app.cine.entity.session.SeatStatus;
import com.app.cine.entity.session.Session;
import com.app.cine.entity.session.SessionSeat;
import com.app.cine.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final SessionSeatRepository sessionSeatRepository;

    public SessionService(
            SessionRepository sessionRepository,
            MovieRepository movieRepository,
            RoomRepository roomRepository,
            SeatRepository seatRepository,
            SessionSeatRepository sessionSeatRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
        this.sessionSeatRepository = sessionSeatRepository;
    }

    @Transactional
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

        generateSessionSeats(session);

        return toResponse(session);
    }

    private void generateSessionSeats(Session session) {
        List<Seat> seats = seatRepository.findByRoomId(session.getRoom().getId());

        List<SessionSeat> sessionSeats = seats.stream().map(seat -> {
            SessionSeat ss = new SessionSeat();
            ss.setSession(session);
            ss.setSeat(seat);
            ss.setStatus(SeatStatus.AVAILABLE);
            return ss;
        }).toList();

        sessionSeatRepository.saveAll(sessionSeats);
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
