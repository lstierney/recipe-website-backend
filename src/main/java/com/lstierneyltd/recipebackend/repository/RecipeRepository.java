package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<RecipeIdAndName> findAllRecipeIdAndNameBy();

    class RecipeIdAndName {
        private int id;
        private String name;

        public RecipeIdAndName() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
