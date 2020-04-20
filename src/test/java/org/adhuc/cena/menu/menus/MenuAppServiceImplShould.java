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
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import static org.adhuc.cena.menu.menus.MenuMother.*;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.adhuc.cena.menu.common.AlreadyExistingEntityException;
import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryMenuRepository;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * The {@link MenuAppServiceImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Menu service should")
class MenuAppServiceImplShould {

    private MenuRepository menuRepository;
    private MenuAppServiceImpl service;

    @BeforeEach
    void setUp() {
        menuRepository = new InMemoryMenuRepository();
        service = new MenuAppServiceImpl(new MenuCreationService(menuRepository), menuRepository);
    }

    @Test
    @DisplayName("return unmodifiable list of menus")
    void returnUnmodifiableListOfMenus() {
        assertThrows(UnsupportedOperationException.class, () -> service.getMenus(OWNER).add(menu()));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when getting menus for null owner")
    void throwIAEGetMenusNullOwner() {
        assertThrows(IllegalArgumentException.class, () -> service.getMenus(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when getting menu from null identity")
    void throwIAEGetMenuNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.getMenu(null));
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

    @Nested
    @DisplayName("with no menu for owner")
    class WithNoMenu {

        private Menu todayLunchOtherOwner;

        @BeforeEach
        void setUp() {
            todayLunchOtherOwner = builder().withOwner(OTHER_OWNER).build();
            menuRepository.save(todayLunchOtherOwner);
        }

        @Test
        @DisplayName("return empty list of menus for owner")
        void returnEmptyMenusList() {
            assertThat(service.getMenus(OWNER)).isEmpty();
        }

        @Test
        @DisplayName("create menu successfully")
        void createMenu() {
            var menu = menu();
            var created = service.createMenu(createCommand(menu));
            assertThat(created).isNotNull().isEqualToComparingFieldByField(menu);
        }

        @Test
        @DisplayName("retrieve menu with identity after creation")
        void retrieveMenuWithIdAfterCreation() {
            var menu = menu();
            service.createMenu(createCommand(menu));
            assertThat(service.getMenu(menu.id())).isNotNull().isEqualToComparingFieldByField(menu);
        }

        @Nested
        @TestInstance(PER_CLASS)
        @DisplayName("with today's lunch")
        class WithTodayLunch {

            private Menu todayLunch;

            @BeforeEach
            void setUp() {
                todayLunch = menu();
                menuRepository.save(todayLunch);
            }

            @Test
            @DisplayName("return list of menus containing today's lunch")
            void returnMenusListWithTodayLunch() {
                assertThat(service.getMenus(OWNER)).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactly(todayLunch);
            }

            @Test
            @DisplayName("throw EntityNotFoundException when getting menu from unknown identity")
            void throwEntityNotFoundExceptionUnknownId() {
                assertThrows(EntityNotFoundException.class, () -> service.getMenu(TOMORROW_DINNER_ID));
            }

            @Test
            @DisplayName("return today's lunch when getting menu from id")
            void returnTodayLunch() {
                assertThat(service.getMenu(TODAY_LUNCH_ID)).isEqualToComparingFieldByField(todayLunch);
            }

            @Test
            @DisplayName("create non existing tomorrow dinner successfully")
            void createNonExistingMenu() {
                var menu = builder().withDate(TOMORROW_DINNER_DATE).withMealType(TOMORROW_DINNER_MEAL_TYPE)
                        .withCovers(TOMORROW_DINNER_COVERS).withMainCourseRecipes(TOMORROW_DINNER_MAIN_COURSE_RECIPES).build();
                var createdMenu = service.createMenu(createCommand(menu));
                assertThat(createdMenu).isEqualToComparingFieldByField(menu);
                assertThat(service.getMenu(menu.id())).isNotNull().isEqualToComparingFieldByField(menu);
            }

            @ParameterizedTest
            @MethodSource("createDuplicateSource")
            @DisplayName("fail during duplicated today's lunch creation")
            void failCreatingDuplicateTodayLunch(Covers covers, Collection<RecipeId> mainCourseRecipes) {
                var exception = assertThrows(AlreadyExistingEntityException.class,
                        () -> service.createMenu(createCommand(builder().withCovers(covers)
                                .withMainCourseRecipes(mainCourseRecipes).build())));
                assertThat(exception).hasMessage(String.format("Entity of type Menu with identity 'owner=some owner, date=%s, mealType=LUNCH' already exists", TODAY_LUNCH_DATE));
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
                assumeThat(service.getMenu(ID)).isNotNull();
                service.deleteMenu(deleteCommand());
                assertThrows(EntityNotFoundException.class, () -> service.getMenu(ID));
            }

            @Test
            @DisplayName("throw EntityNotFoundException when deleting unknown menu")
            void throwEntityNotFoundDeleteUnknownMenu() {
                var exception = assertThrows(EntityNotFoundException.class,
                        () -> service.deleteMenu(deleteCommand(TOMORROW_DINNER_ID)));
                assertThat(exception.getIdentity()).isEqualTo(TOMORROW_DINNER_ID.toString());
            }

            @Nested
            @DisplayName("and tomorrow's dinner")
            class AndTomorrowDinner {

                private Menu tomorrowDinner;

                @BeforeEach
                void setUp() {
                    tomorrowDinner = builder()
                            .withDate(TOMORROW_DINNER_DATE)
                            .withMealType(TOMORROW_DINNER_MEAL_TYPE)
                            .withCovers(TOMORROW_DINNER_COVERS)
                            .withMainCourseRecipes(TOMORROW_DINNER_MAIN_COURSE_RECIPES)
                            .build();
                    menuRepository.save(tomorrowDinner);
                }

                @Test
                @DisplayName("return list containing all menus for owner")
                void returnMenusListWithAllMenus() {
                    assertThat(service.getMenus(OWNER)).isNotEmpty().usingFieldByFieldElementComparator()
                            .containsExactlyInAnyOrder(todayLunch, tomorrowDinner);
                }

                @Test
                @DisplayName("return list containing all menus for other owner")
                void returnOtherMenusListForOtherOwner() {
                    assertThat(service.getMenus(OTHER_OWNER)).isNotEmpty().usingFieldByFieldElementComparator()
                            .containsExactly(todayLunchOtherOwner);
                }

            }

        }

    }

}
