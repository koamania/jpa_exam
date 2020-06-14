package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문_테스트() {
        // given
        Member member = createMember();

        int itemPrice = 10000;
        int stockQuantity = 10;

        Book book = createBook(itemPrice, stockQuantity);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(orderId);
        assertThat(findOrder.getStatus())
                .as("주문 상태를 %s여야 한다.", OrderStatus.ORDER)
                .isEqualTo(OrderStatus.ORDER);

        assertThat(findOrder.getOrderItems().size())
                .as("주문 수량의 실제 갯수는 %d여야 한다.", 1)
                .isEqualTo(1);

        assertThat(findOrder.getTotalPrice())
                .as("총 주문 금액은 itemPrice와 orderCount를 곱한 값이여야 한다.")
                .isEqualTo(itemPrice * orderCount);

        assertThat(book.getStockQuantity())
                .as("상품의 재고는 stockQuantity에서 orderCount만큼 줄어들어야 한다.")
                .isEqualTo(stockQuantity - orderCount);
    }

    @Test
    public void 주문_취소가_잘_되는지() {
        // given
        int stockQuantity = 10;
        Member member = createMember();
        Book item = createBook(10000, stockQuantity);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId);
        assertThat(findOrder.getStatus())
                .as("주문 상태는 취소가 되어야 한다.")
                .isEqualTo(OrderStatus.CANCEL);

        assertThat(item.getStockQuantity())
                .as("주문 취소시 아이템의 재고가 주문 수량만큼 다시 더해져야 한다.")
                .isEqualTo(stockQuantity);
    }

    @Test
    public void 상품_주문_재고수량_초과() {
        // given
        Member member = createMember();
        Item item = createBook(10000, 10);

        int orderCount = 11;

        // when

        // then
        assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), orderCount))
                .as("재고 부족 에러가 발생해야 함.")
                .isInstanceOf(NotEnoughStockException.class);
    }

    private Book createBook(int itemPrice, int stockQuantity) {
        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(itemPrice);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("dhlee");
        member.setAddress(new Address("인천", "승학로", "123-123"));
        em.persist(member);
        return member;
    }

}