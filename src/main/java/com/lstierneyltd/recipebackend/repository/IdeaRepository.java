package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdeaRepository extends JpaRepository<Idea, Integer> {
    List<Idea> findAllByOrderByIdDesc();
}
