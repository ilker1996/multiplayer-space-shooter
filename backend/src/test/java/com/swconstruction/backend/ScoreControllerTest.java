package com.swconstruction.backend;



import com.swconstruction.backend.Score.*;
import com.swconstruction.backend.User.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.*;

public class ScoreControllerTest extends BackendApplicationTest{

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void test_getAllScores() throws Exception{

        String uri = "/scores";

        /* Create user in the database */
        User createdUser = userRepository.save(new User("testUser", "testPassword", 1));

        /* Create score in the database */
        Score createdScore = scoreRepository.save(new Score(createdUser,100, Date.valueOf(LocalDate.now())));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();


        /* delete created user and score */
        userRepository.deleteById(createdUser.getId());

        /* check if status is 200 - OK */
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        List<LinkedHashMap> scores = super.mapFromJson(content,List.class);

        List<ScoreEntry> entries = new ArrayList<>();

        for(LinkedHashMap score: scores)
        {

            entries.add(new ScoreEntry() {
                @Override
                public String getUsername() {
                    return score.get("username").toString();
                }

                @Override
                public Integer getScore() {
                    return (Integer) score.get("score");
                }

                @Override
                public java.util.Date getDate() {
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
                    try{
                        return parser.parse(score.get("date").toString());
                    }catch(ParseException ex)
                    {
                        return null;
                    }

                }
            });
        }

        /* check if list has actually any score */
        assertTrue(!entries.isEmpty());

        /* check returned list has score that we created */
        boolean contains = false;

        for (ScoreEntry entry : entries) {

           if (createdScore.getUser().getUsername().equals(entry.getUsername())
                    && (createdScore.getScore() == entry.getScore())
                    && (entry.getDate() != null)) {
                contains = true;
            }
        }
        /* assert there is a score that we created */
        assertTrue(contains);

    }
    @Test
    public void test_createScore_withNotFound() throws Exception{
        String uri = "/scores/";

        //create user
        User createdUser = userRepository.save(new User("testUser","testPassword",1));

        userRepository.deleteById(createdUser.getId());

        String inputJson = "{ \"score\" : 100 }";

        // get rest response
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        //assert status is 404 - OK
        assertEquals(404,status);

    }

    @Test
    public void test_createScore_withOk() throws Exception{
        String uri = "/scores/";

        //create user
        User createdUser = userRepository.save(new User("testUser","testPassword",1));

        // convert give score 100 and convert to json string
        String inputJson = "{ \"score\": 100 }";

        //size of list before insertion
        int oldSize = scoreRepository.findAllByUserId(createdUser.getId()).size();

        // get rest response
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();



        int status = mvcResult.getResponse().getStatus();
        //assert status is 200 - OK
        assertEquals(200,status);

        //size of list after insertion
        int newSize = scoreRepository.findAllByUserId(createdUser.getId()).size();

        // assert exactly one score is created on createdUser
        assertEquals(oldSize,newSize - 1);

        //delete test user (so that test score is deleted also)
        userRepository.deleteById(createdUser.getId());

    }

    @Test
    public void test_deleteScore_withOk() throws Exception{
        String uri = "/scores/";


        User createdUser = userRepository.save(new User("testUser","testPassword",1));

        Score createdScore = scoreRepository.save(new Score(createdUser, 1, Date.valueOf(LocalDate.now())));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(uri + createdScore.getId().toString()))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        // Assert status is 404 - NOT FOUND
        assertEquals(200, status);
        assertEquals(scoreRepository.findById(createdScore.getId()), Optional.empty());

        userRepository.deleteById(createdUser.getId());


    }


    @Test
    public void test_deleteScore_withNotFound() throws Exception{
        String uri = "/scores/";


        User createdUser = userRepository.save(new User("testUser","testPassword",1));

        Score createdScore = scoreRepository.save(new Score(createdUser, 1, Date.valueOf(LocalDate.now())));

        //delete score to test if response is not found
        scoreRepository.deleteById(createdScore.getId());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(uri + createdScore.getId().toString()))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        // Assert status is 404 - NOT FOUND
        assertEquals(404, status);

        userRepository.deleteById(createdUser.getId());


    }
}
