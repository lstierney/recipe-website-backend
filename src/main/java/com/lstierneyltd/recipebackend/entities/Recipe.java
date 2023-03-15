package com.lstierneyltd.recipebackend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "recipe",
        catalog = "recipes",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class Recipe implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 65535)
    private String description;

    @Column(name = "cooking_time", nullable = false)
    private int cookingTime;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<MethodStep> methodSteps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    public Recipe() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    @JsonManagedReference
    public List<MethodStep> getMethodSteps() {
        return this.methodSteps;
    }

    public void setMethodSteps(List<MethodStep> methodSteps) {
        this.methodSteps = methodSteps;
        methodSteps.forEach(methodStep -> methodStep.setRecipe(this));
    }

    @JsonManagedReference
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        ingredients.forEach(ingredient -> ingredient.setRecipe(this));
    }

    //@JsonManagedReference(value="tagsList")
    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cookingTime=" + cookingTime +
                ", methodSteps=" + methodSteps +
                ", ingredients=" + ingredients +
                '}';
    }
}


