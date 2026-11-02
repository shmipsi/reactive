CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users_roles
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_users_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_users_roles_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT uq_users_roles UNIQUE (user_id, role_id)
);

INSERT INTO roles (name) VALUES ('ROLE_CLIENT'), ('ROLE_ADMIN');

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ROLE_' || u.role;

ALTER TABLE users DROP COLUMN role;

CREATE EXTENSION IF NOT EXISTS pgcrypto;
UPDATE users
SET password = crypt(password, gen_salt('bf', 10))
WHERE password NOT LIKE '$2_$%';

DROP TABLE roles_has_users_test;
DROP TABLE roles_test;
DROP TABLE users_test;
