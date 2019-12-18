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

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.ingredients.IngredientMother;
import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.port.adapter.rest.documentation.support.ConstrainedFields;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientsController;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipeModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipesController;
import org.adhuc.cena.menu.port.adapter.rest.support.RequestValidatorDelegate;
import org.adhuc.cena.menu.recipes.RecipeAppService;
import org.adhuc.cena.menu.recipes.RecipeIngredientAppService;

/**
 * The recipe ingredients related rest-services documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("documentation")
@WebMvcTest({RecipeIngredientsController.class, RecipesController.class, IngredientsController.class,
        RequestValidatorDelegate.class, RecipeIngredientModelAssembler.class, RecipeModelAssembler.class,
        IngredientModelAssembler.class})
@ContextConfiguration(classes = ResultHandlerConfiguration.class)
@AutoConfigureRestDocs("build/generated-snippets")
@DisplayName("Recipe ingredients resource documentation")
class RecipeIngredientsDocumentation {

    private static final String RECIPE_INGREDIENTS_API_URL = "/api/recipes/{id}/ingredients";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestDocumentationResultHandler documentationHandler;

    @MockBean
    private RecipeIngredientAppService recipeIngredientAppServiceMock;
    @MockBean
    private RecipeAppService recipeAppServiceMock;
    @MockBean
    private IngredientAppService ingredientAppServiceMock;

    @Test
    @DisplayName("generates recipe ingredients list example")
    void recipeIngredientsListExample() throws Exception {
        when(recipeAppServiceMock.getRecipe(ID)).thenReturn(recipe());

        mvc.perform(get(RECIPE_INGREDIENTS_API_URL, ID)).andExpect(status().isOk())
                .andDo(documentationHandler.document(
                        links(
                                linkWithRel("self").description("This <<resources-recipe-ingredients,recipe's ingredients list>>"),
                                linkWithRel("recipe").description("The related <<resources-recipe,recipe>>")
                        ),
                        pathParameters(
                                parameterWithName("id").description("The <<resources-recipe,recipe>> identity")
                        ),
                        responseFields(
                                subsectionWithPath("_embedded.data")
                                        .description("An array of <<resources-recipe-ingredient, Recipe ingredient resources>>"),
                                subsectionWithPath("_links")
                                        .description("<<resources-recipe-ingredients-links,Links>> to other resources")
                        )
                ));
    }

    @Test
    @DisplayName("generates recipe ingredient creation example")
    void recipeIngredientsCreateExample() throws Exception {
        var fields = new ConstrainedFields(CreateRecipeIngredientRequest.class);
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID).contentType(APPLICATION_JSON)
                .content(String.format("{\"id\":\"%s\"}", IngredientMother.ID)))
                .andExpect(status().isCreated()).andDo(documentationHandler
                .document(pathParameters(
                        parameterWithName("id").description("The <<resources-recipe,recipe>> identity")
                ), requestFields(
                        fields.withPath("id").description("The ingredient identity")
                )));
    }

    @Test
    @DisplayName("generate recipe ingredients deletion example")
    void recipeIngredientsDeletionExample() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENTS_API_URL, ID))
                .andExpect(status().isNoContent()).andDo(documentationHandler
                .document(pathParameters(
                        parameterWithName("id").description("The <<resources-recipe,recipe>> identity")
                )));
    }

}
