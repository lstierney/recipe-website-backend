INSERT INTO authorised_user
VALUES (1, 'lawrence', 'lawrence');

INSERT INTO unit
VALUES (1, 'teaspoon', 'tsp');
INSERT INTO unit
VALUES (4, 'tablespoon', 'tbsp');
INSERT INTO unit
VALUES (5, 'gram', 'g');
INSERT INTO unit
VALUES (6, 'kilogram', 'kg');
INSERT INTO unit
VALUES (7, 'pound', 'lb');
INSERT INTO unit
VALUES (8, 'ounce', 'oz');
INSERT INTO unit
VALUES (9, 'cup', 'cup');
INSERT INTO unit
VALUES (10, '400g can', 'can');
INSERT INTO unit
VALUES (11, 'millilitre', 'ml');

INSERT INTO crockery
VALUES (1, 'White Plates');
INSERT INTO crockery
VALUES (2, 'Green Plates');
INSERT INTO crockery
VALUES (3, 'Brown Plates');
INSERT INTO crockery
VALUES (4, 'White Bowls');
INSERT INTO crockery
VALUES (5, 'Green Bowls');

INSERT INTO tag
VALUES (1, 'easy', 'Simple to make');
INSERT INTO tag
VALUES (2, 'pasta', 'Straight from Italia!');
INSERT INTO tag
VALUES (3,'indian', 'My favourite to cook');
INSERT INTO tag
VALUES (4, 'old-school', 'Just like wot you remember');
INSERT INTO tag
VALUES (5, 'one-pot', 'Gotta keep that washing down');
INSERT INTO tag
VALUES (7,'rice-night','To be served on \"Rice Night\"');
INSERT INTO tag
VALUES (8,'noodles','Theyre worms Michael');
INSERT INTO tag
VALUES (9,'big-fancy','Not your everyday dinner');
INSERT INTO tag
VALUES (10,'bread','Full of carby goodness');
INSERT INTO tag
VALUES (11,'side','The opposite of main');
INSERT INTO tag
VALUES (12,'potato-night','To be served on \"potato night\"');
INSERT INTO tag
VALUES (14,'dinner','Recipes suitable for dinner');
INSERT INTO tag
VALUES (15,'lentils','The wonder food!');
INSERT INTO tag
VALUES (17,'ingredient','An input ingredient to a recipe');

INSERT INTO recipe
VALUES (1, 'Moroccan Vegetable Stew with Cous Cous', 'Mildly spicy, slowish cooked veg with cous cous.', 60,
        'https://www.food.com/recipe/moroccan-vegetable-stew-with-couscous-274742',
        NULL, 0, NULL,
        'Cous_cous.jpg');
INSERT INTO recipe
VALUES (2, 'Spaghetti Bolognese', 'Everybody has their own version of this recipe; this is mine', 30, NULL,
        NULL, 1, NULL, 'bolognese.jpg');
INSERT INTO recipe
VALUES (3, 'Sweet Chilli Noodles', 'A change from the usual stir fry, this recipe has a smoother sweeter flavour.', 45,
        NULL, NULL, 2, NULL, 'sweet-chilli-noodles.jpg');
INSERT INTO recipe
VALUES (4, 'Gnocchi Bake', 'A cheesy, tomatoey, slice of heaven. Not for everyday consumption!', 90, NULL,
        NULL, 3, NULL, 'gnocchi.jpg');
INSERT INTO recipe
VALUES (5, 'Bangers and Mash', 'A stone cold classic with the added twist of some spring onion in the mash', 45, NULL,
        NULL, 4, NULL, 'Bangers and Mash.jpg');

INSERT INTO ingredient
VALUES (1, 'Coriander Seeds', 0.50, 1, 1, 1);
INSERT INTO ingredient
VALUES (2, 'Paprika', 1.00, 1, 1, 2);
INSERT INTO ingredient
VALUES (3, 'Tumeric', 0.50, 1, 1, 3);
INSERT INTO ingredient
VALUES (4, 'Cinnamon', 0.50, 1, 1, 4);
INSERT INTO ingredient
VALUES (5, 'Onion (chopped)', 1.00, 1, NULL, 8);
INSERT INTO ingredient
VALUES (6, 'Carrot (chopped)', 1.00, 1, NULL, 9);
INSERT INTO ingredient
VALUES (8, 'Chick peas (drained)', 1.00, 1, 10, 11);
INSERT INTO ingredient
VALUES (9, 'Chopped tomatoes', 1.00, 1, 10, 12);
INSERT INTO ingredient
VALUES (10, 'salt', 1.00, 1, 1, 6);
INSERT INTO ingredient
VALUES (11, 'Chill Flakes', 0.50, 1, 1, 5);
INSERT INTO ingredient
VALUES (12, 'Raisins', 3.00, 1, 4, 13);
INSERT INTO ingredient
VALUES (13, 'Veggie oxo', 1.00, 1, NULL, 7);
INSERT INTO ingredient
VALUES (14, 'Dried Cous Cous', 8.00, 1, 8, 14);
INSERT INTO ingredient
VALUES (15, 'Butternush Squash (peeled and cubed)', 1.00, 1, NULL, 10);
INSERT INTO ingredient
VALUES (16, 'veggie mince', 10.00, 2, 8, 1);
INSERT INTO ingredient
VALUES (17, 'dried spaghetti', 10.00, 2, 8, 2);
INSERT INTO ingredient
VALUES (18, 'chopped tomatoes', 1.00, 2, 10, 3);
INSERT INTO ingredient
VALUES (19, 'onion (finely chopped)', 0.50, 2, NULL, 5);
INSERT INTO ingredient
VALUES (20, 'cloves of garlic (finely chopped)', 2.00, 2, NULL, 6);
INSERT INTO ingredient
VALUES (21, 'sugar', 1.00, 2, 1, 7);
INSERT INTO ingredient
VALUES (22, 'salt', 1.00, 2, 1, 8);
INSERT INTO ingredient
VALUES (23, 'chilli flakes', 0.25, 2, 4, 9);
INSERT INTO ingredient
VALUES (24, 'oregano', 0.50, 2, 1, 10);
INSERT INTO ingredient
VALUES (25, 'ground pepper', 0.00, 2, NULL, 11);
INSERT INTO ingredient
VALUES (26, 'nests of egg noodles', 4.00, 3, NULL, 1);
INSERT INTO ingredient
VALUES (27, 'small piece of ginger (grated)', 1.00, 3, NULL, 2);
INSERT INTO ingredient
VALUES (28, 'cloves of garlic (finely chopped)', 3.00, 3, NULL, 3);
INSERT INTO ingredient
VALUES (29, 'Spring onions', 2.00, 3, NULL, 4);
INSERT INTO ingredient
VALUES (30, 'red pepper (chopped)', 1.00, 3, NULL, 5);
INSERT INTO ingredient
VALUES (31, 'Chestnut mushrooms', 0.00, 3, NULL, 6);
INSERT INTO ingredient
VALUES (32, 'coconut milk', 0.50, 3, 9, 7);
INSERT INTO ingredient
VALUES (33, 'Soy sauce', 2.00, 3, 4, 8);
INSERT INTO ingredient
VALUES (34, 'sugar', 1.00, 3, 1, 9);
INSERT INTO ingredient
VALUES (35, 'salt', 1.00, 3, 1, 10);
INSERT INTO ingredient
VALUES (36, 'lime (quartered)', 1.00, 3, NULL, 11);
INSERT INTO ingredient
VALUES (37, 'Chilli flakes', 0.50, 3, 1, 12);
INSERT INTO ingredient
VALUES (38, 'cloves of garlic (finely chopped)', 4.00, 4, NULL, 1);
INSERT INTO ingredient
VALUES (39, 'Onion (chopped)', 1.00, 4, NULL, 2);
INSERT INTO ingredient
VALUES (40, 'chilli flakes', 0.50, 4, 1, 3);
INSERT INTO ingredient
VALUES (41, 'sugar', 1.50, 4, 1, 4);
INSERT INTO ingredient
VALUES (42, 'salt', 1.00, 4, 1, 5);
INSERT INTO ingredient
VALUES (43, 'capers', 1.00, 4, 4, 6);
INSERT INTO ingredient
VALUES (44, 'oregano', 1.00, 4, 1, 7);
INSERT INTO ingredient
VALUES (45, 'chopped tomatoes', 2.00, 4, 10, 8);
INSERT INTO ingredient
VALUES (46, 'gnocchi (one and half of Tesco bags)', 750.00, 4, 5, 9);
INSERT INTO ingredient
VALUES (47, 'mozarrella (two Tesco portions drained)', 250.00, 4, 5, 10);
INSERT INTO ingredient
VALUES (48, 'grated cheddar cheese (for topping)', 0.00, 4, NULL, 11);
INSERT INTO ingredient
VALUES (49, 'potatoes (peeled and chopped into 1\" chunks)', 1.75, 5, 7, 1);
INSERT INTO ingredient
VALUES (50, 'sausages', 12.00, 5, NULL, 2);
INSERT INTO ingredient
VALUES (51, 'spring onion (finely chopped)', 1.00, 5, NULL, 3);
INSERT INTO ingredient
VALUES (52, 'salt', 1.00, 5, 1, 4);
INSERT INTO ingredient
VALUES (53, 'pepper', 0.00, 5, NULL, 5);
INSERT INTO ingredient
VALUES (54, 'butter (chopped small)', 1.00, 5, 8, 6);
INSERT INTO ingredient
VALUES (55, 'milk', 2.00, 5, 4, 7);

INSERT INTO method_step
VALUES (1, 1, 1, 'Fry the onion, carrot, butternut squash/sweet potato until they start to brown.');
INSERT INTO method_step
VALUES (2, 1, 2,
        'Then add, to same pot, chick peas, tomatoes, raisins, salt and dried spices. Cook this stew for about half an hour');
INSERT INTO method_step
VALUES (3, 1, 3,
        'Once stew is ready prepare the cous cous: put the cous cous in red plastic bowl, crush and mix stock cube in and then add 290ml of boiling water. Leave to stand for 5 mins');
INSERT INTO method_step
VALUES (4, 2, 1, 'Fry the onion and garlic gently until translucent');
INSERT INTO method_step
VALUES (5, 2, 2,
        'Then add tomatoes, sugar, salt, pepper, chill flakes, oregano. Simmer for at least 15mins or until you start to see oil on the op.');
INSERT INTO method_step
VALUES (6, 2, 3, 'Cook spaghetti');
INSERT INTO method_step
VALUES (7, 2, 4, 'Add veggie mince to sauce while spaghetti is cooking');
INSERT INTO method_step
VALUES (8, 2, 5, 'Serve in heated, white bowls.');
INSERT INTO method_step
VALUES (9, 3, 1, 'Cook the noodles as per instructions');
INSERT INTO method_step
VALUES (10, 3, 2, 'In the wok fry the garlic, ginger and chopped whites of the Spring onions (only takes a few mins)');
INSERT INTO method_step
VALUES (11, 3, 3, 'Add the mushrooms and pepper. Cook for a few mins (dont want them soggy tho)');
INSERT INTO method_step
VALUES (12, 3, 4, 'Add the noodles and cook for about 5 mins');
INSERT INTO method_step
VALUES (13, 3, 5, 'Add the soy, coconut milk, sugar, chilli flakes, salt and pepper. Cook until well coated');
INSERT INTO method_step
VALUES (14, 3, 6, 'Serve in heated white bowls with chopped green bits of spring onions over top and lime on side.');
INSERT INTO method_step
VALUES (15, 4, 1, 'Fry off the onion and garlic until translucent');
INSERT INTO method_step
VALUES (16, 4, 2,
        'Add the tomatoes, chilli flakes, sugar, salt, pepper, oregano and capers. Cook for as long as possible for a better taste, or at least until the oil separates, and then blend (or not)');
INSERT INTO method_step
VALUES (17, 4, 3,
        'Add the gnocchi to the medium size glass dish. Cut the mozzarella into chunks (1 inchish) and mix with gnocchi.');
INSERT INTO method_step
VALUES (18, 4, 4, 'Pour the tomato sauce over the gnocchi in the dish and sprinkle cheddar cheese over the top.');
INSERT INTO method_step
VALUES (19, 4, 5, 'Cook in the oven for about 45mins. It wants to be brown on top but not burnt. ');
INSERT INTO method_step
VALUES (20, 4, 6, 'Remove from oven and leave to rest for about 10 mins');
INSERT INTO method_step
VALUES (21, 4, 7, 'Serve on heated white plates.');
INSERT INTO method_step
VALUES (22, 5, 1, 'Put the sausages on to cook on the griddle pan, brush with a little oil');
INSERT INTO method_step
VALUES (23, 5, 2, 'Add the potatoes to salted, boiling water. Cook for about 12-15mins');
INSERT INTO method_step
VALUES (24, 5, 3, 'While everything is cooking mix all the added ingredients for the mash in the metal masala bowl');
INSERT INTO method_step
VALUES (25, 5, 4,
        'Drain the potatoes, add the "mash ingredients" and give a GOOD mash (when you think its done enough do it a bit more)');
INSERT INTO method_step
VALUES (26, 5, 5, 'Serve on heated brown plates with gravy.');
/*!40000 ALTER TABLE method_step ENABLE KEYS */;

--
-- Dumping data for table `note`
--

/*!40000 ALTER TABLE note DISABLE KEYS */;
INSERT INTO note
VALUES (1, 1, 1, 'Serve with Pitta bread');
INSERT INTO note
VALUES (2, 1, 2, 'Use Sweet Potato if Butternut Squash isnt available');
INSERT INTO note
VALUES (3, 1, 3, 'The pigs can eat Sweet Potato and Butternut Squash (including peels from both)');
INSERT INTO note
VALUES (4, 1, 4, 'Recipe is tagged as rice-night for reasons');
INSERT INTO note
VALUES (5, 2, 1, 'If you need to dilute the sauce use milk instead of water');
INSERT INTO note
VALUES (6, 2, 2, 'You can blend sauce before adding mince');
INSERT INTO note
VALUES (7, 2, 3, 'Dont let mince cook for too long in sauce as it tends to go mushy.');
INSERT INTO note
VALUES (8, 3, 1, 'Make sure to use MEDIUM egg noodles');
INSERT INTO note
VALUES (9, 3, 2, 'Partially break the nests of noodles whilst still in the packet');
INSERT INTO note
VALUES (10, 3, 4, 'Serve with crackers and sesame seeds');
INSERT INTO note
VALUES (11, 4, 1, 'Cooking time can be reduced to about an hour if the sauce is already made.');
INSERT INTO note
VALUES (12, 4, 2, 'Leave the sauce unblended for a chunkier experience');
INSERT INTO note
VALUES (13, 5, 1, 'Only make half the amount of gravy as per the instructions i.e. 1/4 pint');
INSERT INTO note
VALUES (14, 5, 3, 'Three sausages per person is enough, dont be tempted to do more');

INSERT INTO recipe_tag
VALUES (1, 1, 1);
INSERT INTO recipe_tag
VALUES (2, 1, 5);
INSERT INTO recipe_tag
VALUES (3, 1, 7);
INSERT INTO recipe_tag
VALUES (4, 2, 2);
INSERT INTO recipe_tag
VALUES (5, 2, 1);
INSERT INTO recipe_tag
VALUES (6, 3, 8);
INSERT INTO recipe_tag
VALUES (7, 4, 2);
INSERT INTO recipe_tag
VALUES (8, 4, 9);
INSERT INTO recipe_tag
VALUES (9, 5, 1);
INSERT INTO recipe_tag
VALUES (10, 5, 4);








