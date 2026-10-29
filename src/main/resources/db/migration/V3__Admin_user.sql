INSERT INTO users (email, password, first_name, last_name, role)
VALUES ('admin@payments.ua', 'admin', 'System', 'Administrator', 'ADMIN')
ON CONFLICT (email) DO NOTHING;
