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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The {@link RecipesController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({RecipesController.class})
@DisplayName("Recipes controller should")
class RecipesControllerShould {

    private static final String RECIPES_API_URL = "/api/recipes";

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("respond OK when retrieving recipes")
    void respond200OnList() throws Exception {
        mvc.perform(get(RECIPES_API_URL))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("respond with HAL content when retrieving recipes with no specific requested content")
    void respondHalOnList() throws Exception {
        mvc.perform(get(RECIPES_API_URL))
                .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
    }

    @Test
    @DisplayName("respond with JSON content when requested while retrieving recipes")
    void respondJSONOnList() throws Exception {
        mvc.perform(get(RECIPES_API_URL)
                .accept(APPLICATION_JSON)
        ).andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
    }

    @Test
    @DisplayName("have self link with correct value when retrieving recipes")
    void haveSelfOnList() throws Exception {
        mvc.perform(get(RECIPES_API_URL))
                .andExpect(jsonPath("$._links.self.href", Matchers.endsWith(RECIPES_API_URL)));
    }

    @Test
    @DisplayName("have empty embedded data when retrieving recipes")
    void haveEmptyDataOnEmptyList() throws Exception {
        mvc.perform(get(RECIPES_API_URL))
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    @DisplayName("respond Created when creating recipe")
    void respond201OnCreation() throws Exception {
        mvc.perform(post(RECIPES_API_URL)).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("respond with a Location header when creating recipe successfully")
    void respondWithLocationAfterCreationSuccess() throws Exception {
        mvc.perform(post(RECIPES_API_URL)).andExpect(header().exists(LOCATION));
    }

}
