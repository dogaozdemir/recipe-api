INSERT INTO user_table (username, password)
VALUES ('admin', '123654');
INSERT INTO user_table (username, password)
VALUES ('doga', '123654');

INSERT INTO ingredient (name, vegetarian)
VALUES ('Tomato', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Onion', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Egg', false);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Butter', false);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Bacon', false);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Cheese', false);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Bread', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Planty Sausage', true);

INSERT INTO recipe (name, number_of_serving, user_id)
VALUES ('Tomato Soup', 4, 1);

INSERT INTO instruction (prep_recipe, ingredient_id, recipe_id)
VALUES ('20 minutes in the Oven at 180 degrees', 1, 1);
INSERT INTO instruction (prep_recipe, ingredient_id, recipe_id)
VALUES ('10 minutes in the Oven at 230 degrees', 6, 1);


INSERT INTO recipe (name, number_of_serving, user_id)
VALUES ('Omelette', 1, 1);

INSERT INTO instruction (prep_recipe, ingredient_id, recipe_id)
VALUES ('Boil eggs for 7 minutes', 3, 2);

INSERT INTO recipe (name, number_of_serving, user_id)
VALUES ('Vegan Sausage', 1, 1);

INSERT INTO instruction (prep_recipe, ingredient_id, recipe_id)
VALUES ('Toast for 10 minutes', 8, 3);
