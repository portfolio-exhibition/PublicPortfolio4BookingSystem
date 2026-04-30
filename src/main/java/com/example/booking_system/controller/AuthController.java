package com.example.booking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

     @GetMapping("/login")
     public String login() {
         // ログイン画面のビューを表示
         return "auth/login";
     }
}