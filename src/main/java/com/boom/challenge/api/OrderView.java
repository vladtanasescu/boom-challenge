package com.boom.challenge.api;

import com.boom.challenge.service.validation.OrderDateInput;
import com.boom.challenge.model.Order;
import com.boom.challenge.model.OrderState;
import com.boom.challenge.model.PhotoType;
import com.boom.challenge.model.Photographer;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderView {
    private Long id;

    @NotEmpty(message = "Please provide the name")
    private String name;

    @NotEmpty(message = "Please provide the surname")
    private String surname;

    @NotEmpty(message = "Please provide the email")
    private String email;

    @NotEmpty(message = "Please provide the phone")
    private String phone;

    @NotNull(message = "Please provide the photo type")
    private PhotoType photoType;

    private String title;
    private String logisticInfo;

    @OrderDateInput
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    private OrderState state;
    private Photographer photographer;
    private String photosUrl;

    public static OrderView from(Order order) {
        return OrderView.builder()
                .id(order.getId())
                .name(order.getName())
                .surname(order.getSurname())
                .email(order.getEmail())
                .dateTime(order.getDateTime())
                .phone(order.getPhone())
                .photographer(order.getPhotographer())
                .photosUrl(order.getPhotosUrl())
                .logisticInfo(order.getLogisticInfo())
                .photoType(order.getPhotoType())
                .state(order.getState())
                .title(order.getTitle())
                .build();
    }

    public static Order to(OrderView orderView) {
        return Order.builder()
                .id(orderView.getId())
                .name(orderView.getName())
                .surname(orderView.getSurname())
                .email(orderView.getEmail())
                .dateTime(orderView.getDateTime())
                .phone(orderView.getPhone())
                .photographer(orderView.getPhotographer())
                .photosUrl(orderView.getPhotosUrl())
                .logisticInfo(orderView.getLogisticInfo())
                .photoType(orderView.getPhotoType())
                .state(orderView.getState())
                .title(orderView.getTitle())
                .build();
    }
}