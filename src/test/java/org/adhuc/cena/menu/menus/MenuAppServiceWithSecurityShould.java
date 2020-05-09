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
import org.adhuc.cena.menu.recipes.RecipeAppService;
import org.adhuc.cena.menu.recipes.RecipeMother;
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

    @Autowired
    private MenuAppService service;
    @Autowired
    private MenuRepository menuRepository;
    @MockBean
    private RecipeRepository recipeRepository;
    @MockBean
    private RecipeAppService recipeAppServiceMock;

    @BeforeEach
    void setUp() {
        menuRepository.deleteAll();

        when(recipeAppServiceMock.exists(RecipeMother.ID)).thenReturn(true);
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny menu listing access to community user")
    void denyMenuListingAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.getMenus(listQuery(OWNER)));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("deny menu listing access to authenticated user for menus not owned by user")
    void denyMenuListingAsAuthenticatedUserNotOwner() {
        assertThrows(AccessDeniedException.class, () -> service.getMenus(listQuery(OWNER)));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant menu listing access to authenticated user")
    void grantMenuListingAsAuthenticatedUser() {
        var menu = builder().withOwnerName(AUTHENTICATED_USER).build();
        menuRepository.save(menu);

        assertThat(service.getMenus(listQuery(new MenuOwner(AUTHENTICATED_USER)))).isNotEmpty();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant menu listing access to ingredient manager")
    void grantMenuListingAsIngredientManager() {
        var menu = builder().withOwnerName(INGREDIENT_MANAGER).build();
        menuRepository.save(menu);

        assertThat(service.getMenus(listQuery(new MenuOwner(INGREDIENT_MANAGER)))).isNotEmpty();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant menu listing access to super administrator")
    void grantMenuListingAsSuperAdministrator() {
        var menu = builder().withOwnerName(SUPER_ADMINISTRATOR).build();
        menuRepository.save(menu);

        assertThat(service.getMenus(listQuery(new MenuOwner(SUPER_ADMINISTRATOR)))).isNotEmpty();
    }

    @Test
    @WithCommunityUser
    @DisplayName("deny menu detail access to community user")
    void denyMenuDetailAccessAsCommunityUser() {
        assertThrows(AccessDeniedException.class, () -> service.getMenu(ID));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant menu detail access to authenticated user")
    void denyMenuDetailAccessAsAuthenticatedUserNotOwner() {
        var menu = builder().withOwner(OWNER).build();
        menuRepository.save(menu);

        assertThrows(AccessDeniedException.class, () -> service.getMenu(menu.id()));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("grant menu detail access to authenticated user")
    void grantMenuDetailAccessAsAuthenticatedUser() {
        var menu = builder().withOwnerName(AUTHENTICATED_USER).build();
        menuRepository.save(menu);

        assertThat(service.getMenu(menu.id())).isEqualTo(menu);
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant menu detail access to ingredient manager")
    void grantMenuDetailAccessAsIngredientManager() {
        var menu = builder().withOwnerName(INGREDIENT_MANAGER).build();
        menuRepository.save(menu);

        assertThat(service.getMenu(menu.id())).isEqualTo(menu);
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant menu detail access to super administrator")
    void grantMenuDetailAccessAsSuperAdministrator() {
        var menu = builder().withOwnerName(SUPER_ADMINISTRATOR).build();
        menuRepository.save(menu);

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
        var menu = builder().withOwnerName(AUTHENTICATED_USER).build();
        service.createMenu(createCommand(menu));
        assertThat(service.getMenu(menu.id())).isNotNull();
    }

    @Test
    @WithIngredientManager
    @DisplayName("grant menu creation access to ingredient manager")
    void grantMenuCreationAsIngredientManager() {
        var menu = builder().withOwnerName(INGREDIENT_MANAGER).build();
        service.createMenu(createCommand(menu));
        assertThat(service.getMenu(menu.id())).isNotNull();
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("grant menu creation access to super administrator")
    void grantMenuCreationAsSuperAdministrator() {
        var menu = builder().withOwnerName(SUPER_ADMINISTRATOR).build();
        service.createMenu(createCommand(menu));
        assertThat(service.getMenu(menu.id())).isNotNull();
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
