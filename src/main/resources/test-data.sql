INSERT INTO pokerDB.planning_poker_session (title) VALUES ('Spring Boot Application Planning');

INSERT INTO pokerDB.member (name, session_id) VALUES
  ('John Smith', 1),
  ('Jane Doe', 1),
  ('Bob Johnson', 1);

INSERT INTO pokerDB.user_story (description, status, session_id) VALUES
('As a user, I want to be able to create an account', 'PENDING', 1),
('As a user, I want to be able to log in to my account', 'PENDING', 1),
('As a user, I want to be able to reset my password', 'PENDING', 1);


--INSERT INTO pokerDB.planning_poker_session_member (session_id, member_id) VALUES
--(1, 1), (1, 2), (1, 3);
--
--INSERT INTO pokerDB.planning_poker_session_user_story (session_id, user_story_id) VALUES
--(1, 1), (1, 2), (1, 3);
