package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
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
        final Unit unit = new Unit(ID, NAME, ABBREVIATION);
        assertThat(unit.getId(), equalTo(ID));
        assertThat(unit.getName(), equalTo(NAME));
        assertThat(unit.getAbbreviation(), equalTo(ABBREVIATION));
    }

    @Test
    public void testSetGetAbbreviation() {
        unit.setAbbreviation(ABBREVIATION);
        assertThat(unit.getAbbreviation(), equalTo(ABBREVIATION));
    }

    @Test
    public void testSetGetId() {
        unit.setId(UNIT_ID);
        assertThat(unit.getId(), equalTo(UNIT_ID));
    }

    @Test
    public void testSetGetName() {
        unit.setName(NAME);
        assertThat(unit.getName(), equalTo(NAME));
    }
}
