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
package org.adhuc.cena.menu.port.adapter.rest.ingredients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.adhuc.cena.menu.ingredients.CreateIngredient;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.ingredients.IngredientNameAlreadyUsedException;

/**
 * The {@link IngredientsController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({IngredientsController.class, IngredientResourceAssembler.class})
@DisplayName("Ingredients controller should")
class IngredientsControllerShould {

    private static final String INGREDIENTS_API_URL = "/api/ingredients";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IngredientAppService ingredientAppServiceMock;

    @Nested
    @DisplayName("with 2 ingredients")
    class with2Ingredients {

        private List<Ingredient> ingredients;

        @BeforeEach
        void setUp() {
            ingredients = List.of(ingredient(TOMATO_ID, TOMATO), ingredient(CUCUMBER_ID, CUCUMBER));
            when(ingredientAppServiceMock.getIngredients()).thenReturn(ingredients);
        }

        @Test
        @DisplayName("respond OK when retrieving ingredients")
        void respond200OnList() throws Exception {
            mvc.perform(get(INGREDIENTS_API_URL))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("respond with HAL content when retrieving ingredients with no specific requested content")
        void respondHalOnList() throws Exception {
            mvc.perform(get(INGREDIENTS_API_URL))
                    .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
        }

        @Test
        @DisplayName("respond with JSON content when requested while retrieving ingredients")
        void respondJSONOnList() throws Exception {
            mvc.perform(get(INGREDIENTS_API_URL)
                    .accept(APPLICATION_JSON)
            ).andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        }

        @Test
        @DisplayName("have self link with correct value when retrieving ingredients")
        void haveSelfOnList() throws Exception {
            mvc.perform(get(INGREDIENTS_API_URL))
                    .andExpect(jsonPath("$._links.self.href", Matchers.endsWith(INGREDIENTS_API_URL)));
        }

        @Test
        @DisplayName("have embedded data with 2 ingredients when retrieving ingredients")
        void haveDataWith2Ingredients() throws Exception {
            var result = mvc.perform(get(INGREDIENTS_API_URL))
                    .andExpect(jsonPath("$._embedded.data").isArray())
                    .andExpect(jsonPath("$._embedded.data").isNotEmpty())
                    .andExpect(jsonPath("$._embedded.data", hasSize(2)));
            assertJsonContainsIngredient(result, "$._embedded.data[0]", ingredients.get(0));
            assertJsonContainsIngredient(result, "$._embedded.data[1]", ingredients.get(1));
        }
    }

    @Nested
    @DisplayName("with empty list")
    class WithEmptyList {
        @BeforeEach
        void setUp() {
            when(ingredientAppServiceMock.getIngredients()).thenReturn(List.of());
        }

        @Test
        @DisplayName("have empty embedded data when retrieving ingredients")
        void haveEmptyDataOnEmptyList() throws Exception {
            mvc.perform(get(INGREDIENTS_API_URL))
                    .andExpect(jsonPath("$._embedded.data").isArray())
                    .andExpect(jsonPath("$._embedded.data").isEmpty());
        }
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
    @DisplayName("respond Bad Request when creating ingredient without name")
    void respond400OnCreationWithoutName() throws Exception {
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("respond Bad Request when creating ingredient with blank name")
    void respond400OnCreationWithBlankName() throws Exception {
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\" \t\"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("respond Bad Request when ingredient service throws IngredientNameAlreadyUsedException")
    void respond400OnCreationIngredientNameAlreadyUsed() throws Exception {
        doThrow(new IngredientNameAlreadyUsedException("Tomato")).when(ingredientAppServiceMock).createIngredient(any());
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato\"}")
        ).andExpect(status().isBadRequest());
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
    @DisplayName("respond with a Location header when creating ingredient successfully")
    void respondWithLocationAfterCreationSuccess() throws Exception {
        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato\"}")
        ).andExpect(header().exists(LOCATION));
    }

    @Test
    @DisplayName("call application service when creating ingredient")
    void callServiceOnCreation() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(CreateIngredient.class);

        mvc.perform(post(INGREDIENTS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"name\":\"%s\"}", NAME))
        ).andExpect(status().isCreated());

        verify(ingredientAppServiceMock).createIngredient(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualToIgnoringGivenFields(createCommand(), "ingredientId");
    }

    @Test
    @DisplayName("respond No Content when deleting ingredients")
    void respond204OnDeletion() throws Exception {
        mvc.perform(delete(INGREDIENTS_API_URL))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("call application service when deleting ingredients")
    void callServiceOnDeletion() throws Exception {
        mvc.perform(delete(INGREDIENTS_API_URL))
                .andExpect(status().isNoContent());

        verify(ingredientAppServiceMock).deleteIngredients();
    }

    void assertJsonContainsIngredient(ResultActions resultActions, String jsonPath,
                                      Ingredient ingredient) throws Exception {
        resultActions.andExpect(jsonPath(jsonPath + ".name").exists())
                .andExpect(jsonPath(jsonPath + ".name", equalTo(ingredient.name())));
    }

}
