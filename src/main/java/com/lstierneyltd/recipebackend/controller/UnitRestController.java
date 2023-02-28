package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Unit;
import com.lstierneyltd.recipebackend.repository.UnitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnitRestController {
    private UnitRepository unitRepository;

    public UnitRestController(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @GetMapping("/units")
    public List<Unit> all() {
        return unitRepository.findAll();
    }
}
