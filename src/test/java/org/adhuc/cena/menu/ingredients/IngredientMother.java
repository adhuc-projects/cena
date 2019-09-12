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
package org.adhuc.cena.menu.ingredients;

/**
 * An object mother to create testing domain elements related to {@link Ingredient}s.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @see https://www.martinfowler.com/bliki/ObjectMother.html
 * @since 0.1.0
 */
public class IngredientMother {

    public static final IngredientId TOMATO_ID = IngredientId.generate();
    public static final IngredientId CUCUMBER_ID = IngredientId.generate();

    public static final String TOMATO = "Tomato";
    public static final String CUCUMBER = "Cucumber";

    public static final IngredientId ID = TOMATO_ID;
    public static final String NAME = TOMATO;

    public static CreateIngredient createCommand() {
        return createCommand(ingredient());
    }

    public static CreateIngredient createCommand(Ingredient ingredient) {
        return new CreateIngredient(ingredient.getId(), ingredient.getName());
    }

    public static Ingredient ingredient() {
        return ingredient(ID, NAME);
    }

    public static Ingredient ingredient(IngredientId id, String name) {
        return new Ingredient(id, name);
    }

}
