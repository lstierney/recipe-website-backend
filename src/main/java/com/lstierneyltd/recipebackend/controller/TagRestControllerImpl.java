package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Tag;
import com.lstierneyltd.recipebackend.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class TagRestControllerImpl implements TagRestController {
    final static String COULD_NOT_FIND_TAG_WITH_ID = "Could not find tag with id: ";
    private final TagRepository tagRepository;

    public TagRestControllerImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @GetMapping
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Integer id) {
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_TAG_WITH_ID + id));
    }

    @Override
    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Integer id, @RequestBody Tag tag) {
        final Tag existingTag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_TAG_WITH_ID + id));
        existingTag.setName(tag.getName());
        existingTag.setDescription(tag.getDescription());
        return tagRepository.save(existingTag);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Integer id) {
        tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(COULD_NOT_FIND_TAG_WITH_ID + id));
        tagRepository.deleteById(id);
    }
}
