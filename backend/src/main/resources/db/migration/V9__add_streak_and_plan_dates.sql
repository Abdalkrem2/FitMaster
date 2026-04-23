-- Add streak fields to users table
ALTER TABLE users 
ADD COLUMN daily_streak INT DEFAULT 0 NOT NULL,
ADD COLUMN streak_last_updated DATETIME NULL;

-- Add date and count fields to workout_plans table
ALTER TABLE workout_plans
ADD COLUMN plan_start_date DATETIME NULL,
ADD COLUMN plan_end_date DATETIME NULL,
ADD COLUMN completed_days_count INT DEFAULT 0 NOT NULL,
ADD COLUMN total_days_in_plan INT NULL;



-- Index for better query performance
CREATE INDEX idx_workout_plans_member_status ON workout_plans(member_id, status);
CREATE INDEX idx_workout_plans_plan_end_date ON workout_plans(plan_end_date);
