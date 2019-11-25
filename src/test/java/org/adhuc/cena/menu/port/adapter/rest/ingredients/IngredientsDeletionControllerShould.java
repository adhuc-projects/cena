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
package org.adhuc.cena.menu.port.adapter.rest.ingredients;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.adhuc.cena.menu.configuration.MenuGenerationProperties;
import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.port.adapter.rest.support.RequestValidatorDelegate;
import org.adhuc.cena.menu.support.WithSuperAdministrator;

/**
 * The {@link IngredientsDeletionController} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Tag("integration")
@Tag("restController")
@WebMvcTest({IngredientsController.class, IngredientsDeletionController.class, RequestValidatorDelegate.class, IngredientModelAssembler.class})
@TestPropertySource(properties = "cena.menu-generation.features.ingredients-deletion=false")
@EnableConfigurationProperties(MenuGenerationProperties.class)
@DisplayName("Ingredients deletion controller should")
class IngredientsDeletionControllerShould {

    private static final String INGREDIENTS_API_URL = "/api/ingredients";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IngredientAppService ingredientAppServiceMock;

    @Test
    @WithSuperAdministrator
    @DisplayName("respond Method Not Allowed when deleting ingredients while feature is disabled")
    void respond405OnIngredientsDeletionDisabled() throws Exception {
        mvc.perform(delete(INGREDIENTS_API_URL)).andExpect(status().isMethodNotAllowed());
    }

}
