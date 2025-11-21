package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Idea;
import com.lstierneyltd.recipebackend.repository.IdeaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.lstierneyltd.recipebackend.service.IdeaServiceImpl.COULD_NOT_FIND_IDEA_WITH_ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.IDEA_ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.LOGGED_IN_USER;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getIdea;
import static com.lstierneyltd.recipebackend.utils.TestUtils.areWithinSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class IdeaServiceImplTest {
    @Mock
    private IdeaRepository ideaRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private IdeaServiceImpl ideaService;

    @Captor
    private ArgumentCaptor<Idea> ideaArgumentCaptor;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(ideaRepository, userService);
    }

    @Test
    public void testGetIdeaById_tagFound() {
        // Given
        given(ideaRepository.findById(IDEA_ID)).willReturn(Optional.of(getIdea()));

        // When
        ideaService.getIdeaById(IDEA_ID);

        // Then
        then(ideaRepository).should().findById(IDEA_ID);
    }

    @Test
    public void testGetIdeaById_ideaNotFound() {
        // Given
        given(ideaRepository.findById(IDEA_ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoSuchElementException.class, () -> ideaService.getIdeaById(IDEA_ID));

        // Then
        then(ideaRepository).should().findById(IDEA_ID);
        assertThat(exception.getMessage(), is(COULD_NOT_FIND_IDEA_WITH_ID + IDEA_ID));
    }

    @Test
    public void testGetAll() {
        // When
        ideaService.getAllIdeas();

        // then
        then(ideaRepository).should().findAllByOrderByIdDesc();
    }

    @Test
    public void testCreateIdea() {
        // given
        final Idea idea = getIdea();
        given(userService.getLoggedInUsername()).willReturn(LOGGED_IN_USER);

        // when
        ideaService.createIdea(idea);

        // then
        then(ideaRepository).should().save(ideaArgumentCaptor.capture());

        // Test that the audit data was added
        Idea savedIdea = ideaArgumentCaptor.getValue();
        assertThat(savedIdea.getCreatedBy(), is(LOGGED_IN_USER));
        assertTrue(areWithinSeconds(savedIdea.getCreatedDate(), LocalDateTime.now(), 5));
    }

    @Test
    public void testDelete_ideaFound() {
        // When
        ideaService.deleteIdea(IDEA_ID);

        // Then
        then(ideaRepository).should().deleteById(IDEA_ID);
    }

    @Test
    public void testUpdateIdea() {
        // Given
        given(userService.getLoggedInUsername()).willReturn(LOGGED_IN_USER);

        final Idea idea = getIdea();
        idea.setName("updated name");
        idea.setUrl("updated url");

        // When
        ideaService.updateIdea(idea);

        // Then
        then(ideaRepository).should().save(ideaArgumentCaptor.capture());
        then(userService).should().getLoggedInUsername();

        Idea updatedIdea = ideaArgumentCaptor.getValue();
        assertThat(updatedIdea.getLastUpdatedBy(), is(LOGGED_IN_USER));
        assertThat(updatedIdea.getName(), is("updated name"));
        assertThat(updatedIdea.getUrl(), is("updated url"));
        assertTrue(areWithinSeconds(updatedIdea.getLastUpdatedDate(), LocalDateTime.now(), 5));
    }
}
