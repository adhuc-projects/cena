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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.ingredients.MeasurementType;
import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.port.adapter.rest.documentation.support.ConstrainedFields;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The ingredients related rest-services documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("documentation")
@WebMvcTest({IngredientsController.class, IngredientsDeletionController.class, IngredientModelAssembler.class})
@ContextConfiguration(classes = ResultHandlerConfiguration.class)
@AutoConfigureRestDocs("build/generated-snippets")
@DisplayName("Ingredients resource documentation")
class IngredientsDocumentation {

    private static final String INGREDIENTS_API_URL = "/api/ingredients";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestDocumentationResultHandler documentationHandler;

    @MockBean
    private IngredientAppService ingredientAppServiceMock;

    @Test
    @DisplayName("generates ingredients list example")
    void ingredientsListExample() throws Exception {
        when(ingredientAppServiceMock.getIngredients()).thenReturn(List.of(
                ingredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES),
                ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES)));

        mvc.perform(get(INGREDIENTS_API_URL)).andExpect(status().isOk())
                .andDo(documentationHandler.document(
                        links(linkWithRel("self").description("This <<resources-ingredients,ingredients list>>")),
                        responseFields(
                                subsectionWithPath("_embedded.data")
                                        .description("An array of <<resources-ingredient, Ingredient resources>>"),
                                subsectionWithPath("_links")
                                        .description("<<resources-ingredients-links,Links>> to other resources"))));
    }

    @Test
    @WithIngredientManager
    @DisplayName("generates ingredient creation example")
    void ingredientsCreateExample() throws Exception {
        when(ingredientAppServiceMock.createIngredient(any())).thenReturn(ingredient());

        var fields = new ConstrainedFields(CreateIngredientRequest.class);
        mvc.perform(post(INGREDIENTS_API_URL).contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato\",\"measurementTypes\":[\"WEIGHT\", \"COUNT\"]}"))
                .andExpect(status().isCreated()).andDo(documentationHandler
                .document(requestFields(
                        fields.withPath("name").description("The name of the ingredient, unique in the system"),
                        fields.withPath("measurementTypes").description("The <<measurement-types-list, types of measurement>> of the ingredient")
                        ),
                        new MeasurementTypesSnippet()));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("generates ingredients deletion example")
    void ingredientsDeleteExample() throws Exception {
        mvc.perform(delete(INGREDIENTS_API_URL))
                .andExpect(status().isNoContent()).andDo(documentationHandler.document());
    }

    private static class MeasurementTypesSnippet extends TemplatedSnippet {
        public MeasurementTypesSnippet() {
            super("measurement-types", null);
        }

        @Override
        protected Map<String, Object> createModel(Operation operation) {
            final Map<String, Object> model = new HashMap<>();
            model.put("measurementTypes", List.of(MeasurementType.values()));
            return model;
        }
    }

}
