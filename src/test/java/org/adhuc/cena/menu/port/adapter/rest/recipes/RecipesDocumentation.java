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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipes;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.port.adapter.rest.documentation.support.ConstrainedFields;
import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientsController;
import org.adhuc.cena.menu.port.adapter.rest.support.RequestValidatorDelegate;
import org.adhuc.cena.menu.recipes.RecipeAppService;
import org.adhuc.cena.menu.recipes.RecipeIngredientAppService;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The recipes related rest-services documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("documentation")
@WebMvcTest({RecipesController.class, RecipesDeletionController.class, RecipeIngredientsController.class,
        RequestValidatorDelegate.class, RecipeModelAssembler.class, RecipeIngredientModelAssembler.class})
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
    private RecipeAppService recipeAppServiceMock;
    @MockBean
    private RecipeIngredientAppService recipeIngredientAppServiceMock;

    @Test
    @DisplayName("generates recipes list example")
    void recipesListExample() throws Exception {
        when(recipeAppServiceMock.getRecipes()).thenReturn(new ArrayList<>(recipes()));

        mvc.perform(get(RECIPES_API_URL)).andExpect(status().isOk())
                .andDo(documentationHandler.document(
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
        when(recipeAppServiceMock.createRecipe(any())).thenReturn(recipe());

        var fields = new ConstrainedFields(CreateRecipeRequest.class);
        mvc.perform(post(RECIPES_API_URL).contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"}"))
                .andExpect(status().isCreated()).andDo(documentationHandler
                .document(requestFields(
                        fields.withPath("name").description("The name of the recipe"),
                        fields.withPath("content").description("The content of the recipe")
                )));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("generates recipes deletion example")
    void recipesDeleteExample() throws Exception {
        mvc.perform(delete(RECIPES_API_URL))
                .andExpect(status().isNoContent()).andDo(documentationHandler.document());
    }

}
