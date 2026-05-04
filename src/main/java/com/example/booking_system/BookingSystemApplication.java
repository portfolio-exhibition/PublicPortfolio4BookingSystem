package com.example.booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// データベースの無効化を解除する時に(exclude = {DataSourceAutoConfiguration.class})を削除
@SpringBootApplication   //(exclude = {DataSourceAutoConfiguration.class})
public class BookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingSystemApplication.class, args);
	}

}
