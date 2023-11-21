package com.lstierneyltd.recipebackend.entities;

import java.time.LocalDateTime;

public interface IAuditable {
    LocalDateTime getCreatedDate();

    void setCreatedDate(LocalDateTime createdDate);

    LocalDateTime getLastUpdatedDate();

    void setLastUpdatedDate(LocalDateTime lastUpdatedDate);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    String getLastUpdatedBy();

    void setLastUpdatedBy(String lastUpdatedBy);
}
