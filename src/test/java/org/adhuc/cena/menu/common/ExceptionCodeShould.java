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
package org.adhuc.cena.menu.common;

import java.util.Arrays;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The {@link ExceptionCode} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Exception codes should")
class ExceptionCodeShould {

    @Test
    @DisplayName("always have code with 6 digits")
    void have6DigitsCode() {
        SoftAssertions.assertSoftly(s -> {
            Arrays.stream(ExceptionCode.values()).forEach(code -> s.assertThat(code.code()).isBetween(100000, 999999));
        });
    }

    @Test
    @DisplayName("always have a non empty description")
    void haveNonEmptyDescription() {
        SoftAssertions.assertSoftly(s -> {
            Arrays.stream(ExceptionCode.values()).forEach(code -> s.assertThat(code.description()).isNotBlank());
        });
    }

}
