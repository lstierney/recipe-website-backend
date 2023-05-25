package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.Tag;
import com.lstierneyltd.recipebackend.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    public final static String COULD_NOT_FIND_TAG_WITH_ID = "Could not find tag with id: ";

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Integer id) {
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_TAG_WITH_ID + id));
    }

    @Override
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Integer id, Tag tag) {
        final Tag existingTag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TagServiceImpl.COULD_NOT_FIND_TAG_WITH_ID + id));
        existingTag.setName(tag.getName());
        existingTag.setDescription(tag.getDescription());
        return tagRepository.save(existingTag);
    }

    @Override
    public void deleteTag(Integer id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TagServiceImpl.COULD_NOT_FIND_TAG_WITH_ID + id));

        for (Recipe recipe : tag.getRecipes()) {
            recipe.getTags().remove(tag);
        }
        tag.getRecipes().clear();

        tagRepository.deleteById(id);
    }
}
