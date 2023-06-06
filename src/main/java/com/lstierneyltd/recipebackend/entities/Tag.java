package com.lstierneyltd.recipebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lstierneyltd.recipebackend.annotation.Generated;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "tag",
        catalog = "recipes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
public class Tag implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", unique = true, nullable = false, length = 10)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Recipe> recipes = new ArrayList<>();


    public Tag() {
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

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
