package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<RecipeIdAndName> findAllRecipeIdAndNameBy();

    interface RecipeIdAndName {
        int getId();

        String getName();
    }
}
