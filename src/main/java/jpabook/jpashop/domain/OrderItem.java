package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    //OrderItem N : item 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    //orderItem N : order 1
    //order와 orderItem의 연관관계를 보면 order에 여러개의 orderItem이 있으므로 FK가 되고, 연관관계의 주인이 된다.
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count;  //주문 수량

    //===생성 메소드===//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //===비즈니스 로직===//
    public void cancel() {
        getItem().addStock(count);
    }

    //===조회 로직===//
    /**
     * 주문할 때 주문 가격과 주문 수량이 있으므로 주문 총 금액을 가지고 있다.
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
