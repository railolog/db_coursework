-- Inserting data into user table
BEGIN TRANSACTION;

INSERT INTO "user" (USERNAME, PASSWORD, ROLE, BALANCE)
VALUES ('user1', 'password1', 'ADMIN', 500);

END TRANSACTION;

-- Inserting data into location table
INSERT INTO location (TITLE, DESCRIPTION)
VALUES ('Route 1', 'A peaceful route between Pallet Town and Viridian City.'),
       ('Pallet Town', 'A small and quaint town with lush fields and calm waters.'),
       ('Viridian City', 'A city surrounded by forests and exhibits natural harmony.');

-- Inserting data into trainer table
INSERT INTO trainer (ORIGIN, IS_MALE, NAME, AGE, ID)
VALUES ('JP', 'True', 'Ash', 12, 1),
       ('US', 'False', 'Misty', 14, 2),
       ('UK', 'True', 'Brock', 17, 3);

-- Inserting data into pokemon table
INSERT INTO pokemon (HP, ATTACK, PREVIOUS_STAGE, TRAINER_ID, DEFENSE, SPEED, NAME, ID)
VALUES (100, 50, NULL, 1, 40, 60, 'Pikachu', 1),
       (150, 70, NULL, 2, 60, 80, 'Togepi', 2),
       (120, 80, NULL, 3, 50, 90, 'Onix', 3);

-- Inserting data into fight table
-- INSERT INTO fight (FIRST_POKEMON_ID, SECOND_POKEMON_ID, LOCATION_ID)
-- VALUES (1, 2, 1),
--        (2, 3, 1),
--        (1, 3, 2);

-- Inserting data into bet table
-- INSERT INTO bet (USER_ID, FIGHT_ID, CREDITS, COEFFICIENT, CHOICE)
-- VALUES (1, 1, 100, 1.5, 'True');

-- Inserting data into pokemon_type table
INSERT INTO pokemon_type (TYPE)
VALUES ('Electric'),
       ('Water'),
       ('Rock');

-- Inserting data into has_type table
INSERT INTO has_type (TYPE_ID, POKEMON_ID)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 3);

-- Inserting data into location_type table
-- INSERT INTO location_type (TYPE_ID, LOCATION_ID)
-- VALUES (1, 1),
--        (2, 2),
--        (3, 3);

-- Inserting data into abilities table
INSERT INTO abilities (TITLE, DESCRIPTION, EFFECT_HP, EFFECT_ATTACK, EFFECT_DEFENCE, EFFECT_SPEED)
VALUES ('Boost Power', 'Increases attack by 50% for 3 turns', 0, 50, 0, 0),
       ('Quick Guard', 'Protects against priority moves for one turn', 0, 0, 100, 0),
       ('Speed Up', 'Increases speed by 20% for 5 turns', 0, 0, 0, 20);

-- Inserting data into has_ability table
-- INSERT INTO has_ability (POKEMON_ID, ABILITY_ID)
-- VALUES (1, 1),
--        (2, 2),
--        (3, 3);