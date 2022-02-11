INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (1, 'User B', 'User B', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (0, 'User A', 'User A', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (2, 'User C', 'User C', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (3, 'User D', 'User D', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (4, 'User E', 'User E', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (5, 'User F', 'User F', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});

INSERT INTO bank_account (id, balance, iban, is_internal, name, owner_name, application_user) VALUES (0, 237.45, 'NL45INGB4987790602', true, 'Account A', null, 5);
INSERT INTO bank_account (id, balance, iban, is_internal, name, owner_name, application_user) VALUES (1, 2642.21, 'BE27999287873135', true, 'Account B', null, 5);
INSERT INTO bank_account (id, balance, iban, is_internal, name, owner_name, application_user) VALUES (2, 0.0, null, false, 'Account C', 'Five Guys', 5);

ALTER SEQUENCE user_sequence RESTART WITH 6;
ALTER SEQUENCE account_sequence RESTART WITH 3;