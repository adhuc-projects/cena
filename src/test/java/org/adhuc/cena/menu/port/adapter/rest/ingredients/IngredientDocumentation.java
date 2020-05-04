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

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.adhuc.cena.menu.ingredients.IngredientMother.ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.ingredient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.support.WithIngredientManager;

/**
 * The ingredient related rest-services documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("documentation")
@WebMvcTest({IngredientsController.class, IngredientController.class, IngredientModelAssembler.class})
@ContextConfiguration(classes = ResultHandlerConfiguration.class)
@AutoConfigureRestDocs("build/generated-snippets")
@DisplayName("Ingredient resource documentation")
class IngredientDocumentation {

    private static final String INGREDIENT_API_URL = "/api/ingredients/{id}";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestDocumentationResultHandler documentationHandler;

    @MockBean
    private IngredientAppService ingredientAppServiceMock;

    @Test
    @DisplayName("generates ingredient detail example")
    void ingredientDetailExample() throws Exception {
        when(ingredientAppServiceMock.getIngredient(ID)).thenReturn(ingredient());

        mvc.perform(get(INGREDIENT_API_URL, ID.toString())).andExpect(status().isOk())
                .andDo(documentationHandler.document(
                        pathParameters(parameterWithName("id").description("The ingredient identity")),
                        links(linkWithRel("self").description("This <<resources-ingredient,ingredient>>")),
                        ingredientResponseFields("<<resources-ingredient-links,Links>> to other resources")));
    }

    @Test
    @WithIngredientManager
    @DisplayName("generates ingredient deletion example")
    void ingredientDeleteExample() throws Exception {
        mvc.perform(delete(INGREDIENT_API_URL, ID.toString())).andExpect(status().isNoContent())
                .andDo(documentationHandler.document(
                        pathParameters(parameterWithName("id").description("The ingredient identity"))));
    }

    static ResponseFieldsSnippet ingredientResponseFields(String linksDescription) {
        return responseFields(fieldWithPath("id").description("The ingredient identity"),
                fieldWithPath("name").description("The name of the ingredient"),
                fieldWithPath("measurementTypes").description("The types of measurements of the ingredient"),
                subsectionWithPath("_links").description(linksDescription));
    }

}
