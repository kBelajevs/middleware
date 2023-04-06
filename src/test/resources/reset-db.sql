DELETE FROM pokerDB.vote;
DELETE FROM pokerDB.member;
DELETE FROM pokerDB.user_story;
DELETE FROM pokerDB.planning_poker_session;

ALTER TABLE pokerDB.planning_poker_session ALTER COLUMN id RESTART WITH 1;
ALTER TABLE pokerDB.member ALTER COLUMN id RESTART WITH 1;
ALTER TABLE pokerDB.user_story ALTER COLUMN id RESTART WITH 1;
ALTER TABLE pokerDB.vote ALTER COLUMN id RESTART WITH 1;

INSERT INTO pokerDB.planning_poker_session (title) VALUES ('Spring Boot Application Planning');

INSERT INTO pokerDB.member (name, session_id) VALUES
  ('John Smith', 1),
  ('Jane Doe', 1),
  ('Bob Johnson', 1);

INSERT INTO pokerDB.user_story (description, story_ref, status, session_id) VALUES
('As a user, I want to be able to create an account', 'JIRA-1', 'PENDING', 1),
('As a user, I want to be able to log in to my account', 'JIRA-2', 'VOTING', 1),
('As a user, I want to be able to reset my password', 'JIRA-3', 'VOTED', 1),
('As a user, I want to be able to log in to my account', 'JIRA-4', 'VOTING', 1);


INSERT INTO pokerDB.vote (user_story_id, member_id, value) VALUES
(1, 1, 5),
(4, 1, 5);