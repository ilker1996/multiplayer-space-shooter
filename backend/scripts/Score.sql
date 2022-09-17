CREATE TABLE score (
  score_id INT(6) AUTO_INCREMENT,
  user_id INT(6),
  score INT(5) NULL,
  created_date DATE NULL,
  PRIMARY KEY (score_id),
  FOREIGN KEY (user_id)
    REFERENCES user(user_id)
    ON DELETE CASCADE
);