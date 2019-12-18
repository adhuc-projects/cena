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
package org.adhuc.cena.menu.steps.serenity.recipes.ingredients;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;
import org.adhuc.cena.menu.steps.serenity.support.resource.HateoasClientResourceSupport;

/**
 * A recipe ingredient value on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(includeFieldNames = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(NON_EMPTY)
public class RecipeIngredientValue extends HateoasClientResourceSupport {

    private static final String RECIPE_LINK = "recipe";

    public static final Comparator<RecipeIngredientValue> COMPARATOR = new RecipeIngredientIdComparator();

    private final String id;

    public static RecipeIngredientValue buildUnknownRecipeIngredientValue(IngredientValue ingredient, RecipeValue recipe) {
        var recipeIngredient = new RecipeIngredientValue(ingredient);
        recipeIngredient.addLink(SELF_LINK, String.format("%s/%s", recipe.getIngredients(), ingredient.id()));
        return recipeIngredient;
    }

    public RecipeIngredientValue(IngredientValue ingredient) {
        this.id = ingredient != null ? ingredient.id() : null;
    }

    @JsonIgnore
    public String getRecipe() {
        return link(RECIPE_LINK);
    }

    static class RecipeIngredientIdComparator implements Comparator<RecipeIngredientValue> {
        @Override
        public int compare(RecipeIngredientValue ingredient1, RecipeIngredientValue ingredient2) {
            return Comparator.comparing(RecipeIngredientValue::id, String::compareTo).compare(ingredient1, ingredient2);
        }
    }

}
