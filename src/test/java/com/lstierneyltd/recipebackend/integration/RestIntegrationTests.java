package com.lstierneyltd.recipebackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lstierneyltd.recipebackend.entities.*;
import com.lstierneyltd.recipebackend.utils.FileUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Set;

import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_DESCRIPTION;
import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_NAME;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getTag;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

/**
 * Yes I'm ordering the tests. Shoot me!
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
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

    // TODO - why broken?
    @Test
    @Order(2)
    void testGetUnits() {
        // When
        ResponseEntity<Unit[]> response = testRestTemplate.getForEntity("/api/units", Unit[].class);

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
        assertThat(unit.getName(), equalTo("gram"));
        assertThat(unit.getAbbreviation(), equalTo("g"));
    }

    @Test
    @Order(3)
    public void postRecipe() throws Exception {
        String json = FileUtils.getFileAsString("json/sample_post.json");
        ClassPathResource classPathResource = new ClassPathResource("/images/bolognese_test.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imageFile", classPathResource);
        body.add("recipe", json);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Recipe> response = testRestTemplate.postForEntity("/api/recipes", requestEntity, Recipe.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Good recipe?
        verifyRecipe(requireNonNull(response.getBody()));
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

        // Tags
        assertThat(recipe.getTags().size(), equalTo(2));
        verifyTag("one-pot", "Gotta keep that washing down", recipe.getTags().toArray(new Tag[0]));
        verifyTag("old-school", "Just like wot you remember", recipe.getTags().toArray(new Tag[0]));
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

    private void verifyTag(String name, String description, Tag... tags) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);
        assertThat(Set.of(tags), hasItem(tag));
    }

    @Test
    @Order(4)
    public void testGetAllRecipes() {
        ResponseEntity<Recipe[]> response = testRestTemplate.getForEntity("/api/recipes", Recipe[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipes
        final Recipe[] recipes = requireNonNull(response.getBody());
        assertThat(recipes.length, equalTo(1));

        // Just one just now
        verifyRecipe(recipes[0]);
    }

    @Test
    @Order(5)
    public void testGetRecipeById() {
        ResponseEntity<Recipe> response = testRestTemplate.getForEntity("/api/recipes/1", Recipe.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        verifyRecipe(requireNonNull(response.getBody()));
    }

    @Test
    @Order(20)
    public void testPostTag() {
        final Tag tag = getTag();
        final ResponseEntity<Tag> response = testRestTemplate.postForEntity("/api/tags", tag, Tag.class);

        verifyStatusOk(response.getStatusCode());
        final Tag returnedTag = requireNonNull(response.getBody());

        verifyTag(TAG_NAME, TAG_DESCRIPTION, returnedTag);
    }

    @Test
    @Order(21)
    public void testGetAllTags() {
        final ResponseEntity<Tag[]> response = testRestTemplate.getForEntity("/api/tags", Tag[].class);

        verifyStatusOk(response.getStatusCode());
        final Tag[] returnedTags = requireNonNull(response.getBody());

        assertThat(returnedTags.length, equalTo(7));
        verifyTag("easy", "Simple to make", returnedTags[0]);
    }

    @Test
    @Order(22)
    public void testGetTagById() {
        final ResponseEntity<Tag> response = testRestTemplate.getForEntity("/api/tags/5", Tag.class);

        verifyStatusOk(response.getStatusCode());
        verifyTag("one-pot", "Gotta keep that washing down", requireNonNull(response.getBody()));
    }

    @Test
    @Order(23)
    public void testUpdateTag() {
        final Tag tag = getTag();
        String newName = "new name";
        String newDescription = "new description";
        tag.setName(newName);
        tag.setDescription(newDescription);

        testRestTemplate.put("/api/tags/1", tag);

        final ResponseEntity<Tag> response = testRestTemplate.getForEntity("/api/tags/1", Tag.class);
        final Tag updatedTag = requireNonNull(response.getBody());

        verifyStatusOk(response.getStatusCode());
        assertThat(updatedTag.getDescription(), equalTo(newDescription));
        assertThat(updatedTag.getName(), equalTo(newName));
    }

    @Test
    @Order(24)
    public void testDeleteTag() {
        testRestTemplate.delete("/api/tags/1");

        final ResponseEntity<Tag[]> response = testRestTemplate.getForEntity("/api/tags", Tag[].class);
        final Tag[] tags = requireNonNull(response.getBody());

        verifyStatusOk(response.getStatusCode());
        assertThat(tags.length, equalTo(6));
    }

    @Test
    @Order(25)
    public void testGetRecipeByTagName() {
        ResponseEntity<Recipe[]> response = testRestTemplate.getForEntity("/api/recipes?tagName=one-pot", Recipe[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipes
        final Recipe[] recipes = requireNonNull(response.getBody());
        assertThat(recipes.length, equalTo(1));

        // Just one just now
        verifyRecipe(recipes[0]);
    }
}
