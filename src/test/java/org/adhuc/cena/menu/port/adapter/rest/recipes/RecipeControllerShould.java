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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.recipes.RecipeMother.ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.recipes.Recipe;
import org.adhuc.cena.menu.recipes.RecipeAppService;

/**
 * The {@link RecipeController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({RecipesController.class, RecipeController.class, RecipeModelAssembler.class})
@DisplayName("Recipe controller should")
class RecipeControllerShould {

    private static final String RECIPE_API_URL = "/api/recipes/{id}";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RecipeAppService recipeAppServiceMock;

    @Test
    @DisplayName("respond Not Found when retrieving unknown recipe")
    void respond404GetUnknownRecipe() throws Exception {
        when(recipeAppServiceMock.getRecipe(ID)).thenThrow(new EntityNotFoundException(Recipe.class, ID));
        mvc.perform(get(RECIPE_API_URL, ID)).andExpect(status().isNotFound());
    }

    @Nested
    @DisplayName("getting tomato, cucumber and mozzarella salad detail")
    class TomatoCucumberMozzaSaladDetail {

        @BeforeEach
        void setUp() {
            when(recipeAppServiceMock.getRecipe(ID)).thenReturn(recipe());
        }

        @Test
        @DisplayName("return OK status")
        void getRecipeFoundStatusOK() throws Exception {
            mvc.perform(get(RECIPE_API_URL, ID)).andExpect(status().isOk());
        }

        @Test
        @DisplayName("respond with HAL content when retrieving recipe with no specific requested content")
        void respondHalOnDetail() throws Exception {
            mvc.perform(get(RECIPE_API_URL, ID))
                    .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
        }

        @Test
        @DisplayName("respond with JSON content when requested while retrieving recipe")
        void respondJSONOnDetail() throws Exception {
            mvc.perform(get(RECIPE_API_URL, ID).accept(APPLICATION_JSON))
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).andReturn();
        }

        @Test
        @DisplayName("contain recipe data")
        void getRecipeFoundContainsData() throws Exception {
            mvc.perform(get(RECIPE_API_URL, ID))
                    .andExpect(jsonPath("$.id").value(recipe().id().toString()))
                    .andExpect(jsonPath("$.name").value(recipe().name()))
                    .andExpect(jsonPath("$.content").value(recipe().content()));
        }

        @Test
        @DisplayName("contain self link to detail")
        void getRecipeHasSelfLink() throws Exception {
            var result = mvc.perform(get(RECIPE_API_URL, ID));
            result.andExpect(jsonPath("$._links.self.href").exists())
                    .andExpect(jsonPath("$._links.self.href", equalTo(result.andReturn().getRequest().getRequestURL().toString())));
        }

    }

}