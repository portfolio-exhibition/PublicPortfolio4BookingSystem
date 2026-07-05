package com.example.booking_system.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.booking_system.dto.ExportRecords;

@Controller
@RequestMapping("/export")
public class ExportController {
	
	@PostMapping("/csv")
	public ResponseEntity<byte[]> exportCSV(@ModelAttribute("csvForm") ExportRecords records) throws IOException {
		
		byte[] csv = {0,1};

		return ResponseEntity.ok()
	            .header(
	                HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"employee.csv\"")
	            .contentType(
	                MediaType.parseMediaType("text/csv;charset=UTF-8"))
	            .body(csv);
		//return "admin/users/index";
	}
}
