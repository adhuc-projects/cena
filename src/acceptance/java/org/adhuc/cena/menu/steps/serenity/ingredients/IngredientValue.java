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
package org.adhuc.cena.menu.steps.serenity.ingredients;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.hal.Jackson2HalModule;

/**
 * An ingredient value on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Data
@ToString(exclude = {"id", "links"}, includeFieldNames = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientValue {

    public static final Comparator<IngredientValue> COMPARATOR = new IngredientNameComparator();

    private String id;
    private final String name;
    @JsonProperty("_links")
    @JsonDeserialize(using = Jackson2HalModule.HalLinkListDeserializer.class)
    @JsonIgnoreProperties(allowSetters = true)
    private List<Link> links;

    String selfLink() {
        Optional<String> self = link("self");
        return self.orElseGet(() -> fail("Unable to get self link from links " + links));
    }

    Optional<String> link(String rel) {
        return links.stream().filter(l -> l.getRel().equals(rel)).map(Link::getHref).findFirst();
    }

    void assertEqualTo(IngredientValue expected) {
        assertThat(this).usingComparator(COMPARATOR).isEqualTo(expected);
    }

    public static class IngredientNameComparator implements Comparator<IngredientValue> {
        @Override
        public int compare(IngredientValue ingredient1, IngredientValue ingredient2) {
            return Comparator.comparing(IngredientValue::name, String::compareTo).compare(ingredient1, ingredient2);
        }
    }

}
