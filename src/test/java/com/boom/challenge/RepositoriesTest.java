package com.boom.challenge;

import com.boom.challenge.model.Order;
import com.boom.challenge.model.Photographer;
import com.boom.challenge.repository.OrderRepository;
import com.boom.challenge.repository.PhotographerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
public class RepositoriesTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PhotographerRepository photographerRepository;

    @Test
    public void GIVEN_order_WHEN_order_saved_THEN_order_persisted() {
        Order order = Order.builder().name("Vlad").build();

        Order saved = this.orderRepository.save(order);
        Optional <Order> found = this.orderRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void GIVEN_photographer_WHEN_photographer_saved_THEN_photographer_persisted() {
        Photographer photographer = Photographer.builder().name("bob").build();

        Photographer saved = this.photographerRepository.save(photographer);
        Optional <Photographer> found = this.photographerRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getName(), found.get().getName());
    }
}
