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
package org.adhuc.cena.menu.port.adapter.rest.menus;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.adhuc.cena.menu.menus.MenuMother.*;
import static org.adhuc.cena.menu.support.UserProvider.AUTHENTICATED_USER;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.menus.MealType;
import org.adhuc.cena.menu.menus.MenuAppService;
import org.adhuc.cena.menu.menus.MenuOwner;
import org.adhuc.cena.menu.port.adapter.rest.ResultHandlerConfiguration;
import org.adhuc.cena.menu.port.adapter.rest.documentation.support.ConstrainedFields;
import org.adhuc.cena.menu.recipes.RecipeMother;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;

/**
 * The menus related rest-services documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("integration")
@Tag("documentation")
@WebMvcTest({MenusController.class, MenuModelAssembler.class, MenuIdConverter.class})
@ContextConfiguration(classes = ResultHandlerConfiguration.class)
@AutoConfigureRestDocs("build/generated-snippets")
@DisplayName("Menus resource documentation")
class MenusDocumentation {

    private static final String MENUS_API_URL = "/api/menus";
    private static final String MENU_OWNER_NAME = AUTHENTICATED_USER;
    private static final MenuOwner MENU_OWNER = new MenuOwner(MENU_OWNER_NAME);

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestDocumentationResultHandler documentationHandler;

    @MockBean
    private MenuAppService menuAppServiceMock;

    @Test
    @WithAuthenticatedUser
    @DisplayName("generates menus list example")
    void menusListExample() throws Exception {
        when(menuAppServiceMock.getMenus(MENU_OWNER)).thenReturn(List.of(
                builder().withOwnerName(MENU_OWNER_NAME).withDate(TODAY_LUNCH_DATE).withMealType(TODAY_LUNCH_MEAL_TYPE)
                        .withCovers(TODAY_LUNCH_COVERS).withMainCourseRecipes(TODAY_LUNCH_MAIN_COURSE_RECIPES).build(),
                builder().withOwnerName(MENU_OWNER_NAME).withDate(TOMORROW_DINNER_DATE).withMealType(TOMORROW_DINNER_MEAL_TYPE)
                        .withCovers(TOMORROW_DINNER_COVERS).withMainCourseRecipes(TOMORROW_DINNER_MAIN_COURSE_RECIPES).build()));

        mvc.perform(get(MENUS_API_URL)).andExpect(status().isOk())
                .andDo(documentationHandler.document(
                        links(linkWithRel("self").description("This <<resources-menus,menus list>>")),
                        responseFields(
                                subsectionWithPath("_embedded.data")
                                        .description("An array of <<resources-menu, Menu resources>>"),
                                subsectionWithPath("_links")
                                        .description("<<resources-menus-links,Links>> to other resources"))));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("generates menu creation example")
    void menusCreateExample() throws Exception {
        var fields = new ConstrainedFields(CreateMenuRequest.class);
        mvc.perform(post(MENUS_API_URL).contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID)))
                .andExpect(status().isCreated()).andDo(documentationHandler
                .document(requestFields(
                        fields.withPath("date").description("The date of the menu"),
                        fields.withPath("mealType").description("The <<meal-types-list, meal type>> of the menu"),
                        fields.withPath("covers").description("The number of covers of the menu"),
                        fields.withPath("mainCourseRecipes").description("The <<resources-recipe,recipes>> related to the menu's" +
                                "main course, in the form of a list of recipe identities. Main course consists of at least one recipe")
                        ),
                        new MenusDocumentation.MealTypesSnippet()));
    }

    private static class MealTypesSnippet extends TemplatedSnippet {
        public MealTypesSnippet() {
            super("meal-types", null);
        }

        @Override
        protected Map<String, Object> createModel(Operation operation) {
            final Map<String, Object> model = new HashMap<>();
            model.put("mealTypes", List.of(MealType.values()));
            return model;
        }
    }

}
