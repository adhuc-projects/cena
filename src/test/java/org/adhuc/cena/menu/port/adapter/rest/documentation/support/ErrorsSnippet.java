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
package org.adhuc.cena.menu.port.adapter.rest.documentation.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import org.adhuc.cena.menu.common.exception.ExceptionCode;

/**
 * A {@link org.springframework.restdocs.snippet.Snippet} implementation that lists the exception codes from the
 * application. This snippet definition does not interpret {@link Operation} information, so any operation can be
 * processed with the same result.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class ErrorsSnippet extends TemplatedSnippet {

    private final List<ExceptionCode> exceptionCodes;

    /**
     * Creates a snippet based on the specified exception codes.
     *
     * @param exceptionCodes the exception codes to document.
     */
    public ErrorsSnippet(List<ExceptionCode> exceptionCodes) {
        super("errors", null);
        this.exceptionCodes = Collections.unmodifiableList(exceptionCodes);
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        final Map<String, Object> model = new HashMap<>();
        model.put("errors", exceptionCodes);
        return model;
    }

}
