ALTER TABLE member_profiles
DROP COLUMN days_per_week,
    ADD COLUMN split_type VARCHAR(50);

ALTER TABLE workout_plans
DROP COLUMN days_per_week,
    ADD COLUMN split_type VARCHAR(50) NOT NULL DEFAULT 'BRO_SPLIT_5DAY';