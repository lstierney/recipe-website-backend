package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    Tag getTagById(Integer id);

    Tag createTag(Tag tag);

    Tag updateTag(Integer id, Tag tag);

    void deleteTag(Integer id);
}
