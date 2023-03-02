package com.lstierneyltd.recipebackend.stubs;

import com.lstierneyltd.recipebackend.entities.Ingredient;
import com.lstierneyltd.recipebackend.entities.MethodStep;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.Unit;

import java.util.Date;
import java.util.List;

public class TestStubs {
    public static final int ID = 123;
    public static final String NAME = "name";
    public static final String ABBREVIATION = "abbreviation";
    public static final String DESCRIPTION = "description";
    public static final int ORDERING = 420;
    public static final long QUANTITY = 69;

    public static final Date COOKING_TIME = new Date();

    public final static Unit UNIT_1 = new Unit(ID, NAME, ABBREVIATION);

    public final static Recipe RECIPE_1 = new Recipe();

    public final static Recipe RECIPE_2;

    public final static Recipe RECIPE_3;

    public final static Ingredient INGREDIENT = new Ingredient(ID, RECIPE_1, UNIT_1, DESCRIPTION, QUANTITY);

    public final static MethodStep METHOD_STEP = new MethodStep(ID, RECIPE_1, ORDERING, DESCRIPTION);

    static {
        RECIPE_2 = new Recipe(
                ID, NAME, DESCRIPTION, COOKING_TIME
        );

        RECIPE_3 = new Recipe(
                ID, NAME, DESCRIPTION, COOKING_TIME, List.of(METHOD_STEP), List.of(INGREDIENT)
        );

    }
}
