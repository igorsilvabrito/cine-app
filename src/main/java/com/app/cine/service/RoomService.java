package com.app.cine.service;

import com.app.cine.dto.room.RoomCreateRequest;
import com.app.cine.dto.room.RoomResponse;
import com.app.cine.entity.room.Room;
import com.app.cine.entity.seats.Seat;
import com.app.cine.repository.RoomRepository;
import com.app.cine.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public RoomResponse create(RoomCreateRequest dto) {

        Room room = new Room();
        room.setName(dto.name());
        room.setTotalRows(dto.rows());
        room.setTotalColumns(dto.columns());

        roomRepository.save(room); // 1️⃣ salva primeiro

        generateSeats(room);       // 2️⃣ agora sim gera cadeiras

        return toResponse(room);
    }
    public List<RoomResponse> listActive() {
        return roomRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getTotalRows(),
                room.getTotalColumns()
        );
    }

    private void generateSeats(Room room) {
        List<Seat> seats = new ArrayList<>();

        for (int row = 0; row < room.getTotalRows(); row++) {
            char rowLetter = (char) ('A' + row);

            for (int col = 1; col <= room.getTotalColumns(); col++) {
                Seat seat = new Seat();
                seat.setCode(rowLetter + String.valueOf(col));
                seat.setRoom(room);
                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }
}
