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
package org.adhuc.cena.menu.steps.serenity.recipes;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.Comparator;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.assertj.core.api.SoftAssertions;

import org.adhuc.cena.menu.steps.serenity.support.resource.HateoasClientResourceSupport;

/**
 * A recipe value on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
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

    public static final String NAME_FIELD = "name";
    public static final String CONTENT_FIELD = "content";

    private static final String RECIPE_INGREDIENTS_LINK = "ingredients";

    public static final Comparator<RecipeValue> COMPARATOR = Comparator.comparing(RecipeValue::name);
    public static final Comparator<RecipeValue> NAME_AND_CONTENT_COMPARATOR = COMPARATOR.thenComparing(RecipeValue::content);

    public static final String DEFAULT_NAME = "Recipe default name";
    public static final String DEFAULT_CONTENT = "Recipe default content";
    public static final int DEFAULT_SERVINGS = 2;
    // Servings set by server if no servings value is specified in creation request
    public static final int DEFAULT_SERVER_SERVINGS = 4;

    private final String id;
    private final String name;
    private final String content;
    private final String author;
    private final Integer servings;

    public static RecipeValue buildUnknownRecipeValue(String recipesResourceUrl) {
        return buildUnknownRecipeValue(DEFAULT_NAME, recipesResourceUrl);
    }

    public static RecipeValue buildUnknownRecipeValue(String name, String recipesResourceUrl) {
        var recipe = new RecipeValue(UUID.randomUUID().toString(), name, DEFAULT_CONTENT, null, DEFAULT_SERVINGS);
        recipe.addLink(SELF_LINK, String.format("%s/%s", recipesResourceUrl, recipe.id));
        recipe.addLink(RECIPE_INGREDIENTS_LINK, String.format("%s/%s/ingredients", recipesResourceUrl, recipe.id));
        return recipe;
    }

    public RecipeValue(String name) {
        this(name, DEFAULT_CONTENT, DEFAULT_SERVINGS);
    }

    public RecipeValue(String name, String content, Integer servings) {
        this(null, name, content, null, servings);
    }

    RecipeValue withoutId() {
        return new RecipeValue(null, name, content, author, servings);
    }

    RecipeValue withoutServings() {
        return new RecipeValue(id, name, content, author, null);
    }

    @JsonIgnore
    public String getIngredients() {
        return link(RECIPE_INGREDIENTS_LINK);
    }

    void assertEqualTo(RecipeValue expected) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(this).usingComparator(NAME_AND_CONTENT_COMPARATOR).isEqualTo(expected);
            if (expected.servings != null) {
                softly.assertThat(this.servings).isEqualTo(expected.servings);
            } else {
                softly.assertThat(this.servings).isEqualTo(DEFAULT_SERVER_SERVINGS);
            }
        });
    }

}
