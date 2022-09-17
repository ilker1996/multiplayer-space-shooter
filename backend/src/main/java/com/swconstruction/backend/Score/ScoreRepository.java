package com.swconstruction.backend.Score;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Integer> {


    /* Retrieve all max scores of users sorted by date and score descending */
   @Query(value = "SELECT DISTINCT t.score, t.created_date , t.user_id FROM score t"
           + " WHERE t.score >= ALL"
           + " (SELECT s.score FROM score s"
           + " WHERE s.user_id = t.user_id)"
           + " ORDER BY t.score DESC,t.created_date DESC", nativeQuery = true
    )
   List<Object[]> retrieveScores();

    /*
        Retrieve last 7 days' scores
        From date is today-7
    */
    @Query(value = "SELECT DISTINCT t.score, t.created_date , t.user_id FROM score t"
            + " WHERE t.created_date >= :fromDate"
            + " AND t.score >= ALL"
            + " (SELECT s.score FROM score s"
            + " WHERE s.user_id = t.user_id)"
            + " ORDER BY t.score DESC,t.created_date DESC", nativeQuery = true
    )
    List<Object[]> retrieveWeeklyScores(@Param("fromDate") Date date);

    // for testing
    List<Score> findAllByUserId(Integer id);
}


