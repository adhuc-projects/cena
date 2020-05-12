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

import static org.adhuc.cena.menu.recipes.QueryRecipes.query;
import static org.adhuc.cena.menu.recipes.RecipeMother.ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.adhuc.cena.menu.configuration.ApplicationSecurityConfiguration;
import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.ingredients.IngredientConsultationAppService;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link RecipeConsultationAppServiceImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Recipe consultation service with security should")
class RecipeConsultationAppServiceWithSecurityShould {

    @Autowired
    private RecipeConsultationAppService service;
    @Autowired
    private RecipeRepository repository;
    @MockBean
    private IngredientConsultationAppService ingredientConsultationMock;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(recipe());
    }

    @Test
    @WithCommunityUser
    @DisplayName("grant recipes listing access to community user")
    void grantRecipeListAsCommunityUser() {
        assertThat(service.getRecipes(query())).isNotEmpty();
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant recipes listing access to authenticated user")
    void grantRecipeListAsAuthenticatedUser() {
        assertThat(service.getRecipes(query())).isNotEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant recipes listing access to ingredient manager")
    void grantRecipeListAsIngredientManager() {
        assertThat(service.getRecipes(query())).isNotEmpty();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipes listing access to super administrator")
    void grantRecipeListAsSuperAdministrator() {
        assertThat(service.getRecipes(query())).isNotEmpty();
    }

    @Test
    @WithCommunityUser
    @DisplayName("grant recipe detail access to community user")
    void grantRecipeDetailAsCommunityUser() {
        assertThat(service.getRecipe(ID)).isNotNull();
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant recipe detail access to authenticated user")
    void grantRecipeDetailAsAuthenticatedUser() {
        assertThat(service.getRecipe(ID)).isNotNull();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant recipe detail access to ingredient manager")
    void grantRecipeDetailAsIngredientManager() {
        assertThat(service.getRecipe(ID)).isNotNull();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe detail access to super administrator")
    void grantRecipeDetailAsSuperAdministrator() {
        assertThat(service.getRecipe(ID)).isNotNull();
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
