# recipe-website-backend

## Synopsis

This is the first of what is expected to be multiple backends, implemented in various languages, for the recipe-website.

This particular version uses Java 17, Spring Boot, Spring Data JPA (Hibernate) and REST Controllers, with Maven
controlling the build process.

The main purpose of the recipe-website-backend is to expose the recipe data etc as JSON, to be consumed by the
front-end.

## Status
|                |                                                                                                                |
|----------------|----------------------------------------------------------------------------------------------------------------|
| Build Status   | ![example workflow](https://github.com/lstierney/recipe-website-backend/actions/workflows/maven.yml/badge.svg) |
| Last Commit    | ![Latest Commit](https://img.shields.io/github/last-commit/lstierney/recipe-website-backend)                   |
| Latest Version | ![Latest Version](https://img.shields.io/badge/latest-v1.3.0-brightgreen)                                      |
| License        | [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)    |

## Demo Link
http://myveggierecipes.com:8080

## Installation

### Checkout

* `git clone git@github.com:lstierney/recipe-website-backend.git`

### Configuration

As this is a Spring Boot application configuration is controlled by a series of `application.properties` files

* `application.properties` - common properties
* `application-dev.properties` - development properties. Uses an in memory HSQLDB database
* `application-test.properties` - test properties. Uses an in memory HSQLDB database
* `application-live.properties` - live properties. Used a mySQL installation (somewhere). You would set your DB
  details in here.

### SQL

SQL Files are provided which will generate the schema AND insert required meta-data and samples.

* `/src/main/resources/schema.sql` - this will create the schema
* `/src/main/resources/data.sql` - this will insert the meta-data and some sample recipes.

*Note*: these scripts will automatically execute when the application properties are set to `dev`, `test` i.e. against the HSQLDB
but will not automatically execute in the `live` environment.

### Build executable JAR

* Build JAR (currently int tests run as part of *normal* build): `mvn clean package`
* The built JAR can be found in `$project.baseDir/target/`
* *Hint*: make sure you use the *executable* JAR that is generated.

### Installing the JAR

* *Hint*: make sure that before you install a new version of the Application you stop any previous running version (see
  below)
* The application is intended to be installed as a `systemd` service. An example Systemd Unit file is supplied in `/src/main/resources/scripts`

#### Setting the correct Spring profile in .conf

* The Application defaults to running under "dev" profile.
* Whilst running on a server it should use profile "live"
* In order to configure the Application to use one of these profiles create a file, in the same directory as the JAR,
  with the same filename as the JAR but with extension ".conf" instead of ".jar".
* The contents of the conf file should be similar to:

```
JAVA_OPTS="-Dspring.profiles.active=live -DMYSQL_USER=<MYSQL_USER> -DMYSQL_PASSWORD=<MYSQL_PASSWD> -DJWT_SECRET=<JWT_SECRET> -Dcross.origin.allowed.host=<CROSS_ORIGIN_HOST>
``` 
* Note that as well as the Active Spring Profile the MySQL User/Password, JWT Secret and allowed Cross Origin Host are also specified.
* `dev` profile will work via the JAR but is expected to be used in development environment as, as mentioned above, it uses an in mem DB with preloaded data.

### Starting/Stopping/Monitoring the Service

* Once the JAR/Service has been installed it can be controlled with the
  familiar:

```sudo systemctl recipe-website-springboot.service start|stop|restart|status``` etc.
* Log output can be monitored with `sudo journalctl -u recipe-website-springboot.service -f`

## REST Endpoints

Note: any POST, DELETE, PATCH methods i.e. those which modify data, require a valid JWT_TOKEN to be supplied request header:

`Authorization`, `Bearer xxxxx.yyyyy.zzzzz`

### GET /api/recipes/{recipe-name}

This is an example of a complete recipe.

```json
{
    "id": 39,
    "name": "Lasagne",
    "description": "Layered pasta sheets with a meaty ragu style sauce.",
    "imageFileName": "lasagne.jpg",
    "cooked": 1,
    "cookingTime": 90,
    "basedOn": "",
    "methodSteps": [
        {
            "id": 229,
            "ordering": 1,
            "description": "Put the bottom lasagne sheets into soak"
        },
        {
            "id": 230,
            "ordering": 2,
            "description": "Meanwhile fry the onion and garlic until translucent (if you're doing any other veg do them here)"
        },
        {
            "id": 231,
            "ordering": 3,
            "description": "Add the chopped tomatoes, salt, pepper, chilli flakes and oregano. Cook for a good 15mins or so."
        },
        {
            "id": 232,
            "ordering": 4,
            "description": "Add the veggie mince and leave to bubble away"
        },
        {
            "id": 233,
            "ordering": 5,
            "description": "Meanwhile, make the white sauce: melt the butter, add the corn flour and mix into paste, put back on heat and gradually add the milk and mustard until smooth and creamy."
        },
        {
            "id": 234,
            "ordering": 6,
            "description": "Arrange the soaked lasagne sheets on the bottom the big square oven dish. Add half the filling. Arrange a layer of uncooked lasagne sheets on top. Add the remaining filling. Add another layer of lasagne sheets. Add the white sauce and spread evenly over top. Spread grated cheese with some paprika"
        },
        {
            "id": 235,
            "ordering": 7,
            "description": "Cook for 45 mins and leave to to rest for at least 10 mins"
        }
    ],
    "ingredients": [
        {
            "id": 383,
            "unit": null,
            "description": "onions (chopped)",
            "quantity": 2.00,
            "ordering": 1
        },
        {
            "id": 384,
            "unit": null,
            "description": "cloves of garlic (chopped)",
            "quantity": 6.00,
            "ordering": 2
        },
        {
            "id": 385,
            "unit": {
                "id": 10,
                "name": "400g can",
                "abbreviation": "can"
            },
            "description": "chopped tomatoes",
            "quantity": 2.00,
            "ordering": 3
        },
        {
            "id": 386,
            "unit": {
                "id": 1,
                "name": "teaspoon",
                "abbreviation": "tsp"
            },
            "description": "salt",
            "quantity": 2.00,
            "ordering": 4
        },
        {
            "id": 387,
            "unit": null,
            "description": "Ground Pepper",
            "quantity": 0.00,
            "ordering": 5
        },
        {
            "id": 388,
            "unit": {
                "id": 1,
                "name": "teaspoon",
                "abbreviation": "tsp"
            },
            "description": "chilli flakes",
            "quantity": 0.50,
            "ordering": 6
        },
        {
            "id": 389,
            "unit": {
                "id": 1,
                "name": "teaspoon",
                "abbreviation": "tsp"
            },
            "description": "dried oregano",
            "quantity": 1.00,
            "ordering": 7
        },
        {
            "id": 390,
            "unit": {
                "id": 5,
                "name": "gram",
                "abbreviation": "g"
            },
            "description": "veggie mince (one packet)",
            "quantity": 450.00,
            "ordering": 8
        },
        {
            "id": 391,
            "unit": {
                "id": 8,
                "name": "ounce",
                "abbreviation": "oz"
            },
            "description": "corn flour",
            "quantity": 1.50,
            "ordering": 10
        },
        {
            "id": 392,
            "unit": {
                "id": 8,
                "name": "ounce",
                "abbreviation": "oz"
            },
            "description": "cooking butter",
            "quantity": 1.50,
            "ordering": 11
        },
        {
            "id": 393,
            "unit": {
                "id": 11,
                "name": "millilitre",
                "abbreviation": "ml"
            },
            "description": "milk",
            "quantity": 600.00,
            "ordering": 12
        },
        {
            "id": 394,
            "unit": null,
            "description": "Grated cheddar cheese",
            "quantity": 0.00,
            "ordering": 13
        },
        {
            "id": 395,
            "unit": null,
            "description": "Lasagne sheets",
            "quantity": 0.00,
            "ordering": 14
        },
        {
            "id": 396,
            "unit": {
                "id": 1,
                "name": "teaspoon",
                "abbreviation": "tsp"
            },
            "description": "mustard",
            "quantity": 1.00,
            "ordering": 15
        }
    ],
    "notes": [
        {
            "id": 110,
            "ordering": 1,
            "description": "Make sure to put the bottom sheets in to soak (in the big cake box) before starting anything else"
        },
        {
            "id": 111,
            "ordering": 2,
            "description": "You can start with a [r]soffritto[/r] "
        }
    ],
    "tags": [
        {
            "id": 2,
            "name": "pasta",
            "description": "Straight from Italia!"
        },
        {
            "id": 4,
            "name": "old-school",
            "description": "Just like wot you remember"
        },
        {
            "id": 9,
            "name": "big-fancy",
            "description": "Not your everyday dinner"
        }
    ],
    "servedOn": {
        "id": 84,
        "crockery": {
            "id": 2,
            "description": "Green Plates"
        },
        "heated": true
    }
}
```

### GET /api/recipes

Gets ALL the data for all the recipes. These are returned as an array of recipes.

```json
[
  {...recipe1...},
  {...recipe2...}
]

```

### GET /api/recipes/list

Gets PREVIEW data for all the recipes. Preview data is intended to be used in cards/search results etc

```json
[
  {
    "name": "Moroccan Stew with Cous Cous",
    "id": 1,
    "description": "Mildly spicy, slowish cooked veg with cous cous.",
    "cooked": 1,
    "imageFileName": "Cous_cous.jpg"
  },
  {
    "name": "Spaghetti Bolognese",
    "id": 2,
    "description": "Everybody has their own version of this recipe; this is mine",
    "cooked": 2,
    "imageFileName": "bolognese.jpg"
  }
  ...etc...
]
```
### GET /api/recipes/latest

Returns PREVIEW data for the SIX most recently added recipes. Preview data is in the form as per [GET /api/recipes/list](#get-apirecipeslist) above

### GET /api/recipes/random

As per [GET /api/recipes/latest](#get-apirecipeslatest) except preview data for ONE random recipe is returned.

### GET /api/recipes?tagNames=TAG1,TAG2,TAG3

Returns preview data (as above) for any Recipes which match ALL of the provided Tags.

### POST /api/recipes

Add/create a new Recipe. This example is for a complete recipe (not all fields are required)

```json
{
  "name": "Test Recipe",
  "description": "This is the description for the test recipe.",
  "cookingTime": 45,
  "cooked": 0,
  "basedOn": "http://www.bbc.co.uk",
  "ingredients": [
    {
      "description": "ingredient with no unit",
      "quantity": 1,
      "ordering": 1
    },
    {
      "description": "ingredient with unit",
      "quantity": 2,
      "unit": {
        "id": 8
      },
      "ordering": 2
    },
    {
      "description": "Ingredient with no amount or unit",
      "quantity": 0,
      "ordering": 3
    }
  ],
  "methodSteps": [
    {
      "description": "Method step 1",
      "ordering": 1
    },
    {
      "description": "Method step 2",
      "ordering": 2
    },
    {
      "description": "Method step 3",
      "ordering": 3
    }
  ],
  "notes": [
    {
      "description": "This is note 1",
      "ordering": 1
    },
    {
      "description": "This is note 2",
      "ordering": 2
    }
  ],
  "imageFileName": "",
  "tags": [
    {
      "id": 14,
      "name": "dinner",
      "description": "Recipes suitable for dinner"
    },
    {
      "id": 2,
      "name": "pasta",
      "description": "Straight from Italia!"
    },
    {
      "id": 1,
      "name": "easy",
      "description": "Simple to make"
    }
  ],
  "servedOn": {
    "crockery": {
      "id": 5
    },
    "heated": true
  }
}
```

### PUT /api/recipes

Updates an existing recipe.

```json
{
  "name": "Spaghetti Aglio e Olio",
  "description": "Spaghetti with garlic and oil. Simple yet so satisfying.",
  "cookingTime": 30,
  "cooked": 5,
  "basedOn": "https://www.bbc.co.uk/food/recipes/peperoncino_99009",
  "ingredients": [
    {
      "id": 112,
      "unit": {
        "id": 8,
        "name": "ounce",
        "abbreviation": "oz"
      },
      "description": "spaghetti",
      "quantity": 12,
      "ordering": 1
    },
    {
      "id": 118,
      "unit": {
        "id": 8,
        "name": "ounce",
        "abbreviation": "oz"
      },
      "description": "parsley (roughly chopped)",
      "quantity": 1,
      "ordering": 2
    },
    {
      "id": 113,
      "unit": null,
      "description": "big cloves of garlic (finely chopped)",
      "quantity": 3,
      "ordering": 3
    },
    {
      "id": 115,
      "unit": {
        "id": 4,
        "name": "tablespoon",
        "abbreviation": "tbsp"
      },
      "description": "chilli flakes",
      "quantity": 1,
      "ordering": 4
    },
    {
      "id": 116,
      "unit": {
        "id": 1,
        "name": "teaspoon",
        "abbreviation": "tsp"
      },
      "description": "salt",
      "quantity": 1,
      "ordering": 5
    },
    {
      "id": 117,
      "unit": null,
      "description": "Pepper to taste",
      "quantity": 0,
      "ordering": 6
    },
    {
      "id": 114,
      "unit": {
        "id": 4,
        "name": "tablespoon",
        "abbreviation": "tbsp"
      },
      "description": "olive oil",
      "quantity": 5,
      "ordering": 7
    }
  ],
  "methodSteps": [
    {
      "id": 65,
      "ordering": 1,
      "description": "Put the spaghetti onto boil"
    },
    {
      "id": 66,
      "ordering": 2,
      "description": "Meanwhile...heat the oil and gently fry the garlic, chilli, 3/4 of the parsley and salt. "
    },
    {
      "id": 67,
      "ordering": 3,
      "description": "Once pasta is ready mix with the oil and serve in hot white bowls. Sprinkle extra parsley over top."
    },
    {
      "id": 110,
      "ordering": 4,
      "description": "Once pasta is ready mix with the oil, sprinkle extra parsley over top. Serve"
    }
  ],
  "notes": [
    {
      "id": 29,
      "ordering": 1,
      "description": "B doesn't like extra parsley on top"
    },
    {
      "id": 30,
      "ordering": 2,
      "description": "The oil doesn't need to be cooked per se, just enough to get the flavours infusing. "
    },
    {
      "id": 31,
      "ordering": 3,
      "description": "The oil can be prepared before the pasta is ready; they don't need to be ready at the same time."
    }
  ],
  "imageFileName": "spaghetti_aglio_e_olio.jpg",
  "tags": [
    {
      "id": 1,
      "name": "easy",
      "description": "Simple to make"
    },
    {
      "id": 2,
      "name": "pasta",
      "description": "Straight from Italia!"
    },
    {
      "id": 14,
      "name": "dinner",
      "description": "Recipes suitable for dinner"
    }
  ],
  "servedOn": {
    "crockery": {
      "id": 4
    },
    "heated": true
  },
  "id": 11
}

```

### POST /recipes/markascooked/{id}

Marks the recipe (as per it's id) as cooked.

### GET /api/units

Gets ALL the "Unit of Measurement" meta-data

```json
[
    {
        "id": 1,
        "name": "teaspoon",
        "abbreviation": "tsp"
    },
    {
        "id": 4,
        "name": "tablespoon",
        "abbreviation": "tbsp"
    }
    ...etc...
]
```

### GET /api/crockery

Gets ALL the "Crockery" meta-data

```json
[
    {
        "id": 3,
        "description": "Brown Plates"
    },
    {
        "id": 5,
        "description": "Green Bowls"
    }
    ...etc...
]
```

### GET /api/tags

Gets ALL the "Tags" meta-data

```json
[
    {
        "id": 17,
        "name": "ingredient",
        "description": "An input ingredient to a recipe"
    },
    {
        "id": 15,
        "name": "lentils",
        "description": "The wonder food!"
    }
    ...etc...
]
```
### POST /api/tags

Add a new Tag

```json
{
    "name": "ingredient",
    "description": "An input ingredient to a recipe"
}
```

### PUT /api/tags/{tag.id}

Update a Tag

```json
{
    "name": "ingredient",
    "description": "An input ingredient to a recipe"
}
```

### DELETE /api/tags/{tag.id}

Deletes a Tag.

### POST /api/authenticate

Authenticates a User and returns a JWT Token if successful

```json
{
  "username": "username",
  "password": "password"
}
```

## License
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT)



