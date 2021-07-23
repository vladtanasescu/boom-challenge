package com.boom.challenge.service.updater;

import com.boom.challenge.model.Order;
import com.boom.challenge.model.OrderState;

public class CancelUpdater implements OrderUpdater {
    @Override
    public void updateOrder(Order order) {
        order.setState(OrderState.CANCELLED);
    }
}
