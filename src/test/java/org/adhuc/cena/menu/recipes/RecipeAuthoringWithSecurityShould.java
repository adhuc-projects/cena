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
package org.adhuc.cena.menu.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import static org.adhuc.cena.menu.common.security.RolesDefinition.INGREDIENT_MANAGER_ROLE;
import static org.adhuc.cena.menu.common.security.RolesDefinition.USER_ROLE;
import static org.adhuc.cena.menu.ingredients.IngredientMother.*;
import static org.adhuc.cena.menu.recipes.RecipeMother.ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.createCommand;
import static org.adhuc.cena.menu.recipes.RecipeMother.deleteCommand;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.adhuc.cena.menu.configuration.ApplicationSecurityConfiguration;
import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.ingredients.IngredientConsultation;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link RecipeAuthoringImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Recipe authoring service with security should")
class RecipeAuthoringWithSecurityShould {

    private static final String RECIPE_AUTHOR_NAME = "recipe author";

    private static final Condition<RecipeIngredient> TOMATO_PRESENT = new Condition<>(i -> i.ingredientId().equals(TOMATO_ID),
            "tomato ingredient");

    @Autowired
    private RecipeAuthoring service;
    @Autowired
    private RecipeRepository repository;
    @MockBean
    private IngredientConsultation ingredientConsultationMock;

    @BeforeEach
    void setUp() {
        when(ingredientConsultationMock.getIngredient(TOMATO_ID)).thenReturn(ingredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES));
        when(ingredientConsultationMock.getIngredient(CUCUMBER_ID)).thenReturn(ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES));

        repository.deleteAll();
        repository.save(builder().withAuthorName(RECIPE_AUTHOR_NAME).withIngredients(CUCUMBER_ID).build());
        assumeThat(repository.findNotNullById(ID).ingredients()).noneMatch(i -> i.ingredientId().equals(TOMATO_ID));
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe creation access to community user")
    void denyRecipeCreationAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.createRecipe(createCommand()));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant recipe creation access to authenticated user")
    void grantRecipeCreationAsAuthenticatedUser() {
        assumeThat(repository.exists(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isFalse();
        service.createRecipe(createCommand(builder().withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID).build()));
        assertThat(repository.exists(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isTrue();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant recipe creation access to ingredient manager")
    void grantRecipeCreationAsIngredientManager() {
        assumeThat(repository.exists(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isFalse();
        service.createRecipe(createCommand(builder().withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID).build()));
        assertThat(repository.exists(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isTrue();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe creation access to super administrator")
    void grantRecipeCreationAsSuperAdministrator() {
        assumeThat(repository.exists(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isFalse();
        service.createRecipe(createCommand(builder().withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID).build()));
        assertThat(repository.exists(ID)).isTrue();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe deletion access to community user")
    void denyRecipeDeletionAsCommunityUser() {
        assumeThat(repository.exists(ID)).isTrue();
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipe(deleteCommand()));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = USER_ROLE)
    @DisplayName("grant recipe deletion access to recipe author")
    void grantRecipeDeletionAsRecipeAuthor() {
        assumeThat(repository.exists(ID)).isTrue();
        service.deleteRecipe(deleteCommand());
        assertThat(repository.exists(ID)).isFalse();
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny recipe deletion access to authenticated user that is not recipe author")
    void denyRecipeDeletionAsAuthenticatedUserNotRecipeAuthor() {
        assumeThat(repository.exists(ID)).isTrue();
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipe(deleteCommand()));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = INGREDIENT_MANAGER_ROLE)
    @DisplayName("grant recipe deletion access to ingredient manager that is recipe author")
    void grantRecipeDeletionAsIngredientManagerRecipeAuthor() {
        assumeThat(repository.exists(ID)).isTrue();
        service.deleteRecipe(deleteCommand());
        assertThat(repository.exists(ID)).isFalse();
    }

    @Test
    @WithIngredientManager
    @DisplayName("deny recipe deletion access to ingredient manager that is not recipe author")
    void denyRecipeDeletionAsIngredientManagerNotRecipeAuthor() {
        assumeThat(repository.exists(ID)).isTrue();
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipe(deleteCommand()));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe deletion access to super administrator")
    void grantRecipeDeletionAsSuperAdministrator() {
        assumeThat(repository.exists(ID)).isTrue();
        service.deleteRecipe(deleteCommand());
        assertThat(repository.exists(ID)).isFalse();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe ingredient addition access to community user")
    void denyRecipeIngredientAdditionAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.addIngredientToRecipe(addIngredientCommand()));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = USER_ROLE)
    @DisplayName("grant recipe ingredient addition access to recipe author")
    void grantRecipeIngredientAdditionAsRecipeAuthor() {
        var command = addIngredientCommand();
        service.addIngredientToRecipe(command);
        assertThat(repository.findNotNullById(ID).ingredients()).haveExactly(1, TOMATO_PRESENT);
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny recipe ingredient addition access to authenticated user that is not recipe author")
    void denyRecipeIngredientAdditionAsAuthenticatedUserNotRecipeAuthor() {
        assertThrows(AccessDeniedException.class, () -> service.addIngredientToRecipe(addIngredientCommand()));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = INGREDIENT_MANAGER_ROLE)
    @DisplayName("grant recipe ingredient addition access to ingredient manager that is recipe author")
    void grantRecipeIngredientAdditionAsIngredientManagerRecipeAuthor() {
        service.addIngredientToRecipe(addIngredientCommand());
        assertThat(repository.findNotNullById(ID).ingredients()).haveExactly(1, TOMATO_PRESENT);
    }

    @Test
    @WithIngredientManager
    @DisplayName("deny recipe ingredient addition access to ingredient manager that is not recipe author")
    void denyRecipeIngredientAdditionAsIngredientManagerNotRecipeAuthor() {
        assertThrows(AccessDeniedException.class, () -> service.addIngredientToRecipe(addIngredientCommand()));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe ingredient addition access to super administrator")
    void grantRecipeIngredientAdditionAsSuperAdministrator() {
        service.addIngredientToRecipe(addIngredientCommand());
        assertThat(repository.findNotNullById(ID).ingredients()).haveExactly(1, TOMATO_PRESENT);
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe ingredient removal access to community user")
    void denyRecipeIngredientRemovalAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID)));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = USER_ROLE)
    @DisplayName("grant recipe ingredient removal access to recipe author")
    void grantRecipeIngredientRemovalAsRecipeAuthor() {
        assumeThat(repository.findNotNullById(ID).ingredients()).isNotEmpty();
        service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID));
        assertThat(repository.findNotNullById(ID).ingredients()).isEmpty();
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny recipe ingredient removal access to authenticated user that is not recipe author")
    void denyRecipeIngredientRemovalAsAuthenticatedUserNotRecipeAuthor() {
        assertThrows(AccessDeniedException.class, () -> service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID)));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = INGREDIENT_MANAGER_ROLE)
    @DisplayName("grant recipe ingredient removal access to ingredient manager that is recipe author")
    void grantRecipeIngredientRemovalAsIngredientManagerRecipeAuthor() {
        assumeThat(repository.findNotNullById(ID).ingredients()).isNotEmpty();
        service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID));
        assertThat(repository.findNotNullById(ID).ingredients()).isEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("deny recipe ingredient removal access to ingredient manager that is not recipe author")
    void denyRecipeIngredientRemovalAsIngredientManagerNotRecipeAuthor() {
        assertThrows(AccessDeniedException.class, () -> service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID)));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe ingredient removal access to super administrator")
    void grantRecipeIngredientRemovalAsSuperAdministrator() {
        assumeThat(repository.findNotNullById(ID).ingredients()).isNotEmpty();
        service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID));
        assertThat(repository.findNotNullById(ID).ingredients()).isEmpty();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe ingredients removal access to community user")
    void denyRecipeIngredientsRemovalAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.removeIngredientsFromRecipe(removeIngredientsCommand()));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = USER_ROLE)
    @DisplayName("grant recipe ingredients removal access to recipe author")
    void grantRecipeIngredientsRemovalAsRecipeAuthor() {
        assumeThat(repository.findNotNullById(ID).ingredients()).isNotEmpty();
        service.removeIngredientsFromRecipe(removeIngredientsCommand());
        assertThat(repository.findNotNullById(ID).ingredients()).isEmpty();
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny recipe ingredients removal access to authenticated user that is not recipe author")
    void denyRecipeIngredientsRemovalAsAuthenticatedUserNotRecipeAuthor() {
        assertThrows(AccessDeniedException.class, () -> service.removeIngredientsFromRecipe(removeIngredientsCommand()));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = INGREDIENT_MANAGER_ROLE)
    @DisplayName("grant recipe ingredients removal access to ingredient manager that is recipe author")
    void grantRecipeIngredientsRemovalAsIngredientManagerRecipeAuthor() {
        assumeThat(repository.findNotNullById(ID).ingredients()).isNotEmpty();
        service.removeIngredientsFromRecipe(removeIngredientsCommand());
        assertThat(repository.findNotNullById(ID).ingredients()).isEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("deny recipe ingredients removal access to ingredient manager that is not recipe author")
    void denyRecipeIngredientsRemovalAsIngredientManagerNotRecipeAuthor() {
        assertThrows(AccessDeniedException.class, () -> service.removeIngredientsFromRecipe(removeIngredientsCommand()));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe ingredients removal access to super administrator")
    void grantRecipeIngredientsRemovalAsSuperAdministrator() {
        assumeThat(repository.findNotNullById(ID).ingredients()).isNotEmpty();
        service.removeIngredientsFromRecipe(removeIngredientsCommand());
        assertThat(repository.findNotNullById(ID).ingredients()).isEmpty();
    }

    @Configuration
    @Import(ApplicationSecurityConfiguration.class)
    @EnableConfigurationProperties(MenuGenerationProperties.class)
    @ComponentScan("org.adhuc.cena.menu.recipes")
    static class ServiceWithSecurityConfiguration {
        @Bean
        RecipeRepository recipeRepository() {
            return new InMemoryRecipeRepository();
        }
    }

}
