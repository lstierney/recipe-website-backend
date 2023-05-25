package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Tag;
import com.lstierneyltd.recipebackend.service.TagService;
import com.lstierneyltd.recipebackend.utils.TestStubs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.lstierneyltd.recipebackend.utils.TestConstants.ID;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getTag;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
public class TagRestControllerImplTest {
    @Mock
    private TagService tagService;
    @InjectMocks
    private TagRestControllerImpl tagRestController;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void testGetTagById() {
        // When
        tagRestController.getTagById(ID);

        // Then
        then(tagService).should().getTagById(ID);
    }

    @Test
    public void testGetAll() {
        // When
        tagRestController.getAllTags();

        // then
        then(tagService).should().getAllTags();
    }

    @Test
    public void testCreateTag() {
        // given
        final Tag tag = getTag();

        // when
        tagRestController.createTag(tag);

        // then
        then(tagService).should().createTag(tag);
    }

    @Test
    public void testDelete() {
        // When
        tagRestController.deleteTag(ID);

        // Then
        then(tagService).should().deleteTag(ID);
    }

    @Test
    public void testUpdateTag_tagFound() {
        // Given
        Tag tag = TestStubs.getTag();

        // When
        tagRestController.updateTag(ID, tag);

        // Then
        then(tagService).should().updateTag(ID, tag);
    }
}
