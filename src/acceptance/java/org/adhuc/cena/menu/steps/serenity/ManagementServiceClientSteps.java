/*
 * Copyright (C) 2019 Alexandre Carbenay
 *
 * This file is part of Cena Project.
 *
 * Cena Project is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Cena Project is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Cena Project. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.adhuc.cena.menu.steps.serenity;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import io.restassured.response.ValidatableResponse;
import net.thucydides.core.annotations.Step;
import org.springframework.boot.actuate.health.Status;

import org.adhuc.cena.menu.steps.serenity.support.resource.HateoasClientResourceSupport;

/**
 * The spring management service client steps.
 *
 * @author Alexandre Carbenay
 * @version 0.0.1
 * @since 0.0.1
 */
public class ManagementServiceClientSteps extends AbstractServiceClientSteps {

    private ValidatableResponse healthCheckResponse;

    @Step("Call health check service")
    public void callHealthCheckService() {
        healthCheckResponse = rest().get(getHealthUrl()).then();
    }

    @Step("Assert health response is OK")
    public void assertResponseIsOk() {
        assertOk(healthCheckResponse);
    }

    @Step("Assert detail is not available")
    public void assertDetailIsNotAvailable() {
        healthCheckResponse.assertThat().body("details", nullValue());
    }

    @Step("Assert disk usage detail is available")
    public void assertDiskUsageIsAvailable() {
        healthCheckResponse.assertThat().body("details.diskSpace.status", equalTo(Status.UP.getCode()));
    }

    private String getHealthUrl() {
        return getManagementResource().getHealthUrl();
    }

    private ManagementResource getManagementResource() {
        return getResource(getApiClientResource().getManagement(), ManagementResource.class);
    }

    private static class ManagementResource extends HateoasClientResourceSupport {
        String getHealthUrl() {
            return getLink("health");
        }
    }

}
