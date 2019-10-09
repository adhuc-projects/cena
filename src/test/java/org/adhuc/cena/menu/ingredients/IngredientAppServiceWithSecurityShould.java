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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;

/**
 * The {@link IngredientAppServiceImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
// TODO avoid using SpringBootTest as it starts the complete Spring context, but we only need a service with security
@SpringBootTest
@DisplayName("Ingredient service with security should")
class IngredientAppServiceWithSecurityShould {

    @Autowired
    private IngredientAppService service;
    @Autowired
    private IngredientRepository repository;

    @BeforeEach
    void setUp() {
        repository.save(ingredient(CUCUMBER_ID, CUCUMBER));
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
    @WithCommunityUser
    @DisplayName("deny ingredient creation access to community user")
    void denyIngredientCreationAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.createIngredient(createCommand()));
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant ingredient creation access to ingredient manager")
    void grantIngredientCreationAsIngredientManager() {
        service.createIngredient(createCommand());
        assertThat(service.getIngredient(ID)).isNotNull();
    }

    @Test
    @WithCommunityUser
    @DisplayName("grant ingredient deletion access to community user")
    void grantIngredientDeletionAsCommunityUser() {
        assumeThat(service.getIngredient(CUCUMBER_ID)).isNotNull();
        service.deleteIngredient(deleteCommand(CUCUMBER_ID));
        assertThrows(EntityNotFoundException.class, () -> service.getIngredient(ID));
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant ingredient deletion access to ingredient manager")
    void grantIngredientDeletionAsIngredientManager() {
        assumeThat(service.getIngredient(CUCUMBER_ID)).isNotNull();
        service.deleteIngredient(deleteCommand(CUCUMBER_ID));
        assertThrows(EntityNotFoundException.class, () -> service.getIngredient(ID));
    }

    @Test
    @WithCommunityUser
    @DisplayName("grant ingredients deletion access to community user")
    void grantIngredientsDeletionAsCommunityUser() {
        assumeThat(service.getIngredients()).isNotEmpty();
        service.deleteIngredients();
        assertThat(service.getIngredients()).isEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant ingredients deletion access to ingredient manager")
    void grantIngredientsDeletionAsIngredientManager() {
        assumeThat(service.getIngredients()).isNotEmpty();
        service.deleteIngredients();
        assertThat(service.getIngredients()).isEmpty();
    }

}
