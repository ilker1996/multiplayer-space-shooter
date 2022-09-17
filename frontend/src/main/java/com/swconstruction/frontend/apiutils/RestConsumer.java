package com.swconstruction.frontend.apiutils;

import com.swconstruction.frontend.Main;
import com.swconstruction.frontend.multiplayer.OnlinePlayer;
import com.swconstruction.frontend.sidecomponents.LeaderBoardType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestConsumer {

    public RestConsumer()
    {
    }

    // Checks if given server address is alive
    public static boolean isServerValid(String address)
    {
        Socket s = null;
        String host;
        int port;
        try
        {
            // Crop "http:// part of the host address
            // Crop latest ":" part
            host = address.substring(0,address.lastIndexOf(":")).replace("http://","");
            // Crop after the last ":" part
            port = Integer.parseInt(address.substring(address.lastIndexOf(":") + 1));
        }
        // If couldn't parse correctly , it means address is wrong
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }


        try
        {
            s = new Socket(host, port);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            if(s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // Adding user to the database (sign-up)
    public HttpStatus addUser(User user){

        RestTemplate restTemplate = new RestTemplate();

        try{
            // Specify url for sign up
            URI uri = new URI(Main.backEndServer + "/sign_up");
            // Get the response from server
            ResponseEntity response = restTemplate.postForEntity(uri , user , ResponseEntity.class);

            // Return response status
            return response.getStatusCode();
        }
        catch (HttpClientErrorException ex)
        {
            // If any error status appears, return it
            return ex.getStatusCode();

        }
        catch(URISyntaxException ex)
        {
            // If uri is wrong
            // Shutdown
            System.out.println(Arrays.toString(ex.getStackTrace()));
            // Get back to the main game menu
            Main.mainScene.setRoot(Main.gameLobby);
        }

        return null;

    }

    // Returns user object of logged in User
    public User logUser(User user){

        RestTemplate restTemplate = new RestTemplate();

        try {

            // Specify url for login
            URI uri = new URI(Main.backEndServer + "/login");
            // Login successful
            return restTemplate.postForObject(uri,user,User.class);

        }catch (HttpClientErrorException ex) {

            // Not successful login
            return null;

        }
        catch (URISyntaxException ex)
        {
            // Wrong api url
            System.out.println(Arrays.toString(ex.getStackTrace()));
            // Get back to the main game menu
            Main.mainScene.setRoot(Main.serverForm);
        }
        return null;

    }
    // Get leaderboard list for type of the request ( weekly or all times)
    public List<Score> getLeaderBoard(LeaderBoardType type)
    {
        List<Score> leaderBoard = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        // Type converter for json objects
        ParameterizedTypeReference<List<Score>> typeRef = new ParameterizedTypeReference<List<Score>>(){};


        // If weekly leader board
        URI uri = null;
        if(type == LeaderBoardType.WEEKLY)
        {
            try {
                // Api url for weekly leaderboard
                uri = new URI(Main.backEndServer + "/scores/weekly");
            }catch(URISyntaxException ex)
            {
                // If wrong api url
                // Shutdown
                System.out.println(Arrays.toString(ex.getStackTrace()));
                // Get back to the main game menu
                Main.mainScene.setRoot(Main.gameLobby);
            }
            ResponseEntity<List<Score>>  response = restTemplate.exchange(uri , HttpMethod.GET , null ,typeRef);
            leaderBoard = response.getBody();
        }
        // If all times leader board
        else if(type == LeaderBoardType.ALL)
        {
            try
            {
                // Api url for all times leader board
                uri = new URI(Main.backEndServer + "/scores");
            }catch(URISyntaxException ex)
            {
                // If wrong api url
                // Shutdown
                System.out.println(Arrays.toString(ex.getStackTrace()));
                // Get back to the main game menu
                Main.mainScene.setRoot(Main.gameLobby);
            }
            // Get the response from the server
            ResponseEntity<List<Score>>  response = restTemplate.exchange(uri , HttpMethod.GET , null ,typeRef);
            leaderBoard = response.getBody();
        }
        // Return response body (i.e. list of scores)
        return leaderBoard;
    }

    // Add new score to the database
    public void addScore(int score)
    {
        RestTemplate restTemplate = new RestTemplate();

        // User id of the player
        String userId = Main.loggedPlayer.getUserId().toString();

        // Score to be add
        // Just put score since backend will handle the rest of it
        Score newScore = new Score(null, score , null);


        try {

            // Specify url for adding score
            URI uri = new URI(Main.backEndServer + "/scores/" + userId);
            restTemplate.postForObject(uri,newScore,Score.class);

        }catch (HttpClientErrorException | URISyntaxException ex) {

            // Adding is not successful
            System.out.println(Arrays.toString(ex.getStackTrace()));
            // Get back to the main game menu
            Main.mainScene.setRoot(Main.gameLobby);
        }

    }

    public OnlinePlayer connectOpponent()
    {
        RestTemplate restTemplate = new RestTemplate();

        try {

            // Current player's connection information
            // Will determine IP in the server
            OnlinePlayer currentPlayer = new OnlinePlayer(null,null,null,Main.loggedPlayer.getUsername());
            // Url of the matchmaker server
            URI uri = new URI(Main.matchMakerServer + "/connect");
            // Return opponent player

            return restTemplate.postForObject(uri , currentPlayer ,OnlinePlayer.class);

        } catch (URISyntaxException | HttpServerErrorException | HttpClientErrorException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));

            // If anything goes wrong return null
            return null;
        }



    }


}
