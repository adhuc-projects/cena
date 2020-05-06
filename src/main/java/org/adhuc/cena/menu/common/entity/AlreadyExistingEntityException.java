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
package org.adhuc.cena.menu.common.entity;

import static org.springframework.http.HttpStatus.CONFLICT;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.adhuc.cena.menu.common.exception.CenaException;
import org.adhuc.cena.menu.common.exception.ExceptionCode;

/**
 * An exception occurring while creating an already existing entity (based on its identity).
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Getter
@ResponseStatus(CONFLICT)
public class AlreadyExistingEntityException extends CenaException {

    private static final ExceptionCode EXCEPTION_CODE = ExceptionCode.ALREADY_EXISTING_ENTITY;

    /**
     * Constructs a new {@code AlreadyExistingEntityException} with the specified entity type and identity.
     *
     * @param entityType the entity type.
     * @param identity   the identity.
     */
    public AlreadyExistingEntityException(Class<? extends Entity<?>> entityType, Identity identity) {
        this(entityType, identity.toString());
    }

    /**
     * Constructs a new {@code AlreadyExistingEntityException} with the specified entity type and identity.
     *
     * @param entityType the entity type.
     * @param identity   the identity value.
     */
    public AlreadyExistingEntityException(Class<? extends Entity<?>> entityType, String identity) {
        this("Entity of type " + entityType.getSimpleName() + " with identity '" + identity + "' already exists");
    }

    /**
     * Constructs a new {@code AlreadyExistingEntityException} with the specified message.
     *
     * @param message the error message.
     */
    public AlreadyExistingEntityException(String message) {
        super(message, EXCEPTION_CODE);
    }

}
