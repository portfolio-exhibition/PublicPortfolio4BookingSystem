package com.example.booking_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking_system.entity.Reservation;
import com.example.booking_system.entity.User;


public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	public Page<Reservation> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	public Reservation findFirstByOrderByIdDesc();
}