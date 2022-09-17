package com.swconstruction.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.swconstruction.backend.User.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;


public class UserControllerTest extends BackendApplicationTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }


    public User getRandomUser() {
        User user = new User();

        // randomize username
        StringBuffer rndUsername = new StringBuffer(UUID.randomUUID().toString());
        rndUsername.setLength(new Random().nextInt(rndUsername.length() + 1));

        // randomize password
        StringBuffer rndPassword = new StringBuffer(UUID.randomUUID().toString());

        // randomize more by slicing it with random number
        rndPassword.setLength(new Random().nextInt(rndPassword.length() + 1));

        //set user object
        user.setUsername(rndUsername.toString());
        user.setPassword(rndPassword.toString());

        return user;
    }

    @Test
    public void test_getUsersList_withOk() throws Exception {
        String uri = "/users";

        // Create user in the database
        User createdUser = userRepository.save(new User("testUser", "testPassword", 1));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        // check if status is 200 - OK
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        User[] userList = super.mapFromJson(content, User[].class);

        // check if list has actually any user
        assertTrue(userList.length > 0);


        // check returned list has user that we created
        boolean contains = false;
        for (int i = 0; i < userList.length; i++) {
            if (createdUser.getUsername().equals(userList[i].getUsername())
                    && createdUser.getPassword().equals(userList[i].getPassword())) {
                contains = true;
            }
        }

        // assert there is a user that we created
        assertTrue(contains);
        //delete created user
        userRepository.deleteById(createdUser.getId());
    }

    @Test
    public void test_getUserById_withNotFound() throws Exception{
        String uri = "/users/";

        // Create user in the database
        User createdUser = userRepository.save(new User("testUser", passwordEncoder.encode("testPassword"), 1));

        //delete created user
        userRepository.deleteById(createdUser.getId());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + createdUser.getId().toString() )
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        // check if status is 200 - OK
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void test_getUserById_withOk() throws Exception{
        String uri = "/users/";

        // Create user in the database
        User createdUser = userRepository.save(new User("testUser", passwordEncoder.encode("testPassword"), 1));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + createdUser.getId().toString() )
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        // check if status is 200 - OK
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        User user = super.mapFromJson(content, User.class);

        // check username and password is same
        assertEquals("testUser",user.getUsername());
        assertTrue(passwordEncoder.matches("testPassword",user.getPassword()));

        //delete created user
        userRepository.deleteById(createdUser.getId());
    }
    @Test
    public void test_createUser_withCreatedAndBadRequest() throws Exception {
        String uri = "/sign_up";

        // 100 randomized create user test
        for (int i = 0; i < 100; i++) {

            User user = getRandomUser();

            // convert user object to json
            String inputJson = super.mapToJson(user);

            // get rest response
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(inputJson)).andReturn();


            int status = mvcResult.getResponse().getStatus();

            if (user == null || user.getUsername() == null || user.getPassword() == null ||
                    user.getPassword().isEmpty() || user.getUsername().isEmpty() || user.getUsername().length() > 12) {
                // check if status 400 - BAD REQUEST
                assertEquals(400, status);
            } else {
                // Test if status is 201 CREATED
                assertEquals(201, status);

                Optional<User> createdUser = userRepository.findByUsername(user.getUsername());
                // Test if our user is exist in repository after response 201 - CREATED
                assertTrue(createdUser.isPresent());
                assertTrue(createdUser.get().getUsername().equals(user.getUsername()));
                assertTrue(passwordEncoder.matches(user.getPassword(), createdUser.get().getPassword()));

                //delete created user
                userRepository.deleteById(createdUser.get().getId());
            }

        }
    }

    @Test
    public void test_createUser_withConflict() throws Exception {
        String uri = "/sign_up";

        User user = new User("testUser", "testPassword", 1);
        User createdUser = userRepository.save(user);


        // convert user object to json
        String inputJson = super.mapToJson(user);

        // get rest response
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();


        int status = mvcResult.getResponse().getStatus();

        // Should be conflict since we created same user before
        assertEquals(409, status);

        //delete test user
        userRepository.deleteById(createdUser.getId());

    }

    @Test
    public void test_login_withOk() throws Exception {
        String uri = "/login";

        User user = new User("testUser",passwordEncoder.encode("testPassword"),1);

        User createdUser = userRepository.save(user);

        user.setPassword("testPassword");

        String inputJson = super.mapToJson(user);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        userRepository.deleteById(createdUser.getId());

        int status = mvcResult.getResponse().getStatus();

        // assert status 200 - OK
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();

        User responseUser = super.mapFromJson(content , User.class);

        assertEquals(responseUser.getId(),createdUser.getId());
        assertEquals(responseUser.getUsername(),createdUser.getUsername());
        // Password should be returned null
        assertNull(responseUser.getPassword());
        assertEquals(responseUser.getLevel(),createdUser.getLevel());




    }

    @Test
    public void test_login_withNotFound() throws Exception {
        String uri = "/login";

        User user = new User("testUser",passwordEncoder.encode("testPassword"),1);

        //save user
        User createdUser = userRepository.save(user);

        user.setPassword("testPassword");

        //delete user to ensure to get 404 - not found error
        userRepository.deleteById(createdUser.getId());

        // reset auto increment
        userRepository.resetUsers();

        String inputJson = super.mapToJson(user);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        // assert status 404 - NotFound
        assertEquals(404, status);

        String content = mvcResult.getResponse().getContentAsString();

        // Make sure that returning custom error is given correctly
        assertTrue(content.contains("Incorrect username or password!"));

    }

    @Test
    public void test_deleteUser_withOk() throws Exception {
        String uri = "/users/";

        User user = new User("testUser", "testPassword", 1);
        User createdUser = userRepository.save(user);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(uri + createdUser.getId().toString()))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        // Assert status is 200 - OK
        assertEquals(200, status);
        assertEquals(userRepository.findById(createdUser.getId()), Optional.empty());

    }

    @Test
    public void test_deleteUser_withNotFound() throws Exception{
        String uri = "/users/";

        User user = new User("testUser", "testPassword", 1);
        User createdUser = userRepository.save(user);

        //delete user to test if not found arrive
        userRepository.deleteById(createdUser.getId());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(uri + createdUser.getId().toString()))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        // Assert status is 404 - NOT FOUND
        assertEquals(404, status);
        assertEquals(userRepository.findById(createdUser.getId()),Optional.empty());


    }
}