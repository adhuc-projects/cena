package org.adhuc.cena.menu;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

public class HealthAcceptanceTest {

    @Test
    public void callHealth() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/actuator/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
