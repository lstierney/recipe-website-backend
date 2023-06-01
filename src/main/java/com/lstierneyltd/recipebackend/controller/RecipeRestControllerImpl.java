package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.service.RecipeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class RecipeRestControllerImpl implements RecipeRestController {
    static final String COULD_NOT_FIND_RECIPE_WITH_ID = "Could not find RECIPE with id: ";

    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;

    public RecipeRestControllerImpl(RecipeRepository recipeRepository, RecipeService recipeService) {
        this.recipeRepository = recipeRepository;
        this.recipeService = recipeService;
    }

    @Override
    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Integer id) {
        return recipeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_RECIPE_WITH_ID + id));
    }

    @Override
    @GetMapping(params = "tagName")
    public List<Recipe> getRecipesByTagName(@RequestParam("tagName") String tagName) {
        return recipeRepository.findByTagName(tagName);
    }

    @Override
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    @GetMapping("/list")
    public List<RecipeRepository.RecipeIdAndName> getRecipesList() {
        return recipeRepository.findAllRecipeIdAndNameBy();
    }

    @Override
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Recipe addRecipe(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "recipe") String recipeString) {
        return recipeService.addRecipe(imageFile, recipeString);
    }
}
