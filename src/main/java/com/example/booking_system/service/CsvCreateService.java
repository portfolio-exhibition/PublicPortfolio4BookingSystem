package com.example.booking_system.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.booking_system.dto.ExportDTO;
import com.example.booking_system.dto.UserRecords;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;


@Service
public class CsvCreateService {

    public byte[] createCsv(UserRecords records) throws IOException {

        List<ExportDTO> csvList = new ArrayList<>();

        for (int i = 0; i < records.getId().size(); i++) {
            csvList.add(new ExportDTO(
                    records.getId().get(i),
                    records.getName().get(i),
                    records.getFurigana().get(i),
                    records.getPostalCode().get(i),
                    records.getAddress().get(i)
            ));
        }

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(ExportDTO.class).withHeader();

        String csv = mapper.writer(schema)
                .writeValueAsString(csvList);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // UTF-8 の BOM データである 0xEF, 0xBB, 0xBF の3バイトを書き込む
        // CSVファイルのBOM出力：
        // ExcelなどでBOMなしのUTF-8 CSVを開くと日本語が文字化けすることがある
        // ファイルの先頭にこのバイト列を書き込むことで、文字化けを防ぐことができる
        out.write(0xEF);
        out.write(0xBB);
        out.write(0xBF);

        out.write(csv.getBytes(StandardCharsets.UTF_8));

        return out.toByteArray();
    }
}
