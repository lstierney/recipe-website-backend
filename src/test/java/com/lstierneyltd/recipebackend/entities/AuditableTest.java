package com.lstierneyltd.recipebackend.entities;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.lstierneyltd.recipebackend.utils.TestUtils.areWithinSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuditableTest {
    private static final String USERNAME = "lawrence";
    private Auditable auditable;

    @BeforeEach
    void before() {
        auditable = new Auditable();
    }

    @Test
    public void testGetSetLastUpdatedDate() {
        LocalDateTime lastUpdatedDate = LocalDateTime.now();
        auditable.setLastUpdatedDate(lastUpdatedDate);
        assertThat(auditable.getLastUpdatedDate(), is(lastUpdatedDate));
    }

    @Test
    public void testGetSetCreatedDate() {
        LocalDateTime createdDate = LocalDateTime.now();
        auditable.setCreatedDate(createdDate);
        assertThat(auditable.getCreatedDate(), is(createdDate));
    }

    @Test
    public void testGetSetCreatedBy() {
        auditable.setCreatedBy(USERNAME);
        assertThat(auditable.getCreatedBy(), is(USERNAME));
    }

    @Test
    public void testGetSetLastUpdatedBy() {
        auditable.setLastUpdatedBy(USERNAME);
        assertThat(auditable.getLastUpdatedBy(), is(USERNAME));
    }

    @Test
    public void testMarkAsCreated() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveSecsAfter = now.plusSeconds(5);

        auditable.markAsCreated(USERNAME);
        assertThat(auditable.getCreatedBy(), is(USERNAME));
        assertThat(auditable.getCreatedDate(), CoreMatchers.is(lessThanOrEqualTo(fiveSecsAfter)));
    }

    @Test
    public void testMarkAsUpdated() {
        auditable.markAsUpdated(USERNAME);
        assertThat(auditable.getLastUpdatedBy(), is(USERNAME));
        assertTrue(areWithinSeconds(auditable.getLastUpdatedDate(), LocalDateTime.now(), 5));
    }
}
