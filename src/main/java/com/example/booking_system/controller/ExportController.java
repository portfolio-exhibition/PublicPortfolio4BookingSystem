package com.example.booking_system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.booking_system.dto.ExportDTO;
import com.example.booking_system.dto.ExportRecords;

@Controller
@RequestMapping("/export")
public class ExportController {
	
	@PostMapping("/csv")
	public ResponseEntity<List<ExportDTO>> exportCSV(@ModelAttribute("csvForm") ExportRecords records) throws IOException {
		
		System.out.println("ExportController->csv:" + records.getId());
		byte[] csv = {0,1};
		List<ExportDTO> csvList = new ArrayList<>();
		for (int i = 0; i < records.getId().size(); i++) { // レコードの数ぶんだけループ回して
		      //csvList.add(new ExportDTO(records.getId().get(i), records.getName().get(i), records.getFurigana().get(i), records.getPostalCode().get(i), records.getAddress().get(i), records.getPhoneNumber().get(i), records.getEmail().get(i)));
		      csvList.add(new ExportDTO(records.getId().get(i), records.getName().get(i), records.getFurigana().get(i), null, null, null, null));
		    }

		return ResponseEntity.ok()
	            .header(
	                HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"users.csv\"")
	            .contentType(
	                MediaType.parseMediaType("text/csv;charset=UTF-8"))
	            .body(csvList);
	}
}
