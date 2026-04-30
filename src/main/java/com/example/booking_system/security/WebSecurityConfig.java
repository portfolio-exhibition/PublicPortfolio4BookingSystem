package com.example.booking_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests  // アクセス許可に関する設定
                .requestMatchers("/hello", "/login", "/resources/**").permitAll()  // 全ユーザーにアクセスを許可するURL
                .anyRequest().authenticated()  // 上記以外のURLはログインが必要（ロールを問わない）
            )
            .formLogin((form) -> form  // ログインに関する設定
                .loginPage("/login")              // ログインページのURL
                .loginProcessingUrl("/login")     // ログインフォームの送信先URL
                .defaultSuccessUrl("/adminuser?loggedIn")  // ログイン成功時のリダイレクト先URL
                .failureUrl("/login?error")       // ログイン失敗時のリダイレクト先URL
                .permitAll()
            )
            .logout((logout) -> logout  // ログアウトに関する設定
                .logoutSuccessUrl("/?loggedOut")  // ログアウト時のリダイレクト先URL
                .permitAll()
            );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}