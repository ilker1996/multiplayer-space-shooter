package com.swconstruction.frontend.apiutils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public class Score {
    private String username;
    private Integer score;
    private Date date;

    // Json constructor for successful type conversion from server response to score object
    @JsonCreator
    public Score(@JsonProperty("username") String username, @JsonProperty("score") Integer score , @JsonProperty("date") Date date) {
        this.username = username;
        this.score = score;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
