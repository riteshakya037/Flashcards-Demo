package com.sorcery.flashcards;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void name() throws Exception {
        String string = "cards/set11";
        assertEquals("Set " + string.replaceAll("[^\\d]+", ""), "Set 1");
    }
}