package com.swconstruction.backend.Score;

import com.swconstruction.backend.User.User;
import com.swconstruction.backend.User.UserNotFoundException;
import com.swconstruction.backend.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ScoreController {

    /* Score Database */
    private ScoreRepository scoreRepository;
    /* User Database */
    private UserRepository userRepository;

    @Autowired
    public ScoreController(ScoreRepository scoreRepository, UserRepository userRepository) {
        this.scoreRepository = scoreRepository;
        this.userRepository = userRepository;
    }

    /**
     * This method returns all scores sorted by descending order.
     * @param list  list of objects to converted
     * @return List<ScoreEntry>  Convert list of object arrays to score entries with username,score,date in database.
     */
    public  List<ScoreEntry> SqlToScoreEntry(List<Object[]> list)
    {
        List<ScoreEntry> scores = new ArrayList<>();
        list.forEach(object -> {
            String username =
                    userRepository.findById((Integer) object[2]).
                            get().getUsername();

            Integer score = (Integer) object[0];
            Date date = (Date) object[1];
            scores.add(new ScoreEntry() {
                @Override
                public String getUsername() {
                    return username;
                }

                @Override
                public Integer getScore() {
                    return score;
                }

                @Override
                public java.util.Date getDate() {
                    return date;
                }
            });
        });

        return scores;
    }

    /**
     * This method returns all scores sorted by descending order.
     * @return Iterable<ScoreEntry>  List of all score entries with username,score,date in database.
     */
    @GetMapping(path="/scores")
    public @ResponseBody Iterable<ScoreEntry> getAllScores() {
        List<Object[]> list =  scoreRepository.retrieveScores();

        return SqlToScoreEntry(list);

    }

    /**
     * This method returns scores of last 7 days sorted by score in descending order.
     * It subtracts 7 from local date to find the beginning of the 7 days from the current day.
     * @return Iterable<ScoreEntry>  List of score entries with username,score,date achieved in last 7 days
     */
    @GetMapping(path="/scores/weekly")
    public @ResponseBody Iterable<ScoreEntry> getWeeklyScores() {

        List<Object[]> list =  scoreRepository.retrieveWeeklyScores(Date.valueOf(LocalDate.now().minusDays(7)));

        return SqlToScoreEntry(list);
    }

    /**
     * This method adds a new score of the user according to the user id.
     * @param newScore  score to be added
     * @param id        id of the user
     * @return ScoreEntry    newly added score entry with username, score and date
     * @throws UserNotFoundException if the user does not exist with the given id
     *
     */
    @PostMapping(path="/scores/{id}")
    public @ResponseBody ScoreEntry createScore(@RequestBody Score newScore
                                            ,@PathVariable Integer id) throws UserNotFoundException{
            Optional<User> user = userRepository.findById(id);
            /* Throws an exception if the user doesn't exist with the given id */
            if(!user.isPresent()) {
                throw new UserNotFoundException("User not found!");
            }
            /* Set date as today */
            newScore.setDate(Date.valueOf(LocalDate.now()));
            /* Set user with the given id */
            newScore.setUser(user.get());

            /* Add new score to the database */
            Score savedScore = scoreRepository.save(newScore);

            //return projected ScoreEntry
            return new ScoreEntry() {
                @Override
                public String getUsername() {
                    return savedScore.getUser().getUsername();
                }

                @Override
                public Integer getScore() {
                    return savedScore.getScore();
                }

                @Override
                public java.util.Date getDate() {
                    return savedScore.getDate();
                }
            };
    }

    /**
     * This method deletes the score of the given id
     * @param id id of the score to be deleted
     * @throws UserNotFoundException if the score does not exist with the given id
     */
    @DeleteMapping("scores/{id}")
    public void deleteScore(@PathVariable Integer id) throws UserNotFoundException {
        Optional<Score> score = scoreRepository.findById(id);
        if(!score.isPresent()) {
            throw new UserNotFoundException("Score not found!");
        }
        /* Delete score */
        scoreRepository.deleteById(id);
    }
}
