package com.lstierneyltd.recipebackend.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "unit",
        catalog = "recipes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "abbreviation"),
                @UniqueConstraint(columnNames = "name")
        }
)
public class Unit implements java.io.Serializable {
    private int id;
    private String name;
    private String abbreviation;

    public Unit() {
    }
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", unique = true, nullable = false, length = 20)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "abbreviation", unique = true, nullable = false, length = 10)
    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}


