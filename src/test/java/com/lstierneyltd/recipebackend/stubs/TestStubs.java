package com.lstierneyltd.recipebackend.stubs;

import com.lstierneyltd.recipebackend.entities.Ingredient;
import com.lstierneyltd.recipebackend.entities.MethodStep;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.Unit;

public class TestStubs {
    public static final int ID = 123;
    public static final String NAME = "name";
    public static final String ABBREVIATION = "abbreviation";
    public static final String DESCRIPTION = "description";
    public static final int ORDERING = 420;
    public static final long QUANTITY = 69;

    public final static Unit UNIT_1 = new Unit(ID, NAME, ABBREVIATION);

    public final static Recipe RECIPE = new Recipe();

    public final static Ingredient INGREDIENT = new Ingredient(ID, RECIPE, UNIT_1, DESCRIPTION, QUANTITY);

    public final static MethodStep METHOD_STEP = new MethodStep(ID, RECIPE, ORDERING, DESCRIPTION);
}
