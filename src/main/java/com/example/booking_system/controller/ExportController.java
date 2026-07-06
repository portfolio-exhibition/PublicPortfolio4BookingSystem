package com.example.booking_system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.booking_system.dto.ExportDTO;
import com.example.booking_system.dto.UserRecords;
import com.example.booking_system.helper.DownloadHelper;
import com.example.booking_system.service.CsvExportService;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Controller
@RequestMapping("/export")
public class ExportController {
	
	@Autowired
	CsvExportService csvExportService;
	
	@Autowired
	DownloadHelper downloadHelper;
	
	@PostMapping("/csv")
	public ResponseEntity<byte[]> exportCSV(@ModelAttribute("csvForm") UserRecords records) throws IOException {
		CsvSchema csvSchema;
        List<ExportDTO> userRecordsList = new ArrayList<>();
        ExportDTO userRecords = new ExportDTO(1, "admin", "管理者", "445-0082", "西尾市");
        userRecordsList.add(userRecords);
        HttpHeaders headers = new HttpHeaders();
        downloadHelper.addContentDisposition(headers, "ユーザーリスト.csv");
        System.out.print(csvExportService.getCsvHeader());

        csvSchema = csvExportService.getCsvHeader();
        return new ResponseEntity<>(csvExportService.WriteCsvText(userRecordsList, csvSchema).getBytes("MS932"), headers, HttpStatus.OK);
	}
}
