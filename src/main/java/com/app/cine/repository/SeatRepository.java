package com.app.cine.repository;

import com.app.cine.entity.seats.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
