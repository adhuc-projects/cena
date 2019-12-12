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
package org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients;

import static java.lang.String.format;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;

import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientsController;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipeModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipesController;
import org.adhuc.cena.menu.port.adapter.rest.support.RequestValidatorDelegate;
import org.adhuc.cena.menu.recipes.*;

/**
 * The {@link RecipeIngredientsController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({RecipeIngredientsController.class, RecipesController.class, IngredientsController.class,
        RequestValidatorDelegate.class, RecipeIngredientModelAssembler.class, RecipeModelAssembler.class,
        IngredientModelAssembler.class})
@DisplayName("Recipe ingredients controller should")
class RecipeIngredientsControllerShould {

    private static final String RECIPE_INGREDIENTS_API_URL = "/api/recipes/{recipeId}/ingredients";
    private static final RecipeId RECIPE_ID = RecipeMother.ID;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RecipeIngredientsController controller;
    @MockBean
    private RecipeIngredientAppService recipeIngredientAppServiceMock;
    @MockBean
    private RecipeAppService recipeAppServiceMock;
    @MockBean
    private IngredientAppService ingredientAppServiceMock;

    @Nested
    @DisplayName("with 2 ingredients")
    class with2Ingredients {

        private List<RecipeIngredient> ingredients;

        @BeforeEach
        void setUp() {
            when(recipeAppServiceMock.getRecipe(RECIPE_ID)).thenReturn(recipe());
            ingredients = List.of(recipeIngredient(CUCUMBER_ID), recipeIngredient(TOMATO_ID));
        }

        @Test
        @DisplayName("respond OK when retrieving recipe ingredients")
        void respond200OnList() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENTS_API_URL, RECIPE_ID))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("respond with HAL content when retrieving recipe ingredients with no specific requested content")
        void respondHalOnList() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENTS_API_URL, RECIPE_ID))
                    .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
        }

        @Test
        @DisplayName("respond with JSON content when requested while retrieving recipe ingredients")
        void respondJSONOnList() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENTS_API_URL, RECIPE_ID)
                    .accept(APPLICATION_JSON)
            ).andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        }

        @Test
        @DisplayName("have self link with correct value when retrieving recipe ingredients")
        void haveSelfOnList() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENTS_API_URL, RECIPE_ID));
            result.andExpect(jsonPath("$._links.self.href",
                    equalTo(result.andReturn().getRequest().getRequestURL().toString())));
        }

        @Test
        @DisplayName("have recipe link with correct value when retrieving recipe ingredients")
        void haveRecipeLinkOnList() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENTS_API_URL, RECIPE_ID));
            result.andExpect(jsonPath("$._links.recipe.href",
                    endsWith(format("/api/recipes/%s", RECIPE_ID))));
        }

        @Test
        @DisplayName("have embedded data with 2 recipe ingredients when retrieving recipe ingredients")
        void haveDataWith2RecipeIngredients() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENTS_API_URL, RECIPE_ID))
                    .andExpect(jsonPath("$._embedded.data").isArray())
                    .andExpect(jsonPath("$._embedded.data").isNotEmpty())
                    .andExpect(jsonPath("$._embedded.data", hasSize(2)));
            assertJsonContainsRecipeIngredient(result, "$._embedded.data[0]", ingredients.get(0));
            assertJsonContainsRecipeIngredient(result, "$._embedded.data[1]", ingredients.get(1));
        }
    }

    @Nested
    @DisplayName("with empty list")
    class WithEmptyList {
        @BeforeEach
        void setUp() throws Exception {
            when(recipeAppServiceMock.getRecipe(RECIPE_ID)).thenReturn(fromDefault().build());
        }

        @Test
        @DisplayName("have empty embedded data when retrieving recipe ingredients")
        void haveEmptyDataOnEmptyList() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENTS_API_URL, RECIPE_ID))
                    .andExpect(jsonPath("$._embedded").doesNotExist());
        }
    }

    @Test
    @DisplayName("respond Created when creating recipe ingredient with JSON content")
    void respond201OnCreationJson() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, RECIPE_ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("respond Created when creating recipe ingredient with HAL content")
    void respond201OnCreationHal() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, RECIPE_ID)
                .contentType(HAL_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("respond with a Location header when creating recipe ingredient successfully")
    void respondWithLocationAfterCreationSuccess() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, RECIPE_ID)
                .contentType(HAL_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"}")
        ).andExpect(header().exists(LOCATION))
                .andExpect(header().string(LOCATION, endsWith("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }

    void assertJsonContainsRecipeIngredient(ResultActions resultActions, String jsonPath,
                                  RecipeIngredient recipeIngredient) throws Exception {
        resultActions.andExpect(jsonPath(jsonPath + ".id").exists())
                .andExpect(jsonPath(jsonPath + ".id", equalTo(recipeIngredient.ingredientId().toString())));
    }

}
