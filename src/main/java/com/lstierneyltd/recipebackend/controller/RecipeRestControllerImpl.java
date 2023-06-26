package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.service.RecipeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class RecipeRestControllerImpl implements RecipeRestController {


    private final RecipeService recipeService;

    public RecipeRestControllerImpl(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Integer id) {
        return recipeService.findById(id);
    }

    @Override
    @GetMapping(params = "tagName")
    public List<Recipe> getRecipesByTagName(@RequestParam("tagName") String tagName) {
        return recipeService.findByTagName(tagName);
    }

    @Override
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.findAll();
    }

    @Override
    @GetMapping("/list")
    public List<RecipeRepository.RecipeIdAndName> getRecipesList() {
        return recipeService.findAllRecipeIdAndNameBy();
    }

    @Override
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Recipe addRecipe(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "recipe") String recipeString) {
        return recipeService.addRecipe(imageFile, recipeString);
    }
}
