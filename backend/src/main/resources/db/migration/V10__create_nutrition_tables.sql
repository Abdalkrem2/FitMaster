
CREATE TABLE nutrition_plans (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 member_id BIGINT NOT NULL,
                                 goal VARCHAR(50) NOT NULL,
                                 daily_calories INT NOT NULL,
                                 protein_grams INT NOT NULL,
                                 carbs_grams INT NOT NULL,
                                 fat_grams INT NOT NULL,
                                 status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                                 created_at DATETIME NOT NULL,
                                 CONSTRAINT fk_np_member FOREIGN KEY (member_id) REFERENCES users(user_id)
);

CREATE TABLE nutrition_meals (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 plan_id BIGINT NOT NULL,
                                 name VARCHAR(100) NOT NULL,
                                 meal_time VARCHAR(50) NOT NULL,
                                 prep_time VARCHAR(50),
                                 total_calories INT NOT NULL,
                                 CONSTRAINT fk_nm_plan FOREIGN KEY (plan_id) REFERENCES nutrition_plans(id)
);

CREATE TABLE nutrition_foods (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 meal_id BIGINT NOT NULL,
                                 name VARCHAR(200) NOT NULL,
                                 amount VARCHAR(100) NOT NULL,
                                 calories INT NOT NULL,
                                 protein_grams INT NOT NULL,
                                 carbs_grams INT NOT NULL,
                                 fat_grams INT NOT NULL,
                                 CONSTRAINT fk_nf_meal FOREIGN KEY (meal_id) REFERENCES nutrition_meals(id)
);

CREATE TABLE nutrition_recipe_steps (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        meal_id BIGINT NOT NULL,
                                        step_order INT NOT NULL,
                                        instruction TEXT NOT NULL,
                                        CONSTRAINT fk_nrs_meal FOREIGN KEY (meal_id) REFERENCES nutrition_meals(id)
);