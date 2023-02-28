create table recipe
(
    id           int unsigned auto_increment primary key,
    name         varchar(100) not null,
    description  text         not null,
    cooking_time time         not null,
    constraint recipes_name_uk
        unique (name)
);

create table method_step
(
    id          int unsigned auto_increment primary key,
    recipe_id   int unsigned not null,
    ordering    int          not null,
    description varchar(255) not null,
    constraint method_recipe_id_fk
        foreign key (recipe_id) references recipe (id)
);

create table unit
(
    id           int unsigned auto_increment primary key,
    name         varchar(20) not null,
    abbreviation varchar(10) not null,
    constraint unit_abbreviation_uk
        unique (abbreviation),
    constraint unit_name_uk
        unique (name)
);

create table ingredient
(
    id          int unsigned auto_increment primary key,
    description varchar(255) not null,
    quantity    decimal      not null,
    recipe_id   int unsigned not null,
    unit_id     int unsigned not null,
    constraint ingredient_unit_fk
        foreign key (unit_id) references unit (id),
    constraint ingredient_recipe_fk
        foreign key (recipe_id) references recipe (id)
);

