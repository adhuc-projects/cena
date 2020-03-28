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
import static org.assertj.core.api.Assertions.assertThat;

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
 * @version 0.3.0
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

    public static final String ID_FIELD = "id";

    private static final String RECIPE_LINK = "recipe";

    public static final boolean DEFAULT_MAIN_INGREDIENT = false;
    public static final int DEFAULT_QUANTITY = 200;
    public static final String DEFAULT_MEASUREMENT_UNIT = "GRAM";

    public static final Comparator<RecipeIngredientValue> COMPARATOR = Comparator.comparing(RecipeIngredientValue::id, String::compareTo);
    public static final Comparator<RecipeIngredientValue> ID_AND_QUANTITY_COMPARATOR = COMPARATOR
            .thenComparing(RecipeIngredientValue::quantity, Comparator.nullsLast(Integer::compareTo))
            .thenComparing(RecipeIngredientValue::measurementUnit, Comparator.nullsLast(String::compareTo));

    private final String id;
    // Ingredient name is only used to be able to retrieve the ingredient ID from a list of ingredients
    // It is not serialized to call API, nor deserialized from the API response
    @JsonIgnore
    private final String ingredientName;
    private final Boolean mainIngredient;
    private final Integer quantity;
    private final String measurementUnit;

    public static RecipeIngredientValue buildUnknownRecipeIngredientValue(IngredientValue ingredient, RecipeValue recipe) {
        var recipeIngredient = new RecipeIngredientValue(ingredient);
        recipeIngredient.addLink(SELF_LINK, String.format("%s/%s", recipe.getIngredients(), ingredient.id()));
        return recipeIngredient;
    }

    public RecipeIngredientValue(IngredientValue ingredient) {
        this(ingredient, null, null, null);
    }

    public RecipeIngredientValue(IngredientValue ingredient, Boolean mainIngredient, Integer quantity, String measurementUnit) {
        this.id = ingredient != null ? ingredient.id() : null;
        this.ingredientName = ingredient != null ? ingredient.name() : null;
        this.mainIngredient = mainIngredient;
        this.quantity = quantity;
        this.measurementUnit = measurementUnit;
    }

    @JsonIgnore
    public String getRecipe() {
        return link(RECIPE_LINK);
    }

    public void assertMainIngredient(boolean isMainIngredient) {
        assertThat(mainIngredient).isNotNull().isEqualTo(isMainIngredient);
    }

}
