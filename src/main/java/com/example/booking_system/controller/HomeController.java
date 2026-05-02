package com.example.booking_system.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.booking_system.entity.House;
import com.example.booking_system.service.HouseService;

//クラスに@Controllerアノテーションをつけることで、そのクラスがコントローラとして機能するようになります。
@Controller
public class HomeController {
	private final HouseService houseService;

    public HomeController(HouseService houseService) {
        this.houseService = houseService;
    }
    
	/*
	　コントローラ内のメソッドに@GetMappingアノテーションをつけることで、HTTPリクエストのGETメソッドをそのメソッドにマッピング（対応づけ） できます。
	　なお、引数にはマッピングするルートパス（ドメイン名を省略したパス）を指定します。
	　　【HTTPリクエストメソッド：説明】
　　　　　GET　 ：単純にページを表示する場合など、サーバーから情報を取得するために使う。
　　　　　POST　：フォームの入力内容を送信してデータの作成や更新を行う場合など、サーバー上のデータを変更するために使う。
	**/
    @GetMapping("/")
    public String index(Model model) {
        List<House> newHouses = houseService.findTop8HousesByOrderByCreatedAtDesc();
        List<House> popularHouses = houseService.findTop3HousesByOrderByReservationCountDesc();
        model.addAttribute("newHouses", newHouses);
        model.addAttribute("popularHouses", popularHouses);
        
    	/*
    	　メソッドの最後では、呼び出すビューのパス（src/main/resources/templates/以降のパス）をreturnで返します。
    	　なお、拡張子の.htmlは省略する点に注意してください。
    	　return "index";と記述すれば、src/main/resources/templates/index.htmlファイルが呼び出されます。
    	**/
        return "index";
    }
}