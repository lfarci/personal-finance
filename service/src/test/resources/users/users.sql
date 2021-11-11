INSERT INTO application_user (id, name, created, updated) VALUES (0, 'User A', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, name, created, updated) VALUES (1, 'User B', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, name, created, updated) VALUES (2, 'User C', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, name, created, updated) VALUES (3, 'User D', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
INSERT INTO application_user (id, name, created, updated) VALUES (4, 'User E', {ts '2021-01-01 00:00:00.00'}, {ts '2021-01-01 00:00:00.00'});
ALTER SEQUENCE user_sequence RESTART WITH 5;