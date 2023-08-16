package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Crockery;
import com.lstierneyltd.recipebackend.service.CrockeryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crockery")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class CrockeryRestControllerImpl implements CrockeryRestController {
    private final CrockeryService crockeryService;

    public CrockeryRestControllerImpl(CrockeryService crockeryService) {
        this.crockeryService = crockeryService;
    }

    @Override
    @GetMapping
    public List<Crockery> getAllCrockery() {
        return crockeryService.getAllCrockery();
    }
}
