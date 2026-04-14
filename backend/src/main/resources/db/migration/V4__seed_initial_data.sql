-- Roles
INSERT IGNORE INTO roles (role_id, role_name) VALUES (1, 'ADMIN'), (2, 'EMPLOYEE'), (3, 'MEMBER');

-- Admin user (password: admin, bcrypt encoded)
INSERT IGNORE INTO users (user_id, full_name, phone, password_hash, gender, is_activated, deleted, created_at)
VALUES (1, 'admin', '0780000000', '$2a$10$28AGr/PqBzUpkdOLTa32tuAb1EMCQyyQ//lXF3FQnWDTBRkGd.Fny', 'Male', 1, 0, NOW());

UPDATE users SET created_by = 1, admin_role_assigned_at = NOW() WHERE user_id = 1;

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 1);

-- Default package
INSERT IGNORE INTO packages (name, description, price, duration_days, status, deleted)
VALUES ('1 Month', 'premium offer', 65.00, 30, 'ACTIVE', 0);