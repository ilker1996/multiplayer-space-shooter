package com.swconstruction.backend.Score;

import com.swconstruction.backend.User.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

// For JPA projection of Score
// In other words just return username,score and date of score
public interface ScoreEntry {

    // Score.getUser().getUsername()
    @Value("#{target.user.username}")
    String getUsername();

    // Score.getScore();
    Integer getScore();

    //Score.getDate();
    Date getDate();

}

