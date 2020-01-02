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
package org.adhuc.cena.menu.util;

import java.util.Comparator;
import java.util.List;

/**
 * Naïve {@link Comparator} implementation for lists. Lists are considered equivalent if they have equivalent elements,
 * based on mutual {@link List#containsAll} predicate, whatever the elements order. In other cases, return {@code -1}.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class ListComparator<T> implements Comparator<List<T>> {

    @Override
    public int compare(List<T> l1, List<T> l2) {
        if (l1 == null) {
            return l2 == null || l2.isEmpty() ? 0 : -1;
        }
        if (l2 == null) {
            return l1.isEmpty() ? 0 : -1;
        }
        return l1.isEmpty() && l2.isEmpty() || l1.containsAll(l2) && l2.containsAll(l1) ? 0 : -1;
    }

}
