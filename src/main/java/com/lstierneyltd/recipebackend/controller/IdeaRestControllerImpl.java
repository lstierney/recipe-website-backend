package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Idea;
import com.lstierneyltd.recipebackend.service.IdeaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideas")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class IdeaRestControllerImpl implements IdeaRestController {
    private final IdeaService ideaService;

    public IdeaRestControllerImpl(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @Override
    @GetMapping
    public List<Idea> getAllIdeas() {
        return ideaService.getAllIdeas();
    }

    @Override
    @GetMapping("/{id}")
    public Idea getIdeaById(@PathVariable Integer id) {
        return ideaService.getIdeaById(id);
    }

    @Override
    @PostMapping
    public Idea createIdea(@RequestBody Idea idea) {
        return ideaService.createIdea(idea);
    }

    @Override
    @PutMapping()
    public Idea updateIdea(@RequestBody Idea idea) {
        return ideaService.updateIdea(idea);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteIdea(@PathVariable Integer id) {
        ideaService.deleteIdea(id);
    }
}
