package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Unit;
import com.lstierneyltd.recipebackend.repository.UnitRepository;
import com.lstierneyltd.recipebackend.service.UnitService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class UnitRestControllerImpl implements UnitRestController {
    private final UnitService unitService;

    public UnitRestControllerImpl(UnitService unitService) {
        this.unitService = unitService;
    }

    @Override
    @GetMapping
    public List<Unit> getAllUnits() {
        return unitService.getAllUnits();
    }
}
