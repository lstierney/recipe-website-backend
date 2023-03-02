package com.lstierneyltd.recipebackend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "recipe",
        catalog = "recipes",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class Recipe implements java.io.Serializable {
    private int id;
    private String name;
    private String description;
    private Date cookingTime;
    private List<MethodStep> methodSteps = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", unique = true, nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = false, length = 65535)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.TIME)
    @Column(name = "cooking_time", nullable = false, length = 8)
    public Date getCookingTime() {
        return this.cookingTime;
    }

    public void setCookingTime(Date cookingTime) {
        this.cookingTime = cookingTime;
    }

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonManagedReference
    public List<MethodStep> getMethodSteps() {
        return this.methodSteps;
    }

    public void setMethodSteps(List<MethodStep> methodSteps) {
        this.methodSteps = methodSteps;
        methodSteps.forEach(methodStep -> methodStep.setRecipe(this));
    }

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonManagedReference
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        ingredients.forEach(ingredient -> ingredient.setRecipe(this));
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cookingTime=" + cookingTime +
                '}';
    }

}


