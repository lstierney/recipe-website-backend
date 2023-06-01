package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Ingredient;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.Tag;
import com.lstierneyltd.recipebackend.entities.Unit;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.repository.TagRepository;
import com.lstierneyltd.recipebackend.repository.UnitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {
    static final String COULD_NOT_FIND_UNIT_WITH_ID = "Could not find UNIT with id: ";
    static final String COULD_NOT_FIND_TAG_WITH_ID = "Could not find TAG with id: ";
    private final Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);
    private final UnitRepository unitRepository;
    private final TagRepository tagRepository;
    private final FileService fileService;
    private final ObjectMapperService objectMapperService;
    private final RecipeRepository recipeRepository;

    @Value("${isgithub:false}")
    private String isGitHub;

    public RecipeServiceImpl(UnitRepository unitRepository, TagRepository tagRepository, FileService fileService, ObjectMapperService objectMapperService, RecipeRepository recipeRepository) {
        this.unitRepository = unitRepository;
        this.tagRepository = tagRepository;
        this.fileService = fileService;
        this.objectMapperService = objectMapperService;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe addRecipe(MultipartFile imageFile, String recipeString) {
        final Recipe recipe = objectMapperService.jsonStringToObject(recipeString, Recipe.class);

        logger.info("Adding new recipe: " + recipe);

        populateUnits(recipe);
        populateTags(recipe);
        handleUploadedFile(imageFile, recipe);

        final Recipe newRecipe = recipeRepository.save(recipe);

        logger.info("New recipe added. Id: " + newRecipe.getId());

        return newRecipe;
    }

    private void populateUnits(Recipe recipe) {
        // TODO try and use stream().map() here
        for (final Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getUnit() != null) {
                final int unitId = ingredient.getUnit().getId();
                final Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_UNIT_WITH_ID + unitId));
                ingredient.setUnit(unit);
            }
        }
    }

    private void populateTags(Recipe recipe) {
        final List<Tag> managedTags = new ArrayList<>();
        for (final Tag tag : recipe.getTags()) {
            final int tagId = tag.getId();
            managedTags.add(tagRepository.findById(tagId).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_TAG_WITH_ID + tagId)));
        }
        recipe.setTags(managedTags);
    }

    private void handleUploadedFile(MultipartFile imageFile, Recipe recipe) {
        logger.info("isGitHub: " + isGitHub);

        if (!Boolean.parseBoolean(isGitHub)) {
            fileService.saveMultiPartFile(imageFile);
        }

        recipe.setImageFileName(imageFile.getOriginalFilename());
    }
}
