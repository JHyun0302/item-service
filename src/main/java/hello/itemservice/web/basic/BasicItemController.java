package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor //final이 붙은 객체를 이용해 construct 생성해줌
public class BasicItemController {
    private final ItemRepository itemRepository; //ItemRespsitory도 @Repository로 빈으로 등록되어 있음.

/*    @Autowired //생성자가 1개면 @Autowired 생략가능
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }*/

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items"; //return: view
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        /**
         * @ModelAttribute 2가지 역할
         * 1. item객체에 set 대신 해줌
         * 2. model.addAttribute 대신해줌. attributeName: @ModelAttribute("item")
         *
         * @ModelAttribute의 이름 생략시 모델에 저장될 클래스 명의 첫글자를 소문자로 변경한게 곧 모델 이름이 됨.
         * ex) @ModelAttribute HelloWorld helloworld -> 모델 이름: helloWorld
         */
        itemRepository.save(item);
//        model.addAttribute("item", item);

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV4(Item item) {
        /**
         * int, String, Integer 같은 단순 타입 = @RequestParam
         * 나머지(Argment Resolver로 지정해둔 타입 외) = @ModelAttribute
         */
        itemRepository.save(item);
        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId(); // URL에 변수를 더해서 사용하면 위험 - URL 인코딩이 안됨!!
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        //view template에 이 값이 있으면 저장되었음
        return "redirect:/basic/items/{itemId}";
        /**
         * redirectAttributes의 attributeName: "itemId", value: savedItem.getId()가 {itemId}로 치환됨
         * 나머지 status 등은 쿼리 파라미터로 넘어감
         */
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
