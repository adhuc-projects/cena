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
package org.adhuc.cena.menu.steps.serenity.support.resource;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.hal.Jackson2HalModule.HalLinkListDeserializer;

/**
 * An abstract REST resource providing HATEOAS support on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.0.1
 */
@Getter
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public abstract class HateoasClientResourceSupport {

    @JsonProperty("_links")
    @JsonDeserialize(using = HalLinkListDeserializer.class)
    private List<Link> links;

    @JsonIgnore
    public String getId() {
        return getMaybeLink(Link.REL_SELF).get();
    }

    Optional<String> getMaybeLink(String rel) {
        return links.stream().filter(link -> link.getRel().equals(rel)).map(Link::getHref).findFirst();
    }

    public String getLink(String rel) {
        return getMaybeLink(rel).orElseThrow(() -> new IllegalStateException("Could not find link with rel " + rel + " in resource links"));
    }

}
