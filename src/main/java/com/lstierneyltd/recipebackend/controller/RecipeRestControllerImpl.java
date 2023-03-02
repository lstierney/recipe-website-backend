package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Ingredient;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.Unit;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.repository.UnitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeRestControllerImpl implements RecipeRestController {
    static final String COULD_NOT_FIND_RECIPE_WITH_ID = "Could not find RECIPE with id: ";
    static final String COULD_NOT_FIND_UNIT_WITH_ID = "Could not find UNIT with id: ";
    private final RecipeRepository recipeRepository;
    private final UnitRepository unitRepository;

    public RecipeRestControllerImpl(RecipeRepository recipeRepository, UnitRepository unitRepository) {
        this.recipeRepository = recipeRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    @GetMapping("/recipes/{id}")
    public Recipe getRecipeById(@PathVariable Integer id) {
        return recipeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_RECIPE_WITH_ID + id));
    }

    @Override
    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    @PostMapping("/recipes")
    public Recipe newRecipe(@RequestBody Recipe recipe) {
        for (final Ingredient ingredient : recipe.getIngredients()) {
            final int unitId = ingredient.getUnit().getId();
            final Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_UNIT_WITH_ID + unitId));
            ingredient.setUnit(unit);
        }
        return recipeRepository.save(recipe);
    }
}
