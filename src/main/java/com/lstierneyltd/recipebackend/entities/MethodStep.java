package com.lstierneyltd.recipebackend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lstierneyltd.recipebackend.annotation.Generated;
import jakarta.persistence.*;

@Entity
@Table(
        name = "method_step",
        catalog = "recipes"
)
public class MethodStep implements java.io.Serializable, Orderable {
    private int id;
    private Recipe recipe;
    private int ordering;
    private String description;

    public MethodStep() {
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

    @ManyToOne()
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonBackReference
    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Column(name = "ordering", nullable = false)
    public int getOrdering() {
        return this.ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    @Override
    public String toString() {
        return "MethodStep{" +
                "id=" + id +
                ", ordering=" + ordering +
                ", description='" + description + '\'' +
                '}';
    }
}


