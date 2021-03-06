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
package org.adhuc.cena.menu.steps.serenity.support.resource;

/**
 * A REST resource encapsulating API information on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.0.1
 */
public class ApiClientResource extends HateoasClientResourceSupport {

    public static final String MENUS_LINK = "menus";

    public String getManagement() {
        return link("management");
    }

    public String getDocumentation() {
        return link("documentation");
    }

    public String getOpenApi() {
        return link("openapi");
    }

    public String getIngredients() {
        return link("ingredients");
    }

    public String getRecipes() {
        return link("recipes");
    }

    public String getMenus() {
        return link(MENUS_LINK);
    }

}
