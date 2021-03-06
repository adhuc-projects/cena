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
package org.adhuc.cena.menu.port.adapter.rest.ingredients;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientConsultation;
import org.adhuc.cena.menu.ingredients.IngredientManagement;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link IngredientController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({IngredientsController.class, IngredientController.class, IngredientModelAssembler.class})
@DisplayName("Ingredient controller should")
class IngredientControllerShould {

    private static final String INGREDIENT_API_URL = "/api/ingredients/{id}";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IngredientConsultation ingredientConsultationMock;
    @MockBean
    private IngredientManagement ingredientManagementMock;

    @Test
    @DisplayName("respond Not Found when retrieving unknown ingredient")
    void respond404GetUnknownIngredient() throws Exception {
        when(ingredientConsultationMock.getIngredient(ID)).thenThrow(new EntityNotFoundException(Ingredient.class, ID));
        mvc.perform(get(INGREDIENT_API_URL, ID)).andExpect(status().isNotFound());
    }

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when deleting ingredient as an anonymous user")
    void respond401OnDeletionAsAnonymous() throws Exception {
        mvc.perform(delete(INGREDIENT_API_URL, ID)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithIngredientManager
    @DisplayName("respond Not Found when deleting unknown ingredient")
    void respond404DeleteUnknownIngredient() throws Exception {
        doThrow(new EntityNotFoundException(Ingredient.class, ID)).when(ingredientManagementMock).deleteIngredient(deleteCommand());
        mvc.perform(delete(INGREDIENT_API_URL, ID)).andExpect(status().isNotFound());
    }

    @Test
    @WithIngredientManager
    @DisplayName("respond No Content when deleting ingredient successfully")
    void respond204DeleteIngredient() throws Exception {
        mvc.perform(delete(INGREDIENT_API_URL, ID)).andExpect(status().isNoContent());
        verify(ingredientManagementMock).deleteIngredient(deleteCommand());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("respond No Content when deleting ingredient successfully as super administrator")
    void respond204DeleteIngredientAsSuperAdmin() throws Exception {
        mvc.perform(delete(INGREDIENT_API_URL, ID)).andExpect(status().isNoContent());
        verify(ingredientManagementMock).deleteIngredient(deleteCommand());
    }

    @Nested
    @DisplayName("getting tomato detail")
    class TomatoDetail {

        @BeforeEach
        void setUp() {
            when(ingredientConsultationMock.getIngredient(ID)).thenReturn(ingredient());
        }

        @Test
        @DisplayName("return OK status")
        void getIngredientFoundStatusOK() throws Exception {
            mvc.perform(get(INGREDIENT_API_URL, ID)).andExpect(status().isOk());
        }

        @Test
        @DisplayName("respond with HAL content when retrieving ingredient with no specific requested content")
        void respondHalOnDetail() throws Exception {
            mvc.perform(get(INGREDIENT_API_URL, ID))
                    .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
        }

        @Test
        @DisplayName("respond with JSON content when requested while retrieving ingredient")
        void respondJSONOnDetail() throws Exception {
            mvc.perform(get(INGREDIENT_API_URL, ID).accept(APPLICATION_JSON))
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        }

        @Test
        @DisplayName("contain tomato data")
        void getIngredientFoundContainsData() throws Exception {
            var ingredient = ingredient();
            assumeThat(ingredient.measurementTypes().size()).isEqualTo(2);

            mvc.perform(get(INGREDIENT_API_URL, ID))
                    .andExpect(jsonPath("$.id").value(ingredient.id().toString()))
                    .andExpect(jsonPath("$.name").value(ingredient.name().value()))
                    .andExpect(jsonPath("$.measurementTypes").isArray())
                    .andExpect(jsonPath("$.measurementTypes", hasSize(2)))
                    .andExpect(jsonPath("$.measurementTypes[0]").value(ingredient.measurementTypes().get(0).toString()))
                    .andExpect(jsonPath("$.measurementTypes[1]").value(ingredient.measurementTypes().get(1).toString()));
        }

        @Test
        @DisplayName("contain self link to detail")
        void getIngredientHasSelfLink() throws Exception {
            var result = mvc.perform(get(INGREDIENT_API_URL, ID));
            result.andExpect(jsonPath("$._links.self.href").exists())
                    .andExpect(jsonPath("$._links.self.href", equalTo(result.andReturn().getRequest().getRequestURL().toString())));
        }

    }

    @Test
    @DisplayName("not contain any measurement type when retrieving ingredient without measurement type")
    void getIngredientWithoutMeasurementType() throws Exception {
        var ingredient = ingredient(ID, NAME, List.of());
        when(ingredientConsultationMock.getIngredient(ID)).thenReturn(ingredient);

        mvc.perform(get(INGREDIENT_API_URL, ID))
                .andExpect(jsonPath("$.id").value(ingredient.id().toString()))
                .andExpect(jsonPath("$.name").value(ingredient.name().value()))
                .andExpect(jsonPath("$.measurementTypes").doesNotExist());
    }

}
