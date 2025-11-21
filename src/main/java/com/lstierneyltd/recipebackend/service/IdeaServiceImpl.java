package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Idea;
import com.lstierneyltd.recipebackend.repository.IdeaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class IdeaServiceImpl implements IdeaService {
    public final static String COULD_NOT_FIND_IDEA_WITH_ID = "Could not find Idea with id: ";
    private final IdeaRepository ideaRepository;

    private final UserService userService;

    public IdeaServiceImpl(IdeaRepository ideaRepository, UserService userService) {
        this.ideaRepository = ideaRepository;
        this.userService = userService;
    }

    @Override
    public List<Idea> getAllIdeas() {
        return ideaRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Idea getIdeaById(Integer id) {
        return ideaRepository.findById(id).orElseThrow(() -> new NoSuchElementException(COULD_NOT_FIND_IDEA_WITH_ID + id));
    }

    @Override
    public Idea createIdea(Idea idea) {
        idea.markAsCreated(userService.getLoggedInUsername());
        return ideaRepository.save(idea);
    }

    @Override
    public Idea updateIdea(Idea idea) {
        idea.markAsUpdated(userService.getLoggedInUsername());
        return ideaRepository.save(idea);
    }

    @Override
    public void deleteIdea(Integer id) {
        ideaRepository.deleteById(id);
    }
}
