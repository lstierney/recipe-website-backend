package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.service.CrockeryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
public class CrockeryRestControllerImplTest {
    @Mock
    private CrockeryService crockeryService;

    @InjectMocks
    private CrockeryRestControllerImpl crockeryRestController;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(crockeryService);
    }

    @Test
    public void testGetAllCrockery() {
        // when
        crockeryRestController.getAllCrockery();

        // then
        then(crockeryService).should().getAllCrockery();
    }
}
