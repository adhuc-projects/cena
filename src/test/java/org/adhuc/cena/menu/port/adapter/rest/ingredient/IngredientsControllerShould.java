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
package org.adhuc.cena.menu.port.adapter.rest.ingredient;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The {@link IngredientsController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest(IngredientsController.class)
@DisplayName("Ingredients controller should")
class IngredientsControllerShould {

    private static final String INGREDIENTS_API_URL = "/api/ingredients";

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("respond OK when getting ingredients")
    void respond200OnList() throws Exception {
        mvc.perform(get(INGREDIENTS_API_URL))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("respond with HAL content")
    void respondHalOnList() throws Exception {
        mvc.perform(get(INGREDIENTS_API_URL))
                .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
    }

    @Test
    @DisplayName("respond with JSON content when requested")
    void respondJSONOnList() throws Exception {
        mvc.perform(get(INGREDIENTS_API_URL)
                .accept(APPLICATION_JSON)
            ).andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
    }

    @Test
    @DisplayName("have self link with correct value when getting ingredients")
    void haveSelfOnList() throws Exception {
        mvc.perform(get(INGREDIENTS_API_URL))
                .andExpect(jsonPath("$._links.self.href", endsWith(INGREDIENTS_API_URL)));
    }

    @Test
    @DisplayName("respond Unsupported Media Type when creating ingredient with XML content")
    void respond415OnCreationXML() throws Exception {
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(APPLICATION_XML)
                .content("<ingredient><name>Tomato</name></ingredient>")
            ).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("respond Created when creating ingredient with JSON content")
    void respond201OnCreationJson() throws Exception {
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato\"}")
            ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("respond Created when creating ingredient with HAL content")
    void respond201OnCreationHal() throws Exception {
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato\"}")
            ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("respond with a Location header")
    void respondWithLocation() throws Exception {
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato\"}")
            ).andExpect(header().exists(LOCATION));
    }

    @Test
    @DisplayName("respond No Content when deleting ingredients")
    void respond204OnDeletion() throws Exception {
        mvc.perform(delete(INGREDIENTS_API_URL))
            .andExpect(status().isNoContent());
    }

}
