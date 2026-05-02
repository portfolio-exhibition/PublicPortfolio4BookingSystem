package com.example.booking_system.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.booking_system.entity.House;
import com.example.booking_system.form.HouseEditForm;
import com.example.booking_system.form.HouseRegisterForm;
import com.example.booking_system.service.HouseService;

/*
　クラスに@RequestMappingアノテーションをつけることで、ルートパスの基準値を設定することができます。
　例えば今回のように@RequestMapping("/admin/houses")と指定すれば、
　このコントローラ内の各メソッドが担当するURLは「https://ドメイン名/admin/houses/○○○」となります。
　つまり、@RequestMappingアノテーションをつけることで、各メソッドに共通のパス（今回の場合は「/admin/houses」）を繰り返し記述する必要がなくなるということです。
　index()メソッドの@GetMappingアノテーションにはマッピングするルートパスを指定していませんが、
　この場合は「/admin/houses」がそのままマッピングされます。
**/
@Controller
@RequestMapping("/admin/houses")
public class AdminHouseController {
    private final HouseService houseService;

    public AdminHouseController(HouseService houseService) {
        this.houseService = houseService;
    }

//    @GetMapping（Service の一覧リスト表示に対応したコード）
//    public String index(Model model) {
//        List<House> houses = houseService.findAllHouses();
//
//        model.addAttribute("houses", houses);
//
//        return "admin/houses/index";
//    }

//    @GetMapping（旧コード）
//    public String index(@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
//        Page<House> housePage = houseService.findAllHouses(pageable);
//
//        model.addAttribute("housePage", housePage);
//
//        return "admin/houses/index";
//    }
    
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
	                    @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
	                    Model model)
	{        
	    Page<House> housePage;
	
	    // keywordパラメータが存在する場合は部分一致検索を行い、そうでなければ通常どおり全件のデータを取得しています。
	    if (keyword != null && !keyword.isEmpty()) {
	        housePage = houseService.findHousesByNameLike(keyword, pageable);
	    } else {
	        housePage = houseService.findAllHouses(pageable);
	    }        
	
	    model.addAttribute("housePage", housePage);
	    model.addAttribute("keyword", keyword);     //ビューにkeyword（文字列）を渡しています。
	
	    return "admin/houses/index";
	}
	
	/*　@PathVariableアノテーション
	　コントローラ内ではメソッドの引数に@PathVariableアノテーションをつけることで、URLの一部をその引数にバインドする（割り当てる）ことができます。
	　これにより、URLの一部を変数のように扱って、コントローラ内でその値を利用することができます。
	　（例）https://ドメイン名/admin/houses/3 にアクセスした場合
	　URLの{id}の部分にある値（3）がshow()メソッドの引数idにバインドされます。
	　これにより、show()メソッド内ではidの値を利用して処理を行うことができます。
	　@PathVariableアノテーションのname属性にはバインドさせたいURLの{}内の文字列（今回は/admin/houses/{id}なので、"id"）を指定します。
	 */
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        Optional<House> optionalHouse  = houseService.findHouseById(id);

        // 民宿が存在しない場合に民宿一覧ページにリダイレクトさせる処理
        if (optionalHouse.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "民宿が存在しません。");

            return "redirect:/admin/houses";
        }

        /* Optional型をHouse型に変換する
        　民宿詳細ページではエンティティの各フィールドにアクセスし、民宿の説明や住所などを表示させます。
        　しかし、Optional<House>型のままではエンティティの各フィールドに直接アクセスできません。
        　そこでOptionalクラスのget()メソッドを使い、House型に変換してからビューに渡しています。
        */
        House house = optionalHouse.get();
        model.addAttribute("house", house);

        return "admin/houses/show";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("houseRegisterForm", new HouseRegisterForm());

        return "admin/houses/register";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model)
    {
        if (bindingResult.hasErrors()) {
            model.addAttribute("houseRegisterForm", houseRegisterForm);

            return "admin/houses/register";
        }

        houseService.createHouse(houseRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。");

        return "redirect:/admin/houses";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        Optional<House> optionalHouse = houseService.findHouseById(id);

        if (optionalHouse.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "民宿が存在しません。");

            return "redirect:/admin/houses";
        }

        House house = optionalHouse.get();
        
        // フォームクラスをインスタンス化する
        //更新前の民宿の各フィールドの値を使ってフォームクラスをインスタンス化し、ビューに渡します。
        //更新前の民宿画像はビュー内でパスを指定して表示するため（<img th:src="@{/storage/__${house.imageName}__}">）、MultipartFile型のファイルを直接渡す必要はありません。
        //よって、コンストラクタの引数にはnullを指定します。
        HouseEditForm houseEditForm = new HouseEditForm(house.getName(), null, house.getDescription(), house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(), house.getPhoneNumber());

        // 生成したインスタンスをビューに渡す
        model.addAttribute("house", house);
        model.addAttribute("houseEditForm", houseEditForm);

        return "admin/houses/edit";
    }
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated HouseEditForm houseEditForm,
                         BindingResult bindingResult,
                         @PathVariable(name = "id") Integer id,
                         RedirectAttributes redirectAttributes,
                         Model model)
    {
        Optional<House> optionalHouse = houseService.findHouseById(id);

        if (optionalHouse.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "民宿が存在しません。");

            return "redirect:/admin/houses";
        }

        House house = optionalHouse.get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("house", house);
            model.addAttribute("houseEditForm", houseEditForm);

            return "admin/houses/edit";
        }

        houseService.updateHouse(houseEditForm, house);
        redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");

        return "redirect:/admin/houses";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        Optional<House> optionalHouse = houseService.findHouseById(id);

        if (optionalHouse.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "民宿が存在しません。");

            return "redirect:/admin/houses";
        }

        House house = optionalHouse.get();
        houseService.deleteHouse(house);
        redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。");

        return "redirect:/admin/houses";
    }
}