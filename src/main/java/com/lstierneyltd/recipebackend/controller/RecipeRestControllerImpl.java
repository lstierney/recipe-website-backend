package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.dto.RecipeDto;
import com.lstierneyltd.recipebackend.dto.RecipePreviewDto;
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
    public RecipeDto getRecipeById(@PathVariable Integer id) {
        return recipeService.findById(id);
    }

    @Override
    @GetMapping("/{name}")
    public RecipeDto getRecipeByName(@PathVariable String name) {
        return recipeService.findByName(name);
    }

    @Override
    @GetMapping(params = "tagNames")
    public List<RecipeDto> getRecipesByTags(@RequestParam("tagNames") List<String> tagNames) {
        return recipeService.findByTagNames(tagNames);
    }

    @Override
    @GetMapping("/latest")
    public List<RecipePreviewDto> getLatestRecipePreviews() {
        return recipeService.findLatestPreviews();
    }

    @Override
    @GetMapping("/randomDinners")
    public List<RecipePreviewDto> getRandomDinnerPreviews() {
        return recipeService.findRandomDinnersPreviews();
    }

    @Override
    @GetMapping("/randomDinner")
    public RecipePreviewDto getRandomDinnerPreview() {
        return recipeService.findRandomDinnerPreview();
    }

    @Override
    @PostMapping("/markascooked/{id}")
    public RecipeDto markRecipeAsCooked(@PathVariable Integer id) {
        return recipeService.markAsCooked(id);
    }

    @Override
    @GetMapping
    public List<RecipeDto> getAllActiveRecipes() {
        return recipeService.findAllActiveRecipes();
    }

    @Override
    @GetMapping("/list")
    public List<RecipePreviewDto> getRecipesPreviewList() {
        return recipeService.findAllActiveRecipePreview();
    }

    @Override
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RecipeDto addRecipe(@RequestParam(value = "imageFile") MultipartFile imageFile, @RequestParam(value = "recipe") String recipeString) {
        return recipeService.addRecipe(imageFile, recipeString);
    }

    @Override
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RecipeDto updateRecipe(@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, @RequestParam(value = "recipe") String recipeString) {
        return recipeService.updateRecipe(imageFile, recipeString);
    }

    @Override
    @PutMapping("/markAsDeleted/{id}")
    public RecipeDto markRecipeAsDeleted(@PathVariable Integer id) {
        return recipeService.markAsDeleted(id);
    }

    @Override
    @PutMapping("/restore/{id}")
    public RecipeDto restore(@PathVariable Integer id) {
        return recipeService.restore(id);
    }

    @Override
    @GetMapping("/listIgnoreDeleted")
    public List<RecipeDto> getAllRecipesIgnoreDeleted() {
        return recipeService.findAll();
    }
}
