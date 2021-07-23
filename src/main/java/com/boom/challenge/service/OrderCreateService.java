package com.boom.challenge.service;

import com.boom.challenge.api.OrderView;
import com.boom.challenge.model.Order;
import com.boom.challenge.model.OrderState;
import com.boom.challenge.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class OrderCreateService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderView createOrder(@Valid OrderView orderView) {
        Order order = OrderView.to(orderView);
        order.setState(resolveCreateState(orderView));
        return OrderView.from(this.orderRepository.save(order));
    }

    private OrderState resolveCreateState(OrderView orderView) {
        return orderView.getDateTime() != null ? OrderState.PENDING : OrderState.UNSCHEDULED;
    }
}
