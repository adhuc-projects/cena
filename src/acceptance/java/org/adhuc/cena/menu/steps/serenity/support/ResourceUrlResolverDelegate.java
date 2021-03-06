/*
 * Copyright (C) 2019-2020 Alexandre Carbenay
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import static org.adhuc.cena.menu.steps.serenity.support.resource.ApiClientResource.MENUS_LINK;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;

import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationProvider;
import org.adhuc.cena.menu.steps.serenity.support.resource.ApiClientResource;

/**
 * A delegate providing convenient methods for resolving resource URLs. Resource URLs are resolved once for each
 * authentication, to ensure that every authentication has its access to its own resources.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.0.1
 */
@Slf4j
public final class ResourceUrlResolverDelegate {

    private static final String API_RESOURCE_SESSION_KEY_PREFIX = "apiClientResource";

    private static final String SERVICE_NAME = "menu-generation";
    private static final String EXPOSED_PORT = "8080";
    private static final String PORT_PROPERTY_NAME = SERVICE_NAME + ".tcp." + EXPOSED_PORT;

    private final String port = System.getProperty(PORT_PROPERTY_NAME, EXPOSED_PORT);

    private final AuthenticationProvider authenticationProvider = AuthenticationProvider.instance();
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();

    public ApiClientResource apiClientResource() {
        var sessionKey = apiResourceSessionKey();
        if (!Serenity.hasASessionVariableCalled(sessionKey)) {
            Serenity.setSessionVariable(sessionKey).to(getResource(getApiIndexUrl(), ApiClientResource.class));
        }
        return Serenity.sessionVariableCalled(sessionKey);
    }

    public String ingredientsResourceUrl() {
        return apiClientResource().getIngredients();
    }

    public String recipesResourceUrl() {
        return apiClientResource().getRecipes();
    }

    public String menusResourceUrl() {
        return apiClientResource().getMenus();
    }

    public String notAccessibleMenusResourceUrl() {
        assertThat(apiClientResource().maybeLink(MENUS_LINK)).isEmpty();
        return String.format("%s/%s", getApiIndexUrl(), MENUS_LINK);
    }

    public <T> T getResource(String url, Class<T> resourceClass) {
        return restClientDelegate.rest().get(url).then().statusCode(OK.value()).extract().as(resourceClass);
    }

    private String getApiIndexUrl() {
        log.info("Call API index with port {}", port);
        return String.format("http://localhost:%s/api", port);
    }

    private String apiResourceSessionKey() {
        return API_RESOURCE_SESSION_KEY_PREFIX + "-" + authenticationProvider.currentAuthentication();
    }

}
