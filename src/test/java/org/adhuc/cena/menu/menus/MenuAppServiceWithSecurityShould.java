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
package org.adhuc.cena.menu.menus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.menus.MenuMother.*;
import static org.adhuc.cena.menu.recipes.RecipeMother.recipe;

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
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryMenuRepository;
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryRecipeRepository;
import org.adhuc.cena.menu.recipes.RecipeRepository;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link MenuAppServiceImpl} security tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("integration")
@Tag("appService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@DisplayName("Menu service with security should")
class MenuAppServiceWithSecurityShould {

    private static final String MENU_OWNER_NAME = "menu owner";
    private static final MenuOwner MENU_OWNER = new MenuOwner(MENU_OWNER_NAME);
    private Menu menu;

    @Autowired
    private MenuAppService service;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        menu = builder().withOwnerName(MENU_OWNER_NAME).build();
        menuRepository.deleteAll();
        menuRepository.save(menu);
        recipeRepository.save(recipe());
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny menu listing access to community user")
    void denyMenuListingAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.getMenus(MENU_OWNER));
    }

    // TODO manage access restrictions to menus based on authenticated user

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant menu listing access to authenticated user")
    void grantMenuListingAsAuthenticatedUser() {
        assertThat(service.getMenus(MENU_OWNER)).isNotEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant menu listing access to ingredient manager")
    void grantMenuListingAsIngredientManager() {
        assertThat(service.getMenus(MENU_OWNER)).isNotEmpty();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant menu listing access to super administrator")
    void grantMenuListingAsSuperAdministrator() {
        assertThat(service.getMenus(MENU_OWNER)).isNotEmpty();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny menu detail access to community user")
    void denyMenuDetailAccessAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.getMenu(menu.id()));
    }

    // TODO manage access restrictions to identified menu based on authenticated user

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant menu detail access to authenticated user")
    void grantMenuDetailAccessAsAuthenticatedUser() {
        assertThat(service.getMenu(menu.id())).isEqualTo(menu);
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant menu detail access to ingredient manager")
    void grantMenuDetailAccessAsIngredientManager() {
        assertThat(service.getMenu(menu.id())).isEqualTo(menu);
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant menu detail access to super administrator")
    void grantMenuDetailAccessAsSuperAdministrator() {
        assertThat(service.getMenu(menu.id())).isEqualTo(menu);
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
        service.createMenu(createCommand());
        assertThat(service.getMenu(ID)).isNotNull();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant menu creation access to ingredient manager")
    void grantMenuCreationAsIngredientManager() {
        service.createMenu(createCommand());
        assertThat(service.getMenu(ID)).isNotNull();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant menu creation access to super administrator")
    void grantMenuCreationAsSuperAdministrator() {
        service.createMenu(createCommand());
        assertThat(service.getMenu(ID)).isNotNull();
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

        @Bean
        RecipeRepository recipeRepository() {
            return new InMemoryRecipeRepository();
        }
    }

}
