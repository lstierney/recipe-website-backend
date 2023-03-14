package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<RecipeIdAndName> findAllRecipeIdAndNameBy();

    @Query("SELECT r FROM Recipe r JOIN r.tags t WHERE t.name = ?1")
    List<Recipe> findByTagName(String name);

    interface RecipeIdAndName {
        int getId();

        String getName();
    }
}
