package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);   //id는 jpa에 저장하기 전까지 id값이 없으므로 null 체크 진행
        } else {
            em.merge(item); //update와 비슷 | 이미 id값이 있음
            //Item returnItem = em.merge(item);
            //item은 영속 상태로 변하지 않고, returnItem만 영속성 컨텍스트로 관리된다.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
