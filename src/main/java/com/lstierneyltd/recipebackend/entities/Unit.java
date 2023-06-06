package com.lstierneyltd.recipebackend.entities;

import com.lstierneyltd.recipebackend.annotation.Generated;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.util.Objects;

@Entity
@Immutable
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

    public Unit(int id, String name, String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
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

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return id == unit.id;
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


