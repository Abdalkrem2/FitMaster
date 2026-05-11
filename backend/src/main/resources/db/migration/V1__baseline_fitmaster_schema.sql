-- V1: Baseline schema for existing FitMaster tables
-- This captures the current state of fitmaster_db as managed by Hibernate

CREATE TABLE IF NOT EXISTS roles (
    role_id BIGINT NOT NULL AUTO_INCREMENT,
    role_name ENUM('ADMIN','EMPLOYEE','MEMBER'),
    PRIMARY KEY (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    gender VARCHAR(255),
    is_activated BIT(1),
    deleted BIT(1) NOT NULL,
    profile_picture LONGTEXT,
    admin_role_assigned_at DATETIME(6),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    created_by BIGINT,
    PRIMARY KEY (user_id),
    UNIQUE KEY UK_users_phone (phone),
    CONSTRAINT FK_users_created_by FOREIGN KEY (created_by) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_user_roles_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT FK_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS packages (
    package_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    duration_days INT NOT NULL,
    price DECIMAL(38,2) NOT NULL,
    status ENUM('ACTIVE','INACTIVE'),
    deleted BIT(1) NOT NULL,
    PRIMARY KEY (package_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS memberships (
    membership_id BIGINT NOT NULL AUTO_INCREMENT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('ACTIVE','CANCELED','EXPIRED','FROZEN'),
    price DECIMAL(38,2),
    debt DECIMAL(38,2),
    description VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    member_id BIGINT,
    package_id BIGINT,
    PRIMARY KEY (membership_id),
    CONSTRAINT FK_memberships_member FOREIGN KEY (member_id) REFERENCES users (user_id),
    CONSTRAINT FK_memberships_package FOREIGN KEY (package_id) REFERENCES packages (package_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS activity_logs (
    log_id BIGINT NOT NULL AUTO_INCREMENT,
    action ENUM('ADD','CREATE','DELETE','LOGIN','RENEW','UPDATE') NOT NULL,
    entity_type ENUM('EMPLOYEE','MEMBER','MEMBERSHIP'),
    entity_id BIGINT NOT NULL,
    details VARCHAR(255),
    created_at DATETIME(6) NOT NULL,
    preformed_by BIGINT NOT NULL,
    PRIMARY KEY (log_id),
    CONSTRAINT FK_activity_logs_user FOREIGN KEY (preformed_by) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT NOT NULL AUTO_INCREMENT,
    message VARCHAR(255) NOT NULL,
    details TEXT,
    type ENUM('ACTIVITY_LOG') NOT NULL,
    reference_id BIGINT,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notification_user_state (
    id BIGINT NOT NULL AUTO_INCREMENT,
    is_read BIT(1) NOT NULL,
    is_deleted BIT(1) NOT NULL,
    assigned_at DATETIME(6) NOT NULL,
    notification_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_nus_notification FOREIGN KEY (notification_id) REFERENCES notifications (id),
    CONSTRAINT FK_nus_user FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS revenues (
    id BIGINT NOT NULL AUTO_INCREMENT,
    amount DECIMAL(38,2) NOT NULL,
    description VARCHAR(255),
    created_at DATE,
    updated_at DATE,
    created_by BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    membership_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_revenues_created_by FOREIGN KEY (created_by) REFERENCES users (user_id),
    CONSTRAINT FK_revenues_member FOREIGN KEY (member_id) REFERENCES users (user_id),
    CONSTRAINT FK_revenues_membership FOREIGN KEY (membership_id) REFERENCES memberships (membership_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
