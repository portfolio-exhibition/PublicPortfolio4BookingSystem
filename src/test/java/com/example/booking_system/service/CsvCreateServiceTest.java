/*
 *  Coding by ChatGPT
**/
package com.example.booking_system.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.booking_system.dto.UserRecords;

@SpringBootTest
class CsvCreateServiceTest {

    @Autowired
    private CsvCreateService csvCreateService;

    @Test
    void createCsv_正常系() throws Exception {

        // Arrange
        UserRecords records = new UserRecords();

        records.setId(List.of(1));
        records.setName(List.of("山田 太郎"));
        records.setFurigana(List.of("ヤマダ タロウ"));
        records.setPostalCode(List.of("123-4567"));
        records.setAddress(List.of("東京都新宿区"));

        // Act
        byte[] result = csvCreateService.createCsv(records);

        // Assert
        assertNotNull(result);

        // BOM確認
        assertEquals((byte) 0xEF, result[0]);
        assertEquals((byte) 0xBB, result[1]);
        assertEquals((byte) 0xBF, result[2]);

        // BOMを除いて文字列化
        String csv = new String(result, 3, result.length - 3, StandardCharsets.UTF_8);

        // ヘッダ確認
        assertTrue(csv.contains("ユーザーID"));
        assertTrue(csv.contains("名前"));
        assertTrue(csv.contains("フリガナ"));
        assertTrue(csv.contains("郵便番号"));
        assertTrue(csv.contains("住所"));

        // データ確認
        assertTrue(csv.contains("1"));
        assertTrue(csv.contains("山田 太郎"));
        assertTrue(csv.contains("ヤマダ タロウ"));
        assertTrue(csv.contains("123-4567"));
        assertTrue(csv.contains("東京都新宿区"));
    }

    @Test
    void createCsv_複数件() throws IOException {

        UserRecords records = new UserRecords();

        records.setId(List.of(1, 2));
        records.setName(List.of("山田 太郎", "佐藤 花子"));
        records.setFurigana(List.of("ヤマダ タロウ", "サトウ ハナコ"));
        records.setPostalCode(List.of("111-1111", "222-2222"));
        records.setAddress(List.of("東京都", "大阪府"));

        byte[] result = csvCreateService.createCsv(records);

        String csv = new String(result, 3, result.length - 3, StandardCharsets.UTF_8);

        assertTrue(csv.contains("山田 太郎"));
        assertTrue(csv.contains("佐藤 花子"));
        assertTrue(csv.contains("東京都"));
        assertTrue(csv.contains("大阪府"));
    }

    @Test
    void createCsv_空データ() throws IOException {

        UserRecords records = new UserRecords();

        records.setId(List.of());
        records.setName(List.of());
        records.setFurigana(List.of());
        records.setPostalCode(List.of());
        records.setAddress(List.of());

        byte[] result = csvCreateService.createCsv(records);

        assertNotNull(result);

        String csv = new String(result, 3, result.length - 3, StandardCharsets.UTF_8);

        // ヘッダのみ出力されること
        assertTrue(csv.contains("ユーザーID"));
        assertFalse(csv.contains("山田"));
    }

}
