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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.TOMATO_ID;
import static org.adhuc.cena.menu.recipes.Quantity.UNDEFINED;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.ingredients.IngredientMother;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientsController;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipeModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipesController;
import org.adhuc.cena.menu.recipes.*;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

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
        RecipeIngredientModelAssembler.class, RecipeModelAssembler.class,
        IngredientModelAssembler.class})
@DisplayName("Recipe ingredients controller should")
class RecipeIngredientsControllerShould {

    private static final String RECIPE_INGREDIENTS_API_URL = "/api/recipes/{recipeId}/ingredients";

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

    @Test
    @DisplayName("respond Not Found when retrieving unknown recipe ingredients")
    void respond404GetUnknownRecipeIngredients() throws Exception {
        when(recipeAppServiceMock.getRecipe(ID)).thenThrow(new EntityNotFoundException(Recipe.class, RecipeMother.ID));
        mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID)).andExpect(status().isNotFound());
    }

    @Nested
    @DisplayName("with 2 ingredients")
    class with2Ingredients {

        private List<RecipeIngredient> ingredients;

        @BeforeEach
        void setUp() {
            var recipe = builder().withIngredient(TOMATO_ID, QUANTITY).andIngredient(CUCUMBER_ID, UNDEFINED).build();
            when(recipeAppServiceMock.getRecipe(ID)).thenReturn(recipe);
            ingredients = new ArrayList<>(recipe.ingredients());
        }

        @Test
        @DisplayName("respond OK when retrieving recipe ingredients")
        void respond200OnList() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("respond with HAL content when retrieving recipe ingredients with no specific requested content")
        void respondHalOnList() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID))
                    .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
        }

        @Test
        @DisplayName("respond with JSON content when requested while retrieving recipe ingredients")
        void respondJSONOnList() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID)
                    .accept(APPLICATION_JSON)
            ).andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        }

        @Test
        @DisplayName("have self link with correct value when retrieving recipe ingredients")
        void haveSelfOnList() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID));
            result.andExpect(jsonPath("$._links.self.href",
                    equalTo(result.andReturn().getRequest().getRequestURL().toString())));
        }

        @Test
        @DisplayName("have recipe link with correct value when retrieving recipe ingredients")
        void haveRecipeLinkOnList() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID));
            result.andExpect(jsonPath("$._links.recipe.href",
                    Matchers.endsWith(format("/api/recipes/%s", ID))));
        }

        @Test
        @DisplayName("have embedded data with 2 recipe ingredients when retrieving recipe ingredients")
        void haveDataWith2RecipeIngredients() throws Exception {
            var result = mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID))
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
            when(recipeAppServiceMock.getRecipe(ID)).thenReturn(builder().build());
        }

        @Test
        @DisplayName("have empty embedded data when retrieving recipe ingredients")
        void haveEmptyDataOnEmptyList() throws Exception {
            mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID))
                    .andExpect(jsonPath("$._embedded").doesNotExist());
        }
    }

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when creating recipe ingredient as a community user")
    void respond401OnCreationAsCommunityUser() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"quantity\":1, \"measurementUnit\":\"DOZEN\"}")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Not Found when creating recipe ingredient for unknown recipe")
    void respond404CreateUnknownRecipe() throws Exception {
        doThrow(new EntityNotFoundException(Recipe.class, RecipeMother.ID))
                .when(recipeIngredientAppServiceMock).addIngredientToRecipe(Mockito.any());
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"quantity\":1, \"measurementUnit\":\"DOZEN\"}")
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating recipe ingredient without id")
    void respond400OnCreationWithoutId() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating recipe ingredient with quantity but no measurement unit")
    void respond400OnCreationWithQuantityNoMeasurementUnit() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"quantity\":1}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating recipe ingredient with measurement unit but no quantity")
    void respond400OnCreationWithMeasurementUnitNoQuantity() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"measurementUnit\":\"DOZEN\"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating recipe ingredient with JSON content")
    void respond201OnCreationJson() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"quantity\":1, \"measurementUnit\":\"DOZEN\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating recipe ingredient with HAL content")
    void respond201OnCreationHal() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(HAL_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"quantity\":1, \"measurementUnit\":\"DOZEN\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond with a Location header when creating recipe ingredient successfully")
    void respondWithLocationAfterCreationSuccess() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(HAL_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"quantity\":1, \"measurementUnit\":\"DOZEN\"}")
        ).andExpect(header().exists(LOCATION))
                .andExpect(header().string(LOCATION, Matchers.endsWith("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when creating recipe ingredient with quantity")
    void callServiceWithQuantityOnCreationWithQuantity() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"quantity\":1, \"measurementUnit\":\"DOZEN\"}")
        ).andExpect(status().isCreated());

        verify(recipeIngredientAppServiceMock).addIngredientToRecipe(addIngredientCommand(IngredientMother.ID, ID, QUANTITY));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating recipe ingredient with no quantity")
    void respond201OnCreationWithoutQuantity() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when creating recipe ingredient with no quantity")
    void callServiceWithUnknownQuantityOnCreationWithoutQuantity() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"}")
        ).andExpect(status().isCreated());

        verify(recipeIngredientAppServiceMock).addIngredientToRecipe(addIngredientCommand(IngredientMother.ID, ID, UNDEFINED));
    }

    @Test
    @WithIngredientManager
    @DisplayName("respond Created when creating recipe ingredient as ingredient manager")
    void respond201OnCreationAsIngredientManager() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("respond Created when creating recipe ingredient as super administrator")
    void respond201OnCreationAsSuperAdministrator() throws Exception {
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when deleting recipe's ingredients as a community user")
    void respond401OnDeletionAsCommunityUser() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENTS_API_URL, ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond No Content when deleting recipe's ingredients")
    void respond204OnDeletion() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENTS_API_URL, ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when deleting recipe's ingredients")
    void callServiceOnDeletion() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENTS_API_URL, ID))
                .andExpect(status().isNoContent());

        verify(recipeIngredientAppServiceMock).removeIngredientsFromRecipe(removeIngredientsCommand());
    }

    @Test
    @WithIngredientManager
    @DisplayName("respond No Content when deleting recipe's ingredients as ingredient manager")
    void respond204OnDeletionAsIngredientManager() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENTS_API_URL, ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("respond No Content when deleting recipe's ingredients as super administrator")
    void respond204OnDeletionAsSuperAdministrator() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENTS_API_URL, ID))
                .andExpect(status().isNoContent());
    }

    void assertJsonContainsRecipeIngredient(ResultActions resultActions, String jsonPath,
                                  RecipeIngredient recipeIngredient) throws Exception {
        resultActions.andExpect(jsonPath(jsonPath + ".id").exists())
                .andExpect(jsonPath(jsonPath + ".id", equalTo(recipeIngredient.ingredientId().toString())));
        if (UNDEFINED.equals(recipeIngredient.quantity())) {
            resultActions
                    .andExpect(jsonPath(jsonPath + ".quantity").doesNotExist())
                    .andExpect(jsonPath(jsonPath + ".measurementUnit").doesNotExist());
        } else {
            resultActions
                    .andExpect(jsonPath(jsonPath + ".quantity").exists())
                    .andExpect(jsonPath(jsonPath + ".quantity", equalTo(recipeIngredient.quantity().value())))
                    .andExpect(jsonPath(jsonPath + ".measurementUnit").exists())
                    .andExpect(jsonPath(jsonPath + ".measurementUnit", equalTo(recipeIngredient.quantity().unit().name())));
        }
    }

}
