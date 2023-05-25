package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Tag;
import com.lstierneyltd.recipebackend.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class TagRestControllerImpl implements TagRestController {
    private final TagService tagService;

    public TagRestControllerImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @Override
    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Integer id) {
        return tagService.getTagById(id);
    }

    @Override
    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    @Override
    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Integer id, @RequestBody Tag tag) {
        return tagService.updateTag(id, tag);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
    }
}
