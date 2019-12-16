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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.ingredients.IngredientMother;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientsController;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipeModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipesController;
import org.adhuc.cena.menu.port.adapter.rest.support.RequestValidatorDelegate;
import org.adhuc.cena.menu.recipes.Recipe;
import org.adhuc.cena.menu.recipes.RecipeAppService;
import org.adhuc.cena.menu.recipes.RecipeIngredientAppService;

/**
 * The {@link RecipeIngredientController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({RecipeIngredientController.class, RecipeIngredientsController.class, RecipesController.class,
        IngredientsController.class, RequestValidatorDelegate.class, RecipeIngredientModelAssembler.class,
        RecipeModelAssembler.class, IngredientModelAssembler.class})
@DisplayName("Recipe ingredient controller should")
class RecipeIngredientControllerShould {

    private static final String RECIPE_INGREDIENT_API_URL = "/api/recipes/{recipeId}/ingredients/{ingredientId}";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RecipeIngredientAppService recipeIngredientAppServiceMock;
    @MockBean
    private RecipeAppService recipeAppServiceMock;
    @MockBean
    private IngredientAppService ingredientAppServiceMock;

    @Test
    @DisplayName("respond Not Found when retrieving ingredient from unknown recipe")
    void respond404GetRecipeIngredientUnknownRecipe() throws Exception {
        when(recipeAppServiceMock.getRecipe(ID)).thenThrow(new EntityNotFoundException(Recipe.class, ID));
        mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("respond Not Found when retrieving unknown ingredient from recipe")
    void respond404GetRecipeIngredientUnknownIngredient() throws Exception {
        when(recipeAppServiceMock.getRecipe(ID)).thenReturn(fromDefault().build());
        mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("respond Not Found when deleting ingredient from unknown recipe")
    void respond404DeleteRecipeIngredientUnknownRecipe() throws Exception {
        doThrow(new EntityNotFoundException(Recipe.class, ID)).when(recipeIngredientAppServiceMock)
                .removeIngredientFromRecipe(removeIngredientCommand());
        mvc.perform(delete(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("respond Not Found when deleting unknown ingredient from recipe")
    void respond404DeleteRecipeIngredientUnknownIngredient() throws Exception {
        doThrow(new EntityNotFoundException(Ingredient.class, IngredientMother.ID)).when(recipeIngredientAppServiceMock)
                .removeIngredientFromRecipe(removeIngredientCommand());
        mvc.perform(delete(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("return No Content status on recipe ingredient deletion")
    void deleteRecipeIngredientStatusNoContent() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID)).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("call application service on recipe ingredient deletion")
    void deleteRecipeIngredientCallAppService() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID)).andReturn();
        verify(recipeIngredientAppServiceMock).removeIngredientFromRecipe(removeIngredientCommand());
    }

    @Nested
    @DisplayName("getting tomato in tomato, cucumber and mozzarella salad detail")
    class TomatoDetail {

        @BeforeEach
        void setUp() {
            when(recipeAppServiceMock.getRecipe(ID)).thenReturn(recipe());
        }

        @Test
        @DisplayName("return OK status")
        void getRecipeIngredientFoundStatusOK() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID)).andExpect(status().isOk());
        }

        @Test
        @DisplayName("respond with HAL content when retrieving recipe ingredient with no specific requested content")
        void respondHalOnDetail() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID))
                    .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
        }

        @Test
        @DisplayName("respond with JSON content when requested while retrieving recipe ingredient")
        void respondJSONOnDetail() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID).accept(APPLICATION_JSON))
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).andReturn();
        }

        @Test
        @DisplayName("contain recipe ingredient data")
        void getRecipeIngredientFoundContainsData() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID))
                    .andExpect(jsonPath("$.id").value(IngredientMother.ID.toString()));
        }

        @Test
        @DisplayName("contain self link to detail")
        void getRecipeIngredientHasSelfLink() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID));
            result.andExpect(jsonPath("$._links.self.href").exists())
                    .andExpect(jsonPath("$._links.self.href",
                            equalTo(result.andReturn().getRequest().getRequestURL().toString())));
        }

        @Test
        @DisplayName("contain recipe link")
        void getRecipeIngredientHasRecipeLink() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID));
            result.andExpect(jsonPath("$._links.recipe.href").exists())
                    .andExpect(jsonPath("$._links.recipe.href").value(
                            Matchers.endsWith(String.format("/recipes/%s", ID.toString()))));
        }

        @Test
        @DisplayName("contain ingredient link")
        void getRecipeIngredientHasIngredientLink() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENT_API_URL, ID, IngredientMother.ID));
            result.andExpect(jsonPath("$._links.ingredient.href").exists())
                    .andExpect(jsonPath("$._links.ingredient.href").value(
                            Matchers.endsWith(String.format("/ingredients/%s", IngredientMother.ID.toString()))));
        }

    }

}
