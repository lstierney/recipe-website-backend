package com.lstierneyltd.recipebackend.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lstierneyltd.recipebackend.entities.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_DESCRIPTION;
import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_NAME;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getTag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

/**
 * Yes I'm ordering the tests. Makes it easier for lazy old me! Shoot me!
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("dev")
public class RestIntegrationTests {
    @LocalServerPort
    private int localServerPort;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static void verifyStatusOk(HttpStatusCode response) {
        assertThat(response, equalTo(HttpStatusCode.valueOf(200)));
    }

    @Test
    @Order(1)
    void contextLoads() {
        System.out.println("############ LOCAL_SERVER_PORT: " + localServerPort + " #############");
    }

    @Test
    @Order(2)
    void testGetUnits() throws Exception {
        // When
        ResponseEntity<Unit[]> response = testRestTemplate.getForEntity("/units", Unit[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Units - no point checking all of them...
        final Unit[] units = response.getBody();
        assertThat(units, is(notNullValue()));
        assertThat(units.length, is(greaterThanOrEqualTo(8)));

        Unit unit = units[0];
        assertThat(unit.getName(), equalTo("teaspoon"));
        assertThat(unit.getAbbreviation(), equalTo("tsp"));

        unit = units[4];
        assertThat(unit.getName(), equalTo("pound"));
        assertThat(unit.getAbbreviation(), equalTo("lb"));
    }

    @Test
    @Order(3)
    public void postRecipe() throws Exception {
        final Recipe recipeFromJson = getRecipeFromJsonFile("json/sample_post.json");
        final ResponseEntity<Recipe> response = testRestTemplate.postForEntity("/recipes", recipeFromJson, Recipe.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Good recipe?
        verifyRecipe(response.getBody());
    }

    private Recipe getRecipeFromJsonFile(String fileName) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(new ClassPathResource(fileName).getFile());
        return objectMapper.treeToValue(jsonNode, Recipe.class);
    }

    private void verifyRecipe(final Recipe recipe) {
        // The recipe
        assertThat(recipe.getName(), equalTo("Spaghetti Bolognaise"));
        assertThat(recipe.getDescription(), equalTo("The all time favourite"));
        assertThat(recipe.getCookingTime(), equalTo(30));

        // Ingredients
        assertThat(recipe.getIngredients().size(), equalTo(3));
        verifyIngredient(recipe.getIngredients().get(0), "Chopped Tomatoes", 1, new Unit(8, "400g can", "can"));
        verifyIngredient(recipe.getIngredients().get(1), "Onion (small)", 1, null);
        verifyIngredient(recipe.getIngredients().get(2), "Cloves of Garlic", 2, null);

        // Method steps
        assertThat(recipe.getMethodSteps().size(), equalTo(2));
        verifyMethodStep(recipe.getMethodSteps().get(0), 1, "Finely chop the onion and garlic");
        verifyMethodStep(recipe.getMethodSteps().get(1), 2, "Add to small pot with a little olive oil");

    }

    private void verifyIngredient(Ingredient ingredient1, String description, int quantity, Unit unit) {
        assertThat(ingredient1.getDescription(), equalTo(description));
        assertThat(ingredient1.getQuantity(), equalTo(quantity));
        assertThat(ingredient1.getUnit(), equalTo(unit));
    }

    private void verifyMethodStep(MethodStep methodStep, int ordering, String description) {
        assertThat(methodStep.getOrdering(), equalTo(ordering));
        assertThat(methodStep.getDescription(), equalTo(description));
    }

    @Test
    @Order(4)
    public void testGetAllRecipes() throws Exception {
        ResponseEntity<Recipe[]> response = testRestTemplate.getForEntity("/recipes", Recipe[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipes
        final Recipe[] recipes = response.getBody();
        assertThat(recipes.length, equalTo(1));

        // Just one just now
        verifyRecipe(recipes[0]);
    }

    @Test
    @Order(5)
    public void testGetRecipeById() throws Exception {
        ResponseEntity<Recipe> response = testRestTemplate.getForEntity("/recipes/1", Recipe.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        verifyRecipe(response.getBody());
    }

    @Test
    @Order(20)
    public void testPostTag() throws Exception {
        final Tag tag = getTag();
        final ResponseEntity<Tag> response = testRestTemplate.postForEntity("/api/tags", tag, Tag.class);

        verifyStatusOk(response.getStatusCode());
        final Tag returnedTag = response.getBody();

        verifyTag(returnedTag);
    }

    private void verifyTag(final Tag tag) {
        assertThat(tag.getDescription(), equalTo(TAG_DESCRIPTION));
        assertThat(tag.getName(), equalTo(TAG_NAME));
    }

    @Test
    @Order(21)
    public void testGetAllTags() throws Exception {
        final ResponseEntity<Tag[]> response = testRestTemplate.getForEntity("/api/tags", Tag[].class);

        verifyStatusOk(response.getStatusCode());
        final Tag[] returnedTags = response.getBody();

        assertThat(returnedTags.length, equalTo(1));
        verifyTag(returnedTags[0]);
    }

    @Test
    @Order(22)
    public void testGetTagById() throws Exception {
        final Tag tag = getTag();
        final ResponseEntity<Tag> response = testRestTemplate.getForEntity("/api/tags/1", Tag.class);

        verifyStatusOk(response.getStatusCode());
        verifyTag(response.getBody());
    }

    @Test
    @Order(23)
    public void testUpdateTag() throws Exception {
        final Tag tag = getTag();
        String newName = "new name";
        String newDescription = "new description";
        tag.setName(newName);
        tag.setDescription(newDescription);

        testRestTemplate.put("/api/tags/1", tag);

        final ResponseEntity<Tag> response = testRestTemplate.getForEntity("/api/tags/1", Tag.class);
        final Tag updatedTag = response.getBody();

        verifyStatusOk(response.getStatusCode());
        assertThat(updatedTag.getDescription(), equalTo(newDescription));
        assertThat(updatedTag.getName(), equalTo(newName));
    }

    @Test
    @Order(24)
    public void testDeleteTag() throws Exception {
        testRestTemplate.delete("/api/tags/1");

        final ResponseEntity<Tag[]> response = testRestTemplate.getForEntity("/api/tags", Tag[].class);
        final Tag[] tags = response.getBody();

        verifyStatusOk(response.getStatusCode());
        assertThat(tags.length, equalTo(0));
    }


}
