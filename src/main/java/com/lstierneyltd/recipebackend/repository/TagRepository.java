package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findAllByOrderByIdDesc();
}
