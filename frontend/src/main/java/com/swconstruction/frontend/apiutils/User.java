package com.swconstruction.frontend.apiutils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private Integer userId;
    private String username;
    private String password;
    private Integer level;

    public User()
    {
        this.username = null;
        this.password = null;
    }

    @JsonCreator
    // User constructor for converting user object from response
    public User(@JsonProperty("id") Integer userId,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("level") Integer level) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.level = level;
    }

    // User constructor for creating in the client
    public User(String username , String password)
    {
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
