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
package org.adhuc.cena.menu.steps.serenity.menus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

/**
 * A client used to retrieve menus from the server.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@RequiredArgsConstructor
final class MenuListClientDelegate {

    private final RestClientDelegate restClient = new RestClientDelegate();
    private final StatusAssertionDelegate statusAssertion = new StatusAssertionDelegate();
    private final ResourceUrlResolverDelegate resourceUrlResolver = new ResourceUrlResolverDelegate();

    /**
     * Fetches the menus owned by the currently authenticated user from server.
     *
     * @return the fetched menus.
     */
    public List<MenuValue> fetchMenus() {
        return fetchMenus(restClient::rest);
    }

    /**
     * Fetches the menus owned by the currently authenticated user for the specified date range from server.
     *
     * @param since the date range lower bound.
     * @param until the date range upper bound.
     * @return the fetched menus.
     */
    public List<MenuValue> fetchMenus(LocalDate since, LocalDate until) {
        return fetchMenus(restClient::rest, since, until);
    }

    /**
     * Fetches the menus owned by the specified owner from server.
     *
     * @param owner the authentication type corresponding to the menu owner.
     * @return the fetched menus.
     */
    public List<MenuValue> fetchMenus(AuthenticationType owner) {
        return fetchMenus(() -> restClient.rest(owner));
    }

    /**
     * Fetches the menus owned by the specified owner for the specified date range from server.
     *
     * @param owner the authentication type corresponding to the menu owner.
     * @param since the date range lower bound.
     * @param until the date range upper bound.
     * @return the fetched menus.
     */
    public List<MenuValue> fetchMenus(AuthenticationType owner, LocalDate since, LocalDate until) {
        return fetchMenus(() -> restClient.rest(owner), since, until);
    }

    private List<MenuValue> fetchMenus(Supplier<RequestSpecification> requestSpecificationSupplier) {
        return getRawMenuList(requestSpecificationSupplier).getList("_embedded.data", MenuValue.class);
    }

    private List<MenuValue> fetchMenus(Supplier<RequestSpecification> requestSpecificationSupplier, LocalDate since, LocalDate until) {
        return getRawMenuList(requestSpecificationSupplier, since, until).getList("_embedded.data", MenuValue.class);
    }

    /**
     * Gets the menu corresponding to the specified one from the menus list owned by the currently authenticated user if
     * existing, based on its date and meal type.
     * The menus list is retrieved from the server. If menu is retrieved from server, it should be populated with all
     * attributes, especially its links.
     *
     * @param menu the menu to retrieve in the list.
     * @return the menu retrieved from list.
     */
    public Optional<MenuValue> getFromMenusList(MenuValue menu) {
        return getFromMenusList(menu.date(), menu.mealType());
    }

    /**
     * Gets the menu corresponding to the specified date and meal type from the menus list owned by the currently
     * authenticated user if existing.
     * The menus list is retrieved from the server. If menu is retrieved from server, it should be populated with all
     * attributes, especially its links.
     *
     * @param date     the date of the menu to retrieve in the list.
     * @param mealType the meal type of the menu to retrieve in the list.
     * @return the menu retrieved from list.
     */
    public Optional<MenuValue> getFromMenusList(LocalDate date, String mealType) {
        var rawMenuList = date != null ? getRawMenuList(restClient::rest, date) : getRawMenuList(restClient::rest);
        return Optional.ofNullable(rawMenuList
                .param("date", date != null ? date.toString() : null)
                .param("mealType", mealType != null ? mealType.toUpperCase() : null)
                .getObject("_embedded.data.find { menu->menu.date == date && menu.mealType == mealType }", MenuValue.class));
    }

    /**
     * Gets the menu corresponding to the specified one from the menus list owned by the specified owner if existing,
     * based on its date and meal type.
     * The menus list is retrieved from the server. If menu is retrieved from server, it should be populated with all
     * attributes, especially its links.
     *
     * @param owner the authentication type corresponding to the menu owner.
     * @param menu  the menu to retrieve in the list.
     * @return the menu retrieved from list.
     */
    public Optional<MenuValue> getFromMenusList(AuthenticationType owner, MenuValue menu) {
        return getFromMenusList(owner, menu.date(), menu.mealType());
    }

    /**
     * Gets the menu corresponding to the specified date and meal type from the menus list owned by the specified owner
     * if existing.
     * The menus list is retrieved from the server. If menu is retrieved from server, it should be populated with all
     * attributes, especially its links.
     *
     * @param owner    the authentication type corresponding to the menu owner.
     * @param date     the date of the menu to retrieve in the list.
     * @param mealType the meal type of the menu to retrieve in the list.
     * @return the menu retrieved from list.
     */
    public Optional<MenuValue> getFromMenusList(AuthenticationType owner, LocalDate date, String mealType) {
        return Optional.ofNullable(getRawMenuList(() -> restClient.rest(owner))
                .param("date", date != null ? date.toString() : null)
                .param("mealType", mealType != null ? mealType.toUpperCase() : null)
                .getObject("_embedded.data.find { menu->menu.date == date && menu.mealType == mealType }", MenuValue.class));
    }

    /**
     * Retrieves menus list. This method factorizes the server call, response status assertion and JSON path extraction.
     *
     * @param requestSpecificationSupplier the request specification supplier, to call API with specific authenticated user.
     * @return the {@link JsonPath} corresponding to the menus list.
     */
    private JsonPath getRawMenuList(Supplier<RequestSpecification> requestSpecificationSupplier) {
        var response = requestSpecificationSupplier.get().get(resourceUrlResolver.menusResourceUrl()).then();
        return statusAssertion.assertOk(response).extract().jsonPath();
    }

    /**
     * Retrieves menus list for a date range. This method factorizes the server call, response status assertion and JSON
     * path extraction.
     *
     * @param requestSpecificationSupplier the request specification supplier, to call API with specific authenticated user.
     * @param since                        the date range lower bound.
     * @return the {@link JsonPath} corresponding to the menus list.
     */
    private JsonPath getRawMenuList(Supplier<RequestSpecification> requestSpecificationSupplier, LocalDate since) {
        var response = requestSpecificationSupplier.get()
                .param("filter[date][since]", since.toString())
                .get(resourceUrlResolver.menusResourceUrl()).then();
        return statusAssertion.assertOk(response).extract().jsonPath();
    }

    /**
     * Retrieves menus list for a date range. This method factorizes the server call, response status assertion and JSON
     * path extraction.
     *
     * @param requestSpecificationSupplier the request specification supplier, to call API with specific authenticated user.
     * @param since                        the date range lower bound.
     * @param until                        the date range upper bound.
     * @return the {@link JsonPath} corresponding to the menus list.
     */
    private JsonPath getRawMenuList(Supplier<RequestSpecification> requestSpecificationSupplier, LocalDate since, LocalDate until) {
        var response = requestSpecificationSupplier.get()
                .param("filter[date][since]", since.toString())
                .param("filter[date][until]", until.toString())
                .get(resourceUrlResolver.menusResourceUrl()).then();
        return statusAssertion.assertOk(response).extract().jsonPath();
    }

}
