package com.example.booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking_system.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Role findByName(String name);
}