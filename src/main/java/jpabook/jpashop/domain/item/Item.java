package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categoryList = new ArrayList<>();

    //===비즈니스 로직 추가===//
    //엔티티 자체가 해결할 수 있는 것들은 엔티티 안에 비즈니스 로직을 추가하는 것이 좋다.
    //재고 수량이 엔티티에 있으므로 엔티티안에서 비즈니스 로직이 나가는 것이 응집도가 높다.

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int realStock = this.stockQuantity - quantity;
        if (realStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = realStock;
    }
}
