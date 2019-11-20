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
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.recipes.RecipeMother.ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.recipes.RecipeId;

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
    private static final String RECIPE_API_URL = "/api/recipes/{id}";

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
    @DisplayName("respond Not Found when retrieving unknown recipe")
    void respond404GetUnknownRecipe() throws Exception {
        mvc.perform(get(RECIPE_API_URL, RecipeId.generate())).andExpect(status().isNotFound());
    }

    @Nested
    @DisplayName("getting tomato, cucumber and mozzarella salad detail")
    class TomatoCucumberMozzaSaladDetail {

        @BeforeEach
        void setUp() throws Exception {
            RecipeId.reset();
            mvc.perform(post(RECIPES_API_URL)
                    .contentType(HAL_JSON)
                    .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"}")
            );
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
