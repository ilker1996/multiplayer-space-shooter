CREATE TABLE user (
  user_id INT(6) AUTO_INCREMENT,
  username VARCHAR(12) NOT NULL,
  password VARCHAR(60) NOT NULL,
  level INT(5) NULL,
  PRIMARY KEY (user_id),
  UNIQUE(username)
);