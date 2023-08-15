package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<RecipePreview> findAllRecipePreviewBy();

    @Query("SELECT r FROM Recipe r JOIN r.tags t WHERE t.name = ?1")
    List<Recipe> findByTagName(String name);

    @Query("SELECT r FROM Recipe r ORDER BY RAND() LIMIT 1")
    Optional<RecipePreview> findRecipePreviewOrderByRand();

    List<RecipePreview> findTop6RecipePreviewByOrderByIdDesc();

    @Query("SELECT r FROM Recipe r WHERE lower(r.name) = ?1")
    Optional<Recipe> findByName(String name);

    /**
     * This is a "Projection"
     * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections">...</a>
     */
    interface RecipePreview {
        int getId();

        String getName();

        String getImageFileName();

        String getDescription();
    }
}
