package com.boom.challenge.service;

import com.boom.challenge.api.OrderView;
import com.boom.challenge.model.Order;
import com.boom.challenge.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class OrderRetrieveService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderView getOrderById(@NotNull Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The requested order could not be found"));
        return OrderView.from(order);
    }
}
