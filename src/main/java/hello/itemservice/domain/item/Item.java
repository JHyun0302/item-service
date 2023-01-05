package hello.itemservice.domain.item;

import lombok.Data;

@Data // @Data는 위험! 웬만하면 @Getter @Setter 따로 쓰기
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
