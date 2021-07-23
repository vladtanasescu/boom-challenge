package com.boom.challenge.mvc;

import com.boom.challenge.api.OrderView;
import com.boom.challenge.model.OrderState;
import com.boom.challenge.model.PhotoType;
import com.boom.challenge.model.Photographer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderControllerTest {

    private static final String NAME = "NAME";
    private static final String SURNAME = "SURNAME";
    private static final String EMAIL = "EMAIL";
    private static final String PHONE = "PHONE";
    private static final PhotoType PHOTO_TYPE = PhotoType.FOOD;
    private static final LocalDateTime VALID_DATE = LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(16, 20));
    private static final LocalDateTime INVALID_DATE = LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.of(23, 20));
    private static final String TITLE = "TITLE";
    private static final String INFO = "INFO";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/photographers/add",
                String.class);
    }

    @Test
    public void GIVEN_valid_create_order_without_date_WHEN_order_create_THEN_order_properly_created() {
        Long id = validateCreateOrderWithoutDates();
        validateUpdateDate(id);
    }

    @Test
    public void GIVEN_valid_order_photographer_photos_finalize_requests_WHEN_order_create_and_updated_THEN_order_properly_processed() {
        Long id = validateCreateOrder();
        validateGetOrder(id);
        validateAddPhotographer(id);
        validateAddPhotos(id);
        validateFinalize(id);
    }

    @Test
    public void GIVEN_valid_order_photographer_photos_rejection_requests_WHEN_order_create_and_updated_THEN_order_properly_processed() {
        Long id = validateCreateOrder();
        validateAddPhotographer(id);
        validateAddPhotos(id);
        validateRejection(id);
        validateAddPhotos(id);
        validateFinalize(id);
    }

    @Test
    public void GIVEN_valid_order_cancel_photographer_requests_WHEN_order_create_and_updated_THEN_order_properly_processed() {
        Long id = validateCreateOrder();
        validateCancellation(id);
        validateAddPhotographerFailure(id);
    }

    @Test
    public void GIVEN_valid_order_create_WHEN_invalid_update_request_THEN_error_properly_handled() {
        Long id = validateCreateOrder();
        validateInvalidTargetedStateUpdate(id);
    }

    @Test
    public void GIVEN_valid_order_created_WHEN_invalid_photographer_added_THEN_error_properly_handled() {
        Long id = validateCreateOrder();
        validateInvalidPhotographerUpdate(id);
    }

    @Test
    public void GIVEN_valid_order_created_without_dates_WHEN_missing_date_update_requested_THEN_error_properly_handled() {
        Long id = validateCreateOrderWithoutDates();
        validateUpdateMissingDate(id);
    }

    @Test
    public void GIVEN_valid_order_created_without_dates_WHEN_invalid_date_update_requested_THEN_error_properly_handled() {
        Long id = validateCreateOrderWithoutDates();
        validateUpdateInvalidDate(id);
    }

    private Long validateCreateOrderWithoutDates() {
        ResponseEntity<OrderView> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/order",
                buildValidCreateOrderRequestWithoutValidDate(),
                OrderView.class
        );

        validateCommonFields(response.getBody());
        assertEquals(OrderState.UNSCHEDULED, response.getBody().getState());
        return response.getBody().getId();
    }

    private Long validateCreateOrder() {
        ResponseEntity<OrderView> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/order",
                buildValidCreateOrderRequestWithValidDate(),
                OrderView.class
        );

        validateCommonFields(response.getBody());
        assertEquals(VALID_DATE, response.getBody().getDateTime());
        assertEquals(OrderState.PENDING, response.getBody().getState());

        return response.getBody().getId();
    }

    private void validateGetOrder(Long id) {
        ResponseEntity<OrderView> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/order/" + id,
                OrderView.class
        );

        validateCommonFields(response.getBody());
        assertEquals(VALID_DATE, response.getBody().getDateTime());
        assertEquals(OrderState.PENDING, response.getBody().getState());
    }

    private void validateAddPhotographer(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder()
                        .photographer(Photographer.builder().id(1L).build())
                        .state(OrderState.ASSIGNED).build());

        validateCommonFields(response.getBody());
        assertEquals(OrderState.ASSIGNED, response.getBody().getState());
    }

    private void validateAddPhotographerFailure(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder()
                        .photographer(Photographer.builder().id(1L).build())
                        .state(OrderState.ASSIGNED).build());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private void validateAddPhotos(Long id) {
        ResponseEntity<OrderView> response = restTemplate.exchange(
                "http://localhost:" + port + "/order/" + id + "/photos",
                HttpMethod.PATCH,
                buildMultipartRequest(),
                OrderView.class
        );

        validateCommonFields(response.getBody());
        assertEquals(OrderState.UPLOADED, response.getBody().getState());
    }

    private void validateFinalize(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder().photographer(Photographer.builder().id(1L).build()).state(OrderState.COMPLETED).build());

        validateCommonFields(response.getBody());
        assertEquals(OrderState.COMPLETED, response.getBody().getState());
    }

    private void validateRejection(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder().photographer(Photographer.builder().id(1L).build()).state(OrderState.ASSIGNED).build());

        validateCommonFields(response.getBody());
        assertEquals(OrderState.ASSIGNED, response.getBody().getState());
    }

    private void validateCancellation(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder().photographer(Photographer.builder().id(1L).build()).state(OrderState.CANCELLED).build());

        validateCommonFields(response.getBody());
        assertEquals(OrderState.CANCELLED, response.getBody().getState());
    }

    private void validateUpdateDate(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder().photographer(Photographer.builder().id(1L).build()).state(OrderState.PENDING).dateTime(VALID_DATE).build());

        validateCommonFields(response.getBody());
        assertEquals(VALID_DATE, response.getBody().getDateTime());
        assertEquals(OrderState.PENDING, response.getBody().getState());
    }

    private void validateUpdateMissingDate(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder().state(OrderState.PENDING).build());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private void validateUpdateInvalidDate(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder().photographer(Photographer.builder().id(1L).build()).state(OrderState.PENDING).dateTime(INVALID_DATE).build());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private void validateInvalidTargetedStateUpdate(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder().photographer(Photographer.builder().id(1L).build()).state(OrderState.COMPLETED).build());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private void validateInvalidPhotographerUpdate(Long id) {
        ResponseEntity<OrderView> response = submitUpdateRequest(
                "http://localhost:" + port + "/order/" + id,
                OrderView.builder().photographer(Photographer.builder().id(10000L).build()).state(OrderState.COMPLETED).build());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private ResponseEntity<OrderView> submitUpdateRequest(String url, OrderView request) {
        return restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                buildJsonRequest(request),
                OrderView.class
        );
    }

    private void validateCommonFields(OrderView orderView) {
        assertEquals(NAME, orderView.getName());
        assertEquals(SURNAME, orderView.getSurname());
        assertEquals(EMAIL, orderView.getEmail());
        assertEquals(PHONE, orderView.getPhone());
        assertEquals(TITLE, orderView.getTitle());
        assertEquals(INFO, orderView.getLogisticInfo());
        assertEquals(PHOTO_TYPE, orderView.getPhotoType());
        assertNotNull(orderView.getId());
    }

    private OrderView buildValidCreateOrderRequestWithValidDate() {
        return OrderView.builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .phone(PHONE)
                .photoType(PHOTO_TYPE)
                .dateTime(VALID_DATE)
                .title(TITLE)
                .logisticInfo(INFO)
                .build();
    }

    private OrderView buildValidCreateOrderRequestWithoutValidDate() {
        return OrderView.builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .phone(PHONE)
                .photoType(PHOTO_TYPE)
                .title(TITLE)
                .logisticInfo(INFO)
                .build();
    }

    private HttpEntity<MultiValueMap<String, Object>> buildMultipartRequest() {
        MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<>();
        multipartMap.add("file", new ClassPathResource("application.properties"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("multipart", "form-data"));
        headers.put("type", Collections.singletonList("file"));
        return new HttpEntity<>(multipartMap, headers);
    }

    private HttpEntity<OrderView> buildJsonRequest(OrderView request) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        return new HttpEntity<>(request, headers);
    }
}
