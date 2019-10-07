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
package org.adhuc.cena.menu.steps.serenity.support;

import static org.springframework.http.HttpStatus.OK;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;

import org.adhuc.cena.menu.steps.serenity.support.resource.ApiClientResource;

/**
 * A delegate providing convenient methods for resolving resource URLs.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.0.1
 */
@Slf4j
public final class ResourceUrlResolverDelegate {

    private static final String API_RESOURCE_SESSION_KEY = "apiClientResource";

    private static final String SERVICE_NAME = "menu-generation";
    private static final String EXPOSED_PORT = "8080";
    private static final String PORT_PROPERTY_NAME = SERVICE_NAME + ".tcp." + EXPOSED_PORT;

    private final String port = System.getProperty(PORT_PROPERTY_NAME, EXPOSED_PORT);

    private final RestClientDelegate restClientDelegate = new RestClientDelegate();

    public ApiClientResource apiClientResource() {
        if (!Serenity.hasASessionVariableCalled(API_RESOURCE_SESSION_KEY)) {
            Serenity.setSessionVariable(API_RESOURCE_SESSION_KEY).to(getResource(getApiIndexUrl(), ApiClientResource.class));
        }
        return Serenity.sessionVariableCalled(API_RESOURCE_SESSION_KEY);
    }

    public String ingredientsResourceUrl() {
        return apiClientResource().getIngredients();
    }

    public <T> T getResource(String url, Class<T> resourceClass) {
        return restClientDelegate.rest().get(url).then().statusCode(OK.value()).extract().as(resourceClass);
    }

    private String getApiIndexUrl() {
        log.info("Call API index with port {}", port);
        return String.format("http://localhost:%s/api", port);
    }

}
