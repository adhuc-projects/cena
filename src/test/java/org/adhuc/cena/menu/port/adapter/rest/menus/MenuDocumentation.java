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

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.adhuc.cena.menu.menus.MenuMother.*;
import static org.adhuc.cena.menu.support.UserProvider.AUTHENTICATED_USER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.menus.MenuConsultationAppService;
import org.adhuc.cena.menu.menus.MenuId;
import org.adhuc.cena.menu.menus.MenuManagementAppService;
import org.adhuc.cena.menu.menus.MenuOwner;
import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;

/**
 * The menu related rest-services documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("integration")
@Tag("documentation")
@WebMvcTest({MenusController.class, MenuController.class, MenuModelAssembler.class, MenuIdConverter.class})
@ContextConfiguration(classes = ResultHandlerConfiguration.class)
@AutoConfigureRestDocs("build/generated-snippets")
@DisplayName("Menu resource documentation")
class MenuDocumentation {

    private static final String MENU_API_URL = "/api/menus/{id}";

    private static final MenuId ID = new MenuId(new MenuOwner(AUTHENTICATED_USER), DATE, MEAL_TYPE);

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestDocumentationResultHandler documentationHandler;

    @MockBean
    private MenuConsultationAppService menuConsultationMock;
    @MockBean
    private MenuManagementAppService menuManagementMock;

    @Test
    @WithAuthenticatedUser
    @DisplayName("generates menu detail example")
    void menuDetailExample() throws Exception {
        when(menuConsultationMock.getMenu(id(AUTHENTICATED_USER))).thenReturn(menu());

        mvc.perform(get(MENU_API_URL, String.format("%s-%s", DATE, MEAL_TYPE))).andExpect(status().isOk())
                .andDo(documentationHandler.document(
                        pathParameters(parameterWithName("id").description("The menu identity")),
                        links(linkWithRel("self").description("This <<resources-menu,menu>>")),
                        menuResponseFields("<<resources-menu-links,Links>> to other resources")));
    }

    static ResponseFieldsSnippet menuResponseFields(String linksDescription) {
        return responseFields(
                fieldWithPath("date").description("The date of the menu"),
                fieldWithPath("mealType").description("The <<meal-types-list, meal type>> of the menu"),
                fieldWithPath("covers").description("The number of covers of the menu"),
                fieldWithPath("mainCourseRecipes").description("The <<resources-recipe,recipes>> related to the menu's" +
                        "main course, in the form of a list of recipe identities. Main course consists of at least one recipe"),
                subsectionWithPath("_links").description(linksDescription));
    }

}
