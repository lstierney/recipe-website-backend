package com.lstierneyltd.recipebackend;

import com.lstierneyltd.recipebackend.entities.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeBackendApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testRest() throws Exception {
        ResponseEntity<Unit[]> response = testRestTemplate.getForEntity("/units", Unit[].class);
        Unit[] units = response.getBody();
        assertNotNull(units);
    }
}
