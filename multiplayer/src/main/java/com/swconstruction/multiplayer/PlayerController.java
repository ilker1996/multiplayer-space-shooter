package com.swconstruction.multiplayer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class PlayerController{


    /** First connected player **/
    private Player player1;
    /** Second and last connected player **/
    private Player player2;
    private final Lock lock = new ReentrantLock();
    /** Is other player is found condition **/
    private final Condition isMatched = lock.newCondition();

    public static final int firstPlayerPort = 10000;
    public static final int secondPlayerPort = 10001;

    public PlayerController()
    {
        player1 = null;
        player2 = null;
    }

    private String getClientIp(HttpServletRequest request)
    {
        String remoteAddr = "";

        if (request != null) {
            // It is for if client connected with proxy
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }


    @PostMapping(path="/connect")
    public Callable<Player> connectPlayer(@RequestBody Player player, HttpServletRequest request)
    {
        return new Callable<Player>() {
            @Override
            public @ResponseBody Player call(){
                if(player1 != null && player2 != null)
                {
                    player1 = player2 = null;
                }
                // If first player is not connected yet
                if(player1 == null)
                {
                    player1 = player;
                    System.out.println("Waiting for player2 ...");
                    // Set player1's request port number
                    player1.setRequestPort(request.getRemotePort());
                    // Set player1 's listening port
                    player1.setServerPort(firstPlayerPort);
                    // Set player1's IP address
                    player1.setPublicAddress(getClientIp(request));
                    // Set player1's sending port (i.e. player2's listening port)
                    player1.setClientPort(secondPlayerPort);
                    // Wait until other player arrives
                    while(player2 == null)
                    {
                        synchronized (isMatched)
                        {
                            try{
                                // Wait on the condition until other player is here
                                isMatched.wait();
                            }catch (InterruptedException ex)
                            {
                                // If interrupted (i.e. request timeout occurs)
                                player1 = null;
                                break;
                            }

                        }

                    }

                    Player tempPlayer = player2;
                    player2 = null;
                    // Return player2 's info to the player1
                    return tempPlayer;

                }
                // If first player is waiting for second
                else
                {
                    System.out.println("Player2 is arrived ...");
                    player2 = player;
                    // Set player2's request port number
                    player1.setRequestPort(request.getRemotePort());
                    // Set player2 's listening port
                    player2.setServerPort(secondPlayerPort);
                    // Set player2's IP address
                    player2.setPublicAddress(getClientIp(request));
                    // Set player2's sending port (i.e. player2's listening port)
                    player2.setClientPort(firstPlayerPort);
                    // Notify that second player is arrived
                    synchronized (isMatched)
                    {
                        // Signal the condition
                        isMatched.notify();
                    }


                    Player tempPlayer = player1;
                    player1 = null;
                    // Send info of player1 to the  player2
                    return tempPlayer;
                }
            }
        };

    }

}
