package com.example.booking_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"ユーザーID", "名前", "フリガナ", "郵便番号", "住所", "電話番号", "メールアドレス"})
public class ExportDTO {
	@JsonProperty("ユーザーID")
	private Integer id;

	@JsonProperty("名前")
	private String name;

	@JsonProperty("フリガナ")
	private String furigana;

	@JsonProperty("郵便番号")
	private String postalCode;

	@JsonProperty("住所")
	private String address;
	
	@JsonProperty("電話番号")
	private String phoneNumber;

	@JsonProperty("メールアドレス")
	private String email;
	
	ExportDTO () {}
	
	public ExportDTO (Integer id, String name, String furigana, String postalCode, String address, String phoneNumber, String email) {
	    this.id = id;
	    this.name = name;
	    this.furigana = furigana;
	    this.postalCode = postalCode;
	    this.address = address;
	    this.phoneNumber = phoneNumber;
	    this.email = email;
	}
}
