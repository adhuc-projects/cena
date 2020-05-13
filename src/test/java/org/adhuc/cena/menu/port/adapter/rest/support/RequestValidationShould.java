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
package org.adhuc.cena.menu.port.adapter.rest.support;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.ingredients.IngredientConsultation;
import org.adhuc.cena.menu.ingredients.IngredientManagement;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientsController;
import org.adhuc.cena.menu.support.WithIngredientManager;

/**
 * A test class for request validation behavior.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({IngredientsController.class, IngredientModelAssembler.class})
@DisplayName("Request validation should")
class RequestValidationShould {

    private static final String INGREDIENTS_API_URL = "/api/ingredients";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IngredientConsultation ingredientConsultationMock;
    @MockBean
    private IngredientManagement ingredientManagementMock;

    @Test
    @WithIngredientManager
    @DisplayName("respond with error detail on request validation error")
    void respond400OnRequestValidationError() throws Exception {
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("status", equalTo(400)))
                .andExpect(jsonPath("code", equalTo(101000)))
                .andExpect(jsonPath("error", equalTo("Invalid request")))
                .andExpect(jsonPath("details").isArray())
                .andExpect(jsonPath("details", hasSize(1)))
                .andExpect(jsonPath("details[0]", equalTo("Invalid request body property 'name': must not be blank. Actual value is <null>")));
    }

}
