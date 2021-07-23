package com.boom.challenge.service.updater;

import com.boom.challenge.model.Order;
import com.boom.challenge.model.OrderState;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class DateUpdater implements OrderUpdater {

    private final LocalDateTime dateTime;

    @Override
    public void updateOrder(Order order) {
        order.setDateTime(this.dateTime);
        order.setState(OrderState.PENDING);
    }
}
