package com.boom.challenge.mvc;

import com.boom.challenge.model.Photographer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-init")
public class PhotographerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void GIVEN_container_initialized_WHEN_retrieve_photographers_THEN_results_are_returned() {
        ResponseEntity<Photographer[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/photographers/",
                Photographer[].class
        );

        assertTrue(response.getBody().length > 0);
        assertNotNull(response.getBody()[0].getId());
        assertNotNull(response.getBody()[0].getName());
    }
}
