package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Idea;

import java.util.List;

public interface IdeaService {
    List<Idea> getAllIdeas();

    Idea getIdeaById(Integer id);

    Idea createIdea(Idea idea);

    Idea updateIdea(Idea idea);

    void deleteIdea(Integer id);
}
