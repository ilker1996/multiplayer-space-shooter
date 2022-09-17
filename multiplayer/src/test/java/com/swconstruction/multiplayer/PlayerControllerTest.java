package com.swconstruction.multiplayer;



import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;


public class PlayerControllerTest extends MultiplayerApplicationTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }



    @Test
    public void testMatcherService() throws JsonProcessingException {
        String url = "/connect";

        // Request objects
        Player requestPlayer1 = new Player(null , null , null,"testPlayer1");
        Player requestPlayer2 = new Player(null , null , null,"testPlayer2");

        // Expected response objects
        Player expectedResponse1 = new Player(null , PlayerController.firstPlayerPort , PlayerController.secondPlayerPort,"testPlayer1");
        Player expectedResponse2 = new Player(null , PlayerController.secondPlayerPort, PlayerController.firstPlayerPort,"testPlayer2");

        // First player's request and expected username in the response
        new Thread(new ConnectionTesterThread(requestPlayer1,expectedResponse2,url)).start();
        // Second player's request and expected username in the response
        new Thread(new ConnectionTesterThread(requestPlayer2,expectedResponse1,url)).start();





    }
}