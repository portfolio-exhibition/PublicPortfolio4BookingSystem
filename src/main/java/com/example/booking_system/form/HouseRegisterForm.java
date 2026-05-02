package com.example.booking_system.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/* 民宿画像のフィールドはMultipartFile型で定義する
　民宿登録ページでは、input要素にtype="file"を設定してファイルをアップロードするための入力欄を作成します。
　そして、その入力欄に対応するのがimageFileフィールドです。
　imageFileフィールドはMultipartFile型で定義します。
　MultipartFileはSpring Boot側が用意しているインターフェースで、これを利用することでフォームから送信されたファイルをアプリ側で簡単に処理できるようになります。
　具体的には、ファイル名やサイズ、ファイルの内容など、ファイルの情報を取得するためのさまざまなメソッドを提供してくれます。
 */
@Data
public class HouseRegisterForm {
    @NotBlank(message = "民宿名を入力してください。")
    private String name;

    // 民宿画像のフィールドはMultipartFile型で定義する
    private MultipartFile imageFile;

    @NotBlank(message = "説明を入力してください。")
    private String description;

    @NotNull(message = "宿泊料金を入力してください。")
    @Min(value = 1, message = "宿泊料金は1円以上に設定してください。")
    private Integer price;

    @NotNull(message = "定員を入力してください。")
    @Min(value = 1, message = "定員は1人以上に設定してください。")
    private Integer capacity;

    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;

    @NotBlank(message = "住所を入力してください。")
    private String address;

    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;
}