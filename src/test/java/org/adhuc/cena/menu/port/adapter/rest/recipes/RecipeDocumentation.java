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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.adhuc.cena.menu.recipes.RecipeMother.ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;

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

import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientsController;
import org.adhuc.cena.menu.recipes.RecipeConsultation;
import org.adhuc.cena.menu.recipes.RecipeAuthoring;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The recipe related rest-services documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("documentation")
@WebMvcTest({RecipesController.class, RecipeController.class, RecipeIngredientsController.class,
        RecipeModelAssembler.class, RecipeIngredientModelAssembler.class})
@ContextConfiguration(classes = ResultHandlerConfiguration.class)
@AutoConfigureRestDocs("build/generated-snippets")
@DisplayName("Recipe resource documentation")
class RecipeDocumentation {

    private static final String RECIPE_API_URL = "/api/recipes/{id}";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestDocumentationResultHandler documentationHandler;

    @MockBean
    private RecipeConsultation recipeConsultationMock;
    @MockBean
    private RecipeAuthoring recipeAuthoringMock;

    @Test
    @DisplayName("generates recipe detail example")
    void recipeDetailExample() throws Exception {
        when(recipeConsultationMock.getRecipe(ID)).thenReturn(recipe());

        mvc.perform(get(RECIPE_API_URL, ID.toString())).andExpect(status().isOk())
                .andDo(documentationHandler.document(
                        pathParameters(parameterWithName("id").description("The recipe identity")),
                        links(
                                linkWithRel("self").description("This <<resources-recipe,recipe>>"),
                                linkWithRel("ingredients").description("This <<resources-recipe-ingredients,recipe ingredients list>>")
                        ),
                        recipeResponseFields("<<resources-recipe-links,Links>> to other resources")));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("generates recipe deletion example")
    void recipeDeleteExample() throws Exception {
        mvc.perform(delete(RECIPE_API_URL, ID.toString())).andExpect(status().isNoContent())
                .andDo(documentationHandler.document(
                        pathParameters(parameterWithName("id").description("The recipe identity"))));
    }

    static ResponseFieldsSnippet recipeResponseFields(String linksDescription) {
        return responseFields(fieldWithPath("id").description("The recipe identity"),
                fieldWithPath("name").description("The name of the recipe"),
                fieldWithPath("content").description("The content of the recipe"),
                fieldWithPath("author").description("The author name of the recipe"),
                fieldWithPath("servings").description("The number of servings for the recipe"),
                subsectionWithPath("_links").description(linksDescription));
    }

}
