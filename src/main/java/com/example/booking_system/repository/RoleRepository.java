package com.example.booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking_system.entity.Role;

/*
　JpaRepositoryインターフェースを継承するだけで、基本的なCRUD操作を行うためのメソッドが利用可能になります。
　以下はその一例です。
　・findAll()：テーブル内のすべてのエンティティを取得する
　・findById()：引数に指定したidのエンティティを取得する
　・save()：引数に指定したエンティティを保存または更新する
　・delete()：引数に指定したエンティティを 削除 する
　・deleteById()：引数に指定したidのエンティティを 削除 する
　なお、JpaRepositoryは依存関係に追加したSpring Data JPA（データベースとのやり取りを簡単にするフレームワーク）によって提供されるインターフェースです。
　roles テーブルとやり取りする認可用のリポジトリの場合、第1引数（エンティティのクラス型）はRole、第2引数（主キーのデータ型）はIntegerです。
　※Spring Data JPAの公式リファレンスに記載されているキーワードを使った独自のメソッドを追加することで、基本的なCRUD操作以外にも高度なクエリを実行できるようになります。
**/
public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Role findByName(String name);
}