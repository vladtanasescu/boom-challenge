package com.boom.challenge.service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderDateInputConstraintValidator.class)
public @interface OrderDateInput {
    String message() default "{Time is not between the allowed interval}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}