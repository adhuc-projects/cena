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

import static java.util.stream.Collectors.joining;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.recipes.RecipeMother.*;
import static org.adhuc.cena.menu.recipes.Servings.DEFAULT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.adhuc.cena.menu.ingredients.IngredientMother;
import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientModelAssembler;
import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientsController;
import org.adhuc.cena.menu.recipes.*;
import org.adhuc.cena.menu.support.*;

/**
 * The {@link RecipesController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({RecipesController.class, RecipesDeletionController.class, RecipeIngredientsController.class,
        RecipeModelAssembler.class, RecipeIngredientModelAssembler.class})
@DisplayName("Recipes controller should")
class RecipesControllerShould {

    private static final String RECIPES_API_URL = "/api/recipes";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RecipeConsultation recipeConsultationMock;
    @MockBean
    private RecipeAuthoring recipeAuthoringMock;
    @MockBean
    private RecipeAdministration recipeAdministrationMock;

    @Nested
    @DisplayName("with 2 recipes")
    class With2Recipes {

        private List<Recipe> recipes;

        @BeforeEach
        void setUp() {
            recipes = new ArrayList<>(recipes());
            when(recipeConsultationMock.getRecipes(QueryRecipes.query())).thenReturn(recipes);
        }

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
            var result = mvc.perform(get(RECIPES_API_URL));
            result.andExpect(jsonPath("$._links.self.href",
                    Matchers.equalTo(result.andReturn().getRequest().getRequestURL().toString())));
        }

        @Test
        @DisplayName("have embedded data with 2 recipes when retrieving recipes")
        void haveDataWith2Recipes() throws Exception {
            var result = mvc.perform(get(RECIPES_API_URL))
                    .andExpect(jsonPath("$._embedded.data").isArray())
                    .andExpect(jsonPath("$._embedded.data").isNotEmpty())
                    .andExpect(jsonPath("$._embedded.data", Matchers.hasSize(2)));
            assertJsonContainsRecipe(result, "$._embedded.data[0]", recipes.get(0));
            assertJsonContainsRecipe(result, "$._embedded.data[1]", recipes.get(1));
        }
    }

    @Test
    @DisplayName("not apply ingredient filter when not present")
    void notApplyIngredientFilterWhenNotPresent() throws Exception {
        var queryCaptor = ArgumentCaptor.forClass(QueryRecipes.class);
        when(recipeConsultationMock.getRecipes(any())).thenReturn(new ArrayList<>(recipes()));
        mvc.perform(get(RECIPES_API_URL)).andExpect(status().isOk());
        // At least once because of a Pitest error when defaulting to 1
        verify(recipeConsultationMock, atLeastOnce()).getRecipes(queryCaptor.capture());
        assertThat(queryCaptor.getValue().ingredientId()).isEmpty();
    }

    @Test
    @DisplayName("respond Bad Request when querying recipes list with empty ingredient parameter")
    void respond400OnListWithEmptyUuidFilter() throws Exception {
        mvc.perform(get(RECIPES_API_URL).param("filter[ingredient]", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("respond Bad Request when querying recipes list with ingredient parameter not being a valid UUID")
    void respond400OnListWithNonUuidFilter() throws Exception {
        mvc.perform(get(RECIPES_API_URL).param("filter[ingredient]", "invalid uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("apply ingredient filter when filled with valid UUID")
    void applyIngredientFilterWhenFilled() throws Exception {
        var queryCaptor = ArgumentCaptor.forClass(QueryRecipes.class);
        when(recipeConsultationMock.getRecipes(any())).thenReturn(new ArrayList<>(recipes()));
        mvc.perform(get(RECIPES_API_URL).param("filter[ingredient]", IngredientMother.ID.toString())).andExpect(status().isOk());
        verify(recipeConsultationMock).getRecipes(queryCaptor.capture());
        assertThat(queryCaptor.getValue().ingredientId()).contains(IngredientMother.ID);
    }

    @Nested
    @DisplayName("with empty list")
    class WithEmptyList {
        @BeforeEach
        void setUp() {
            when(recipeConsultationMock.getRecipes(QueryRecipes.query())).thenReturn(List.of());
        }

        @Test
        @DisplayName("have empty embedded data when retrieving recipes")
        void haveEmptyDataOnEmptyList() throws Exception {
            mvc.perform(get(RECIPES_API_URL))
                    .andExpect(jsonPath("$._embedded").doesNotExist());
        }
    }

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when creating recipe as a community user")
    void respond401OnCreationAsCommunityUser() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating recipe without name")
    void respond400OnCreationWithoutName() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating recipe with blank name")
    void respond400OnCreationWithBlankName() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\" \",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating recipe without content")
    void respond400OnCreationWithoutContent() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating recipe with blank content")
    void respond400OnCreationWithBlankContent() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\" \"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating recipe with negative servings")
    void respond400OnCreationWithNegativeServings(int value) throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":%d,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}", value))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating recipe with JSON content")
    void respond201OnCreationJson() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating recipe with HAL content")
    void respond201OnCreationHal() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond with a Location header when creating recipe successfully")
    void respondWithLocationAfterCreationSuccess() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(header().exists(LOCATION));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when creating recipe")
    void callServiceOnCreation() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(CreateRecipe.class);
        var expectedCommand = createCommand(builder().withAuthorName(UserProvider.AUTHENTICATED_USER).build());

        var courseTypes = COURSE_TYPES.stream().map(type -> "\"" + type.name() + "\"").collect(joining(",", "[", "]"));
        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"name\":\"%s\",\"content\":\"%s\",\"servings\":%d,\"courseTypes\":%s}", NAME, CONTENT, SERVINGS.value(), courseTypes))
        ).andExpect(status().isCreated());

        verify(recipeAuthoringMock).createRecipe(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualToIgnoringGivenFields(expectedCommand, "recipeId");
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when creating recipe without servings")
    void callServiceOnCreationWithoutServings() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(CreateRecipe.class);
        var expectedCommand = createCommand(builder().withAuthorName(UserProvider.AUTHENTICATED_USER).withServings(DEFAULT).build());

        var courseTypes = COURSE_TYPES.stream().map(type -> "\"" + type.name() + "\"").collect(joining(",", "[", "]"));
        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"name\":\"%s\",\"content\":\"%s\",\"courseTypes\":%s}", NAME, CONTENT, courseTypes))
        ).andExpect(status().isCreated());

        verify(recipeAuthoringMock).createRecipe(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualToIgnoringGivenFields(expectedCommand, "recipeId");
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when creating recipe without course type")
    void callServiceOnCreationWithoutCourseTypes() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(CreateRecipe.class);
        var expectedCommand = createCommand(builder().withAuthorName(UserProvider.AUTHENTICATED_USER)
                .withCourseTypes(Collections.emptySet()).build());

        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"name\":\"%s\",\"content\":\"%s\",\"servings\":%d}", NAME, CONTENT, SERVINGS.value()))
        ).andExpect(status().isCreated());

        verify(recipeAuthoringMock).createRecipe(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualToIgnoringGivenFields(expectedCommand, "recipeId");
    }

    @Test
    @WithIngredientManager
    @DisplayName("respond Created when creating recipe as ingredient manager")
    void respond201OnCreationAsIngredientManager() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("respond Created when creating recipe as super administrator")
    void respond201OnCreationAsSuperAdministrator() throws Exception {
        mvc.perform(post(RECIPES_API_URL)
                .contentType(HAL_JSON)
                .content("{\"name\":\"Tomato, cucumber and mozzarella salad\",\"content\":\"Cut everything into dices, mix it, dress it\"," +
                        "\"servings\":2,\"courseTypes\":[\"STARTER\",\"MAIN_COURSE\"]}")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when deleting recipes as a community user")
    void respond401OnDeletionAsCommunityUser() throws Exception {
        mvc.perform(delete(RECIPES_API_URL)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Forbidden when deleting recipes as an authenticated user")
    void respond403OnDeletionAsAuthenticatedUser() throws Exception {
        mvc.perform(delete(RECIPES_API_URL)).andExpect(status().isForbidden());
    }

    @Test
    @WithIngredientManager
    @DisplayName("respond Forbidden when deleting recipes as an ingredient manager")
    void respond403OnDeletionAsIngredientManager() throws Exception {
        mvc.perform(delete(RECIPES_API_URL)).andExpect(status().isForbidden());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("respond No Content when deleting recipes")
    void respond204OnDeletion() throws Exception {
        mvc.perform(delete(RECIPES_API_URL))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("call application service when deleting recipes")
    void callServiceOnDeletion() throws Exception {
        mvc.perform(delete(RECIPES_API_URL))
                .andExpect(status().isNoContent());

        verify(recipeAdministrationMock).deleteRecipes();
    }

    void assertJsonContainsRecipe(ResultActions resultActions, String jsonPath,
                                  Recipe recipe) throws Exception {
        resultActions.andExpect(jsonPath(jsonPath + ".name").exists())
                .andExpect(jsonPath(jsonPath + ".name", Matchers.equalTo(recipe.name().value())))
                .andExpect(jsonPath(jsonPath + ".content").exists())
                .andExpect(jsonPath(jsonPath + ".content", Matchers.equalTo(recipe.content())))
                .andExpect(jsonPath(jsonPath + ".author").exists())
                .andExpect(jsonPath(jsonPath + ".author", Matchers.equalTo(recipe.author().authorName())))
                .andExpect(jsonPath(jsonPath + ".servings").exists())
                .andExpect(jsonPath(jsonPath + ".servings", Matchers.equalTo(recipe.servings().value())));
    }

}
