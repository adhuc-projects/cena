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
package org.adhuc.cena.menu.port.adapter.rest.menus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.menus.MealType;
import org.adhuc.cena.menu.menus.Menu;
import org.adhuc.cena.menu.menus.MenuId;
import org.adhuc.cena.menu.menus.MenuOwner;

/**
 * An internal component responsible for converting menu to its identity for resource representation and parsing menu
 * identity from {@link org.springframework.web.bind.annotation.PathVariable PathVariable}s representing the menu identity.
 * During parsing, it will throw an {@link EntityNotFoundException} if provided value does not correspond to expected
 * format (i.e. {@code yyyy-MM-dd-MealType}).
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Component
public class MenuIdConverter {

    private static final String ELEMENTS_SEPARATOR = "-";

    /**
     * Parses a menu identity string representation to the corresponding {@link MenuId}.
     *
     * @param idRepresentation the menu identity string representation, in {@code yyyy-MM-dd-MealType} format.
     * @param menuOwner the menu owner to complete the MenuId constructor.
     * @return the menu identity.
     */
    public MenuId parse(String idRepresentation, MenuOwner menuOwner) {
        var parsedId = new ParsedId(idRepresentation);
        return new MenuId(menuOwner, parsedId.date, parsedId.mealType);
    }

    /**
     * Converts the specified menu's to its identity string representation.
     *
     * @param menu the menu.
     * @return the menu identity string representation.
     */
    public String convert(Menu menu) {
        return menu.date().toString() + ELEMENTS_SEPARATOR + menu.mealType();
    }

    private static class ParsedId {

        private static final int EXPECTED_ELEMENTS_NUMBER = 4;

        private LocalDate date;
        private MealType mealType;

        ParsedId(String idRepresentation) {
            var split = idRepresentation.split(ELEMENTS_SEPARATOR);
            if (split.length != EXPECTED_ELEMENTS_NUMBER) {
                failParsing(idRepresentation);
            }
            var mealType = split[split.length - 1];
            var date = idRepresentation.substring(0, idRepresentation.length() - mealType.length() - 1);
            try {
                this.mealType = MealType.valueOf(mealType);
                this.date = LocalDate.parse(date);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                failParsing(idRepresentation);
            }
        }

        private void failParsing(String idRepresentation) {
            throw new EntityNotFoundException(Menu.class, idRepresentation);
        }
    }

}
