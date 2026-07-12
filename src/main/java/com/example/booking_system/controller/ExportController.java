package com.example.booking_system.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.booking_system.dto.UserRecords;
import com.example.booking_system.service.CsvCreateService;


@Controller
@RequestMapping("/export")
public class ExportController {
	
	private final CsvCreateService csvCreateService;

    public ExportController(CsvCreateService csvCreateService) {
        this.csvCreateService = csvCreateService;
    }

    @PostMapping(value = "/csv", produces = "text/csv")
    public ResponseEntity<byte[]> csvDownload(@ModelAttribute("csvForm") UserRecords records) throws IOException {

        byte[] csv = csvCreateService.createCsv(records);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));

        headers.setContentDisposition(ContentDisposition.attachment()
                                                        .filename("records.csv", StandardCharsets.UTF_8)
                                                        .build());

        return ResponseEntity.ok()
                             .headers(headers)
                             .body(csv);
    }
}
