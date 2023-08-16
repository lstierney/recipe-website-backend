package com.lstierneyltd.recipebackend.entities;

import com.lstierneyltd.recipebackend.annotation.Generated;
import jakarta.persistence.*;

@Entity
@Table(
        name = "crockery",
        catalog = "recipes",
        uniqueConstraints = @UniqueConstraint(columnNames = "description")
)
public class Crockery {
    private int id;
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "description", nullable = false, length = 50)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    @Override
    public String toString() {
        return "Crockery{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
