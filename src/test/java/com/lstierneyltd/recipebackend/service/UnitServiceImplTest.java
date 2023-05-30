package com.lstierneyltd.recipebackend.service;

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
public class UnitServiceImplTest {
    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitServiceImpl unitService;


    @AfterEach
    void after() {
        Mockito.verifyNoMoreInteractions(unitRepository);
    }

    @Test
    public void testGetAllUnits() {
        // When
        unitService.getAllUnits();

        // Then
        then(unitRepository).should().findAll();
    }
}
