package com.example.booking_system.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.booking_system.dto.ReservationDTO;
import com.example.booking_system.entity.House;
import com.example.booking_system.entity.Reservation;
import com.example.booking_system.entity.User;
import com.example.booking_system.form.ReservationInputForm;
import com.example.booking_system.security.UserDetailsImpl;
import com.example.booking_system.service.HouseService;
import com.example.booking_system.service.ReservationService;
//import com.example.booking_system.service.StripeService;   決済機能ありの場合はコメントアウトを解除

import jakarta.servlet.http.HttpSession;

@Controller
public class ReservationController {
    private final ReservationService reservationService;
    private final HouseService houseService;
    //private final StripeService stripeService;   決済機能ありの場合はコメントアウトを解除

    // public ReservationController(ReservationService reservationService, HouseService houseService, StripeService stripeService) {
    public ReservationController(ReservationService reservationService, HouseService houseService) {
        this.reservationService = reservationService;
        this.houseService = houseService;
        //this.stripeService = stripeService;   決済機能ありの場合はコメントアウトを解除
    }

    // Spring Securityが提供する @AuthenticationPrincipal アノテーションを引数につけて現在ログイン中のユーザー情報を取得する
    // アノテーションをつける引数は UserDetailsインターフェースを実装したクラスのオブジェクト（本プロジェクトでは UserDetailsImpl クラスのオブジェクト）
    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model)
    {
        User user = userDetailsImpl.getUser();
        Page<Reservation> reservationPage = reservationService.findReservationsByUserOrderByCreatedAtDesc(user, pageable);

        model.addAttribute("reservationPage", reservationPage);

        return "reservations/index";
    }
    
    @PostMapping("/houses/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
                        @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        HttpSession httpSession,
                        Model model)
    {
        Optional<House> optionalHouse  = houseService.findHouseById(id);

        if (optionalHouse.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "民宿が存在しません。");

            return "redirect:/houses";
        }

        //チェックイン日とチェックアウト日を取得する
        LocalDate checkinDate = reservationInputForm.getCheckinDate();
        LocalDate checkoutDate = reservationInputForm.getCheckoutDate();

        House house = optionalHouse.get();

        // 宿泊人数と民宿の定員を取得する
        Integer numberOfPeople = reservationInputForm.getNumberOfPeople();
        Integer capacity = house.getCapacity();

        if (checkinDate != null && checkoutDate != null && !reservationService.isCheckinBeforeCheckout(checkinDate, checkoutDate)) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "checkinDate", "チェックイン日はチェックアウト日よりも前の日付を選択してください。");
            bindingResult.addError(fieldError);
        }

        if (numberOfPeople != null && !reservationService.isWithinCapacity(numberOfPeople, capacity)) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "宿泊人数が定員を超えています。");
            bindingResult.addError(fieldError);
        }        


        if (bindingResult.hasErrors()) {
            String previousDates = reservationService.getPreviousDates(checkinDate, checkoutDate, bindingResult);
            
            model.addAttribute("house", house);
            model.addAttribute("reservationInputForm", reservationInputForm);
            model.addAttribute("previousDates", previousDates);
            model.addAttribute("errorMessage", "予約内容に不備があります。");

            return "houses/show";
        }


        // 宿泊料金を計算する
        Integer price = house.getPrice();
        Integer amount = reservationService.calculateAmount(checkinDate, checkoutDate, price);

        ReservationDTO reservationDTO = new ReservationDTO(house.getId(), checkinDate, checkoutDate, numberOfPeople, amount);

        // セッションにDTOを保存する
        httpSession.setAttribute("reservationDTO", reservationDTO);

        return "redirect:/reservations/confirm";
    }
    
    @GetMapping("/reservations/confirm")
    public String confirm(RedirectAttributes redirectAttributes, HttpSession httpSession, Model model) {
    /** 決済機能ありの場合はコメントアウトを解除し、上記のメソッド、引数をコメントアウト
    public String confirm(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            RedirectAttributes redirectAttributes,
            HttpSession httpSession,
            Model model)
    {*/
        // セッションからDTOを取得する
        ReservationDTO reservationDTO = (ReservationDTO)httpSession.getAttribute("reservationDTO");

        if (reservationDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "セッションがタイムアウトしました。もう一度予約内容を入力してください。");

            return "redirect:/houses";
        }
        
        //User user = userDetailsImpl.getUser();     // 決済機能ありの場合はコメントアウトを解除

        //String sessionId = stripeService.createStripeSession(reservationDTO, user);     // 決済機能ありの場合はコメントアウトを解除

        model.addAttribute("reservationDTO", reservationDTO);
        //model.addAttribute("sessionId", sessionId);   // 決済機能ありの場合はコメントアウトを解除

        return "reservations/confirm";
    }
    
    // 決済機能なしの場合の処理：create()メソッドで予約情報の登録処理を行い、予約一覧ページにリダイレクトさせる
    @PostMapping("/reservations/create")
    public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        // セッションからDTOを取得する
        ReservationDTO reservationDTO = (ReservationDTO)httpSession.getAttribute("reservationDTO");

        if (reservationDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "セッションがタイムアウトしました。もう一度予約内容を入力してください。");

            return "redirect:/houses";
        }

        User user = userDetailsImpl.getUser();
        reservationService.createReservation(reservationDTO, user);

        // セッションからDTOを削除する
        // セッションのデータはアプリケーションサーバーのメモリ上に保持されることが多く、
        // メモリを節約するためにもセッションの不要なデータは削除する様にした方が無難
        httpSession.removeAttribute("reservationDTO");

        return "redirect:/reservations?reserved";
    }
}