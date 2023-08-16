package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Crockery;
import com.lstierneyltd.recipebackend.repository.CrockeryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrockeryServiceImpl implements CrockeryService {
    private final CrockeryRepository crockeryRepository;

    public CrockeryServiceImpl(CrockeryRepository crockeryRepository) {
        this.crockeryRepository = crockeryRepository;
    }

    @Override
    public List<Crockery> getAllCrockery() {
        return crockeryRepository.findAll();
    }
}
