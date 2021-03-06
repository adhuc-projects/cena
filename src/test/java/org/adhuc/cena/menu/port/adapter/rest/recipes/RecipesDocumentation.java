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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.adhuc.cena.menu.recipes.RecipeMother.recipes;

import java.util.ArrayList;
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

import org.adhuc.cena.menu.ingredients.IngredientMother;
import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.port.adapter.rest.documentation.support.ConstrainedFields;
import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientsController;
import org.adhuc.cena.menu.recipes.CourseType;
import org.adhuc.cena.menu.recipes.RecipeAdministration;
import org.adhuc.cena.menu.recipes.RecipeAuthoring;
import org.adhuc.cena.menu.recipes.RecipeConsultation;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The recipes related rest-services documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("documentation")
@WebMvcTest({RecipesController.class, RecipesDeletionController.class, RecipeIngredientsController.class,
        RecipeModelAssembler.class, RecipeIngredientModelAssembler.class})
@ContextConfiguration(classes = ResultHandlerConfiguration.class)
@AutoConfigureRestDocs("build/generated-snippets")
@DisplayName("Recipes resource documentation")
class RecipesDocumentation {

    private static final String RECIPES_API_URL = "/api/recipes";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestDocumentationResultHandler documentationHandler;

    @MockBean
    private RecipeConsultation recipeConsultationMock;
    @MockBean
    private RecipeAuthoring recipeAuthoringMock;
    @MockBean
    private RecipeAdministration recipeAdministrationMock;

    @Test
    @DisplayName("generates recipes list example")
    void recipesListExample() throws Exception {
        when(recipeConsultationMock.getRecipes(any())).thenReturn(new ArrayList<>(recipes()));

        mvc.perform(get(RECIPES_API_URL).param("filter[ingredient]", IngredientMother.ID.toString()))
                .andExpect(status().isOk())
                .andDo(documentationHandler.document(
                        requestParameters(parameterWithName("filter[ingredient]").optional()
                                .description("The <<resources-ingredient,ingredient>> identity to filter recipes list on, resulting in a list of recipes composed of the specified ingredient _(optional)_")),
                        links(linkWithRel("self").description("This <<resources-recipes,recipes list>>")),
                        responseFields(
                                subsectionWithPath("_embedded.data")
                                        .description("An array of <<resources-recipe, Recipe resources>>"),
                                subsectionWithPath("_links")
                                        .description("<<resources-recipes-links,Links>> to other resources")
                        )
                ));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("generates recipe creation example")
    void recipesCreateExample() throws Exception {
        var fields = new ConstrainedFields(CreateRecipeRequest.class);
        mvc.perform(post(RECIPES_API_URL).contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}"))
                .andExpect(status().isCreated()).andDo(documentationHandler
                .document(requestFields(
                        fields.withPath("name").description("The name of the recipe"),
                        fields.withPath("content").description("The content of the recipe"),
                        fields.withPath("servings").description("The number of servings for the recipe"),
                        fields.withPath("courseTypes").description("The <<course-types-list, types of course>> this recipe can be used for")
                        ),
                        new CourseTypesSnippet()));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("generates recipes deletion example")
    void recipesDeleteExample() throws Exception {
        mvc.perform(delete(RECIPES_API_URL))
                .andExpect(status().isNoContent()).andDo(documentationHandler.document());
    }

    private static class CourseTypesSnippet extends TemplatedSnippet {
        public CourseTypesSnippet() {
            super("course-types", null);
        }

        @Override
        protected Map<String, Object> createModel(Operation operation) {
            final Map<String, Object> model = new HashMap<>();
            model.put("courseTypes", List.of(CourseType.values()));
            return model;
        }
    }

}
