package com.lstierneyltd.recipebackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lstierneyltd.recipebackend.entities.*;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.utils.FileUtils;
import com.lstierneyltd.recipebackend.utils.TestConstants;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_DESCRIPTION;
import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_NAME;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getIdea;
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

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testPostRecipe() throws Exception {
        ResponseEntity<Recipe> response = postRecipe("json/sample_post.json");

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Good recipe?
        verifyRecipe(requireNonNull(response.getBody()));
    }

    private ResponseEntity<Recipe> postRecipe(String pathToRecipeJson) throws IOException {
        String json = FileUtils.getFileAsString(pathToRecipeJson);
        ClassPathResource classPathResource = new ClassPathResource("/images/bolognese_test.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imageFile", classPathResource);
        body.add("recipe", json);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return testRestTemplate.postForEntity(API_RECIPE, requestEntity, Recipe.class);
    }

    private static void verifyRecipePreview(RecipeRepository.RecipePreview preview) {
        assertThat(preview.getId(), is(2));
        assertThat(preview.getName(), is("Spaghetti Bolognese"));
        assertThat(preview.getDescription(), is("Everybody has their own version of this recipe; this is mine"));
        assertThat(preview.getImageFileName(), is("bolognese.jpg"));
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
    public void testGetLatestRecipes() {
        ResponseEntity<RecipePreviewImpl[]> response = testRestTemplate.getForEntity(API_RECIPE + "/latest", RecipePreviewImpl[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        RecipeRepository.RecipePreview[] previews = requireNonNull(response.getBody());
        assertThat(previews.length, is(2));
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
    public void testGetRandomDinners() {
        ResponseEntity<RecipePreviewImpl[]> response = testRestTemplate.getForEntity(API_RECIPE + "/randomDinners", RecipePreviewImpl[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        RecipeRepository.RecipePreview[] previews = requireNonNull(response.getBody());
        assertThat(previews.length, is(2));
    }

    // We'll start the update tests at 200
    @Test
    @Order(200)
    public void testUpdateRecipe() throws Exception {
        ResponseEntity<Recipe> response = postRecipe("json/chana_masala.json");
        verifyStatusOk(response.getStatusCode());

        // So at this point we have inserted the nice, new, shiny recipe.
        // Lets update the recipe, PUT it and test the response
        Recipe recipe = requireNonNull(response.getBody());
        recipe.setName("This is modified recipe name");
        recipe.setDescription("This is the modified description");
        recipe.setCookingTime(123);
        recipe.setImageFileName("new_image.jpg");
        recipe.setBasedOn("http://www.random.com");

        ServedOn servedOn = new ServedOn();
        Crockery crockery = new Crockery();
        crockery.setId(1);
        crockery.setDescription("White Plates");
        servedOn.setHeated(false);
        servedOn.setCrockery(crockery);
        recipe.setServedOn(servedOn);

        // Do the collections

        // Notes
        List<Note> notes = recipe.getNotes();
        // Delete an existing Note
        notes.remove(1);
        // Modify an existing Note
        notes.get(0).setDescription("Note 1 modified desc");
        notes.get(0).setOrdering(69);
        // Add a new Note
        Note newNote = new Note();
        newNote.setDescription("New Note desc");
        newNote.setOrdering(420);
        notes.add(newNote);
        recipe.setNotes(notes);

        // MethodSteps - starts with 8 MethodSteps
        List<MethodStep> methodSteps = recipe.getMethodSteps();
        methodSteps.remove(0);
        methodSteps.remove(0);
        methodSteps.get(0).setDescription("Method Step modified desc");
        methodSteps.get(0).setOrdering(2112);
        MethodStep methodStep = new MethodStep();
        methodStep.setDescription("Desc for new MethodStep");
        methodStep.setOrdering(321);
        methodSteps.add(methodStep);
        recipe.setMethodSteps(methodSteps);

        // Ingredients - starts with 18 Ingredients
        List<Ingredient> ingredients = recipe.getIngredients();
        ingredients.remove(0); // delete the first
        ingredients.remove(ingredients.size() - 1); // delete the last
        ingredients.remove(7); // delete one from in the middle
        ingredients.get(3).setDescription("Modified Ingredient desc");
        ingredients.get(3).setQuantity(BigDecimal.valueOf(404));
        ingredients.get(3).setOrdering(999);
        Unit unit = new Unit();
        unit.setId(6);
        ingredients.get(3).setUnit(unit);
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription("New Ingredient desc");
        ingredient.setOrdering(111);
        ingredient.setQuantity(BigDecimal.valueOf(1001));
        unit = new Unit();
        unit.setId(7);
        ingredient.setUnit(unit);

        ingredients.add(ingredient);
        recipe.setIngredients(ingredients);


        // Tags
        Set<Tag> tags = recipe.getTags();
        Tag tag = new Tag();
        tag.setId(4);
        tag.setName("old-school");
        tag.setDescription("Just like wot you remember");
        tags.add(tag);
        recipe.setTags(tags);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("recipe", objectMapper.writeValueAsString(recipe));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        response = testRestTemplate.exchange(API_RECIPE, HttpMethod.PUT, requestEntity, Recipe.class);
        recipe = requireNonNull(response.getBody());

        // Scalar properties
        assertThat(recipe.getName(), is("This is modified recipe name"));
        assertThat(recipe.getDescription(), is("This is the modified description"));
        assertThat(recipe.getCookingTime(), is(123));
        assertThat(recipe.getImageFileName(), is("new_image.jpg"));
        assertThat(recipe.getBasedOn(), is("http://www.random.com"));
        assertThat(recipe.getLastUpdatedBy(), is("anonymousUser"));
        assertThat(recipe.getCreatedBy(), is("anonymousUser"));
        assertTrue(areWithinSeconds(recipe.getCreatedDate(), LocalDateTime.now(), 10));
        assertTrue(areWithinSeconds(recipe.getLastUpdatedDate(), LocalDateTime.now(), 10));

        // Notes
        assertThat(recipe.getNotes().size(), is(2));
        Note noteToCheck = recipe.getNotes().get(0);
        assertThat(noteToCheck.getDescription(), is("Note 1 modified desc"));
        assertThat(noteToCheck.getOrdering(), is(69));

        noteToCheck = recipe.getNotes().get(1);
        assertThat(noteToCheck.getDescription(), is("New Note desc"));
        assertThat(noteToCheck.getOrdering(), is(420));

        // ServedOn
        servedOn = recipe.getServedOn();
        assertThat(servedOn.isHeated(), is(false));
        assertThat(servedOn.getCrockery().getDescription(), is("White Plates"));
        assertThat(servedOn.getCrockery().getId(), is(1));

        // Tags
        tags = recipe.getTags();
        assertThat(tags.size(), is(2));
        tag.setDescription("old-school");
        assertTrue(tags.contains(tag));
        tag.setDescription("indian");
        assertTrue(tags.contains(tag));

        // MethodSteps
        methodSteps = recipe.getMethodSteps();
        assertThat(methodSteps.size(), is(7));
        methodStep = methodSteps.get(0);
        assertThat(methodStep.getDescription(), is("Method Step modified desc"));
        assertThat(methodStep.getOrdering(), is(2112));
        methodStep = methodSteps.get(6);
        assertThat(methodStep.getDescription(), is("Desc for new MethodStep"));
        assertThat(methodStep.getOrdering(), is(321));

        // Ingredients
        ingredients = recipe.getIngredients();
        assertThat(ingredients.size(), is(16));

        // Test the modified Ingredient
        ingredient = ingredients.get(3);
        assertThat(ingredient.getDescription(), is("Modified Ingredient desc"));
        assertThat(ingredient.getQuantity(), is(BigDecimal.valueOf(404)));
        assertThat(ingredient.getOrdering(), is(999));
        unit = ingredient.getUnit();
        assertThat(unit.getId(), is(6));
        assertThat(unit.getAbbreviation(), is("kg"));
        assertThat(unit.getName(), is("kilogram"));

        // Test the new ingredient
        ingredient = ingredients.get(ingredients.size() - 1);
        assertThat(ingredient.getDescription(), is("New Ingredient desc"));
        assertThat(ingredient.getQuantity(), is(BigDecimal.valueOf(1001)));
        assertThat(ingredient.getOrdering(), is(111));
        unit = ingredient.getUnit();
        assertThat(unit.getId(), is(7));
        assertThat(unit.getAbbreviation(), is("lb"));
        assertThat(unit.getName(), is("pound"));
    }

    // Idea Tests start at 300
    @Test
    @Order(300)
    public void testGetIdeaById() {
        final ResponseEntity<Idea> response = testRestTemplate.getForEntity("/api/ideas/1", Idea.class);

        verifyStatusOk(response.getStatusCode());

        Idea idea = requireNonNull(response.getBody());
        assertThat(idea.getName(), is("Fluffy American pancakes recipe"));
        assertThat(idea.getUrl(), is("https://www.bbc.co.uk/food/recipes/fluffyamericanpancak_74828"));
        assertThat(idea.getLastUpdatedBy(), is("lawrence"));
        assertThat(idea.getCreatedBy(), is("lawrence"));
        assertTrue(areWithinSeconds(idea.getLastUpdatedDate(), LocalDateTime.now(), 60));
        assertTrue(areWithinSeconds(idea.getCreatedDate(), LocalDateTime.now(), 60));
    }

    @Test
    @Order(305)
    public void testGetAllIdeas() {
        expectedNumberOfIdeaIs(1);
    }

    private void expectedNumberOfIdeaIs(int expectedNumberOfIdeas) {
        final ResponseEntity<Idea[]> response = testRestTemplate.getForEntity("/api/ideas", Idea[].class);

        verifyStatusOk(response.getStatusCode());
        final Idea[] returnedIdeas = requireNonNull(response.getBody());

        assertThat(returnedIdeas.length, equalTo(expectedNumberOfIdeas));
    }

    @Test
    @Order(310)
    public void testAddIdea() {
        final Idea idea = getIdea();
        final ResponseEntity<Idea> response = testRestTemplate.postForEntity("/api/ideas", idea, Idea.class);

        verifyStatusOk(response.getStatusCode());

        final Idea returnedIdea = requireNonNull(response.getBody());

        assertThat(returnedIdea.getId(), is(2));
        assertThat(returnedIdea.getName(), is(TestConstants.IDEA_NAME));
        assertThat(returnedIdea.getUrl(), is(TestConstants.IDEA_URL));
        assertThat(returnedIdea.getLastUpdatedBy(), is(nullValue()));
        assertThat(returnedIdea.getLastUpdatedDate(), is(nullValue()));
        assertThat(returnedIdea.getCreatedBy(), is("anonymousUser"));
        assertTrue(areWithinSeconds(returnedIdea.getCreatedDate(), LocalDateTime.now(), 60));

        expectedNumberOfIdeaIs(2);
    }

    @Test
    @Order(315)
    public void testDeleteIdea() {
        testRestTemplate.delete("/api/ideas/2");
        expectedNumberOfIdeaIs(1);
    }

    @Test
    @Order(320)
    public void testUpdateIdea() {
        final Idea idea = getIdea();
        String newUrl = "this is the new URL";
        String newName = "this is the new name";
        idea.setUrl(newUrl);
        idea.setName(newName);
        idea.setId(1); // this should overwrite the record that exists in data.sql

        testRestTemplate.put("/api/ideas", idea);

        final ResponseEntity<Idea> response = testRestTemplate.getForEntity("/api/ideas/1", Idea.class);
        final Idea updatedIdea = requireNonNull(response.getBody());

        verifyStatusOk(response.getStatusCode());
        assertThat(updatedIdea.getName(), equalTo(newName));
        assertThat(updatedIdea.getUrl(), equalTo(newUrl));
        assertThat(updatedIdea.getLastUpdatedBy(), is("anonymousUser"));
        assertTrue(areWithinSeconds(updatedIdea.getLastUpdatedDate(), LocalDateTime.now(), 60));
        assertThat(updatedIdea.getCreatedBy(), is(nullValue()));
        assertThat(updatedIdea.getCreatedDate(), is(nullValue()));
    }

    @Test
    @Order(400)
    public void testMarkRecipeAsDeleted() {
        ResponseEntity<Recipe[]> allRecipesResponse = testRestTemplate.getForEntity(API_RECIPE, Recipe[].class);

        // 7 recipes before "deletion"
        Recipe[] recipes = requireNonNull(allRecipesResponse.getBody());
        assertThat(recipes.length, equalTo(7));

        // "Delete" one
        HttpEntity<Void> httpEntity = new HttpEntity<>(null); // No body, No headers
        int idToDelete = recipes[0].getId();
        ResponseEntity<Recipe> deleteResponse =
                testRestTemplate.exchange(API_RECIPE + "/markAsDeleted/" + idToDelete,
                        HttpMethod.PUT,
                        httpEntity,
                        Recipe.class);
        assertThat(deleteResponse.getBody().isDeleted(), is(true));

        // Should only be 6 recipes now
        allRecipesResponse = testRestTemplate.getForEntity(API_RECIPE, Recipe[].class);
        recipes = requireNonNull(allRecipesResponse.getBody());
        assertThat(recipes.length, equalTo(6));
    }

    @Test
    @Order(410)
    public void testRestore() {
        ResponseEntity<Recipe[]> allRecipesResponse = testRestTemplate.getForEntity(API_RECIPE, Recipe[].class);

        // 6 recipes before "restore"
        Recipe[] recipes = requireNonNull(allRecipesResponse.getBody());
        assertThat(recipes.length, equalTo(6));

        // Restore
        HttpEntity<Void> httpEntity = new HttpEntity<>(null); // No body, No headers
        ResponseEntity<Recipe> restoreResponse =
                testRestTemplate.exchange(API_RECIPE + "/restore/" + 1,
                        HttpMethod.PUT,
                        httpEntity,
                        Recipe.class);
        assertThat(restoreResponse.getBody().isDeleted(), is(false));

        // Should be 7 recipes now
        allRecipesResponse = testRestTemplate.getForEntity(API_RECIPE, Recipe[].class);
        recipes = requireNonNull(allRecipesResponse.getBody());
        assertThat(recipes.length, equalTo(7));
    }
}
