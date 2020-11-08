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
import org.adhuc.cena.menu.recipes.RecipeConsultation;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link IngredientAdministrationImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Ingredient administration service with security should")
class IngredientAdministrationWithSecurityShould {

    @Autowired
    private IngredientManagement service;
    @Autowired
    private IngredientRepository repository;
    @MockBean
    private RecipeConsultation recipeConsultation;
    @MockBean
    private IngredientRelatedService ingredientRelatedService;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES));
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny ingredient creation access to community user")
    void denyIngredientCreationAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.createIngredient(createCommand()));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny ingredient creation access to authenticated user")
    void denyIngredientCreationAsAuthenticatedUser() {
        assertThrows(AccessDeniedException.class, () -> service.createIngredient(createCommand()));
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant ingredient creation access to ingredient manager")
    void grantIngredientCreationAsIngredientManager() {
        service.createIngredient(createCommand());
        assertThat(repository.exists(ID)).isTrue();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant ingredient creation access to super administrator")
    void grantIngredientCreationAsSuperAdministrator() {
        service.createIngredient(createCommand());
        assertThat(repository.exists(ID)).isTrue();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny ingredient deletion access to community user")
    void denyIngredientDeletionAsCommunityUser() {
        assumeThat(repository.exists(CUCUMBER_ID)).isTrue();
        assertThrows(AccessDeniedException.class, () -> service.deleteIngredient(deleteCommand(CUCUMBER_ID)));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny ingredient deletion access to authenticated user")
    void denyIngredientDeletionAsAuthenticatedUser() {
        assumeThat(repository.exists(CUCUMBER_ID)).isTrue();
        assertThrows(AccessDeniedException.class, () -> service.deleteIngredient(deleteCommand(CUCUMBER_ID)));
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant ingredient deletion access to ingredient manager")
    void grantIngredientDeletionAsIngredientManager() {
        assumeThat(repository.exists(CUCUMBER_ID)).isTrue();
        service.deleteIngredient(deleteCommand(CUCUMBER_ID));
        assertThat(repository.exists(CUCUMBER_ID)).isFalse();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant ingredient deletion access to super administrator")
    void grantIngredientDeletionAsSuperAdministator() {
        assumeThat(repository.exists(CUCUMBER_ID)).isTrue();
        service.deleteIngredient(deleteCommand(CUCUMBER_ID));
        assertThat(repository.exists(CUCUMBER_ID)).isFalse();
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
