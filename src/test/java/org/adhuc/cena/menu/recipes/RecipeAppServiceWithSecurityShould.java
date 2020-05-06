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

import static org.adhuc.cena.menu.common.security.RolesDefinition.INGREDIENT_MANAGER_ROLE;
import static org.adhuc.cena.menu.common.security.RolesDefinition.USER_ROLE;
import static org.adhuc.cena.menu.recipes.QueryRecipes.query;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

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

import org.adhuc.cena.menu.common.entity.EntityNotFoundException;
import org.adhuc.cena.menu.configuration.ApplicationSecurityConfiguration;
import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link RecipeAppServiceImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Recipe service with security should")
class RecipeAppServiceWithSecurityShould {

    private static final String RECIPE_AUTHOR_NAME = "recipe author";

    @Autowired
    private RecipeAppService service;
    @Autowired
    private RecipeRepository repository;
    @MockBean
    private IngredientAppService ingredientAppService;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(builder().withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID).withAuthorName(RECIPE_AUTHOR_NAME).build());
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
        service.createRecipe(createCommand());
        assertThat(service.getRecipe(ID)).isNotNull();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant recipe creation access to ingredient manager")
    void grantRecipeCreationAsIngredientManager() {
        service.createRecipe(createCommand());
        assertThat(service.getRecipe(ID)).isNotNull();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe creation access to super administrator")
    void grantRecipeCreationAsSuperAdministrator() {
        service.createRecipe(createCommand());
        assertThat(service.getRecipe(ID)).isNotNull();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipes deletion access to community user")
    void denyRecipesDeletionAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipes());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny recipes deletion access to authenticated user")
    void denyRecipesDeletionAsAuthenticatedUser() {
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipes());
    }

    @Test
    @WithIngredientManager
    @DisplayName("deny recipes deletion access to ingredient manager")
    void denyRecipesDeletionAsIngredientManager() {
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipes());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipes deletion access to super administrator")
    void grantRecipesDeletionAsSuperAdministrator() {
        assumeThat(service.getRecipes(query())).isNotEmpty();
        service.deleteRecipes();
        assertThat(service.getRecipes(query())).isEmpty();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe deletion access to community user")
    void denyRecipeDeletionAsCommunityUser() {
        assumeThat(service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isNotNull();
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipe(deleteCommand(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = USER_ROLE)
    @DisplayName("grant recipe deletion access to recipe author")
    void grantRecipeDeletionAsRecipeAuthor() {
        assumeThat(service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isNotNull();
        service.deleteRecipe(deleteCommand(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
        assertThrows(EntityNotFoundException.class, () -> service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny recipe deletion access to authenticated user that is not recipe author")
    void denyRecipeDeletionAsAuthenticatedUserNotRecipeAuthor() {
        assumeThat(service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isNotNull();
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipe(deleteCommand(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)));
    }

    @Test
    @WithMockUser(username = RECIPE_AUTHOR_NAME, roles = INGREDIENT_MANAGER_ROLE)
    @DisplayName("grant recipe deletion access to ingredient manager that is recipe author")
    void grantRecipeDeletionAsIngredientManagerRecipeAuthor() {
        assumeThat(service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isNotNull();
        service.deleteRecipe(deleteCommand(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
        assertThrows(EntityNotFoundException.class, () -> service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
    }

    @Test
    @WithIngredientManager
    @DisplayName("deny recipe deletion access to ingredient manager that is not recipe author")
    void denyRecipeDeletionAsIngredientManagerNotRecipeAuthor() {
        assumeThat(service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isNotNull();
        assertThrows(AccessDeniedException.class, () -> service.deleteRecipe(deleteCommand(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)));
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe deletion access to super administrator")
    void grantRecipeDeletionAsSuperAdministrator() {
        assumeThat(service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isNotNull();
        service.deleteRecipe(deleteCommand(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
        assertThrows(EntityNotFoundException.class, () -> service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
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
