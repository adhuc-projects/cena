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

import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.adhuc.cena.menu.menus.DateRange.*;
import static org.adhuc.cena.menu.menus.MenuMother.*;
import static org.adhuc.cena.menu.support.UserProvider.AUTHENTICATED_USER;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.adhuc.cena.menu.common.AlreadyExistingEntityException;
import org.adhuc.cena.menu.menus.*;
import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.recipes.RecipeMother;
import org.adhuc.cena.menu.support.WithAuthenticatedUser;
import org.adhuc.cena.menu.support.WithCommunityUser;
import org.adhuc.cena.menu.support.WithIngredientManager;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link MenusController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({MenusController.class, MenuModelAssembler.class, MenuIdConverter.class})
@DisplayName("Menus controller should")
class MenusControllerShould {

    private static final String MENUS_API_URL = "/api/menus";
    private static final String MENU_OWNER_NAME = AUTHENTICATED_USER;
    private static final MenuOwner MENU_OWNER = new MenuOwner(MENU_OWNER_NAME);

    @Autowired
    private MockMvc mvc;
    @MockBean
    private MenuAppService menuAppServiceMock;

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when retrieving menus as a community user")
    void respond401OnListAsCommunityUser() throws Exception {
        mvc.perform(get(MENUS_API_URL))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "01-01-2020", "2020/01/01", "2020-02-30"})
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when listing menus with invalid since date")
    void respond400OnListWithInvalidSince(String value) throws Exception {
        mvc.perform(get(MENUS_API_URL).queryParam("filter[date][since]", value))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "01-01-2020", "2020/01/01", "2020-02-30"})
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when listing menus with invalid until date")
    void respond400OnListWithInvalidUntil(String value) throws Exception {
        mvc.perform(get(MENUS_API_URL).queryParam("filter[date][until]", value))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when listing menus with until date lower than since date")
    void respond400OnListWithUntilLowerThanSince() throws Exception {
        mvc.perform(get(MENUS_API_URL).queryParam("filter[date][until]", now().minusDays(1).toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when listing menus with no date range")
    void callServiceOnListWithNoDateRange() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(ListMenus.class);
        var expectedQuery = new ListMenus(MENU_OWNER, defaultRange());

        mvc.perform(get(MENUS_API_URL)).andExpect(status().isOk());

        verify(menuAppServiceMock).getMenus(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualTo(expectedQuery);
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when listing menus with since date")
    void callServiceOnListWithSince() throws Exception {
        var since = now().plusDays(1);
        var commandCaptor = ArgumentCaptor.forClass(ListMenus.class);
        var expectedQuery = new ListMenus(MENU_OWNER, since(since));

        mvc.perform(get(MENUS_API_URL).queryParam("filter[date][since]", since.toString()))
                .andExpect(status().isOk());

        verify(menuAppServiceMock).getMenus(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualTo(expectedQuery);
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when listing menus with until date")
    void callServiceOnListWithUntil() throws Exception {
        var until = now().plusDays(1);
        var commandCaptor = ArgumentCaptor.forClass(ListMenus.class);
        var expectedQuery = new ListMenus(MENU_OWNER, until(until));

        mvc.perform(get(MENUS_API_URL).queryParam("filter[date][until]", until.toString()))
                .andExpect(status().isOk());

        verify(menuAppServiceMock).getMenus(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualTo(expectedQuery);
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when listing menus with date range")
    void callServiceOnListWithDateRange() throws Exception {
        var since = now().plusDays(1);
        var until = now().plusDays(2);
        var commandCaptor = ArgumentCaptor.forClass(ListMenus.class);
        var expectedQuery = new ListMenus(MENU_OWNER, range(since, until));

        mvc.perform(get(MENUS_API_URL)
                .queryParam("filter[date][since]", since.toString())
                .queryParam("filter[date][until]", until.toString()))
                .andExpect(status().isOk());

        verify(menuAppServiceMock).getMenus(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualTo(expectedQuery);
    }

    @Nested
    @DisplayName("with 2 menus")
    class With2Menus {

        private List<Menu> menus;

        @BeforeEach
        void setUp() {
            menus = List.of(
                    MenuMother.builder().withOwnerName(MENU_OWNER_NAME).withDate(TODAY_LUNCH_DATE).withMealType(TODAY_LUNCH_MEAL_TYPE)
                            .withCovers(TODAY_LUNCH_COVERS).withMainCourseRecipes(TODAY_LUNCH_MAIN_COURSE_RECIPES).build(),
                    MenuMother.builder().withOwnerName(MENU_OWNER_NAME).withDate(TOMORROW_DINNER_DATE).withMealType(TOMORROW_DINNER_MEAL_TYPE)
                            .withCovers(TOMORROW_DINNER_COVERS).withMainCourseRecipes(TOMORROW_DINNER_MAIN_COURSE_RECIPES).build());
            when(menuAppServiceMock.getMenus(listQuery(MENU_OWNER))).thenReturn(menus);
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("respond OK when retrieving menus")
        void respond200OnList() throws Exception {
            mvc.perform(get(MENUS_API_URL))
                    .andExpect(status().isOk());
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("respond with HAL content when retrieving menus with no specific requested content")
        void respondHalOnList() throws Exception {
            mvc.perform(get(MENUS_API_URL))
                    .andExpect(content().contentTypeCompatibleWith(HAL_JSON));
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("respond with JSON content when requested while retrieving menus")
        void respondJSONOnList() throws Exception {
            mvc.perform(get(MENUS_API_URL)
                    .accept(APPLICATION_JSON)
            ).andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("have self link with correct value when retrieving menus")
        void haveSelfOnList() throws Exception {
            mvc.perform(get(MENUS_API_URL))
                    .andExpect(jsonPath("$._links.self.href", Matchers.endsWith(MENUS_API_URL)));
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("have embedded data with 2 menus when retrieving menus")
        void haveDataWith2Menus() throws Exception {
            var result = mvc.perform(get(MENUS_API_URL))
                    .andExpect(jsonPath("$._embedded.data").isArray())
                    .andExpect(jsonPath("$._embedded.data").isNotEmpty())
                    .andExpect(jsonPath("$._embedded.data", hasSize(2)));
            assertJsonContainsMenu(result, "$._embedded.data[0]", menus.get(0));
            assertJsonContainsMenu(result, "$._embedded.data[1]", menus.get(1));
        }
    }

    @Nested
    @DisplayName("with empty list")
    class WithEmptyList {
        @BeforeEach
        void setUp() {
            when(menuAppServiceMock.getMenus(listQuery(MENU_OWNER))).thenReturn(List.of());
        }

        @Test
        @WithAuthenticatedUser
        @DisplayName("have empty embedded data when retrieving menus")
        void haveEmptyDataOnEmptyList() throws Exception {
            mvc.perform(get(MENUS_API_URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded").doesNotExist());
        }
    }

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when creating menu as a community user")
    void respond401OnCreationAsCommunityUser() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Unsupported Media Type when creating menu with XML content")
    void respond415OnCreationXML() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_XML)
                .content("<menu>" +
                        "<date>2020-01-01</name>" +
                        "<mealType>LUNCH</mealType>" +
                        "<covers>2</covers>" +
                        "<mainCourseRecipes>" +
                        "<mainCourseRecipe>" + RecipeMother.ID + "</mainCourseRecipe>" +
                        "</mainCourseRecipes>" +
                        "</menu>")
        ).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu without date")
    void respond400OnCreationWithoutDate() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}", RecipeMother.ID))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("details").isArray())
                .andExpect(jsonPath("details", hasSize(1)))
                .andExpect(jsonPath("details[0]", equalTo("Invalid request body property 'date': must not be null. Actual value is <null>")));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu with wrong date format")
    void respond400OnCreationWrongDateFormat() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\", \"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        new SimpleDateFormat("dd-MM-yyyy").format(new Date()), RecipeMother.ID))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu without meal type")
    void respond400OnCreationWithoutMealType() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}", now(), RecipeMother.ID))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("details").isArray())
                .andExpect(jsonPath("details", hasSize(1)))
                .andExpect(jsonPath("details[0]", equalTo("Invalid request body property 'mealType': must not be null. Actual value is <null>")));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu with unknown meal type")
    void respond400OnCreationWithUnknownMealType() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"UNKNOWN\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu without covers")
    void respond400OnCreationWithoutCovers() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("details").isArray())
                .andExpect(jsonPath("details", hasSize(1)))
                .andExpect(jsonPath("details[0]", equalTo("Invalid request body property 'covers': must not be null. Actual value is <null>")));
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu with negative covers")
    void respond400OnCreationWithNegativeCovers(int covers) throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":%d,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), covers, RecipeMother.ID))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("details").isArray())
                .andExpect(jsonPath("details", hasSize(1)))
                .andExpect(jsonPath("details[0]", equalTo(
                        String.format("Invalid request body property 'covers': must be greater than 0. Actual value is '%d'", covers))));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu without main course recipes")
    void respond400OnCreationWithoutMainCourseRecipes() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2}", now()))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("details").isArray())
                .andExpect(jsonPath("details", hasSize(1)))
                .andExpect(jsonPath("details[0]", equalTo("Invalid request body property 'mainCourseRecipes': must not be empty. Actual value is <null>")));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu with empty main course recipes")
    void respond400OnCreationWithEmptyMainCourseRecipes() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[]}", now()))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("details").isArray())
                .andExpect(jsonPath("details", hasSize(1)))
                .andExpect(jsonPath("details[0]", equalTo("Invalid request body property 'mainCourseRecipes': must not be empty. Actual value is '[]'")));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu with invalid main course recipes")
    void respond400OnCreationWithInvalidMainCourseRecipes() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"invalid\"]}", now()))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Conflict when menu service throws AlreadyExistingEntityException")
    void respond409OnCreationAlreadyExistingEntityException() throws Exception {
        doThrow(new AlreadyExistingEntityException(Menu.class, MenuMother.ID)).when(menuAppServiceMock).createMenu(any());
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isConflict());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating menu with JSON content")
    void respond201OnCreationJson() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating menu with HAL content")
    void respond201OnCreationHal() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond with a Location header when creating menu successfully")
    void respondWithLocationAfterCreationSuccess() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(header().exists(LOCATION))
                .andExpect(header().string(LOCATION, String.format("http://localhost/api/menus/%s-LUNCH", now())));
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("call application service when creating menu")
    void callServiceOnCreation() throws Exception {
        var commandCaptor = ArgumentCaptor.forClass(CreateMenu.class);
        var expectedCommand = createCommand(MenuMother.builder().withOwnerName(MENU_OWNER_NAME).build());

        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isCreated());

        verify(menuAppServiceMock).createMenu(commandCaptor.capture());
        assertThat(commandCaptor.getValue()).isEqualTo(expectedCommand);
    }

    @Test
    @WithIngredientManager
    @DisplayName("respond Created when creating menu as ingredient manager")
    void respond201OnCreationAsIngredientManager() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("respond Created when creating menu as super administrator")
    void respond201OnCreationAsSuperAdministrator() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        now(), RecipeMother.ID))
        ).andExpect(status().isCreated());
    }

    void assertJsonContainsMenu(ResultActions resultActions, String jsonPath, Menu menu) throws Exception {
        resultActions.andExpect(jsonPath(jsonPath + ".date").exists())
                .andExpect(jsonPath(jsonPath + ".date", equalTo(menu.date().toString())))
                .andExpect(jsonPath(jsonPath + ".mealType").exists())
                .andExpect(jsonPath(jsonPath + ".mealType", equalTo(menu.mealType().name())))
                .andExpect(jsonPath(jsonPath + ".covers").exists())
                .andExpect(jsonPath(jsonPath + ".covers", equalTo(menu.covers().value())))
                .andExpect(jsonPath(jsonPath + ".mainCourseRecipes").exists())
                .andExpect(jsonPath(jsonPath + ".mainCourseRecipes").isArray())
                .andExpect(jsonPath(jsonPath + ".mainCourseRecipes").value(containsInAnyOrder(
                        menu.mainCourseRecipes().stream().map(RecipeId::toString).collect(toList()).toArray())));
    }

}
