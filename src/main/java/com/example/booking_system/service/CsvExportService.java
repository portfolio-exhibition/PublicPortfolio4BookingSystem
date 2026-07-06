package com.example.booking_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.booking_system.dto.ExportDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Service
public class CsvExportService {
	@Autowired
    private final CsvMapper mapper;

    @Autowired
    private CsvSchema csvSchema;

    public CsvExportService(CsvMapper mapper, CsvSchema csvSchema) {
        this.csvSchema = csvSchema;
        this.mapper = mapper;
    }

    //ExportDTO からCsvファイルのヘッダー情報を取得
    public CsvSchema getCsvHeader() {
        // ExportDTO の@Jsonpropertyの文字列をヘッダーとして書き込む
        csvSchema = mapper.schemaFor(ExportDTO.class).withHeader();
        return csvSchema;
    }

    // csvファイルの内容をString型で作成します。
    // 引数のcsvDataListはデータ部分、schemaはヘッダー部分
    public String WriteCsvText(List<ExportDTO> userRecordsList, CsvSchema csvSchema) throws JsonProcessingException {
        return mapper.writer(csvSchema).writeValueAsString(userRecordsList);
    }
}
