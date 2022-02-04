INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (1, 'User B', 'User B', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (0, 'User A', 'User A', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (2, 'User C', 'User C', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (3, 'User D', 'User D', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, first_name, last_name, created, updated) VALUES (4, 'User E', 'User E', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
ALTER SEQUENCE user_sequence RESTART WITH 5;