package com.lstierneyltd.recipebackend.utils;

import java.math.BigDecimal;

public final class TestConstants {
    public static final int ID = 123;
    public static final String RECIPE_1_NAME = "Spaghetti Bolognaise";
    public static final String RECIPE_1_DESCRIPTION = "The all time favourite";
    public static final String RECIPE_1_IMAGE_FILENAME = "picture.jpeg";

    public static final int UNIT_ID = 4;
    public static final String NAME = "name";
    public static final String ABBREVIATION = "abbreviation";
    public static final String DESCRIPTION = "description";
    public static final int ORDERING = 420;
    public static final BigDecimal QUANTITY = BigDecimal.valueOf(69);
    public static final int COOKING_TIME = 90;

    public static final int TAG_ID = 12346;
    public static final String TAG_NAME = "onepot";
    public static final String TAG_DESCRIPTION = "A recipe that only uses one pot. Hurrah!";

    public static final String USER_NAME = "lawrence";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "auth_token";


    private TestConstants() {
    }
}
