package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TagRestController {
    @GetMapping
    List<Tag> getAllTags();

    @GetMapping("/{id}")
    Tag getTagById(@PathVariable Integer id);

    @PostMapping
    Tag createTag(@RequestBody Tag tag);

    @PutMapping("/{id}")
    Tag updateTag(@PathVariable Integer id, @RequestBody Tag tag);

    @DeleteMapping("/{id}")
    void deleteTag(@PathVariable Integer id);
}
