package org.adhuc.cena.menu.port.adapter.rest.ingredients;

import lombok.NonNull;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import org.adhuc.cena.menu.ingredients.Ingredient;

/**
 * A {@link org.springframework.hateoas.ResourceAssembler ResourceAssembler} implementation allowing building
 * {@link IngredientResource}s.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Component
public class IngredientResourceAssembler extends ResourceAssemblerSupport<Ingredient, IngredientResource> {

    /**
     * Creates a resource assembler for ingredients.
     */
    IngredientResourceAssembler() {
        super(IngredientsController.class, IngredientResource.class);
    }

    @Override
    public IngredientResource toResource(@NonNull Ingredient ingredient) {
        return createResourceWithId(ingredient.id().toString(), ingredient);
    }

    @Override
    protected IngredientResource instantiateResource(Ingredient ingredient) {
        return new IngredientResource(ingredient);
    }

}
