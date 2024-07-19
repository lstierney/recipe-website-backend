package com.lstierneyltd.recipebackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public class Auditable implements IAuditable {
    private LocalDateTime lastUpdatedDate;
    private LocalDateTime createdDate;
    private String createdBy;
    private String lastUpdatedBy;

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
    @Column(name = "created_date")
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    /*
     I would like to have used @Prepersist and @Preupdate here but @Preupdate only triggers if properties
     on the Recipe itself change NOT if values in nested collections e.g. Notes, Ingredients are updated.
     @Prepersist worked fine (as there was always an INSERT on Recipe) but it seemed incongruous to handle
     created... and updated... differently.
     */
    @Override
    public void markAsCreated(String username) {
        setCreatedDate(LocalDateTime.now());
        setCreatedBy(username);
    }

    @Override
    public void markAsUpdated(String username) {
        setLastUpdatedDate(LocalDateTime.now());
        setLastUpdatedBy(username);
    }

    @Override
    public String toString() {
        return "Auditable{" +
                "lastUpdatedDate=" + lastUpdatedDate +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
