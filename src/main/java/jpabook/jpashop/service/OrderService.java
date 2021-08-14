package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        //원래는 deliverRepository를 생성해서 address 정보를 저장해야 한다.
        //delivery.setAddress 만으로 DB에 저장이 되는 이유는 Order 내에 CascadeType.ALL로 되어있어서 가능하다.
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        //주문 상품도 orderItemRepository를 생성해서 orderItem 정보를 저장해야 한다.
        //OrderItem.createOrderItem 만으로 DB에 저장이 되는 이유는 Order 내에 CascadeType.ALL로 되어있어서 가능하다.
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        //해당 save로 인해(Order에 CascadeType.ALL) Delivery 및 OrderItem에 저장이 된다.
        //동일한 Persist 라이프사이클(저장 및 삭제)을 가지고 있고, Order내에서만 Delivery 및 OrderItem을 참조하고 있으므로 Cascade를 사용한 것이다.
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        //배송 완료 시 취소 못함 -> 상태 취소로 변경 -> 각 OrderItem의 cancel() 호출 -> 각 OrderItem의 stock 증가 addStock()
        //보통 주문 취소의 경우 취소 상태로 변경한 것과 취소한 stock을 증가시키는 update 쿼리가 필요하다. 하지만 JPA의 경우 Entity만 변경해주면 JPA에 의해서 DB에 update 쿼리가 날라가게 된다.
        order.cancel();
    }

    /**
     * 주문 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
