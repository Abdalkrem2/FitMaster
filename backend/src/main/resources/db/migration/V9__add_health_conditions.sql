ALTER TABLE member_profiles
    ADD COLUMN has_diabetes BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN has_heart_conditions BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN has_hypertension BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE member_allergies (
                                  member_id BIGINT NOT NULL,
                                  allergy_type VARCHAR(50) NOT NULL,
                                  CONSTRAINT fk_ma_profile FOREIGN KEY (member_id) REFERENCES member_profiles(member_id)
);