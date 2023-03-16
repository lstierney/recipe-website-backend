package com.lstierneyltd.recipebackend.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public final class FileUtils {
    private FileUtils() {
    }

    public static String getFileAsString(final String fileName) throws IOException {
        final URL url = Resources.getResource(fileName);
        return Resources.toString(url, Charsets.UTF_8);
    }
}

