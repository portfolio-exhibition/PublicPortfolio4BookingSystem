package com.example.booking_system.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class mFileReader {

	// .txt(UTF-8)から１行読み込み文字列を返すメソッド
	public String ReadText(String path_name) {
		File file = new File(path_name);
		String text = "Not Text";
		
		//プログラムを短くするのと、closeを忘れないためにも、Java 7以降で使えるtry-with-resourcesと組み合わせるのが鉄板
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			text = br.readLine();
		} catch(IOException e) {
	            e.printStackTrace();
		}
		
		//System.out.println("text の内容　　　　 ：" + text);
		return text;
	}
}
