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
package org.adhuc.cena.menu.steps.serenity.recipes;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.steps.serenity.support.resource.HateoasClientResourceSupport;

/**
 * A recipe value on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"id", "content"}, includeFieldNames = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(NON_EMPTY)
public class RecipeValue extends HateoasClientResourceSupport {

    public static final Comparator<RecipeValue> COMPARATOR = new RecipeNameComparator();
    public static final Comparator<RecipeValue> NAME_AND_CONTENT_COMPARATOR = new RecipeNameAndContentComparator();

    public static final String DEFAULT_NAME = "Recipe default name";
    public static final String DEFAULT_CONTENT = "Recipe default content";

    private final String id;
    private final String name;
    private final String content;

    public RecipeValue(String name) {
        this(name, DEFAULT_CONTENT);
    }

    public RecipeValue(String name, String content) {
        this.id = null;
        this.name = name;
        this.content = content;
    }

    void assertEqualTo(RecipeValue expected) {
        assertThat(this).usingComparator(NAME_AND_CONTENT_COMPARATOR).isEqualTo(expected);
    }

    static class RecipeNameComparator implements Comparator<RecipeValue> {
        @Override
        public int compare(RecipeValue recipe1, RecipeValue recipe2) {
            return Comparator.comparing(RecipeValue::name, String::compareTo).compare(recipe1, recipe2);
        }
    }

    static class RecipeNameAndContentComparator implements Comparator<RecipeValue> {
        @Override
        public int compare(RecipeValue recipe1, RecipeValue recipe2) {
            return Comparator.comparing(RecipeValue::name, String::compareTo)
                    .thenComparing(RecipeValue::content, String::compareTo).compare(recipe1, recipe2);
        }
    }

}
