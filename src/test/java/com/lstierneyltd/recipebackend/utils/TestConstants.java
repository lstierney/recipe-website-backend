package com.lstierneyltd.recipebackend.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public static final int COOKED = 5;
    public static final LocalDateTime LAST_COOKED = LocalDateTime.now();
    public static final LocalDateTime LAST_UPDATED_DATE = LocalDateTime.now();
    public static final LocalDateTime CREATED_DATE = LocalDateTime.now();
    public static final String LAST_UPDATED_BY = "lawrence";
    public static final String CREATED_BY = "lawrence";

    public static final String LOGGED_IN_USER = "jimbo";

    public static final String BASED_ON = "This recipe was based on another";

    public static final int TAG_ID = 12346;
    public static final String TAG_NAME = "onepot";
    public static final String TAG_DESCRIPTION = "A recipe that only uses one pot. Hurrah!";

    public static final String USER_NAME = "lawrence";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "auth_token";

    public static final int IDEA_ID = 442;
    public static final String IDEA_NAME = "Idea Name";
    public static final String IDEA_URL = "https://www.bbcgoodfood.com/?IGNORE_GEO_REDIRECT_GLOBAL=true&v=1083955399";

    public static final String BASE_IMAGE_FOLDER_PATH = "/opt/recipewebsite/images/";



    private TestConstants() {
    }
}
