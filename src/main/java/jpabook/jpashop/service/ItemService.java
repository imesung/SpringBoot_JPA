package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    //여기서 머지하는 작업은, 아래를 줄인 걸로 볼 수 있다.
    //영속성 컨텍스트가 param으로 들어온 즉, em.merge(item)의 item을 새로운 Item으로 바꿔치기 하여 트랜잭션 커밋될 때 반영이 되는 것이다.
//    @Transactional
//    public void updateItem(Long itemId, Book param) {
//        Item findItem = itemRepository.findOne(itemId);
//        findItem.setPrice(param.getPrice());
//        findItem.setName(param.getName());
//        findItem.setStockQuantity(param.getStockQuantity());
//        findItem.setId(param.getId());
//        findItem.setCategoryList(param.getCategoryList());
//
//        itemRepository.save(findItem);
//    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
