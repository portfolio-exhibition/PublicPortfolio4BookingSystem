package com.example.booking_system.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;


// DTOとはData Transfer Objectの略で、その名のとおりデータを運ぶためのオブジェクトのこと
// DTO（データ転送オブジェクト）は、レイヤー間（Controller ⇔ Service ⇔ Repository）でデータをやりとりするためのオブジェクト です。
/* DTOを使う理由
* 1.エンティティ（DBモデル）とAPIレスポンスを分離できる
* 2.セキュリティ（パスワードなどの機密情報を隠せる）
* 3.APIのリクエストやレスポンスの形式を統一できる
* 4.拡張性が高まり、メンテナンスしやすくなる
**/
// DTOを使わずに、エンティティ（DBモデル）を直接APIのレスポンスに使う のは、一般的に避けるべきです。
@Data
@AllArgsConstructor
public class ReservationDTO {
    private Integer houseId;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private Integer numberOfPeople;

    private Integer amount;
}