package com.boom.challenge.service.updater;

import com.boom.challenge.model.Order;
import com.boom.challenge.model.OrderState;

public class RejectUpdater implements OrderUpdater {
    @Override
    public void updateOrder(Order order) {
        order.setState(OrderState.ASSIGNED);
    }
}
