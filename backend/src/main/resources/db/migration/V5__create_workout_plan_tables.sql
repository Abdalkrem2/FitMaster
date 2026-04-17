CREATE TABLE workout_plans (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               member_id BIGINT NOT NULL,
                               name VARCHAR(255),
                               goal VARCHAR(50) NOT NULL,
                               level VARCHAR(50) NOT NULL,
                               days_per_week INT NOT NULL,
                               status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                               created_at DATETIME NOT NULL,
                               CONSTRAINT fk_wp_member FOREIGN KEY (member_id) REFERENCES users(user_id)
);

CREATE TABLE workout_days (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              workout_plan_id BIGINT NOT NULL,
                              day_number INT NOT NULL,
                              muscle_group_label VARCHAR(100) NOT NULL,
                              CONSTRAINT fk_wd_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id)
);

CREATE TABLE workout_exercises (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   workout_day_id BIGINT NOT NULL,
                                   exercise_id BINARY(16) NOT NULL,
                                   order_index INT NOT NULL,
                                   sets INT NOT NULL,
                                   reps INT,
                                   duration_seconds INT,
                                   CONSTRAINT fk_we_day FOREIGN KEY (workout_day_id) REFERENCES workout_days(id),
                                   CONSTRAINT fk_we_exercise FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);