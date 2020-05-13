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

import static java.time.LocalDate.now;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import static org.adhuc.cena.menu.menus.DateRange.since;
import static org.adhuc.cena.menu.menus.DateRange.until;
import static org.adhuc.cena.menu.menus.MenuMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;

/**
 * The {@link MenuConsultationImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Menu consultation service should")
class MenuConsultationImplShould {

    private MenuRepository menuRepository;
    private MenuConsultationImpl service;

    @BeforeEach
    void setUp() {
        menuRepository = new InMemoryMenuRepository();
        service = new MenuConsultationImpl(menuRepository);
    }

    @Test
    @DisplayName("return unmodifiable list of menus")
    void returnUnmodifiableListOfMenus() {
        assertThrows(UnsupportedOperationException.class, () -> service.getMenus(listQuery()).add(menu()));
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
            assertThat(service.getMenus(listQuery())).isEmpty();
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
            @DisplayName("return empty list of menus querying for one week until yesterday")
            void returnEmptyMenusListUntilYesterday() {
                assertThat(service.getMenus(new ListMenus(OWNER, since(now().minusWeeks(1))))).isEmpty();
            }

            @Test
            @DisplayName("return empty list of menus querying for one week since tomorrow")
            void returnEmptyMenusListSinceTomorrow() {
                assertThat(service.getMenus(new ListMenus(OWNER, since(now().plusDays(1))))).isEmpty();
            }

            @Test
            @DisplayName("return list of menus containing today's lunch querying for one week since today")
            void returnMenusListWithTodayLunchSinceToday() {
                assertThat(service.getMenus(listQuery())).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactly(todayLunch);
            }

            @Test
            @DisplayName("return list of menus containing today's lunch querying for one week until today")
            void returnMenusListWithTodayLunchUntilToday() {
                assertThat(service.getMenus(new ListMenus(OWNER, since(now().minusDays(6)))))
                        .isNotEmpty().usingFieldByFieldElementComparator().containsExactly(todayLunch);
            }

            @Test
            @DisplayName("return list of menus containing today's lunch querying for today")
            void returnMenusListWithTodayLunchForToday() {
                assertThat(service.getMenus(new ListMenus(OWNER, until(now()))))
                        .isNotEmpty().usingFieldByFieldElementComparator().containsExactly(todayLunch);
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
                    assertThat(service.getMenus(listQuery())).isNotEmpty().usingFieldByFieldElementComparator()
                            .containsExactlyInAnyOrder(todayLunch, tomorrowDinner);
                }

                @Test
                @DisplayName("return list of menus containing tomorrow's dinner querying for one week since tomorrow")
                void returnMenusListWithTomorrowDinnerSinceTomorrow() {
                    assertThat(service.getMenus(new ListMenus(OWNER, since(now().plusDays(1)))))
                            .isNotEmpty().usingFieldByFieldElementComparator().containsExactly(tomorrowDinner);
                }

                @Test
                @DisplayName("return list of menus containing today's lunch querying for one week until today")
                void returnMenusListWithTodayLunchUntilToday() {
                    assertThat(service.getMenus(new ListMenus(OWNER, since(now().minusDays(6)))))
                            .isNotEmpty().usingFieldByFieldElementComparator().containsExactly(todayLunch);
                }

                @Test
                @DisplayName("return list containing all menus for other owner")
                void returnOtherMenusListForOtherOwner() {
                    assertThat(service.getMenus(listQuery(OTHER_OWNER))).isNotEmpty().usingFieldByFieldElementComparator()
                            .containsExactly(todayLunchOtherOwner);
                }

            }

        }

    }

}
