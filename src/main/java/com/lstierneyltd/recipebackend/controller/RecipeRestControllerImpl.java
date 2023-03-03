package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Ingredient;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.Unit;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.repository.UnitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeRestControllerImpl implements RecipeRestController {
    private final Logger logger = LoggerFactory.getLogger(RecipeRestControllerImpl.class);
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
        logger.debug("Getting recipe by id:" + id);
        return recipeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_RECIPE_WITH_ID + id));
    }

    @Override
    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        logger.debug("Getting all recipes");
        List<Recipe> recipes = recipeRepository.findAll();
        logger.debug("Got " + recipes.size() + " recipes");
        return recipes;
    }

    @Override
    @PostMapping("/recipes")
    public Recipe newRecipe(@RequestBody Recipe recipe) {
        logger.debug("Adding new recipe: " + recipe);
        for (final Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getUnit() != null) {
                final int unitId = ingredient.getUnit().getId();
                final Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_UNIT_WITH_ID + unitId));
                ingredient.setUnit(unit);
            }
        }
        final Recipe newRecipe = recipeRepository.save(recipe);
        logger.info("New recipe added");

        return newRecipe;
    }
}