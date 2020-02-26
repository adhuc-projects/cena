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

import static java.util.stream.Collectors.toList;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.adhuc.cena.menu.recipes.MeasurementUnit.UNDEFINED;
import static org.adhuc.cena.menu.recipes.RecipeMother.ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
import org.adhuc.cena.menu.ingredients.IngredientMother;
import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.port.adapter.rest.documentation.support.ConstrainedFields;
import org.adhuc.cena.menu.port.adapter.rest.documentation.support.ConstrainedFields.FieldAlias;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientsController;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipeModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipesController;
import org.adhuc.cena.menu.recipes.MeasurementUnit;
import org.adhuc.cena.menu.recipes.RecipeAppService;
import org.adhuc.cena.menu.recipes.RecipeIngredientAppService;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;

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
        RecipeIngredientModelAssembler.class, RecipeModelAssembler.class,
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
    @WithAuthenticatedUser
    @DisplayName("generates recipe ingredient creation example")
    void recipeIngredientsCreateExample() throws Exception {
        var fields = new ConstrainedFields(CreateRecipeIngredientRequest.class, new FieldAlias("id", "ingredientId"));
        mvc.perform(post(RECIPE_INGREDIENTS_API_URL, ID).contentType(APPLICATION_JSON)
                .content(String.format("{\"id\":\"%s\",\"quantity\":10,\"measurementUnit\":\"UNIT\"}", IngredientMother.ID)))
                .andExpect(status().isCreated()).andDo(documentationHandler
                .document(pathParameters(
                        parameterWithName("id").description("The <<resources-recipe,recipe>> identity")
                        ), requestFields(
                        fields.withPath("id").description("The recipe ingredient identity. Corresponds to the identity of the related <<resources-ingredient,ingredient>>"),
                        fields.withPath("quantity").description("The quantity of ingredient in the recipe"),
                        fields.withPath("measurementUnit").description("<<measurement-units-list,Unit of measurement>> " +
                                "for a quantity of ingredient in a recipe. Depends on the <<measurement-types-list,measurement types>> " +
                                "defined for the ingredient")
                        ),
                        new MeasurementUnitsSnippet()));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("generate recipe ingredients deletion example")
    void recipeIngredientsDeletionExample() throws Exception {
        mvc.perform(delete(RECIPE_INGREDIENTS_API_URL, ID))
                .andExpect(status().isNoContent()).andDo(documentationHandler
                .document(pathParameters(
                        parameterWithName("id").description("The <<resources-recipe,recipe>> identity")
                )));
    }

    private static class MeasurementUnitsSnippet extends TemplatedSnippet {
        public MeasurementUnitsSnippet() {
            super("measurement-units", null);
        }

        @Override
        protected Map<String, Object> createModel(Operation operation) {
            final Map<String, Object> model = new HashMap<>();
            model.put("measurementUnits", Arrays.stream(MeasurementUnit.values())
                    .filter(unit -> !UNDEFINED.equals(unit))
                    .map(unit -> new MeasurementUnitRepresentation(unit.name(), unit.associatedType().name()))
                    .collect(toList()));
            return model;
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static class MeasurementUnitRepresentation {
        private final String name;
        private final String type;
    }

}
