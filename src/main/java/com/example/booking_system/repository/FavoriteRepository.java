package com.example.booking_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking_system.entity.Favorite;
import com.example.booking_system.entity.House;
import com.example.booking_system.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
   public Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
   public Favorite findByHouseAndUser(House house, User user);
}