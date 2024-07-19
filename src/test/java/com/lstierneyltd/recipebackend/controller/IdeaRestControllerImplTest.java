package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Idea;
import com.lstierneyltd.recipebackend.service.IdeaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.lstierneyltd.recipebackend.utils.TestConstants.IDEA_ID;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getIdea;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class IdeaRestControllerImplTest {
    @Mock
    private IdeaService ideaService;
    @InjectMocks
    private IdeaRestControllerImpl ideaRestController;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(ideaService);
    }

    @Test
    public void testGetTagById() {
        // When
        ideaRestController.getIdeaById(IDEA_ID);

        // Then
        then(ideaService).should().getIdeaById(IDEA_ID);
    }

    @Test
    public void testGetAll() {
        // When
        ideaRestController.getAllIdeas();

        // then
        then(ideaService).should().getAllIdeas();
    }

    @Test
    public void testCreateTag() {
        // given
        final Idea idea = getIdea();

        // when
        ideaRestController.createIdea(idea);

        // then
        then(ideaService).should().createIdea(idea);
    }

    @Test
    public void testDelete() {
        // When
        ideaRestController.deleteIdea(IDEA_ID);

        // Then
        then(ideaService).should().deleteIdea(IDEA_ID);
    }

    @Test
    public void testUpdateTag() {
        // Given
        Idea idea = getIdea();

        // When
        ideaRestController.updateIdea(idea);

        // Then
        then(ideaService).should().updateIdea(idea);
    }
}
