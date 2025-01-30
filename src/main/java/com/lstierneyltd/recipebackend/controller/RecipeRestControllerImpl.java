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
    @GetMapping("/id/{id}")
    public Recipe getRecipeById(@PathVariable Integer id) {
        return recipeService.findById(id);
    }

    @Override
    @GetMapping("/{name}")
    public Recipe getRecipeByName(@PathVariable String name) {
        return recipeService.findByName(name);
    }

    @Override
    @GetMapping(params = "tagNames")
    public List<Recipe> getRecipesByTags(@RequestParam("tagNames") List<String> tagNames) {
        return recipeService.findByTagNames(tagNames);
    }

    @Override
    @GetMapping("/latest")
    public List<RecipeRepository.RecipePreview> getLatestRecipes() {
        return recipeService.findLatest();
    }

    @Override
    @GetMapping("/randomDinners")
    public List<RecipeRepository.RecipePreview> getRandomDinners() {
        return recipeService.findRandomDinners();
    }

    @Override
    @GetMapping("/randomDinner")
    public RecipeRepository.RecipePreview getRandomDinner() {
        return recipeService.findRandomDinner();
    }

    @Override
    @PostMapping("/markascooked/{id}")
    public Recipe markRecipeAsCooked(@PathVariable Integer id) {
        return recipeService.markAsCooked(id);
    }

    @Override
    @GetMapping
    public List<Recipe> getAllActiveRecipes() {
        return recipeService.findAllActive();
    }

    @Override
    @GetMapping("/list")
    public List<RecipeRepository.RecipePreview> getRecipesPreviewList() {
        return recipeService.findAllRecipePreview();
    }

    @Override
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Recipe addRecipe(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "recipe") String recipeString) {
        return recipeService.addRecipe(imageFile, recipeString);
    }

    @Override
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Recipe updateRecipe(@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, @RequestParam(value = "recipe") String recipeString) {
        return recipeService.updateRecipe(imageFile, recipeString);
    }

    @Override
    @PutMapping("/markAsDeleted/{id}")
    public Recipe markRecipeAsDeleted(@PathVariable Integer id) {
        return recipeService.markAsDeleted(id);
    }

    @Override
    @PutMapping("/restore/{id}")
    public Recipe restore(@PathVariable Integer id) {
        return recipeService.restore(id);
    }

    @Override
    @GetMapping("/listIgnoreDeleted")
    public List<Recipe> getAllRecipesIgnoreDeleted() {
        return recipeService.findAll();
    }
}
