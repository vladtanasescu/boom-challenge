package com.boom.challenge.service.updater;

import com.boom.challenge.model.Order;
import com.boom.challenge.model.OrderState;
import com.boom.challenge.model.Photographer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PhotographerUpdater implements OrderUpdater {

    private final Photographer photographer;

    @Override
    public void updateOrder(Order order) {
        order.setPhotographer(this.photographer);
        order.setState(OrderState.ASSIGNED);
    }
}
