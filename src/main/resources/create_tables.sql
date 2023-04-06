CREATE SCHEMA pokerDB AUTHORIZATION admin;

CREATE TABLE pokerDB.planning_poker_session (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255)
);

CREATE TABLE pokerDB.member (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    session_id INT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES pokerDB.planning_poker_session(id)
);

CREATE TABLE pokerDB.user_story (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(255),
    story_ref VARCHAR(255),
    status VARCHAR(10) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'VOTING', 'VOTED')),
    session_id INT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES pokerDB.planning_poker_session(id)
);

CREATE TABLE pokerDB.vote (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_story_id INT REFERENCES user_story(id),
    member_id INT REFERENCES member(id),
    value INT,
    UNIQUE (user_story_id, member_id)
);

