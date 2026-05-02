package com.example.booking_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


/* 【クラスのアノテーションについて】
　@Entity ：クラスに@Entityアノテーションをつけることで、そのクラスがエンティティとして機能するようになります。
　@Table　：@Tableアノテーションをつけることで、そのエンティティにマッピング（対応づけ）されるテーブル名を指定できます。
　　　　　　例えば@Table(name = "roles")と指定すれば、rolesテーブルがそのエンティティにマッピングされます。
　@Data　 ：プロジェクトを作成した際にLombokという依存関係を追加しましたが、@DataはそのLombokが提供するアノテーションです。
　　　　　　クラスに@Dataアノテーションをつけることで、ゲッターやセッターなどを自動生成できます。
 **/
@Entity
@Table(name = "roles")
@Data
public class Role {
   /*
   　@Column　　　　 　:各フィールドに@Columnアノテーションをつけることで、そのフィールドにマッピングされるカラム名を指定できます。
   　　　　　　　　　　 例えば@Column(name = "id")と指定すれば、idカラムがそのフィールドにマッピングされます。 
   　@Id　　 　　　　 ：フィールドに@Idアノテーションをつけることで、そのフィールドを主キーに指定できます。
　　 @GeneratedValue　：また、@GeneratedValueアノテーションをつけてstrategy = GenerationType.IDENTITYを指定することで、
　　　　　　　　　　　　テーブル内のAUTO_INCREMENTを指定したカラム（idカラム）を利用して値を生成するようになります。
　　　　　　　　　　　　つまり、データの作成時や更新時にidの値を自分で指定しなくても、自動採番されるようになるということです。
   **/
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Integer id;
       
   @Column(name = "name")
   private String name;  
}