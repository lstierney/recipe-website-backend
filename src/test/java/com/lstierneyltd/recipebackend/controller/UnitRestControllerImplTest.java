package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.repository.UnitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
public class UnitRestControllerImplTest {
    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitRestControllerImpl unitRestController;

    @AfterEach
    void after() {
        Mockito.verifyNoMoreInteractions(unitRepository);
    }

    @Test
    public void testGetAllUnits() {
        // when
        unitRestController.getAllUnits();

        // then
        then(unitRepository).should().findAll();
    }
}
