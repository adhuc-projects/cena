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
package org.adhuc.cena.menu.steps.serenity.ingredients;

import static org.assertj.core.api.Assertions.assertThat;

import static org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue.NAME_AND_MEASUREMENT_TYPES_COMPARATOR;

import java.util.Collection;

import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The ingredients list rest-service client steps definition dedicated to assertions.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class IngredientListAssertionsSteps {

    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    private final IngredientListClientDelegate listClient = new IngredientListClientDelegate(
            resourceUrlResolverDelegate.ingredientsResourceUrl());

    @Step("Assert empty ingredients list")
    public void assertEmptyIngredientsList() {
        assertEmptyIngredientsList(listClient.fetchIngredients());
    }

    @Step("Assert empty ingredients list {0}")
    public void assertEmptyIngredientsList(Collection<IngredientValue> ingredients) {
        assertThat(ingredients).isEmpty();
    }

    @Step("Assert ingredient {0} is in ingredients list")
    public void assertInIngredientsList(IngredientValue ingredient) {
        assertThat(listClient.getFromIngredientsList(ingredient)).isPresent().get()
                .usingComparator(NAME_AND_MEASUREMENT_TYPES_COMPARATOR).isEqualTo(ingredient);
    }

    @Step("Assert ingredients {0} are in ingredients list {1}")
    public void assertInIngredientsList(Collection<IngredientValue> expected, Collection<IngredientValue> actual) {
        assertThat(actual).usingElementComparator(NAME_AND_MEASUREMENT_TYPES_COMPARATOR).containsAll(expected);
    }

    @Step("Assert ingredient {0} is not in ingredients list")
    public void assertNotInIngredientsList(IngredientValue ingredient) {
        assertThat(listClient.getFromIngredientsList(ingredient)).isNotPresent();
    }

}
