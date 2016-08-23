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
    public void urlCheck() throws Exception {
        //"https://firebasestorage.googleapis.com/v0/b/flashcards-8ce7c.appspot.com/o/mp3Storage%2Fbiosm.mp3?alt=media&token=1ee74d6e-9f2d-4023-ad34-19d9f22cfcf4"
        String url = "https://firebasestorage.googleapis.com/v0/b/flashcards-8ce7c.appspot.com/o/mp3Storage%2Faggelosm.mp3?alt=media&token=8fc93694-3986-4e67-97bb-9d7755b575d9";
        assertEquals(substringBetween(url, "mp3Storage%2", "?alt="), "Faggelosm.mp3");
    }

    private String substringBetween(String mainString, String leftString, String rightString) {
        return mainString.substring(mainString.indexOf(leftString) + leftString.length(), mainString.indexOf(rightString));
    }
}