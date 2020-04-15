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
package org.adhuc.cena.menu.port.adapter.persistence.memory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.menus.MenuMother.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.menus.Menu;

/**
 * The {@link InMemoryMenuRepository} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("inMemoryRepository")
@DisplayName("In-memory menu repository should")
class InMemoryMenuRepositoryShould {

    private InMemoryMenuRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryMenuRepository();
    }

    @Test
    @DisplayName("throw IllegalArgumentException when finding menu by null owner")
    void throwIAEFindByNullOwner() {
        assertThrows(IllegalArgumentException.class, () -> repository.findByOwner(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when finding menu by null identity")
    void throwIAEFindByNullId() {
        assertThrows(IllegalArgumentException.class, () -> repository.findById(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when finding not null menu from null identity")
    void throwIAEFindNotNullByIdNullId() {
        assertThrows(IllegalArgumentException.class, () -> repository.findNotNullById(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when saving null menu")
    void throwIAESavingNullMenu() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Nested
    @DisplayName("with no menu for owner")
    class WithNoMenuForOwner {

        private Menu todayLunchOtherOwner;

        @BeforeEach
        void setUp() {
            todayLunchOtherOwner = builder().withOwner(OTHER_OWNER).build();
            repository.save(todayLunchOtherOwner);
        }

        @Test
        @DisplayName("return empty collection when finding all menus for owner")
        void returnEmptyCollectionForOwner() {
            assertThat(repository.findByOwner(OWNER)).isEmpty();
        }

        @Nested
        @DisplayName("with today's lunch")
        class WithTodayLunch {

            private Menu todayLunch;

            @BeforeEach
            void setUp() {
                todayLunch = menu();
                repository.save(todayLunch);
            }

            @Test
            @DisplayName("return a collection containing today's lunch")
            void returnCollectionContainingTodayLunch() {
                assertThat(repository.findByOwner(OWNER)).usingFieldByFieldElementComparator()
                        .containsExactly(todayLunch);
            }

            @Test
            @DisplayName("indicate that today's lunch exists")
            void exists() {
                assertThat(repository.exists(TODAY_LUNCH_ID)).isTrue();
            }

            @Test
            @DisplayName("indicate that tomorrow's dinner does not exist")
            void doesntExist() {
                assertThat(repository.exists(TOMORROW_DINNER_ID)).isFalse();
            }

            @Test
            @DisplayName("return empty menu when finding menu with unknown id")
            void returnEmptyUnknownId() {
                assertThat(repository.findById(TOMORROW_DINNER_ID)).isEmpty();
            }

            @Test
            @DisplayName("throw EntityNotFoundException when finding not null menu with unknown id")
            void throwEntityNotFoundExceptionUnknownId() {
                assertThrows(EntityNotFoundException.class, () -> repository.findNotNullById(TOMORROW_DINNER_ID));
            }

            @Test
            @DisplayName("return today's lunch when finding menu with known id")
            void returnMenu() {
                assertThat(repository.findById(TODAY_LUNCH_ID)).isPresent().usingFieldByFieldValueComparator().contains(todayLunch);
            }

            @Test
            @DisplayName("return today's lunch when finding not null menu with known id")
            void returnMenuNotNull() {
                assertThat(repository.findNotNullById(TODAY_LUNCH_ID)).isEqualToComparingFieldByField(todayLunch);
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
                    repository.save(tomorrowDinner);
                }

                @Test
                @DisplayName("returns a collection containing both menus for owner")
                void returnCollectionContainingMenus() {
                    assertThat(repository.findByOwner(OWNER)).usingFieldByFieldElementComparator()
                            .containsExactlyInAnyOrder(todayLunch, tomorrowDinner);
                }

                @Test
                @DisplayName("return a collection containing menu for other owner")
                void returnCollectionContainingMenuForOtherOwner() {
                    assertThat(repository.findByOwner(OTHER_OWNER)).usingFieldByFieldElementComparator()
                            .containsExactly(todayLunchOtherOwner);
                }

                @Test
                @DisplayName("return empty collections for all owners after deleting all menus")
                void returnEmptyCollectionsAfterDeletion() {
                    repository.deleteAll();
                    SoftAssertions.assertSoftly(s -> {
                        s.assertThat(repository.findByOwner(OWNER)).isEmpty();
                        s.assertThat(repository.findByOwner(OTHER_OWNER)).isEmpty();
                    });
                }

            }

        }

    }

}
