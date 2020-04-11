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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

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
@WebMvcTest({MenusController.class})
@DisplayName("Menus controller should")
class MenusControllerShould {

    private static final String MENUS_API_URL = "/api/menus";

    @Autowired
    private MockMvc mvc;

    @Test
    @WithCommunityUser
    @DisplayName("respond Unauthorized when creating menu as a community user")
    void respond401OnCreationAsCommunityUser() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID))
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
                .content(String.format("{\"date\":\"%s\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}", LocalDate.now(), RecipeMother.ID))
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
                        LocalDate.now(), RecipeMother.ID))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Bad Request when creating menu without covers")
    void respond400OnCreationWithoutCovers() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID))
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
                        LocalDate.now(), covers, RecipeMother.ID))
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
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2}", LocalDate.now()))
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
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[]}", LocalDate.now()))
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
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"invalid\"]}", LocalDate.now()))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating menu with JSON content")
    void respond201OnCreationJson() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond Created when creating menu with HAL content")
    void respond201OnCreationHal() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAuthenticatedUser
    @DisplayName("respond with a Location header when creating menu successfully")
    void respondWithLocationAfterCreationSuccess() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID))
        ).andExpect(header().exists(LOCATION))
                .andExpect(header().string(LOCATION, String.format("http://localhost/api/menus/%s-LUNCH", LocalDate.now())));
    }

    @Test
    @WithIngredientManager
    @DisplayName("respond Created when creating menu as ingredient manager")
    void respond201OnCreationAsIngredientManager() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithSuperAdministrator
    @DisplayName("respond Created when creating menu as super administrator")
    void respond201OnCreationAsSuperAdministrator() throws Exception {
        mvc.perform(post(MENUS_API_URL)
                .contentType(HAL_JSON)
                .content(String.format("{\"date\":\"%s\",\"mealType\":\"LUNCH\",\"covers\":2,\"mainCourseRecipes\":[\"%s\"]}",
                        LocalDate.now(), RecipeMother.ID))
        ).andExpect(status().isCreated());
    }

}
