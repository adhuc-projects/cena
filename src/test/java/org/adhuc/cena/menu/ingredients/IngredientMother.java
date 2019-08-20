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
 * @since 0.1.0
 * @see https://www.martinfowler.com/bliki/ObjectMother.html
 */
public class IngredientMother {

    public static final String TOMATO = "Tomato";
    public static final String CUCUMBER = "Cucumber";

    public static final String NAME = TOMATO;

    public static Ingredient ingredient() {
        return ingredient(NAME);
    }

    public static Ingredient ingredient(String name) {
        return new Ingredient(name);
    }

}
