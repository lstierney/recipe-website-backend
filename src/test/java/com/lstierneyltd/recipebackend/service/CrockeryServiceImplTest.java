package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.repository.CrockeryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CrockeryServiceImplTest {
    @Mock
    private CrockeryRepository crockeryRepository;

    @InjectMocks
    private CrockeryServiceImpl crockeryService;


    @AfterEach
    void after() {
        Mockito.verifyNoMoreInteractions(crockeryRepository);
    }

    @Test
    public void testGetAllCrockery() {
        // When
        crockeryService.getAllCrockery();

        // Then
        then(crockeryRepository).should().findAll();
    }
}