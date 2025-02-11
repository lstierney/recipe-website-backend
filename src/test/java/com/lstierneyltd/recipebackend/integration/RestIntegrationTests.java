package com.lstierneyltd.recipebackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lstierneyltd.recipebackend.dto.RecipeDto;
import com.lstierneyltd.recipebackend.dto.RecipePreviewDto;
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

    private static void verifyRecipePreviewDto(RecipePreviewDto recipePreviewDto) {
        assertThat(recipePreviewDto.getId(), is(2));
        assertThat(recipePreviewDto.getName(), is("Spaghetti Bolognese"));
        assertThat(recipePreviewDto.getDescription(), is("Everybody has their own version of this recipe; this is mine"));
    }

    @Test
    @Order(3)
    public void testPostRecipe() throws Exception {
        ResponseEntity<RecipeDto> response = postRecipe("json/sample_post.json");

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Good recipe?
        verifyRecipeDto(requireNonNull(response.getBody()));
    }

    private ResponseEntity<RecipeDto> postRecipe(String pathToRecipeJson) throws IOException {
        String json = FileUtils.getFileAsString(pathToRecipeJson);
        ClassPathResource classPathResource = new ClassPathResource("/images/bolognese_test.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imageFile", classPathResource);
        body.add("recipe", json);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return testRestTemplate.postForEntity(API_RECIPE, requestEntity, RecipeDto.class);
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

    private void verifyRecipeDto(final RecipeDto recipeDto) {
        logger.info("Verifying RecipeDto: " + recipeDto);
        // The recipe
        assertThat(recipeDto.getId(), is(6));
        assertThat(recipeDto.getName(), equalTo("Spaghetti Bolognaise"));
        assertThat(recipeDto.getDescription(), equalTo("The all time favourite"));
        assertThat(recipeDto.getCookingTime(), equalTo(30));
        assertThat(recipeDto.getBasedOn(), equalTo("This recipe was based on this one"));


        // Ingredients
        assertThat(recipeDto.getIngredients().size(), equalTo(3));
        verifyIngredient(recipeDto.getIngredients().get(0), "Chopped Tomatoes", BigDecimal.valueOf(1.00), new Unit(8, "400g can", "can"));
        verifyIngredient(recipeDto.getIngredients().get(1), "Onion (small)", BigDecimal.valueOf(0.50), null);
        verifyIngredient(recipeDto.getIngredients().get(2), "Cloves of Garlic", BigDecimal.valueOf(2.00), null);

        // Method steps
        assertThat(recipeDto.getMethodSteps().size(), equalTo(2));
        verifyMethodStep(recipeDto.getMethodSteps().get(0), 1, "Finely chop the onion and garlic");
        verifyMethodStep(recipeDto.getMethodSteps().get(1), 2, "Add to small pot with a little olive oil");

        // Tags
        assertThat(recipeDto.getTags().size(), equalTo(2));
        verifyTag("old-school", "Just like wot you remember", recipeDto.getTags().toArray(new Tag[0]));
        verifyTag("one-pot", "Gotta keep that washing down", recipeDto.getTags().toArray(new Tag[0]));

        // Notes
        assertThat(recipeDto.getNotes().size(), equalTo(2));
        verifyNote(recipeDto.getNotes().get(0), 1, "This is the first note");
        verifyNote(recipeDto.getNotes().get(1), 2, "This is the second note");

        // ServedOn
        assertThat(recipeDto.getServedOn().isHeated(), is(true));
        assertThat(recipeDto.getServedOn().getCrockery().getDescription(), is("White Plates"));

        // Cooked
        assertThat(recipeDto.getCooked(), is(2));

        // CreatedDate
        assertTrue(areWithinSeconds(recipeDto.getCreatedDate(), LocalDateTime.now(), 10));
        assertThat(recipeDto.getCreatedBy(), is("anonymousUser")); // because we are not logged in
    }

    @Test
    @Order(4)
    public void testGetAllRecipes() {
        ResponseEntity<RecipeDto[]> response = testRestTemplate.getForEntity(API_RECIPE, RecipeDto[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipe
        final RecipeDto[] recipeDtos = requireNonNull(response.getBody());
        assertThat(recipeDtos.length, equalTo(6));

        // Just one just now
        verifyRecipeDto(recipeDtos[5]);
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
        ResponseEntity<RecipeDto> response = testRestTemplate.getForEntity(API_RECIPE + "/id/6", RecipeDto.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        verifyRecipeDto(requireNonNull(response.getBody()));
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
        ResponseEntity<RecipeDto[]> response = testRestTemplate.getForEntity(API_RECIPE + "?tagNames=one-pot", RecipeDto[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipes
        final RecipeDto[] recipeDtos = requireNonNull(response.getBody());
        assertThat(recipeDtos.length, equalTo(2));

        verifyRecipeDto(recipeDtos[1]);
    }

    @Test
    @Order(26)
    public void testGetRecipesByTagNames_1tags() {
        ResponseEntity<RecipeDto[]> response = testRestTemplate.getForEntity(API_RECIPE + "?tagNames=new-name", RecipeDto[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        // Recipes
        final RecipeDto[] recipeDtos = requireNonNull(response.getBody());
        assertThat(recipeDtos.length, equalTo(3));
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
        ResponseEntity<RecipePreviewDto[]> response = testRestTemplate.getForEntity(API_RECIPE + "/latest", RecipePreviewDto[].class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        RecipePreviewDto[] recipePreviewDtos = requireNonNull(response.getBody());
        assertThat(recipePreviewDtos.length, is(2));
        verifyRecipePreviewDto(recipePreviewDtos[0]);
    }

    @Test
    @Order(30)
    public void testGetRecipeByName() {
        ResponseEntity<RecipeDto> response = testRestTemplate.getForEntity(API_RECIPE + "/spaghetti-bolognaise", RecipeDto.class);

        // Good status?
        verifyStatusOk(response.getStatusCode());

        RecipeDto recipeDto = requireNonNull(response.getBody());
        verifyRecipeDto(recipeDto);
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
        final ResponseEntity<RecipeDto> response = testRestTemplate.postForEntity(API_RECIPE + "/markascooked/6", null, RecipeDto.class);
        LocalDateTime now = LocalDateTime.now();

        verifyStatusOk(response.getStatusCode());
        final RecipeDto recipeDto = requireNonNull(response.getBody());

        long secondsBetween = ChronoUnit.SECONDS.between(now, recipeDto.getLastCooked());
        assertThat(recipeDto.getCooked(), is(3));
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
        ResponseEntity<RecipeDto> response = postRecipe("json/chana_masala.json");
        verifyStatusOk(response.getStatusCode());

        // So at this point we have inserted the nice, new, shiny recipe.
        // Lets update the recipe, PUT it and test the response
        RecipeDto recipeDto = requireNonNull(response.getBody());
        recipeDto.setDescription("This is the modified description");
        recipeDto.setCookingTime(123);
        recipeDto.setBasedOn("http://www.random.com");

        ServedOn servedOn = new ServedOn();
        Crockery crockery = new Crockery();
        crockery.setId(1);
        crockery.setDescription("White Plates");
        servedOn.setHeated(false);
        servedOn.setCrockery(crockery);
        recipeDto.setServedOn(servedOn);

        // Do the collections

        // Notes
        List<Note> notes = recipeDto.getNotes();
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
        recipeDto.setNotes(notes);

        // MethodSteps - starts with 8 MethodSteps
        List<MethodStep> methodSteps = recipeDto.getMethodSteps();
        methodSteps.remove(0);
        methodSteps.remove(0);
        methodSteps.get(0).setDescription("Method Step modified desc");
        methodSteps.get(0).setOrdering(2112);
        MethodStep methodStep = new MethodStep();
        methodStep.setDescription("Desc for new MethodStep");
        methodStep.setOrdering(321);
        methodSteps.add(methodStep);
        recipeDto.setMethodSteps(methodSteps);

        // Ingredients - starts with 18 Ingredients
        List<Ingredient> ingredients = recipeDto.getIngredients();
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
        recipeDto.setIngredients(ingredients);


        // Tags
        Set<Tag> tags = recipeDto.getTags();
        Tag tag = new Tag();
        tag.setId(4);
        tag.setName("old-school");
        tag.setDescription("Just like wot you remember");
        tags.add(tag);
        recipeDto.setTags(tags);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("recipe", objectMapper.writeValueAsString(recipeDto));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        response = testRestTemplate.exchange(API_RECIPE, HttpMethod.PUT, requestEntity, RecipeDto.class);
        recipeDto = requireNonNull(response.getBody());

        // Scalar properties
        assertThat(recipeDto.getDescription(), is("This is the modified description"));
        assertThat(recipeDto.getCookingTime(), is(123));
        assertThat(recipeDto.getBasedOn(), is("http://www.random.com"));
        assertThat(recipeDto.getLastUpdatedBy(), is("anonymousUser"));
        assertThat(recipeDto.getCreatedBy(), is("anonymousUser"));
        assertTrue(areWithinSeconds(recipeDto.getCreatedDate(), LocalDateTime.now(), 10));
        assertTrue(areWithinSeconds(recipeDto.getLastUpdatedDate(), LocalDateTime.now(), 10));

        // Notes
        assertThat(recipeDto.getNotes().size(), is(2));
        Note noteToCheck = recipeDto.getNotes().get(0);
        assertThat(noteToCheck.getDescription(), is("Note 1 modified desc"));
        assertThat(noteToCheck.getOrdering(), is(69));

        noteToCheck = recipeDto.getNotes().get(1);
        assertThat(noteToCheck.getDescription(), is("New Note desc"));
        assertThat(noteToCheck.getOrdering(), is(420));

        // ServedOn
        servedOn = recipeDto.getServedOn();
        assertThat(servedOn.isHeated(), is(false));
        assertThat(servedOn.getCrockery().getDescription(), is("White Plates"));
        assertThat(servedOn.getCrockery().getId(), is(1));

        // Tags
        tags = recipeDto.getTags();
        assertThat(tags.size(), is(2));
        tag.setDescription("old-school");
        assertTrue(tags.contains(tag));
        tag.setDescription("indian");
        assertTrue(tags.contains(tag));

        // MethodSteps
        methodSteps = recipeDto.getMethodSteps();
        assertThat(methodSteps.size(), is(7));
        methodStep = methodSteps.get(0);
        assertThat(methodStep.getDescription(), is("Method Step modified desc"));
        assertThat(methodStep.getOrdering(), is(2112));
        methodStep = methodSteps.get(6);
        assertThat(methodStep.getDescription(), is("Desc for new MethodStep"));
        assertThat(methodStep.getOrdering(), is(321));

        // Ingredients
        ingredients = recipeDto.getIngredients();
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
        ResponseEntity<RecipeDto[]> allRecipesResponse = testRestTemplate.getForEntity(API_RECIPE, RecipeDto[].class);

        // 7 recipes before "deletion"
        RecipeDto[] recipeDtos = requireNonNull(allRecipesResponse.getBody());
        assertThat(recipeDtos.length, equalTo(7));

        // "Delete" one
        HttpEntity<Void> httpEntity = new HttpEntity<>(null); // No body, No headers
        int idToDelete = recipeDtos[0].getId();
        ResponseEntity<RecipeDto> deleteResponse =
                testRestTemplate.exchange(API_RECIPE + "/markAsDeleted/" + idToDelete,
                        HttpMethod.PUT,
                        httpEntity,
                        RecipeDto.class);
        assertThat(deleteResponse.getBody().isDeleted(), is(true));

        // Should only be 6 recipes now
        allRecipesResponse = testRestTemplate.getForEntity(API_RECIPE, RecipeDto[].class);
        recipeDtos = requireNonNull(allRecipesResponse.getBody());
        assertThat(recipeDtos.length, equalTo(6));
    }

    @Test
    @Order(410)
    public void testRestore() {
        ResponseEntity<RecipeDto[]> allRecipesResponse = testRestTemplate.getForEntity(API_RECIPE, RecipeDto[].class);

        // 6 recipes before "restore"
        RecipeDto[] recipeDtos = requireNonNull(allRecipesResponse.getBody());
        assertThat(recipeDtos.length, equalTo(6));

        // Restore
        HttpEntity<Void> httpEntity = new HttpEntity<>(null); // No body, No headers
        ResponseEntity<RecipeDto> restoreResponse =
                testRestTemplate.exchange(API_RECIPE + "/restore/" + 1,
                        HttpMethod.PUT,
                        httpEntity,
                        RecipeDto.class);
        assertThat(restoreResponse.getBody().isDeleted(), is(false));

        // Should be 7 recipes now
        allRecipesResponse = testRestTemplate.getForEntity(API_RECIPE, RecipeDto[].class);
        recipeDtos = requireNonNull(allRecipesResponse.getBody());
        assertThat(recipeDtos.length, equalTo(7));
    }
}
