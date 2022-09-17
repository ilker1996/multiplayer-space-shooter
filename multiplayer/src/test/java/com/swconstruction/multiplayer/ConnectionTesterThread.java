package com.swconstruction.multiplayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ConnectionTesterThread implements Runnable {
    private Player requestPlayer;
    private Player expectedResponse;
    private String apiUrl;

    public ConnectionTesterThread(Player requestPlayer, Player expectedResponse, String apiUrl)
    {
        this.requestPlayer = requestPlayer;
        this.expectedResponse = expectedResponse;
        this.apiUrl = apiUrl;
    }
    @Override
    public void run() {

        String requestPlayerJson = null;
        Player response = null;
        try {
            requestPlayerJson = MultiplayerApplicationTest.mapToJson(this.requestPlayer);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }

        // Send request
        MvcResult mvcResult = null;
        try {
            // Post request
            mvcResult = MultiplayerApplicationTest.mvc.perform(MockMvcRequestBuilders.post(this.apiUrl)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(requestPlayerJson)).andReturn();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            response = MultiplayerApplicationTest.mapFromJson(mvcResult.getResponse().getContentAsString(), Player.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Check if the actual response is same with the expected response
        assertEquals(response.getServerPort(),expectedResponse.getServerPort());
        assertEquals(response.getClientPort(),expectedResponse.getClientPort());
        assertEquals(response.getUsername(),expectedResponse.getUsername());
        // Check if public address is null or not
        assertFalse(response.getPublicAddress() == null);

    }
}
