package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Tag;
import com.lstierneyltd.recipebackend.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Optional;

import static com.lstierneyltd.recipebackend.service.TagServiceImpl.COULD_NOT_FIND_TAG_WITH_ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.ID;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getTag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {
    private static final String NEW_DESCRIPTION = "new description";
    private static final String NEW_NAME = "new name";
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private TagServiceImpl tagService;
    @Captor
    private ArgumentCaptor<Tag> tagCaptor;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    public void testGetTagById_tagFound() {
        // Given
        given(tagRepository.findById(ID)).willReturn(Optional.of(getTag()));

        // When
        tagService.getTagById(ID);

        // Then
        then(tagRepository).should().findById(ID);
    }

    @Test
    public void testGetTagById_tagNotFound() {
        // Given
        given(tagRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> tagService.getTagById(ID));

        // Then
        then(tagRepository).should().findById(ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_TAG_WITH_ID + ID));
    }

    @Test
    public void testGetAll() {
        // When
        tagService.getAllTags();

        // then
        then(tagRepository).should().findAllByOrderByIdDesc();
    }

    @Test
    public void testCreateTag() {
        // given
        final Tag tag = getTag();

        // when
        tagService.createTag(tag);

        // then
        then(tagRepository).should().save(tag);
    }

    @Test
    public void testDelete_tagFound() {
        // Given
        given(tagRepository.findById(ID)).willReturn(Optional.of(getTag()));

        // When
        tagService.deleteTag(ID);

        // Then
        then(tagRepository).should().findById(ID);
        then(tagRepository).should().deleteById(ID);
    }

    @Test
    public void testDelete_tagNotFound() {
        // Given
        given(tagRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTag(ID));

        // Then
        then(tagRepository).should().findById(ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_TAG_WITH_ID + ID));
    }

    private Tag getNewTag() {
        final Tag newTag = new Tag();
        newTag.setDescription(NEW_DESCRIPTION);
        newTag.setName(NEW_NAME);
        return newTag;
    }

    @Test
    public void testUpdateTag_tagFound() {
        // Given
        final Tag existingTag = getTag();
        final Tag newTag = getNewTag();

        given(tagRepository.findById(ID)).willReturn(Optional.of(existingTag));

        // When
        tagService.updateTag(ID, newTag);

        // Then
        then(tagRepository).should().findById(ID);
        then(tagRepository).should().save(tagCaptor.capture());

        assertThat(tagCaptor.getValue().getDescription(), equalTo(NEW_DESCRIPTION));
        assertThat(tagCaptor.getValue().getName(), equalTo(NEW_NAME));
    }

    @Test
    public void testUpdateTag_tagNotFound() {
        // Given
        given(tagRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> tagService.updateTag(ID, getNewTag()));

        // Then
        then(tagRepository).should().findById(ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_TAG_WITH_ID + ID));
    }
}
