package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query("SELECT r FROM Recipe r WHERE r.deleted = false")
    List<RecipePreview> findAllActiveRecipePreviews();

    @Query("SELECT r FROM Recipe r " +
            "WHERE ((SELECT COUNT(t) FROM r.tags t WHERE t.name IN :tagNames) = :tagCount) AND (r.deleted = false) " +
            "ORDER BY r.lastCooked ASC")
    List<Recipe> findByAllActiveRecipesByTagNames(List<String> tagNames, Long tagCount);

    @Query("SELECT r FROM Recipe r JOIN r.tags t WHERE t.name = 'dinner' AND r.deleted = false ORDER BY RAND() LIMIT 6")
    List<RecipePreview> findSixRandomActiveDinners();

    @Query("SELECT r FROM Recipe r JOIN r.tags t WHERE t.name = 'dinner' AND r.deleted = false ORDER BY RAND() LIMIT 1")
    RecipePreview findRandomActiveDinnerPreview();

    @Query("SELECT r FROM Recipe r JOIN r.tags t WHERE t.name = 'dinner' AND r.deleted = false ORDER BY r.id desc LIMIT 6")
    List<RecipePreview> findSixLatestActiveDinnerPreviews();

    @Query("SELECT r FROM Recipe r WHERE lower(r.name) = ?1 AND r.deleted = false")
    Optional<Recipe> findActiveByName(String name);

    @Query("SELECT r FROM Recipe r WHERE r.deleted = false")
    List<Recipe> findActiveRecipes();

    /**
     * This is a "Projection"
     * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections">...</a>
     */
    interface RecipePreview {
        int getId();

        String getName();

        String getDescription();

        int getCooked();

        LocalDateTime getLastCooked();
    }
}
