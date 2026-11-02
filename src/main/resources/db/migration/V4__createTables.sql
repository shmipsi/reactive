create table users_test
(
    id bigint generated always as identity primary key not null,
    username varchar(100),
    password varchar(100)
);

create table roles_test
(
    id bigint generated always as identity primary key not null,
    name varchar(100)
);

create table roles_has_users_test
(
    id bigint generated always as identity primary key not null,
    user_id bigint,
    role_id bigint
);

ALTER TABLE roles_has_users_test
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id) REFERENCES users_test(id);

ALTER TABLE roles_has_users_test
    ADD CONSTRAINT fk_role_id
        FOREIGN KEY (role_id) REFERENCES roles_test(id);