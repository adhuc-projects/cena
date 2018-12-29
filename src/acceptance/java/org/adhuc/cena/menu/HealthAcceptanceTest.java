package org.adhuc.cena.menu;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HealthAcceptanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthAcceptanceTest.class);

    static final String SERVICE_NAME = "menu-generation";
    static final String EXPOSED_PORT = "8080";
    static final String PORT_PROPERTY_NAME = SERVICE_NAME + ".tcp." + EXPOSED_PORT;

    @Test
    public void callHealth() {
        String port = System.getProperty(PORT_PROPERTY_NAME);
        LOGGER.info("Call health with port {}", port);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(String.format("http://localhost:%s/actuator/health", port), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
