package com.boom.challenge.service.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@Component
public class OrderDateInputConstraintValidator implements ConstraintValidator<OrderDateInput, LocalDateTime> {

    @Autowired
    private OrderDateValidatorService orderDateValidator;

    @Override
    public void initialize(OrderDateInput constraint) {
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext context) {
        if (localDateTime == null) {
            return true;
        } else {
            return this.orderDateValidator.isValid(localDateTime);
        }
    }
}
