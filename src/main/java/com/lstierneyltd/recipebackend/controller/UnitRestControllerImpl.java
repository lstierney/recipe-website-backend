package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Unit;
import com.lstierneyltd.recipebackend.repository.UnitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnitRestControllerImpl implements UnitRestController {
    private UnitRepository unitRepository;

    public UnitRestControllerImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    @GetMapping("/units")
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }
}
