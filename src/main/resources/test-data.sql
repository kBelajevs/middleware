INSERT INTO pokerDB.planning_poker_session (title) VALUES ('Spring Boot Application Planning');

INSERT INTO pokerDB.member (name, session_id) VALUES
  ('John Smith', 1),
  ('Jane Doe', 1),
  ('Bob Johnson', 1);

INSERT INTO pokerDB.user_story (description, story_ref, status, session_id) VALUES
('As a user, I want to be able to create an account', 'JIRA-1', 'VOTING', 1),
('As a user, I want to be able to log in to my account', 'JIRA-2', 'PENDING', 1),
('As a user, I want to be able to reset my password', 'JIRA-3', 'VOTED', 1);


INSERT INTO pokerDB.vote (user_story_id, member_id, value) VALUES
(1, 1, 5);