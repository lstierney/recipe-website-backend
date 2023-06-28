drop table if exists method_step;
drop table if exists ingredient;
drop table if exists recipe_tag;
drop table if exists unit;
drop table if exists recipe;
drop table if exists tag;
drop table if exists authorised_user;

create table recipe
(
    id             int generated by default as identity(start with 1, increment by 1) primary key,
    name           varchar(100) not null,
    description    varchar(500) not null,
    cooking_time   int          not null,
    image_filename varchar(100),
    constraint recipes_name_uk
        unique (name)
);

create table method_step
(
    id          int generated by default as identity(start with 1, increment by 1) primary key,
    recipe_id   int          not null,
    ordering    int          not null,
    description varchar(255) not null,
    constraint method_recipe_id_fk
        foreign key (recipe_id) references recipe (id)
);

create table unit
(
    id           int generated by default as identity(start with 1, increment by 1) primary key,
    name         varchar(30) not null,
    abbreviation varchar(10) not null,
    constraint unit_abbreviation_uk
        unique (abbreviation),
    constraint unit_name_uk
        unique (name)
);

create table tag
(
    id          int generated by default as identity(start with 1, increment by 1) primary key,
    name        varchar(20) not null,
    description varchar(100),
    constraint tag_name_uk
        unique (name)
);

create table recipe_tag
(
    id        int generated by default as identity(start with 1, increment by 1) primary key,
    recipe_id int not null,
    tag_id    int not null,
    constraint recipe_tag_recipe_id_fk
        foreign key (recipe_id) references recipe (id),
    constraint recipe_tag_recipe_fk
        foreign key (tag_id) references tag (id)
);

create table ingredient
(
    id          int generated by default as identity(start with 1, increment by 1) primary key,
    description varchar(255) not null,
    quantity    decimal      not null,
    recipe_id   int          not null,
    unit_id     int,
    constraint ingredient_unit_fk
        foreign key (unit_id) references unit (id),
    constraint ingredient_recipe_fk
        foreign key (recipe_id) references recipe (id)
);

create table authorised_user
(
    id       int generated by default as identity(start with 1, increment by 1) primary key,
    username varchar(20) not null,
    password varchar(20) not null,
    constraint username_uk
        unique (username)
);

