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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.adhuc.cena.menu.recipes.CreateRecipe;
import org.adhuc.cena.menu.recipes.Recipe;
import org.adhuc.cena.menu.recipes.RecipeAppService;

/**
 * The {@link RecipesController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({RecipesController.class, RecipeModelAssembler.class})
@DisplayName("Recipes controller should")
class RecipesControllerShould {

    private static final String RECIPES_API_URL = "/api/recipes";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RecipeAppService recipeAppServiceMock;

    @Nested
    @DisplayName("with 2 ingredients")
    class with2Ingredients {

        private List<Recipe> recipes;

        @BeforeEach
        void setUp() {
            recipes = List.of(recipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID, TOMATO_CUCUMBER_MOZZA_SALAD_NAME,
                    TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT), recipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID,
                    TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT));
            when(recipeAppServiceMock.getRecipes()).thenReturn(recipes);
        }

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
        @DisplayName("have embedded data with 2 recipes when retrieving recipes")
        void haveDataWith2Recipes() throws Exception {
            var result = mvc.perform(get(RECIPES_API_URL))
                    .andExpect(jsonPath("$._embedded.data").isArray())
                    .andExpect(jsonPath("$._embedded.data").isNotEmpty())
                    .andExpect(jsonPath("$._embedded.data", hasSize(2)));
            assertJsonContainsRecipe(result, "$._embedded.data[0]", recipes.get(0));
            assertJsonContainsRecipe(result, "$._embedded.data[1]", recipes.get(1));
        }
    }

    @Nested
    @DisplayName("with empty list")
    class WithEmptyList {
        @BeforeEach
        void setUp() {
            when(recipeAppServiceMock.getRecipes()).thenReturn(List.of());
        }

        @Test
        @DisplayName("have empty embedded data when retrieving recipes")
        void haveEmptyDataOnEmptyList() throws Exception {
            mvc.perform(get(RECIPES_API_URL))
                    .andExpect(jsonPath("$._embedded").doesNotExist());
        }
    }

    @Test
    @DisplayName("respond Created when creating recipe with JSON content")
    void respond201OnCreationJson() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("respond Created when creating recipe with HAL content")
    void respond201OnCreationHal() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"}")
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("respond with a Location header when creating recipe successfully")
    void respondWithLocationAfterCreationSuccess() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"}")
        ).andExpect(header().exists(LOCATION));
    }

    @Test
    @DisplayName("call application service when creating recipe")
    void callServiceOnCreation() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(CreateRecipe.class);

        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"name\":\"%s\",\"content\":\"%s\"}", NAME, CONTENT))
        ).andExpect(status().isCreated());

        verify(recipeAppServiceMock).createRecipe(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualToIgnoringGivenFields(createCommand(), "recipeId");
    }

    void assertJsonContainsRecipe(ResultActions resultActions, String jsonPath,
                                  Recipe recipe) throws Exception {
        resultActions.andExpect(jsonPath(jsonPath + ".name").exists())
                .andExpect(jsonPath(jsonPath + ".name", equalTo(recipe.name())))
                .andExpect(jsonPath(jsonPath + ".content").exists())
                .andExpect(jsonPath(jsonPath + ".content", equalTo(recipe.content())));
    }

}
