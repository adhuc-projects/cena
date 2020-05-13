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
package org.adhuc.cena.menu.ingredients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.adhuc.cena.menu.configuration.ApplicationSecurityConfiguration;
import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.recipes.RecipeRepository;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link IngredientManagementImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Ingredient management service with security should")
class IngredientManagementWithSecurityShould {

    @Autowired
    private IngredientAdministration service;
    @Autowired
    private IngredientRepository repository;
    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private IngredientRelatedService ingredientRelatedService;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES));
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny ingredient deletion access to community user")
    void denyIngredientsDeletionAsCommunityUser() {
        assumeThat(repository.findAll()).isNotEmpty();
        assertThrows(AccessDeniedException.class, () -> service.deleteIngredients());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny ingredient deletion access to authenticated user")
    void denyIngredientsDeletionAsAuthenticatedUser() {
        assumeThat(repository.findAll()).isNotEmpty();
        assertThrows(AccessDeniedException.class, () -> service.deleteIngredients());
    }

    @Test
    @WithIngredientManager
    @DisplayName("deny ingredient deletion access to ingredient manager")
    void denyIngredientsDeletionAsIngredientManager() {
        assumeThat(repository.findAll()).isNotEmpty();
        assertThrows(AccessDeniedException.class, () -> service.deleteIngredients());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant ingredients deletion access to super administrator")
    void grantIngredientsDeletionAsSuperAdministrator() {
        assumeThat(repository.findAll()).isNotEmpty();
        service.deleteIngredients();
        assertThat(repository.findAll()).isEmpty();
    }

    @Configuration
    @Import(ApplicationSecurityConfiguration.class)
    @EnableConfigurationProperties(MenuGenerationProperties.class)
    @ComponentScan("org.adhuc.cena.menu.ingredients")
    static class ServiceWithSecurityConfiguration {
        @Bean
        IngredientRepository ingredientRepository() {
            return new InMemoryIngredientRepository();
        }
    }

}
