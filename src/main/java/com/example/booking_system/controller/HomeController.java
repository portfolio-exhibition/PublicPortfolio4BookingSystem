package com.example.booking_system.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.booking_system.entity.House;
import com.example.booking_system.service.HouseService;


@Controller
public class HomeController {
	private final HouseService houseService;

    public HomeController(HouseService houseService) {
        this.houseService = houseService;
    }
    
    @GetMapping("/")
    public String index(Model model) {
        List<House> newHouses = houseService.findTop8HousesByOrderByCreatedAtDesc();
        List<House> popularHouses = houseService.findTop3HousesByOrderByReservationCountDesc();
        model.addAttribute("newHouses", newHouses);
        model.addAttribute("popularHouses", popularHouses);
        
        return "index";
    }
}