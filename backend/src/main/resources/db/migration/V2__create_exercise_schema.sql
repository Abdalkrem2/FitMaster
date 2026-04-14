-- V2: Exercise reference data schema (from exercisesdatabase)
-- These tables are READ-ONLY reference data for the workout plan generator

CREATE TABLE IF NOT EXISTS equipment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS muscles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    body_region VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movement_patterns (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tags (
    id BIGINT NOT NULL AUTO_INCREMENT,
    category VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exercises (
    id BINARY(16) NOT NULL,
    code VARCHAR(10),
    difficulty_level VARCHAR(50),
    is_archived TINYINT(1) DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exercise_muscles (
    exercise_id BINARY(16) NOT NULL,
    muscle_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (exercise_id, muscle_id),
    CONSTRAINT FK_ex_muscles_exercise FOREIGN KEY (exercise_id) REFERENCES exercises (id),
    CONSTRAINT FK_ex_muscles_muscle FOREIGN KEY (muscle_id) REFERENCES muscles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exercise_equipment (
    exercise_id BINARY(16) NOT NULL,
    equipment_id BIGINT NOT NULL,
    is_required TINYINT(1) DEFAULT 1,
    PRIMARY KEY (exercise_id, equipment_id),
    CONSTRAINT FK_ex_equip_exercise FOREIGN KEY (exercise_id) REFERENCES exercises (id),
    CONSTRAINT FK_ex_equip_equipment FOREIGN KEY (equipment_id) REFERENCES equipment (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exercise_patterns (
    exercise_id BINARY(16) NOT NULL,
    pattern_id BIGINT NOT NULL,
    PRIMARY KEY (exercise_id, pattern_id),
    CONSTRAINT FK_ex_pattern_exercise FOREIGN KEY (exercise_id) REFERENCES exercises (id),
    CONSTRAINT FK_ex_pattern_pattern FOREIGN KEY (pattern_id) REFERENCES movement_patterns (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exercise_tags (
    exercise_id BINARY(16) NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (exercise_id, tag_id),
    CONSTRAINT FK_ex_tags_exercise FOREIGN KEY (exercise_id) REFERENCES exercises (id),
    CONSTRAINT FK_ex_tags_tag FOREIGN KEY (tag_id) REFERENCES tags (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exercise_translations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    exercise_id BINARY(16) NOT NULL,
    name VARCHAR(255),
    description TEXT,
    instructions TEXT,
    audio_cue_url VARCHAR(500),
    locale VARCHAR(10) DEFAULT 'en',
    PRIMARY KEY (id),
    CONSTRAINT FK_ex_trans_exercise FOREIGN KEY (exercise_id) REFERENCES exercises (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS media_assets (
    id BINARY(16) NOT NULL,
    url VARCHAR(500),
    type VARCHAR(50),
    license_type VARCHAR(50),
    attribution_text VARCHAR(500),
    created_at DATETIME,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS exercise_media (
    id BIGINT NOT NULL AUTO_INCREMENT,
    exercise_id BINARY(16) NOT NULL,
    media_asset_id BINARY(16) NOT NULL,
    role VARCHAR(50),
    sex VARCHAR(50),
    view_angle VARCHAR(50),
    PRIMARY KEY (id),
    CONSTRAINT FK_ex_media_exercise FOREIGN KEY (exercise_id) REFERENCES exercises (id),
    CONSTRAINT FK_ex_media_asset FOREIGN KEY (media_asset_id) REFERENCES media_assets (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
