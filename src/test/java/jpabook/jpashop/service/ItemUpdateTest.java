package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManagerFactory emf;

    @Test
    public void updateTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Book book = new Book();
        book.setName("tete");
        em.persist(book);
        tx.commit();

        EntityTransaction tx2 = em.getTransaction();
        tx2.begin();
        Book findBook = em.find(Book.class, 1L);

        // TX
        // 변경 감지 == dirty checking
        findBook.setName("asdasdasd");
        tx2.commit();

        Book findBook2 = em.find(Book.class, 1L);
        System.out.println(findBook2);
    }
}
