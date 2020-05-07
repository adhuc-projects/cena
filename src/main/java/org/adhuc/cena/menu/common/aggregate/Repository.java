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
package org.adhuc.cena.menu.common.aggregate;

import java.util.Optional;

import lombok.NonNull;

/**
 * A basic repository definition, providing convenient methods to store and retrieve entities.
 *
 * @param <E> the entities type.
 * @param <I> the entities' identity type.
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public interface Repository<E extends Entity<I>, I extends Identity> {

    /**
     * Returns the entity type stored in the repository.
     *
     * @return the entity type (not {@code null}).
     */
    Class<E> entityType();

    /**
     * Indicates whether an entity exists with the specified identity.
     *
     * @param id the entity identity.
     * @return {@code true} if entity exists with identity, {@code false} otherwise.
     */
    boolean exists(I id);

    /**
     * Finds the entity corresponding to the specified identity.
     *
     * @param id the entity identity.
     * @return the entity if existing, empty otherwise.
     */
    Optional<E> findById(I id);

    /**
     * Finds the entity corresponding to the specified identity.
     *
     * @param id the entity identity.
     * @return the entity if existing.
     * @throws EntityNotFoundException if no entity could be found for identity.
     */
    default E findNotNullById(@NonNull I id) {
        var entity = findById(id);
        if (entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException(entityType(), id);
    }

    /**
     * Saves the specified entity.
     *
     * @param entity the entity to save.
     * @return the saved entity.
     */
    <S extends E> S save(S entity);

}
