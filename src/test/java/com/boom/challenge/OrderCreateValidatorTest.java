package com.boom.challenge;

import com.boom.challenge.api.OrderView;
import com.boom.challenge.model.PhotoType;
import com.boom.challenge.service.OrderCreateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class OrderCreateValidatorTest {
    @Autowired
    private OrderCreateService orderService;

    @Test
    public void GIVEN_missing_name_create_order_request_WHEN_create_order_called_THEN_return_validation_exception() {
        assertThrows(ConstraintViolationException.class, () -> this.orderService.createOrder(buildMissingNameCreateOrderRequest()));
    }

    @Test
    public void GIVEN_missing_surname_create_order_request_WHEN_create_order_called_THEN_return_validation_exception() {
        assertThrows(ConstraintViolationException.class, () -> this.orderService.createOrder(buildMissingSurnameCreateOrderRequest()));
    }

    @Test
    public void GIVEN_missing_email_create_order_request_WHEN_create_order_called_THEN_return_validation_exception() {
        assertThrows(ConstraintViolationException.class, () -> this.orderService.createOrder(buildMissingEmailCreateOrderRequest()));
    }

    @Test
    public void GIVEN_missing_phone_create_order_request_WHEN_create_order_called_THEN_return_validation_exception() {
        assertThrows(ConstraintViolationException.class, () -> this.orderService.createOrder(buildMissingPhoneCreateOrderRequest()));
    }

    @Test
    public void GIVEN_missing_photo_type_create_order_request_WHEN_create_order_called_THEN_return_validation_exception() {
        assertThrows(ConstraintViolationException.class, () -> this.orderService.createOrder(buildMissingPhotoTypeCreateOrderRequest()));
    }

    @Test
    public void GIVEN_missing_date_create_order_request_WHEN_create_order_called_THEN_return_no_validation_exception() {
        this.orderService.createOrder(buildValidCreateOrderRequestWithoutDate());
    }

    @Test
    public void GIVEN_valid_date_create_order_request_WHEN_create_order_called_THEN_return_no_validation_exception() {
        this.orderService.createOrder(buildValidCreateOrderRequestWithValidDate());
    }

    @Test
    public void GIVEN_invalid_date_create_order_request_WHEN_create_order_called_THEN_return_validation_exception() {
        assertThrows(ConstraintViolationException.class, () -> this.orderService.createOrder(buildValidCreateOrderRequestWithInvalidDate()));
    }

    private OrderView buildMissingNameCreateOrderRequest() {
        return OrderView.builder()
                .surname("Vlad")
                .email("test@test.com")
                .photoType(PhotoType.FOOD)
                .phone("+40727265888")
                .build();
    }

    private OrderView buildMissingSurnameCreateOrderRequest() {
        return OrderView.builder()
                .name("Vlad")
                .email("test@test.com")
                .photoType(PhotoType.FOOD)
                .phone("+40727265888")
                .build();
    }

    private OrderView buildMissingPhoneCreateOrderRequest() {
        return OrderView.builder()
                .name("Vlad")
                .surname("Vlad")
                .photoType(PhotoType.FOOD)
                .email("test@test.com")
                .build();
    }

    private OrderView buildMissingEmailCreateOrderRequest() {
        return OrderView.builder()
                .name("Vlad")
                .surname("Vlad")
                .photoType(PhotoType.FOOD)
                .phone("+40727265888")
                .build();
    }

    private OrderView buildMissingPhotoTypeCreateOrderRequest() {
        return OrderView.builder()
                .name("Vlad")
                .surname("Vlad")
                .email("test@test.com")
                .phone("+40727265888")
                .build();
    }

    private OrderView buildValidCreateOrderRequestWithoutDate() {
        return OrderView.builder()
                .name("Vlad")
                .surname("Vlad")
                .email("test@test.com")
                .phone("+40727265888")
                .photoType(PhotoType.FOOD)
                .build();
    }

    private OrderView buildValidCreateOrderRequestWithValidDate() {
        return OrderView.builder()
                .name("Vlad")
                .surname("Vlad")
                .email("test@test.com")
                .phone("+40727265888")
                .photoType(PhotoType.FOOD)
                .dateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 20)))
                .build();
    }

    private OrderView buildValidCreateOrderRequestWithInvalidDate() {
        return OrderView.builder()
                .name("Vlad")
                .surname("Vlad")
                .email("test@test.com")
                .phone("+40727265888")
                .photoType(PhotoType.FOOD)
                .dateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 20)))
                .build();
    }
}
