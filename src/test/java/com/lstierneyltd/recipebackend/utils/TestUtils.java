package com.lstierneyltd.recipebackend.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TestUtils {
    public static boolean areWithinSeconds(LocalDateTime dateTime1, LocalDateTime dateTime2, long seconds) {
        Duration duration = Duration.between(dateTime1, dateTime2);
        long differenceInSeconds = Math.abs(duration.getSeconds());
        return differenceInSeconds <= seconds;
    }
}
