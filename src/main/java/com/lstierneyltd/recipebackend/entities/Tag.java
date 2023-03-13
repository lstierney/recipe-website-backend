package com.lstierneyltd.recipebackend.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "tag",
        catalog = "recipes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
public class Tag implements java.io.Serializable {
    private int id;
    private String name;
    private String description;

    public Tag() {
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

    @Column(name = "name", unique = true, nullable = false, length = 10)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 100)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
