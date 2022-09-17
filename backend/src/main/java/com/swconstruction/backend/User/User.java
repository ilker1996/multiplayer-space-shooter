package com.swconstruction.backend.User;


import javax.persistence.*;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer id;

    @Column(name ="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="level")
    private Integer level;

    public User()
    {
        //default level is 1 in our game
        this.level = 1;
    }

    public User(String username, String password, Integer level) {
        this.username = username;
        this.password = password;
        this.level = level;
    }

    /**
     * Simple getter. Returns user id of the user.
     * @return id   id of the user
     */
    public Integer getId() {
        return id;
    }

    /**
     *Simple setter. Set user id to parameter id.
     * @param id     id to be set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Simple getter. Returns username of the user.
     * @return username   username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     *Simple setter. Set user username to parameter username.
     * @param username     username to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Simple getter. Returns password of the user.
     * @return password   password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     *Simple setter. Set user password to parameter password.
     * @param password     password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Simple getter. Returns level of the user.
     * @return level   level of the user
     */
    public Integer getLevel() {
        return level;
    }

    /**
     *Simple setter. Set user level to parameter level.
     * @param level     level to be set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

}