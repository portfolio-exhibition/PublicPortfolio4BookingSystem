package com.example.booking_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/* ポイントは以下の5つです。
　1.クラスに各種アノテーションをつける
　2.メソッドに@Beanアノテーションをつける
　3.誰に、どのページへのアクセスを許可するかを設定する
　4.ログイン・ログアウトに関するURLを設定する
　5.パスワードのハッシュアルゴリズムを設定する
**/
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
  //（旧コード）.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/").permitAll()  // すべてのユーザーにアクセスを許可するURL
  //（旧コード）.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**").permitAll()  // すべてのユーザーにアクセスを許可するURL
  //（旧コード）.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses").permitAll()  // すべてのユーザーにアクセスを許可するURL
  //（旧コード）.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses", "/houses/{id}").permitAll()  // すべてのユーザーにアクセスを許可するURL
  //（旧コード）.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses", "/houses/{id}", "/stripe/webhook").permitAll()  // すべてのユーザーにアクセスを許可するURL
                .requestMatchers("/h2-console/**", "/hello", "/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses", "/houses/{id}", "/stripe/webhook", "/houses/{houseId}/reviews").permitAll()  // すべてのユーザーにアクセスを許可するURL
                .requestMatchers("/admin/**").hasRole("ADMIN")  // 管理者にのみアクセスを許可するURL
                .anyRequest().authenticated()                   // 上記以外のURLはログインが必要（会員または管理者のどちらでもOK）
            )
            .formLogin((form) -> form
                .loginPage("/login")              // ログインページのURL
                .loginProcessingUrl("/login")     // ログインフォームの送信先URL
                .defaultSuccessUrl("/?loggedIn")  // ログイン成功時のリダイレクト先URL
                .failureUrl("/login?error")       // ログイン失敗時のリダイレクト先URL
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/?loggedOut")  // ログアウト時のリダイレクト先URL
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/stripe/webhook", "/h2-console/**"));
            /** .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/stripe/webhook"))); の解説
             * Spring Securityを利用している場合、POSTメソッドでリクエストを行うとCSRF対策のチェックが入ります。
             * フォームの場合は自動的にチェック用のトークンを生成してくれるので問題ないのですが、今回のように外部からPOST送信を受ける場合、そのままではCSRF対策のチェックによってアクセスが拒否されてしまいます。
             * そこで新しく追加した以下のコードでは、「/stripe/webhook」に対するPOST送信についてはCSRF対策のチェックを無効にしています。
             */
            http.headers().frameOptions().disable();
            /** http.headers().frameOptions().disable(); の解説
             ** Spring Securityでデフォルト有効になっている「X-Frame-Options: DENY」ヘッダーを無効にし、iframeによるページ埋め込みを許可する設定です。
             ** H2コンソールやiframeを利用した別サイト連携に必須ですが、クリックジャッキング攻撃のリスクが高まるため注意が必要です。
             */

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}