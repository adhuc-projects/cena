package org.adhuc.cena.menu.steps.management;

import static net.serenitybdd.rest.SerenityRest.rest;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.response.ValidatableResponse;
import net.thucydides.core.annotations.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The spring management service client steps.
 *
 * @author Alexandre Carbenay
 * @version 0.0.1
 * @since 0.0.1
 */
public class ManagementServiceClientSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementServiceClientSteps.class);

    private static final String SERVICE_NAME = "menu-generation";
    private static final String EXPOSED_PORT = "8080";
    private static final String PORT_PROPERTY_NAME = SERVICE_NAME + ".tcp." + EXPOSED_PORT;

    private ValidatableResponse healthCheckResponse;

    @Step("Call health check service")
    public void callHealthCheckService() {
        healthCheckResponse = rest().get(getHealthUrl()).then();
    }

    @Step("Assert rest-service response is OK")
    public void assertResponseIsOk() {
        healthCheckResponse.statusCode(OK.value());
    }

    private String getHealthUrl() {
        String port = System.getProperty(PORT_PROPERTY_NAME);
        LOGGER.info("Call health with port {}", port);
        return String.format("http://localhost:%s/actuator/health", port);
    }

}
