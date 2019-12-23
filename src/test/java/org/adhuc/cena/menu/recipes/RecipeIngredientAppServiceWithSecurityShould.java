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

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.adhuc.cena.menu.configuration.ApplicationSecurityConfiguration;
import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.ingredients.IngredientRepository;
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryIngredientRepository;
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryRecipeRepository;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link RecipeIngredientAppServiceImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Recipe ingredient service with security should")
class RecipeIngredientAppServiceWithSecurityShould {

    private static final Condition<RecipeIngredient> TOMATO_PRESENT = new Condition<>(i -> i.ingredientId().equals(TOMATO_ID),
            "tomato ingredient");

    @Autowired
    private RecipeIngredientAppService service;
    @Autowired
    private RecipeRepository repository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll();
        ingredientRepository.save(ingredient(TOMATO_ID, TOMATO));
        ingredientRepository.save(ingredient(CUCUMBER_ID, CUCUMBER));

        repository.deleteAll();
        repository.save(builder().withIngredients(CUCUMBER_ID).build());
        assumeThat(repository.findNotNullById(RecipeMother.ID).ingredients()).noneMatch(i -> i.ingredientId().equals(TOMATO_ID));
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe ingredient addition access to community user")
    void denyRecipeIngredientAdditionAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.addIngredientToRecipe(addIngredientCommand()));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant recipe ingredient addition access to authenticated user")
    void grantRecipeIngredientAdditionAsAuthenticatedUser() {
        service.addIngredientToRecipe(addIngredientCommand());
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).haveExactly(1, TOMATO_PRESENT);
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant recipe ingredient addition access to ingredient manager")
    void grantRecipeIngredientAdditionAsIngredientManager() {
        service.addIngredientToRecipe(addIngredientCommand());
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).haveExactly(1, TOMATO_PRESENT);
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe ingredient addition access to super administrator")
    void grantRecipeIngredientAdditionAsSuperAdministrator() {
        service.addIngredientToRecipe(addIngredientCommand());
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).haveExactly(1, TOMATO_PRESENT);
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe ingredient removal access to community user")
    void denyRecipeIngredientRemovalAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID)));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant recipe ingredient removal access to authenticated user")
    void grantRecipeIngredientRemovalAsAuthenticatedUser() {
        assumeThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isNotEmpty();
        service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID));
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant recipe ingredient removal access to ingredient manager")
    void grantRecipeIngredientRemovalAsIngredientManager() {
        assumeThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isNotEmpty();
        service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID));
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isEmpty();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe ingredient removal access to super administrator")
    void grantRecipeIngredientRemovalAsSuperAdministrator() {
        assumeThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isNotEmpty();
        service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID));
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isEmpty();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny recipe ingredients removal access to community user")
    void denyRecipeIngredientsRemovalAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.removeIngredientsFromRecipe(removeIngredientsCommand()));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant recipe ingredients removal access to authenticated user")
    void grantRecipeIngredientsRemovalAsAuthenticatedUser() {
        assumeThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isNotEmpty();
        service.removeIngredientsFromRecipe(removeIngredientsCommand());
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant recipe ingredients removal access to ingredient manager")
    void grantRecipeIngredientsRemovalAsIngredientManager() {
        assumeThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isNotEmpty();
        service.removeIngredientsFromRecipe(removeIngredientsCommand());
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isEmpty();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant recipe ingredients removal access to super administrator")
    void grantRecipeIngredientsRemovalAsSuperAdministrator() {
        assumeThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isNotEmpty();
        service.removeIngredientsFromRecipe(removeIngredientsCommand());
        assertThat(repository.findNotNullById(RecipeMother.ID).ingredients()).isEmpty();
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

        @Bean
        IngredientRepository ingredientRepository() {
            return new InMemoryIngredientRepository();
        }
    }

}
