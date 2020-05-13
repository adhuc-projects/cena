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
package org.adhuc.cena.menu.menus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import static org.adhuc.cena.menu.menus.MenuMother.*;
import static org.adhuc.cena.menu.support.UserProvider.*;

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
import org.adhuc.cena.menu.recipes.RecipeMother;
import org.adhuc.cena.menu.recipes.RecipeRepository;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link MenuConsultationImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Menu management service with security should")
class MenuManagementWithSecurityShould {

    @Autowired
    private MenuManagement service;
    @Autowired
    private MenuRepository menuRepository;
    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private RecipeConsultation recipeConsultationMock;

    @BeforeEach
    void setUp() {
        menuRepository.deleteAll();

        when(recipeConsultationMock.exists(RecipeMother.ID)).thenReturn(true);
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny menu creation access to community user")
    void denyMenuCreationAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.createMenu(createCommand()));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant menu creation access to authenticated user")
    void grantMenuCreationAsAuthenticatedUser() {
        var menu = builder().withOwnerName(AUTHENTICATED_USER).build();
        service.createMenu(createCommand(menu));
        assertThat(menuRepository.exists(menu.id())).isTrue();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant menu creation access to ingredient manager")
    void grantMenuCreationAsIngredientManager() {
        var menu = builder().withOwnerName(INGREDIENT_MANAGER).build();
        service.createMenu(createCommand(menu));
        assertThat(menuRepository.exists(menu.id())).isTrue();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant menu creation access to super administrator")
    void grantMenuCreationAsSuperAdministrator() {
        var menu = builder().withOwnerName(SUPER_ADMINISTRATOR).build();
        service.createMenu(createCommand(menu));
        assertThat(menuRepository.exists(menu.id())).isTrue();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny menu deletion access to community user")
    void denyMenuDeletionAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.deleteMenu(deleteCommand()));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny menu deletion access to authenticated user that is not menu owner")
    void denyMenuDeletionAsAuthenticatedUserNotOwner() {
        var menu = builder().withOwner(OWNER).build();
        menuRepository.save(menu);

        assertThrows(AccessDeniedException.class, () -> service.deleteMenu(deleteCommand(menu.id())));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant menu deletion access to authenticated user")
    void grantMenuDeletionAsAuthenticatedUser() {
        var menu = builder().withOwnerName(AUTHENTICATED_USER).build();
        menuRepository.save(menu);

        service.deleteMenu(deleteCommand(menu.id()));
        assertThat(menuRepository.exists(menu.id())).isFalse();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant menu deletion access to ingredient manager")
    void grantMenuDeletionAsIngredientManager() {
        var menu = builder().withOwnerName(INGREDIENT_MANAGER).build();
        menuRepository.save(menu);

        service.deleteMenu(deleteCommand(menu.id()));
        assertThat(menuRepository.exists(menu.id())).isFalse();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant menu deletion access to super administrator")
    void grantMenuDeletionAsSuperAdministrator() {
        var menu = builder().withOwnerName(SUPER_ADMINISTRATOR).build();
        menuRepository.save(menu);

        service.deleteMenu(deleteCommand(menu.id()));
        assertThat(menuRepository.exists(menu.id())).isFalse();
    }

    @Configuration
    @Import(ApplicationSecurityConfiguration.class)
    @EnableConfigurationProperties(MenuGenerationProperties.class)
    @ComponentScan("org.adhuc.cena.menu.menus")
    static class ServiceWithSecurityConfiguration {
        @Bean
        MenuRepository menuRepository() {
            return new InMemoryMenuRepository();
        }
    }

}
