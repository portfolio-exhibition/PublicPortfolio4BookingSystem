package com.example.booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking_system.entity.VerificationToken;


public interface VerificationTokenRepository extends JpaRepository< VerificationToken, Integer> {
    public VerificationToken findByToken(String token);
}