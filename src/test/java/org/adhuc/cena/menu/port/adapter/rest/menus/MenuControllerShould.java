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
package org.adhuc.cena.menu.port.adapter.rest.menus;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.menus.MenuMother.*;
import static org.adhuc.cena.menu.support.UserProvider.AUTHENTICATED_USER;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.menus.Menu;
import org.adhuc.cena.menu.menus.MenuAppService;
import org.adhuc.cena.menu.menus.MenuId;
import org.adhuc.cena.menu.menus.MenuOwner;
import org.adhuc.cena.menu.recipes.RecipeMother;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;

/**
 * The {@link MenuController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({MenusController.class, MenuController.class, MenuModelAssembler.class, MenuIdConverter.class})
@DisplayName("Menu controller should")
class MenuControllerShould {

    private static final String MENU_API_URL = "/api/menus/{id}";

    private static final String ID_PARAM = String.format("%s-%s", DATE, MEAL_TYPE);
    private static final MenuId ID = new MenuId(new MenuOwner(AUTHENTICATED_USER), DATE, MEAL_TYPE);

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MenuController controller;

    @MockBean
    private MenuAppService menuAppServiceMock;

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "01-01-2020-LUNCH", "LUNCH-2020-01-01", "2020-01-01-INVALID", "2020/01/01-LUNCH", "2020-01-01_LUNCH", "32-01-2020-LUNCH"})
    @WithAuthenticatedUser
    @DisplayName("respond Not Found when retrieving menu with id in invalid format")
    void respond404GetMenuInvalidIdFormat(String menuId) throws Exception {
        mvc.perform(get(MENU_API_URL, menuId)).andExpect(status().isNotFound());
    }

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when retrieving menu as an anonymous user")
    void respond401OnGetAsAnonymous() throws Exception {
        mvc.perform(get(MENU_API_URL, ID_PARAM)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Not Found when retrieving unknown menu")
    void respond404GetUnknownMenu() throws Exception {
        when(menuAppServiceMock.getMenu(ID)).thenThrow(new EntityNotFoundException(Menu.class, ID));
        mvc.perform(get(MENU_API_URL, ID_PARAM)).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "01-01-2020-LUNCH", "LUNCH-2020-01-01", "2020-01-01-INVALID", "2020/01/01-LUNCH", "2020-01-01_LUNCH", "32-01-2020-LUNCH"})
    @WithAuthenticatedUser
    @DisplayName("respond Not Found when deleting menu with id in invalid format")
    void respond404DeleteMenuInvalidIdFormat(String menuId) throws Exception {
        mvc.perform(delete(MENU_API_URL, menuId)).andExpect(status().isNotFound());
    }

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when deleting menu as an anonymous user")
    void respond401OnDeletionAsAnonymous() throws Exception {
        mvc.perform(delete(MENU_API_URL, ID_PARAM)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond No Content when deleting recipe successfully as an authenticated user")
    void respond204OnDeletionAsAuthenticatedUser() throws Exception {
        mvc.perform(delete(MENU_API_URL, ID_PARAM)).andExpect(status().isNoContent());
        verify(menuAppServiceMock).deleteMenu(deleteCommand(ID));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Not Found when deleting unknown menu")
    void respond404DeleteUnknownMenu() throws Exception {
        doThrow(new EntityNotFoundException(Menu.class, ID)).when(menuAppServiceMock).deleteMenu(deleteCommand(ID));
        mvc.perform(delete(MENU_API_URL, ID_PARAM)).andExpect(status().isNotFound());
    }

    @Nested
    @DisplayName("getting today's lunch")
    class TodayLunchDetail {

        @BeforeEach
        void setUp() {
            when(menuAppServiceMock.getMenu(ID)).thenReturn(menu());
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("return OK status")
        void getMenuFoundStatusOK() throws Exception {
            mvc.perform(get(MENU_API_URL, ID_PARAM)).andExpect(status().isOk());
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("respond with HAL content when retrieving menu with no specific requested content")
        void respondHalOnDetail() throws Exception {
            mvc.perform(get(MENU_API_URL, ID_PARAM))
                    .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("respond with JSON content when requested while retrieving menu")
        void respondJSONOnDetail() throws Exception {
            mvc.perform(get(MENU_API_URL, ID_PARAM).accept(APPLICATION_JSON))
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("contain menu data")
        void getMenuFoundContainsData() throws Exception {
            mvc.perform(get(MENU_API_URL, ID_PARAM))
                    .andExpect(jsonPath("$.date").value(DATE.toString()))
                    .andExpect(jsonPath("$.mealType").value(MEAL_TYPE.name()))
                    .andExpect(jsonPath("$.covers").value(COVERS.value()))
                    .andExpect(jsonPath("$.mainCourseRecipes").isArray())
                    .andExpect(jsonPath("$.mainCourseRecipes", hasSize(1)))
                    .andExpect(jsonPath("$.mainCourseRecipes[0]").value(RecipeMother.ID.toString()));
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("contain self link to detail")
        void getMenuHasSelfLink() throws Exception {
            var result = mvc.perform(get(MENU_API_URL, ID_PARAM));
            result.andExpect(jsonPath("$._links.self.href").exists())
                    .andExpect(jsonPath("$._links.self.href", equalTo(result.andReturn().getRequest().getRequestURL().toString())));
        }

    }

}
