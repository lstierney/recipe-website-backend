package com.lstierneyltd.recipebackend.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "idea",
        catalog = "recipes",
        uniqueConstraints = @UniqueConstraint(columnNames = "url")
)
public class Idea extends Auditable {
    private int id;
    private String name;
    private String url;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", unique = true, nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "url", unique = true, nullable = false, length = 500)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Idea{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}' +
                super.toString();
    }
}
