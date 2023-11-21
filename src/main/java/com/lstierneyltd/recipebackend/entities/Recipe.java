package com.lstierneyltd.recipebackend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lstierneyltd.recipebackend.annotation.Generated;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "recipe",
        catalog = "recipes",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class Recipe implements java.io.Serializable, IAuditable {
    private int id;
    private String name;
    private String description;
    private String imageFileName;
    private int cooked;
    private LocalDateTime lastCooked;
    private int cookingTime;
    private String basedOn;
    private List<MethodStep> methodSteps = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private Set<Tag> tags = new HashSet<>();
    private ServedOn servedOn;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
    private String createdBy;
    private String lastUpdatedBy;

    public Recipe() {
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

    @Column(name = "name", unique = true, nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = false, length = 500)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "cooking_time", nullable = false)
    public int getCookingTime() {
        return this.cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    @Column(name = "last_cooked")
    public LocalDateTime getLastCooked() {
        return this.lastCooked;
    }

    public void setLastCooked(LocalDateTime lastCooked) {
        this.lastCooked = lastCooked;
    }

    @Column(name = "cooked", nullable = false)
    public int getCooked() {
        return this.cooked;
    }

    public void setCooked(int cooked) {
        this.cooked = cooked;
    }

    public void markedAsCooked() {
        this.cooked++;
        this.lastCooked = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OrderBy("ordering ASC")
    public List<MethodStep> getMethodSteps() {
        return this.methodSteps;
    }

    public void setMethodSteps(List<MethodStep> methodSteps) {
        this.methodSteps = methodSteps;
        methodSteps.forEach(methodStep -> methodStep.setRecipe(this));
    }

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OrderBy("ordering ASC")
    public List<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notes.forEach(note -> note.setRecipe(this));
    }

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OrderBy("ordering ASC")
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        ingredients.forEach(ingredient -> ingredient.setRecipe(this));
    }

    @ManyToMany()
    @JoinTable(
            name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @OrderBy("id ASC")
    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Column(name = "image_filename", length = 100)
    public String getImageFileName() {
        return this.imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Column(name = "based_on", length = 250)
    public String getBasedOn() {
        return this.basedOn;
    }

    public void setBasedOn(String basedOn) {
        this.basedOn = basedOn;
    }

    @JoinColumn(name = "served_on_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL)
    public ServedOn getServedOn() {
        return servedOn;
    }

    public void setServedOn(ServedOn servedOn) {
        this.servedOn = servedOn;
    }

    @Override
    @Column(name = "created_date")
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    @Column(name = "last_updated_date")
    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @Override
    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    @Column(name = "created_by", length = 100)
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    @Column(name = "last_updated_by", length = 100)
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @Override
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Generated
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cookingTime=" + cookingTime +
                ", cooked=" + cooked +
                ", lastCooked=" + lastCooked +
                ", basedOn=" + basedOn +
                ", methodSteps=" + methodSteps +
                ", ingredients=" + ingredients +
                ", notes=" + notes +
                ", servedOn=" + servedOn +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                ", lastUpdatedBy=" + lastUpdatedBy +
                ", lastUpdatedDate=" + lastUpdatedDate +
                '}';
    }


    /*
     I would like to have used @Prepersist and @Preupdate here but @Preupdate only triggers if properties
     on the Recipe itself change NOT if values in nested collections e.g. Notes, Ingredients are updated.
     @Prepersist worked fine (as there was always an INSERT on Recipe) but it seemed incongruous to handle
     created... and updated... differently.
     */
    public void markAsCreated(String username) {
        createdDate = LocalDateTime.now();
        createdBy = username;
    }

    public void markAsUpdated(String username) {
        lastUpdatedDate = LocalDateTime.now();
        lastUpdatedBy = username;
    }
}


