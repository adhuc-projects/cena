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
package org.adhuc.cena.menu.steps.serenity.support.authentication;

import io.restassured.specification.RequestSpecification;

/**
 * An anonymous authentication process, responsible for ensuring that no authentication is set on a request
 * specification.
 *
 * @author Alexandre Carbenay
 * @version 0.0.1
 * @since 0.0.1
 */
class AnonymousAuthentication implements Authentication {
    @Override
    public RequestSpecification authenticate(RequestSpecification specification) {
        return specification.auth().none();
    }
}