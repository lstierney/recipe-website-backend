package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Idea;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IdeaRestController {
    List<Idea> getAllIdeas();

    Idea getIdeaById(@PathVariable Integer id);

    Idea createIdea(@RequestBody Idea idea);

    Idea updateIdea(@RequestBody Idea idea);

    void deleteIdea(@PathVariable Integer id);
}
