package com.lstierneyltd.recipebackend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(
        name = "ingredient",
        catalog = "recipes"
)
public class Ingredient implements java.io.Serializable {
    private int id;
    private Recipe recipe;
    private Unit unit;
    private String description;
    private long quantity;

    public Ingredient() {
    }

    public Ingredient(int id, Recipe recipe, Unit unit, String description, long quantity) {
        this.id = id;
        this.recipe = recipe;
        this.unit = unit;
        this.description = description;
        this.quantity = quantity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonBackReference
    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @JoinColumn(name = "unit_id", nullable = false, referencedColumnName = "id")
    @OneToOne
    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "quantity", nullable = false, precision = 10, scale = 0)
    public long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}


