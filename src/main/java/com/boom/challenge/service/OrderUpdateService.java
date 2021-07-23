package com.boom.challenge.service;

import com.boom.challenge.api.OrderView;
import com.boom.challenge.model.File;
import com.boom.challenge.model.Order;
import com.boom.challenge.model.OrderState;
import com.boom.challenge.repository.FileRepository;
import com.boom.challenge.repository.OrderRepository;
import com.boom.challenge.service.updater.*;
import com.boom.challenge.service.validation.OrderDateValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Service
@Validated
public class OrderUpdateService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private OrderDateValidatorService orderDateValidatorService;

    public OrderView updateOrder(@NotNull Long id, @NotNull OrderView orderView) {
        return updateOrder(id, orderView, null);
    }

    public OrderView updateOrder(@NotNull Long id, @NotNull File file) {
        return updateOrder(id, OrderView.builder().state(OrderState.UPLOADED).build(), file);
    }

    private OrderView updateOrder(@NotNull Long id, @NotNull OrderView orderView, File file) {
        Order order = getOrderById(id);

        OrderUpdater orderUpdater = buildOrderUpdater(order.getState(), orderView, file);
        orderUpdater.updateOrder(order);

        Order persistedOrder = this.orderRepository.save(order);
        return OrderView.from(persistedOrder);
    }

    private Order getOrderById(@NotNull Long id) {
        return this.orderRepository.findById(id)
                .orElseThrow((() -> new IllegalArgumentException("The requested order id can't be found")));
    }

    private OrderUpdater buildOrderUpdater(OrderState currentState, OrderView orderView, File file) {
        OrderState targetedState = orderView.getState();

        if (OrderState.CANCELLED == currentState) {
            throw new UnsupportedOperationException("The order is cancelled. No updates are allowed");
        }

        if (OrderState.CANCELLED == targetedState) {
            return new CancelUpdater();
        }

        if (OrderState.UNSCHEDULED == currentState && OrderState.PENDING == targetedState) {
            return buildDateUpdater(orderView);
        }

        if (OrderState.PENDING == currentState && OrderState.ASSIGNED == targetedState) {
            return new PhotographerUpdater(this.photographerService.findById(orderView.getPhotographer().getId()));
        }

        if (OrderState.ASSIGNED == currentState && OrderState.UPLOADED == targetedState) {
            return buildPhotoUpdater(file);
        }

        if (OrderState.UPLOADED == currentState && OrderState.COMPLETED == targetedState) {
            return new FinalizeUpdater();
        }

        if (OrderState.UPLOADED == currentState && OrderState.ASSIGNED == targetedState) {
            return new RejectUpdater();
        }

        throw new IllegalStateException("Invalid order currentState or update request");
    }

    private OrderUpdater buildPhotoUpdater(File file) {
        if (file != null) {
            return new PhotosUpdater(file, this.fileRepository);
        } else {
            throw new ValidationException("The request is missing the photos");
        }
    }

    private DateUpdater buildDateUpdater(OrderView orderView) {
        if (orderView.getDateTime() != null && this.orderDateValidatorService.isValid(orderView.getDateTime())) {
            return new DateUpdater(orderView.getDateTime());
        } else {
            throw new ValidationException("The requested date is invalid");
        }
    }
}
