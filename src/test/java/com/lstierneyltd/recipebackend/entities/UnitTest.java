package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.stubs.TestStubs.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UnitTest {
    private Unit unit;

    @BeforeEach
    void init() {
        unit = new Unit();
    }

    @Test
    public void testConstructor() {
        assertThat(UNIT_1.getId(), equalTo(ID));
        assertThat(UNIT_1.getName(), equalTo(NAME));
        assertThat(UNIT_1.getAbbreviation(), equalTo(ABBREVIATION));
    }

    @Test
    public void testSetGetAbbreviation() {
        unit.setAbbreviation(ABBREVIATION);
        assertThat(unit.getAbbreviation(), equalTo(ABBREVIATION));
    }

    @Test
    public void testSetGetId() {
        unit.setId(ID);
        assertThat(unit.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetName() {
        unit.setName(NAME);
        assertThat(unit.getName(), equalTo(NAME));
    }
}
