package com.lstierneyltd.recipebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lstierneyltd.recipebackend.annotation.Generated;
import jakarta.persistence.*;

@Entity
@Table(
        name = "served_on",
        catalog = "recipes"
)
public class ServedOn {
    private int id;
    private Crockery crockery;
    private boolean isHeated;
    private Recipe recipe;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JoinColumn(name = "crockery_id", referencedColumnName = "id")
    @OneToOne
    public Crockery getCrockery() {
        return crockery;
    }

    public void setCrockery(Crockery crockery) {
        this.crockery = crockery;
    }

    @JsonIgnore
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    @OneToOne
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean isHeated() {
        return isHeated;
    }

    public void setHeated(boolean heated) {
        isHeated = heated;
    }

    @Generated
    @Override
    public String toString() {
        return "ServedOn{" +
                "id=" + id +
                ", crockery=" + crockery +
                ", isHeated=" + isHeated +
                '}';
    }
}
