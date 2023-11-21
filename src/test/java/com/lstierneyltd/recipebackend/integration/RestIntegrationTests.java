package com.lstierneyltd.recipebackend.integration;

//import com.fasterxml.jackson.databind.ObjectMapper;

import com.lstierneyltd.recipebackend.entities.*;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.utils.FileUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_DESCRIPTION;
import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_NAME;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getTag;
import static com.lstierneyltd.recipebackend.utils.TestUtils.areWithinSeconds;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Yes I'm ordering the tests. Shoot me!
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class RestIntegrationTests {
    private static final String API_RECIPE = "/api/recipes";
    private final Logger logger = LoggerFactory.getLogger(RestIntegrationTests.class);

    @LocalServerPort
    private int localServerPort;
    //private final ObjectMapper objectMapper = new ObjectMapper();

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
    void testGetUnits() {
        // When
        ResponseEntity<Unit[]> response = testRestTemplate.getForEntity("/api/units", Unit[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Units - no point checking all of them...
        final Unit[] units = response.getBody();
        assertThat(units, is(notNullValue()));
        assertThat(units.length, equalTo(9));

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
        String json = FileUtils.getFileAsString("json/sample_post.json");
        ClassPathResource classPathResource = new ClassPathResource("/images/bolognese_test.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imageFile", classPathResource);
        body.add("recipe", json);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Recipe> response = testRestTemplate.postForEntity(API_RECIPE, requestEntity, Recipe.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Good recipe?
        verifyRecipe(requireNonNull(response.getBody()));
    }

    private static void verifyRecipePreview(RecipeRepository.RecipePreview preview) {
        assertThat(preview.getId(), is(6));
        assertThat(preview.getName(), is("Spaghetti Bolognaise"));
        assertThat(preview.getDescription(), is("The all time favourite"));
        assertThat(preview.getImageFileName(), is("bolognese_test.jpg"));
    }

    private void verifyIngredient(Ingredient ingredient1, String description, BigDecimal quantity, Unit unit) {
        assertThat(ingredient1.getDescription(), equalTo(description));
        assertThat(ingredient1.getQuantity(), comparesEqualTo(quantity));
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

    private void verifyNote(Note note, int ordering, String description) {
        assertThat(note.getOrdering(), equalTo(ordering));
        assertThat(note.getDescription(), equalTo(description));
    }

    private void verifyRecipe(final Recipe recipe) {
        logger.info("Verifying Recipe: " + recipe);
        // The recipe
        assertThat(recipe.getId(), is(6));
        assertThat(recipe.getName(), equalTo("Spaghetti Bolognaise"));
        assertThat(recipe.getDescription(), equalTo("The all time favourite"));
        assertThat(recipe.getCookingTime(), equalTo(30));
        assertThat(recipe.getBasedOn(), equalTo("This recipe was based on this one"));


        // Ingredients
        assertThat(recipe.getIngredients().size(), equalTo(3));
        verifyIngredient(recipe.getIngredients().get(0), "Chopped Tomatoes", BigDecimal.valueOf(1.00), new Unit(8, "400g can", "can"));
        verifyIngredient(recipe.getIngredients().get(1), "Onion (small)", BigDecimal.valueOf(0.50), null);
        verifyIngredient(recipe.getIngredients().get(2), "Cloves of Garlic", BigDecimal.valueOf(2.00), null);

        // Method steps
        assertThat(recipe.getMethodSteps().size(), equalTo(2));
        verifyMethodStep(recipe.getMethodSteps().get(0), 1, "Finely chop the onion and garlic");
        verifyMethodStep(recipe.getMethodSteps().get(1), 2, "Add to small pot with a little olive oil");

        // Tags
        assertThat(recipe.getTags().size(), equalTo(2));
        verifyTag("old-school", "Just like wot you remember", recipe.getTags().toArray(new Tag[0]));
        verifyTag("one-pot", "Gotta keep that washing down", recipe.getTags().toArray(new Tag[0]));

        // Notes
        assertThat(recipe.getNotes().size(), equalTo(2));
        verifyNote(recipe.getNotes().get(0), 1, "This is the first note");
        verifyNote(recipe.getNotes().get(1), 2, "This is the second note");

        // ServedOn
        assertThat(recipe.getServedOn().isHeated(), is(true));
        assertThat(recipe.getServedOn().getCrockery().getDescription(), is("White Plates"));

        // Cooked
        assertThat(recipe.getCooked(), is(2));

        // CreatedDate
        assertTrue(areWithinSeconds(recipe.getCreatedDate(), LocalDateTime.now(), 10));
        assertThat(recipe.getCreatedBy(), is("anonymousUser")); // because we are not logged in
    }

    @Test
    @Order(4)
    public void testGetAllRecipes() {
        ResponseEntity<Recipe[]> response = testRestTemplate.getForEntity(API_RECIPE, Recipe[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipe
        final Recipe[] recipes = requireNonNull(response.getBody());
        assertThat(recipes.length, equalTo(6));

        // Just one just now
        verifyRecipe(recipes[5]);
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
    @Order(5)
    public void testGetRecipeById() {
        ResponseEntity<Recipe> response = testRestTemplate.getForEntity(API_RECIPE + "/id/6", Recipe.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        verifyRecipe(requireNonNull(response.getBody()));
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
        String newName = "new-name";
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
    @Order(21)
    public void testGetAllTags() {
        final ResponseEntity<Tag[]> response = testRestTemplate.getForEntity("/api/tags", Tag[].class);

        verifyStatusOk(response.getStatusCode());
        final Tag[] returnedTags = requireNonNull(response.getBody());

        // It's '15' because we POSTed one above
        assertThat(returnedTags.length, equalTo(15));

        verifyTag("onepot", "A recipe that only uses one pot. Hurrah!", returnedTags[0]);
    }

    @Test
    @Order(24)
    public void testDeleteTag() {
        testRestTemplate.delete("/api/tags/17");

        final ResponseEntity<Tag[]> response = testRestTemplate.getForEntity("/api/tags", Tag[].class);
        final Tag[] tags = requireNonNull(response.getBody());

        verifyStatusOk(response.getStatusCode());
        assertThat(tags.length, equalTo(14));
    }

    @Test
    @Order(25)
    public void testGetRecipesByTagName() {
        ResponseEntity<Recipe[]> response = testRestTemplate.getForEntity(API_RECIPE + "?tagNames=one-pot", Recipe[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipes
        final Recipe[] recipes = requireNonNull(response.getBody());
        assertThat(recipes.length, equalTo(2));

        verifyRecipe(recipes[1]);
    }

    @Test
    @Order(26)
    public void testGetRecipesByTagNames_1tags() {
        ResponseEntity<Recipe[]> response = testRestTemplate.getForEntity(API_RECIPE + "?tagNames=new-name", Recipe[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipes
        final Recipe[] recipes = requireNonNull(response.getBody());
        assertThat(recipes.length, equalTo(3));
    }
    @Test
    @Order(27)
    public void testGetRecipesByTagNames_2tags() {
        ResponseEntity<Recipe[]> response = testRestTemplate.getForEntity(API_RECIPE + "?tagNames=pasta,big-fancy", Recipe[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipes
        final Recipe[] recipes = requireNonNull(response.getBody());
        assertThat(recipes.length, equalTo(1));
    }

    @Test
    @Order(29)
    public void testGetLatestRecipe() {
        ResponseEntity<RecipePreviewImpl[]> response = testRestTemplate.getForEntity(API_RECIPE + "/latest", RecipePreviewImpl[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        RecipeRepository.RecipePreview[] previews = requireNonNull(response.getBody());
        assertThat(previews.length, is(6));
        verifyRecipePreview(previews[0]);
    }

    @Test
    @Order(30)
    public void testGetRecipeByName() {
        ResponseEntity<Recipe> response = testRestTemplate.getForEntity(API_RECIPE + "/spaghetti-bolognaise", Recipe.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        Recipe recipe = requireNonNull(response.getBody());
        verifyRecipe(recipe);
    }

    @Test
    @Order(40)
    void testGetCrockery() {
        // When
        ResponseEntity<Crockery[]> response = testRestTemplate.getForEntity("/api/crockery", Crockery[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Crockery - no point checking all of them...
        final Crockery[] allCrockery = response.getBody();
        assertThat(allCrockery, is(notNullValue()));
        assertThat(allCrockery.length, equalTo(5));

        Crockery crockery = allCrockery[0];
        assertThat(crockery.getId(), is(1));
        assertThat(crockery.getDescription(), equalTo("White Plates"));

        crockery = allCrockery[4];
        assertThat(crockery.getId(), is(5));
        assertThat(crockery.getDescription(), equalTo("Green Bowls"));
    }

    @Test
    @Order(50)
    void testMarkRecipeAsCooked() {
        final ResponseEntity<Recipe> response = testRestTemplate.postForEntity(API_RECIPE + "/markascooked/6", null, Recipe.class);
        LocalDateTime now = LocalDateTime.now();

        verifyStatusOk(response.getStatusCode());
        final Recipe recipe = requireNonNull(response.getBody());

        long secondsBetween = ChronoUnit.SECONDS.between(now, recipe.getLastCooked());
        assertThat(recipe.getCooked(), is(3));
        assertThat(secondsBetween, lessThanOrEqualTo(5L));
    }

    @Test
    @Order(60)
    public void testGetRandomRecipes() {
        ResponseEntity<RecipePreviewImpl[]> response = testRestTemplate.getForEntity(API_RECIPE + "/random", RecipePreviewImpl[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        RecipeRepository.RecipePreview[] previews = requireNonNull(response.getBody());
        assertThat(previews.length, is(6));
    }
}
