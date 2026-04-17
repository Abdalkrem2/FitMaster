CREATE TABLE member_profiles (
  member_id BIGINT PRIMARY KEY,
  goal VARCHAR(50) NOT NULL,
  fitness_level VARCHAR(50) NOT NULL,
  days_per_week INT NOT NULL,
  training_style VARCHAR(50),
  weight DOUBLE,
  height DOUBLE,
  age INT,
  CONSTRAINT fk_mp_member FOREIGN KEY (member_id) REFERENCES users(user_id)
);

CREATE TABLE member_injuries (
   member_id BIGINT NOT NULL,
   injury_type VARCHAR(50) NOT NULL,
   CONSTRAINT fk_mi_profile FOREIGN KEY (member_id) REFERENCES member_profiles(member_id)
);