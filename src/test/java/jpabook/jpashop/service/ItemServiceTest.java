package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void 아이템_넣는_테스트() {
        // given
        Book book = new Book();
        book.setName("JPA book");
        book.setIsdn("i123");
        book.setAuthor("dhlee");
        book.setPrice(5000);
        book.setStockQuantity(100);


        // when
        itemService.saveItem(book);

        // then
        assertThat(itemRepository.findAll())
                .contains(book);
    }
}