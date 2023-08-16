package com.lstierneyltd.recipebackend.entities;

import com.lstierneyltd.recipebackend.utils.TestStubs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ServedOnTest {
    private ServedOn servedOn;

    @BeforeEach
    void init() {
        servedOn = new ServedOn();
    }

    @Test
    public void testSetGetId() {
        servedOn.setId(ID);
        assertThat(servedOn.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetRecipe() {
        final Recipe recipe = TestStubs.getRecipe();

        servedOn.setRecipe(recipe);
        assertThat(servedOn.getRecipe(), equalTo(recipe));
    }

    @Test
    public void testSetGetHeated() {
        servedOn.setHeated(true);
        assertThat(servedOn.isHeated(), equalTo(true));
    }

    @Test
    public void testSetGetCrockery() {
        Crockery crockery = new Crockery();
        servedOn.setCrockery(crockery);
        assertThat(servedOn.getCrockery(), equalTo(crockery));
    }
}
