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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.adhuc.cena.menu.configuration.ApplicationSecurityConfiguration;
import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.recipes.RecipeConsultation;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link IngredientConsultationImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Ingredient consultation service with security should")
class IngredientConsultationWithSecurityShould {

    @Autowired
    private IngredientConsultation service;
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
    @DisplayName("grant ingredient listing access to community user")
    void grantIngredientListAsCommunityUser() {
        assertThat(service.getIngredients()).isNotEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant ingredient listing access to ingredient manager")
    void grantIngredientListAsIngredientManager() {
        assertThat(service.getIngredients()).isNotEmpty();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant ingredient listing access to super administrator")
    void grantIngredientListAsSuperAdministrator() {
        assertThat(service.getIngredients()).isNotEmpty();
    }

    @Test
    @WithCommunityUser
    @DisplayName("grant ingredient detail access to community user")
    void grantIngredientDetailAsCommunityUser() {
        assertThat(service.getIngredient(CUCUMBER_ID)).isNotNull();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant ingredient listing access to ingredient manager")
    void grantIngredientDetailAsIngredientManager() {
        assertThat(service.getIngredient(CUCUMBER_ID)).isNotNull();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant ingredient listing access to super administrator")
    void grantIngredientDetailAsSuperAdministrator() {
        assertThat(service.getIngredient(CUCUMBER_ID)).isNotNull();
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
