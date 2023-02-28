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
public class RecipeRestController {
    private final RecipeRepository recipeRepository;
    private final UnitRepository unitRepository;

    public RecipeRestController(RecipeRepository recipeRepository, UnitRepository unitRepository) {
        this.recipeRepository = recipeRepository;
        this.unitRepository = unitRepository;
    }

    @GetMapping("/recipes/{id}")
    public Recipe one(@PathVariable Integer id) {
        return recipeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not find Recipe with id: " + id));
    }

    @GetMapping("/recipes")
    public List<Recipe> all() {
        return recipeRepository.findAll();
    }


    @PostMapping("/recipes")
    public Recipe newRecipe(@RequestBody Recipe recipe) {
        for (final Ingredient ingredient : recipe.getIngredients()) {
            final int unitId = ingredient.getUnit().getId();
            final Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new EntityNotFoundException("Could not find Unit with id: " + unitId));
            ingredient.setUnit(unit);
        }
        return recipeRepository.save(recipe);
    }
}
