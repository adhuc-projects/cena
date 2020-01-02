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
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(NON_EMPTY)
public class RecipeValue extends HateoasClientResourceSupport {

    private static final String RECIPE_INGREDIENTS_LINK = "ingredients";

    public static final Comparator<RecipeValue> COMPARATOR = Comparator.comparing(RecipeValue::name);
    public static final Comparator<RecipeValue> NAME_AND_CONTENT_COMPARATOR = COMPARATOR.thenComparing(RecipeValue::content);

    public static final String DEFAULT_NAME = "Recipe default name";
    public static final String DEFAULT_CONTENT = "Recipe default content";

    private final String id;
    private final String name;
    private final String content;
    private final String author;

    public static RecipeValue buildUnknownRecipeValue(String name, String recipesResourceUrl) {
        var recipe = new RecipeValue(UUID.randomUUID().toString(), name, DEFAULT_CONTENT, null);
        recipe.addLink(SELF_LINK, String.format("%s/%s", recipesResourceUrl, recipe.id));
        recipe.addLink(RECIPE_INGREDIENTS_LINK, String.format("%s/%s/ingredients", recipesResourceUrl, recipe.id));
        return recipe;
    }

    public RecipeValue(String name) {
        this(name, DEFAULT_CONTENT);
    }

    public RecipeValue(String name, String content) {
        this(null, name, content, null);
    }

    RecipeValue withoutId() {
        return new RecipeValue(null, name, content, author);
    }

    @JsonIgnore
    public String getIngredients() {
        return link(RECIPE_INGREDIENTS_LINK);
    }

    void assertEqualTo(RecipeValue expected) {
        assertThat(this).usingComparator(NAME_AND_CONTENT_COMPARATOR).isEqualTo(expected);
    }

}
