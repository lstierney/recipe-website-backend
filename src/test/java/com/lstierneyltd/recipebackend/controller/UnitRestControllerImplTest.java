package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.service.UnitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
public class UnitRestControllerImplTest {
    @Mock
    private UnitService unitService;

    @InjectMocks
    private UnitRestControllerImpl unitRestController;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(unitService);
    }

    @Test
    public void testGetAllUnits() {
        // when
        unitRestController.getAllUnits();

        // then
        then(unitService).should().getAllUnits();
    }
}
