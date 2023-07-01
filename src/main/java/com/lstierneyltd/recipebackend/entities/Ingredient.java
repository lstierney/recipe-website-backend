package com.lstierneyltd.recipebackend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lstierneyltd.recipebackend.annotation.Generated;
import jakarta.persistence.*;

@Entity
@Table(
        name = "ingredient",
        catalog = "recipes"
)
public class Ingredient implements java.io.Serializable, Orderable {
    private int id;
    private Recipe recipe;
    private Unit unit;
    private String description;
    private int quantity;
    private int ordering;

    public Ingredient() {
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

    @JoinColumn(name = "unit_id", referencedColumnName = "id")
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
    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Column(name = "ordering", nullable = false)
    public int getOrdering() {
        return this.ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    @Generated
    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", unit=" + unit +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}


