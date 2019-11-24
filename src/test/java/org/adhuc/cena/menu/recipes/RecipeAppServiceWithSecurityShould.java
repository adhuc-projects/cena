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
package org.adhuc.cena.menu.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.recipes.RecipeMother.createCommand;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.adhuc.cena.menu.configuration.ApplicationSecurityConfiguration;
import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
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

    @Autowired
    private RecipeAppService service;

    @Test
    @WithCommunityUser
    @DisplayName("deny recipes deletion access to community user")
    void denyRecipesDeletionAsCommunityUser() {
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
        service.createRecipe(createCommand());
        assumeThat(service.getRecipes()).isNotEmpty();
        service.deleteRecipes();
        assertThat(service.getRecipes()).isEmpty();
    }

    @Configuration
    @Import(ApplicationSecurityConfiguration.class)
    @EnableConfigurationProperties(MenuGenerationProperties.class)
    @ComponentScan("org.adhuc.cena.menu.recipes")
    static class ServiceWithSecurityConfiguration {
    }

}
