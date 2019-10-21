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
package org.adhuc.cena.menu.common;

import static java.lang.String.format;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception occurring while requesting a resource that cannot be found.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Getter
@ResponseStatus(NOT_FOUND)
public class EntityNotFoundException extends CenaException {

    private static final ExceptionCode EXCEPTION_CODE = ExceptionCode.ENTITY_NOT_FOUND;

    private final Class<? extends Entity<?>> entityType;
    private final String identity;

    /**
     * Constructs a new {@code EntityNotFoundException} with the specified entity type and identity.
     *
     * @param entityType the entity type.
     * @param identity   the identity.
     */
    public EntityNotFoundException(Class<? extends Entity<?>> entityType, Identity identity) {
        this(entityType, identity.toString());
    }

    /**
     * Constructs a new {@code EntityNotFoundException} with the specified entity type and identity.
     *
     * @param entityType the entity type.
     * @param identity   the identity value.
     */
    public EntityNotFoundException(Class<? extends Entity<?>> entityType, String identity) {
        super(format("Cannot find entity of type %s with identity %s", entityType, identity), EXCEPTION_CODE);
        this.entityType = entityType;
        this.identity = identity;
    }

}
