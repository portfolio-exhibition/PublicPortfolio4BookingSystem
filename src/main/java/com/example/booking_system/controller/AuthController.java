package com.example.booking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.booking_system.entity.User;
import com.example.booking_system.entity.VerificationToken;
import com.example.booking_system.event.SignupEventPublisher;
import com.example.booking_system.form.SignupForm;
import com.example.booking_system.service.UserService;
import com.example.booking_system.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;


/* ポイントは以下の2つです。
1.引数でHttpServletRequestオブジェクトを受け取る
　メールに記載するメール認証用のURLは「https://ドメイン名/signup/verify?token=生成したトークン」ですが、
　「ドメイン名」の部分はローカル環境と本番環境で異なるうえ、本番環境を移動する際などにも変更される可能性があります。
　その度に直接コード内のURLを修正してもよいのですが、HttpServletRequestインターフェースを利用して動的にURLを取得する事もできます。
　HttpServletRequestは、HTTPリクエストに関するさまざまな情報を提供するインターフェースです。
　Spring Bootでは、コントローラのメソッドの引数でHttpServletRequestオブジェクトを受け取ることで、
　そのHTTPリクエストに関するさまざまな情報を取得できるようになります。
　以下のコードではgetRequestURL()メソッドを使い、リクエストURL（https://ドメイン名/signup）を取得します。
2.イベントを発行する
 */
@Controller
public class AuthController {
	private final UserService userService;
	private final SignupEventPublisher signupEventPublisher;
	private final VerificationTokenService verificationTokenService;

	public AuthController(UserService userService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
    }
    
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    /*
    ・メソッドにModel型の引数を指定する
    ・メソッド内でaddAttribute()メソッドを使い、以下の引数を渡す
　　　第1引数：ビュー側から参照する変数名
　　　第2引数：ビューに渡すデータ
    ・auth/signup.htmlファイル内でsignupFormという変数を使うことで、
    　コントローラから渡されたSignupFormクラスのインスタンス（new SignupForm()の戻り値）を参照できます。
     */
    @GetMapping("/signup")
    public String signup(Model model) {                       //ビューにフォームクラスのインスタンスを渡す
        model.addAttribute("signupForm", new SignupForm());   //Modelクラスを使ってビューにデータを渡す
        return "auth/signup";
    }
    
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest httpServletRequest,
                         Model model)
    {
    	/*
    	・FieldErrorクラスのインスタンスを作成し、それをaddError()メソッドに渡す
　　　　・FieldErrorクラスのコンストラクタに渡す引数は以下のとおり
　　　　　第1引数：エラー内容を格納するオブジェクト名
　　　　　第2引数：エラーを発生させるフィールド名
　　　　　第3引数：エラーメッセージ
    	 */
        // メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);
        }

        // パスワードとパスワード（確認用）の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("signupForm", signupForm);

            return "auth/signup";
        }

        /* 旧コード
        ・RedirectAttributesは、リダイレクト先にデータを渡すための機能を提供するインターフェース
　　　　・RedirectAttributesインターフェースが提供するaddFlashAttribute()メソッドを使うことで、
　　　　　リダイレクト先にデータを渡すことができる
　　　　　引数は以下のとおり
　　　　　　第1引数：リダイレクト先から参照する変数名
　　　　　　第2引数：リダイレクト先に渡すデータ
　　　　・なお、addFlashAttribute()メソッドで渡されたデータはリダイレクト先で取得されたあと、自動的に削除されます。
　　　　　よって、リダイレクトの直後に1回限り利用するデータを渡す際に使います。
         */
        //userService.createUser(signupForm);
        //redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました。");
        
        User createdUser = userService.createUser(signupForm);
        String requestUrl = new String(httpServletRequest.getRequestURL());
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

        return "redirect:/";
    }
    
    /* ポイントは以下の2つです。
　　1.引数に@RequestParamアノテーションをつける
　　　メソッドの引数に@RequestParamアノテーションをつけることで、
　　　リクエストパラメータの値をその引数にバインドする（割り当てる）ことができます。
　　　・リクエストパラメータ ＝ URLの末尾に?パラメータ名=値の形式で付与されるデータや、フォームから送信されるデータなど、HTTPリクエストに含まれるデータのこと。
　　2.トークンが存在すれば、会員を有効にする
     */
    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        
        if (verificationToken != null) {
            User user = verificationToken.getUser();  
            userService.enableUser(user);
            String successMessage = "会員登録が完了しました。";
            model.addAttribute("successMessage", successMessage);            
        } else {
            String errorMessage = "トークンが無効です。";
            model.addAttribute("errorMessage", errorMessage);
        }
        
        return "auth/verify";         
    }
}