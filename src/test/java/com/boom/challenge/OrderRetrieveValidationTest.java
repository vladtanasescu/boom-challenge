package com.boom.challenge;

import com.boom.challenge.service.OrderRetrieveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class OrderRetrieveValidationTest {

    @Autowired
    private OrderRetrieveService orderRetrieveService;

    @Test
    public void GIVEN_missing_id_WHEN_get_order_THEN_validation_exception_is_thrown() {
        assertThrows(ConstraintViolationException.class, () -> this.orderRetrieveService.getOrderById(null));
    }
}
