package com.boom.challenge.controller;

import com.boom.challenge.api.ErrorResponse;
import com.boom.challenge.api.OrderView;
import com.boom.challenge.model.File;
import com.boom.challenge.service.OrderCreateService;
import com.boom.challenge.service.OrderRetrieveService;
import com.boom.challenge.service.OrderUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestController
public class OrderController {

    @Autowired
    private OrderCreateService orderCreateService;

    @Autowired
    private OrderRetrieveService orderRetrieveService;

    @Autowired
    private OrderUpdateService orderUpdateService;

    @PostMapping("order")
    public OrderView createOrder(@RequestBody OrderView orderView) {
        return this.orderCreateService.createOrder(orderView);
    }

    @GetMapping("order/{id}")
    public OrderView getOrderById(@PathVariable Long id) {
        return this.orderRetrieveService.getOrderById(id);
    }

    @PatchMapping(value = "order/{id}")
    public OrderView updateOrder(@PathVariable Long id, @RequestBody OrderView orderView) {
        return this.orderUpdateService.updateOrder(id, orderView);
    }

    @PatchMapping(value = "order/{id}/photos")
    public OrderView uploadPhotos(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return this.orderUpdateService.updateOrder(id, File.builder().fileName(file.getOriginalFilename()).build());
    }

    @ExceptionHandler({ConstraintViolationException.class, ValidationException.class})
    public ResponseEntity<ErrorResponse> handleInputValidationErrors(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleErrors(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder().message("Internal server error").build());
    }
}
