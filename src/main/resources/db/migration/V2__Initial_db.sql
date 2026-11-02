
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'CLIENT'
);

CREATE TABLE bank_account
(
    id      BIGSERIAL PRIMARY KEY,
    number  VARCHAR(34)    NOT NULL UNIQUE,
    balance NUMERIC(19, 2) NOT NULL DEFAULT 0,
    status  VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    user_id BIGINT         NOT NULL,
    CONSTRAINT fk_bank_account_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE credit_card
(
    id          BIGSERIAL PRIMARY KEY,
    number      VARCHAR(19) NOT NULL UNIQUE,
    expiry_date DATE        NOT NULL,
    account_id  BIGINT      NOT NULL,
    CONSTRAINT fk_credit_card_account FOREIGN KEY (account_id) REFERENCES bank_account (id) ON DELETE CASCADE
);

CREATE TABLE payment
(
    id                  BIGSERIAL PRIMARY KEY,
    receiver_account_id BIGINT         NOT NULL,
    giver_account_id    BIGINT,
    amount              NUMERIC(19, 2) NOT NULL,
    type                VARCHAR(20)    NOT NULL,
    date                TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description         VARCHAR(255),
    status              VARCHAR(20)    NOT NULL,
    CONSTRAINT fk_payment_receiver FOREIGN KEY (receiver_account_id) REFERENCES bank_account (id),
    CONSTRAINT fk_payment_giver FOREIGN KEY (giver_account_id) REFERENCES bank_account (id)
);
