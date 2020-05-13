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
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.adhuc.cena.menu.menus.MenuMother.*;
import static org.adhuc.cena.menu.recipes.RecipeMother.TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.adhuc.cena.menu.common.aggregate.AlreadyExistingEntityException;
import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;
import org.adhuc.cena.menu.recipes.RecipeConsultation;
import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.recipes.RecipeMother;

/**
 * The {@link MenuManagementImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Menu management service should")
class MenuManagementImplShould {

    private MenuRepository menuRepository;
    private RecipeConsultation recipeAppService;
    private MenuManagementImpl service;

    @BeforeEach
    void setUp() {
        menuRepository = new InMemoryMenuRepository();
        recipeAppService = mock(RecipeConsultation.class);
        service = new MenuManagementImpl(new MenuCreation(menuRepository, recipeAppService), menuRepository);

        when(recipeAppService.exists(RecipeMother.ID)).thenReturn(true);
    }

    @Test
    @DisplayName("throw IllegalArgumentException when creating menu from null command")
    void throwIAECreateMenuNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.createMenu(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when deleting menu from null command")
    void throwIAEDeleteMenuNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteMenu(null));
    }

    @Test
    @DisplayName("create menu with unknown main course recipe")
    void failCreatingMenuWithUnknownMainCourseRecipe() {
        var exception = assertThrows(MenuNotCreatableWithUnknownRecipeException.class,
                () -> service.createMenu(createCommand(builder().withMainCourseRecipes(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID).build())));
        assertThat(exception).hasMessage(String.format("Menu scheduled at %s's lunch cannot be created with unknown recipes [%s]",
                TODAY_LUNCH_DATE, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
    }

    @Test
    @DisplayName("create menu with unknown main course recipes")
    void failCreatingMenuWithUnknownMainCourseRecipes() {
        var anotherRecipeId = new RecipeId("2211b1fc-a5f3-42c1-b591-fb979e0449d1");
        var exception = assertThrows(MenuNotCreatableWithUnknownRecipeException.class,
                () -> service.createMenu(createCommand(builder().withMainCourseRecipes(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID, anotherRecipeId).build())));
        assertThat(exception).hasMessage(String.format("Menu scheduled at %s's lunch cannot be created with unknown recipes [%s, %s]",
                TODAY_LUNCH_DATE, anotherRecipeId, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
    }

    @Nested
    @TestInstance(PER_CLASS)
    @DisplayName("with today's lunch")
    class WithTodayLunch {

        @BeforeEach
        void setUp() {
            menuRepository.save(menu());
            menuRepository.save(builder().withOwner(OTHER_OWNER).build());
            when(recipeAppService.exists(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("create non existing tomorrow dinner successfully")
        void createNonExistingMenu() {
            var menu = builder().withDate(TOMORROW_DINNER_DATE).withMealType(TOMORROW_DINNER_MEAL_TYPE)
                    .withCovers(TOMORROW_DINNER_COVERS).withMainCourseRecipes(TOMORROW_DINNER_MAIN_COURSE_RECIPES).build();
            service.createMenu(createCommand(menu));
            assertThat(menuRepository.findNotNullById(menu.id())).isNotNull().isEqualToComparingFieldByField(menu);
        }

        @ParameterizedTest
        @MethodSource("createDuplicateSource")
        @DisplayName("fail during duplicated today's lunch creation")
        void failCreatingDuplicateTodayLunch(Covers covers, Collection<RecipeId> mainCourseRecipes) {
            var exception = assertThrows(AlreadyExistingEntityException.class,
                    () -> service.createMenu(createCommand(builder().withCovers(covers)
                            .withMainCourseRecipes(mainCourseRecipes).build())));
            assertThat(exception).hasMessage(String.format("Menu is already scheduled at %s's lunch", TODAY_LUNCH_DATE));
        }

        private Stream<Arguments> createDuplicateSource() {
            return Stream.of(
                    Arguments.of(TODAY_LUNCH_COVERS, TODAY_LUNCH_MAIN_COURSE_RECIPES),
                    Arguments.of(TOMORROW_DINNER_COVERS, TODAY_LUNCH_MAIN_COURSE_RECIPES),
                    Arguments.of(TODAY_LUNCH_COVERS, TOMORROW_DINNER_MAIN_COURSE_RECIPES),
                    Arguments.of(TOMORROW_DINNER_COVERS, TOMORROW_DINNER_MAIN_COURSE_RECIPES)
            );
        }

        @Test
        @DisplayName("delete today's lunch successfully")
        void deleteTodayLunch() {
            assumeThat(menuRepository.exists(ID)).isTrue();
            service.deleteMenu(deleteCommand());
            assertThat(menuRepository.exists(ID)).isFalse();
        }

        @Test
        @DisplayName("throw EntityNotFoundException when deleting unknown menu")
        void throwEntityNotFoundDeleteUnknownMenu() {
            var exception = assertThrows(EntityNotFoundException.class,
                    () -> service.deleteMenu(deleteCommand(TOMORROW_DINNER_ID)));
            assertThat(exception.getIdentity()).isEqualTo(TOMORROW_DINNER_ID.toString());
        }

    }

}
