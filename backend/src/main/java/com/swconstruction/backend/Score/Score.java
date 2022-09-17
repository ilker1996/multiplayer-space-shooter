package com.swconstruction.backend.Score;


import com.swconstruction.backend.User.User;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "score")
public class Score {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "score")
    private int score;

    @Column(name = "created_date")
    private Date date;

    /**
     *  Empty constructor of Score class.
     */
    public Score()
    {
        this.score = 0;
        this.date = null;
    }

    /**
     * Constructor of Score class.
     * @param user  user to be assigned
     * @param score score of the user
     * @param date  date of the score is achieved
     */
    public Score(User user, int score, Date date) {
        this.user = user;
        this.score = score;
        this.date = date;
    }

    /**
     * Simple getter. Returns score id.
     * @return id   id of the score
     */
    public Integer getId() {
        return id;
    }

    /**
    *Simple setter. Set score id to parameter id.
    * @param id     id to be set
    */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Simple getter. Returns user.
     * @return user   user of the score
     */
    public User getUser() {
        return user;
    }

    /**
     *Simple setter. Set user to parameter user.
     * @param user    user to be set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Simple getter. Returns score.
     * @return score   Score value
     */
    public int getScore() {
        return score;
    }

    /**
     *Simple setter. Set score to parameter score.
     * @param score    score value to be set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Simple getter. Returns date.
     * @return date   Date value
     */
    public Date getDate() {
        return date;
    }

    /**
     *Simple setter. Set date to parameter date.
     * @param date    Date value to be set
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
