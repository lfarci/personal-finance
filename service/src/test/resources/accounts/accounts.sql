INSERT INTO application_user (id, name, created, updated) VALUES (0, 'User A', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});

INSERT INTO bank_account (id, balance, iban, is_internal, name, owner, user) VALUES (0, 237.45, 'NL46INGB4987790602', true, 'Account A', null, 0);

ALTER SEQUENCE user_sequence RESTART WITH 1;
ALTER SEQUENCE account_sequence RESTART WITH 1;