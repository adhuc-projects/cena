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
package org.adhuc.cena.menu.steps.serenity.support.resource;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalLinkListDeserializer;

/**
 * An abstract REST resource providing HATEOAS support on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.0.1
 */
@Getter
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIgnoreProperties(value = {"_links"}, ignoreUnknown = true, allowSetters = true)
public abstract class HateoasClientResourceSupport {

    protected static final String SELF_LINK = "self";

    @JsonProperty("_links")
    @JsonDeserialize(using = HalLinkListDeserializer.class)
    private List<Link> links = new ArrayList<>();

    public String selfLink() {
        return link(SELF_LINK);
    }

    public Optional<String> maybeLink(String rel) {
        return links.stream().filter(link -> link.getRel().value().equals(rel)).map(Link::getHref).findFirst();
    }

    public String link(String rel) {
        return maybeLink(rel).orElseGet(() -> fail("Unable to get " + rel + " link from links " + links));
    }

    protected void addLink(String rel, String link) {
        this.links.add(new Link(link, rel));
    }

}
