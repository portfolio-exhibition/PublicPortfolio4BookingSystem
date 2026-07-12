package com.example.booking_system.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserRecords {
	List<Integer> id;

	List<String> name;

	List<String> furigana;

	List<String> postalCode;

	List<String> address;
	
	//List<String> phoneNumber;

	//List<String> email;
}
