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
package org.adhuc.cena.menu.port.adapter.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link ApiIndexController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.0.1
 */
@Tag("integration")
@Tag("restController")
@ExtendWith(SpringExtension.class)
@WebMvcTest(ApiIndexController.class)
@DisplayName("API index controller should")
class ApiIndexControllerShould {

    private static final String API_URL = "/api";

    @Autowired
    private MockMvc mvc;

    @Test
    @WithCommunityUser
    @DisplayName("return accessible links while getting index as community user")
    void returnAccessibleLinksCommunityUser() throws Exception {
        mvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.documentation").exists())
                .andExpect(jsonPath("_links.management").exists())
                .andExpect(jsonPath("_links.ingredients").exists())
                .andExpect(jsonPath("_links.recipes").exists())
                .andExpect(jsonPath("_links.menus").doesNotExist());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("return accessible links while getting index as authenticated user")
    void returnAccessibleLinksAuthenticatedUser() throws Exception {
        mvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.documentation").exists())
                .andExpect(jsonPath("_links.management").exists())
                .andExpect(jsonPath("_links.ingredients").exists())
                .andExpect(jsonPath("_links.recipes").exists())
                .andExpect(jsonPath("_links.menus").exists());
    }

    @Test
    @WithIngredientManager
    @DisplayName("return accessible links while getting index as ingredient manager")
    void returnAccessibleLinksIngredientManager() throws Exception {
        mvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.documentation").exists())
                .andExpect(jsonPath("_links.management").exists())
                .andExpect(jsonPath("_links.ingredients").exists())
                .andExpect(jsonPath("_links.recipes").exists())
                .andExpect(jsonPath("_links.menus").exists());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("return accessible links while getting index as super administrator")
    void returnAccessibleLinksSuperAdministrator() throws Exception {
        mvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.documentation").exists())
                .andExpect(jsonPath("_links.management").exists())
                .andExpect(jsonPath("_links.ingredients").exists())
                .andExpect(jsonPath("_links.recipes").exists())
                .andExpect(jsonPath("_links.menus").exists());
    }

}
