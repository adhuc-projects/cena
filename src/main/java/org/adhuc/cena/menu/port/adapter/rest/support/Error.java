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
package org.adhuc.cena.menu.port.adapter.rest.support;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;
import org.springframework.http.HttpStatus;

import org.adhuc.cena.menu.common.ExceptionCode;

/**
 * A REST error representation, consistent with the the error structure as returned by the {@link RestControllerErrorAttributes}.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@With
@JsonAutoDetect(fieldVisibility = ANY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Error {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;
    private final int code;
    private final List<String> details;

    Error(HttpStatus httpStatus, ExceptionCode exceptionCode) {
        this.status = httpStatus.value();
        this.error = exceptionCode.description();
        this.message = exceptionCode.description();
        this.code = exceptionCode.code();
        this.details = Collections.emptyList();
    }

}
