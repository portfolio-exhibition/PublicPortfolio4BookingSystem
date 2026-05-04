package com.example.booking_system.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.booking_system.entity.User;
import com.example.booking_system.repository.UserRepository;

/* ポイントは以下の5つです。
1.クラスに@Serviceアノテーションをつける
2.UserDetailsServiceインターフェースを実装する
3.コンストラクタで依存性の注入（DI）を行う（コンストラクタインジェクション）
4.loadUserByUsername()メソッドを上書きする
5.UserDetailsImplクラスのインスタンスを生成する
**/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    //@Autowired ← コンストラクタが1つしかない場合は無くても良い
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* UserDetailsServiceImplクラスの役割は、UserDetailsImplクラスのインスタンスを生成することです。
       loadUserByUsername()メソッド内では以下の処理をおこなっています。
	1.フォームから送信されたメールアドレスに一致するユーザーを取得する
	2.そのユーザーのロールを取得する
	3.上記2つの情報をUserDetailsImplクラスのコンストラクタに渡し、インスタンスを生成する 
    **/
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email);
            String userRoleName = user.getRole().getName();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(userRoleName));
            return new UserDetailsImpl(user, authorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
        }
    }
}