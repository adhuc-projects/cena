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
package org.adhuc.cena.menu.steps.serenity.menus;

import java.util.Optional;
import java.util.function.Supplier;

import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * A client used to retrieve menus from the server.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@RequiredArgsConstructor
final class MenuListClientDelegate {

    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    @NonNull
    private final String menusResourceUrl;

    /**
     * Gets the menu corresponding to the specified one from the menus list if existing, based on its date and meal type.
     * The menus list is retrieved from the server. If menu is retrieved from server, it should be populated with all
     * attributes, especially its links.
     *
     * @param menu the menu to retrieve in the list.
     * @return the menu retrieved from list.
     */
    public Optional<MenuValue> getFromMenusList(MenuValue menu) {
        return Optional.ofNullable(getRawMenuList(restClientDelegate::rest).param("date", menu.date().toString()).param("mealType", menu.mealType())
                .getObject("_embedded.data.find { menu->menu.date == date && menu.mealType == mealType }", MenuValue.class));
    }

    /**
     * Retrieves menus list based on the supplied request specification. This method factorizes the server call,
     * response status assertion and JSON path extraction.
     *
     * @param requestSpecificationSupplier the request specification.
     * @return the {@link JsonPath} corresponding to the menus list.
     */
    private JsonPath getRawMenuList(Supplier<RequestSpecification> requestSpecificationSupplier) {
        var response = requestSpecificationSupplier.get().get(menusResourceUrl).then();
        return statusAssertionDelegate.assertOk(response).extract().jsonPath();
    }

}
