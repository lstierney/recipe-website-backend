package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {

}
