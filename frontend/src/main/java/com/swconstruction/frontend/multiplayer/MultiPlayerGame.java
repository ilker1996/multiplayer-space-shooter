package com.swconstruction.frontend.multiplayer;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.swconstruction.frontend.Main;
import com.swconstruction.frontend.apiutils.RestConsumer;
import com.swconstruction.frontend.customview.MessageScreen;
import com.swconstruction.frontend.game.Game;
import com.swconstruction.frontend.game.GameSettings;
import com.swconstruction.frontend.mobs.Monster;
import com.swconstruction.frontend.mobs.Player;
import com.swconstruction.frontend.sidecomponents.Direction;
import com.swconstruction.frontend.sidecomponents.HitState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MultiPlayerGame extends Game {

    // Opponent player and its bullet list
    private Player opponentPlayer;
    private List<Circle> opponentPlayerBullets;

    // Animations of opponent player
    private Timeline opponentBulletCreate;
    private Timeline opponentBulletUpdate;
    private Timeline bossMotion;

    //  Client socket for sending motion commands of the local player
    private Client client;
    // Server socket for receiving opponent's motion commands
    private Server server;


    public MultiPlayerGame(OnlinePlayer opponent) {

        super(4);

        // Add css property
        this.getStyleClass().add("game-screen");


        this.opponentPlayer = new Player(this.settings.get("playerHealth"));
        // Set opponent's color blue
        this.opponentPlayer.setFill(Color.BLUE);
        this.getChildren().add(this.opponentPlayer);


        this.opponentPlayerBullets = new ArrayList<>();
        // Reposition the players and the boss
        this.positionMobs();

        // Create and update opponent player's bullet
        this.setOpponentAnimations();
        // Make boss move in every animation tick
        this.setBossMotion();

        // Start opponent  thread including server socket
        this.initializeServer(opponent);
        // Start client socket
        this.initializeClient(opponent);
    }
    private void initializeServer(OnlinePlayer opponent) {
        this.server = new Server();
        Kryo kryo = this.server.getKryo();
        // Register classes to be able to send and receive easily
        kryo.register(Direction.class);
        kryo.register(HitState.class);

        // Listen coming opponent motion commands
        this.server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                // If motion command
                if(object instanceof  Direction)
                {
                    Direction motionCommand = (Direction) object;
                    if (motionCommand ==  Direction.LEFT) {
                        // Go left
                        opponentPlayer.goLeft();
                    } else if (motionCommand == Direction.RIGHT) {
                        // Go right
                        opponentPlayer.goRight();
                    } else if(motionCommand == Direction.STOP) {
                        // Stop
                        opponentPlayer.stop();

                    }
                }
                // If the message stating that one of the player is won
                else if(object instanceof HitState)
                {
                    HitState state = (HitState) object;
                    // If opponent get hit
                    if(state == HitState.HIT)
                    {
                        // If opponent player health is 0
                        if(opponentPlayer.decHealth() == 0)
                        {
                            // You won
                            // Increase the score of player
                            Main.score += settings.get("scoreMultiplier");
                            scoreBar.incScore(settings.get("scoreMultiplier"));
                            // End the game
                            goNextLevel();
                        }

                    }
                }

            }

            public void disconnected(Connection connection)
            {
                System.err.println("Server is disconnected");
                // If opponent is disconnected during the battle
                handleOpponentDisconnection();
            }
        });

        try {
            // Listen packets from opponent's client socket
            this.server.bind(opponent.getClientPort());
        } catch (IOException e) {
            System.err.println("Server port number is wrong!");
        }
        // Start the server
        this.server.start();

    }


    private void initializeClient(OnlinePlayer opponent){
        this.client = new Client();

        Kryo kryo = this.client.getKryo();

        // Register classes to be able to send and receive easily
        kryo.register(Direction.class);
        kryo.register(HitState.class);

        // Start the client
        this.client.start();
        try {
            // Get blocked for 5 second max
            this.client.connect(5000,
                    opponent.getPublicAddress(),
                    opponent.getServerPort());
        } catch (IOException e) {
            System.err.println("Opponent is disconnected");

            // If opponent is disconnected while waiting for opponent
            this.handleOpponentDisconnection();

        }

    }
    @Override
    protected void registerEventHandlers()
    {

        // Left and right pressed sets specific speed to the player in the corresponding direction
        this.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode())
            {
                case LEFT:
                    if(this.player.getX() > 0)
                        // Send that player will move to the left
                        this.client.sendTCP(Direction.LEFT);
                        this.player.goLeft();
                    break;
                case RIGHT:
                    if(this.player.getX() < this.getWidth() - this.player.getWidth())
                        // Send that player will move to the right
                        this.client.sendTCP(Direction.RIGHT);
                        this.player.goRight();
                    break;
            }
        });

        // If key released set player's speed to 0
        this.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode())
            {
                case LEFT:
                case RIGHT:
                    // Send stop command to the opponent player
                    this.client.sendTCP(Direction.STOP);
                    this.player.stop();
                    break;
            }
        });
    }
    private void positionMobs()
    {
        // There is only 1 boss
        Monster boss = this.monsters.get(0);

        boss.widthProperty().bind(this.widthProperty().divide(15));
        boss.heightProperty().bind(boss.widthProperty());

        // Boss will move so unbind its early binding x position
        boss.xProperty().unbind();
        boss.setX(Main.mainScene.getWidth() / 2 - boss.getWidth());

        this.opponentPlayer.widthProperty().bind(this.player.widthProperty());
        this.opponentPlayer.heightProperty().bind(this.player.heightProperty());

        this.opponentPlayer.yProperty().bind(this.player.yProperty());

        // Players are starting at the middle of the screen
        this.player.setX((0.5) * Main.mainScene.getWidth());
        this.opponentPlayer.setX((0.5) * Main.mainScene.getWidth());



    }

    // Override monster  bullets animation
    @Override
    protected void updateMonsterBulletAnimation()
    {
        // Move bullets down
        EventHandler<ActionEvent> updateBullets = actionEvent -> {
            for (Iterator<Circle> iterator = this.monsterBullets.iterator();iterator.hasNext();) {

                Circle bullet = iterator.next();

                // Move bullet down
                double newY = bullet.getCenterY() + this.settings.get("monsterBulletSpeed");
                bullet.setCenterY(newY);

                // If bullet hits the player
                if (isHit(bullet , this.player))
                {

                    // Remove bullet from list
                    iterator.remove();

                    //Remove from pane
                    getChildren().remove(bullet);

                    // Decrease the health bar
                    this.healthBar.decreaseHealth();

                    // Send other player that you hit
                    this.client.sendTCP(HitState.HIT);

                    // End game if player's health is 0
                    if(this.player.decHealth() == 0 )
                    {
                        this.endGame();
                    }

                }
                else if(isHit(bullet , this.opponentPlayer))
                {
                    // Remove bullet from list
                    iterator.remove();
                    // Remove from pane
                    getChildren().remove(bullet);
                }
                // If out of bounds of the screen, remove the bullet
                else if (isOut(bullet)) {
                    // Remove from list
                    iterator.remove();
                    //Remove from pane
                    getChildren().remove(bullet);
                }

            }
        };

        // Update monster bullet location every 100 ms
        this.aniMonsterBulletUpdate.getKeyFrames().add(new KeyFrame(Duration.millis(100),updateBullets));
        this.aniMonsterBulletUpdate.setCycleCount(Timeline.INDEFINITE);
        this.aniMonsterBulletUpdate.play();
    }

    private void setOpponentAnimations()
    {
        this.opponentBulletCreate = new Timeline();
        this.opponentBulletUpdate = new Timeline();

        this.opponentCreateBullets();
        this.opponentUpdateBullets();


    }
    // Animation for creating the opponent bullets
    private void opponentCreateBullets()
    {
        // Shoot a bullet from player position
        EventHandler<ActionEvent> createBullet = actionEvent -> {

            // Start from up of the player
            double startingX = this.opponentPlayer.getX() + this.opponentPlayer.getWidth() / 2;
            double startingY = this.opponentPlayer.getY() - GameSettings.getBulletRadius();

            Circle bullet = new Circle(startingX,startingY,GameSettings.getBulletRadius());
            bullet.setStroke(Color.DARKGRAY);
            bullet.setFill(Color.AQUA);
            bullet.setStrokeWidth(1);

            this.opponentPlayerBullets.add(bullet);
            this.getChildren().add(bullet);
        };


        // Create new bullet every 300 ms
        this.opponentBulletCreate.getKeyFrames().add(new KeyFrame(Duration.millis(300),createBullet));
        this.opponentBulletCreate.setCycleCount(Timeline.INDEFINITE);
        this.opponentBulletCreate.play();
    }
    // Animation for updating opponent bullets
    private void opponentUpdateBullets()
    {
        // Move bullets up
        EventHandler<ActionEvent> updateBullets = actionEvent -> {
            for (Iterator<Circle> iterator = this.opponentPlayerBullets.iterator(); iterator.hasNext();) {

                Circle bullet = iterator.next();

                // Move bullet up
                double newY = bullet.getCenterY() - this.settings.get("playerBulletSpeed");
                bullet.setCenterY(newY);


                Monster monsterHit = isHit(bullet);

                // If bullet hits the monster :
                // Decrease health of the monster
                // Extinguish bullet
                if (monsterHit != null) {

                    // Remove bullet from list
                    iterator.remove();

                    //Remove bullet from pane
                    getChildren().remove(bullet);
                    // If boss died
                    if(monsterHit.decHealth() == 0)
                    {
                        this.monsters.remove(monsterHit);
                        // This means opponent wins
                        this.endGame();
                    }

                }
                // If out of bounds of the screen, remove the bullet
                else if (isOut(bullet)) {

                    // Remove from list
                    iterator.remove();
                    // Remove from pane
                    getChildren().remove(bullet);
                }

            }
        };

        // Update bullet's location every 10 ms
        this.opponentBulletUpdate.getKeyFrames().add(new KeyFrame(Duration.millis(10),updateBullets));
        this.opponentBulletUpdate.setCycleCount(Timeline.INDEFINITE);
        this.opponentBulletUpdate.play();
    }
    private void setBossMotion()
    {
        this.bossMotion = new Timeline();

        EventHandler<ActionEvent> move = actionEvent -> {
           Monster boss;
           try {
               boss = this.monsters.get(0);
           }// If boss died
           catch (NullPointerException | IndexOutOfBoundsException ex)
           {
               this.bossMotion.stop();
               return;
           }

           boss.setX(boss.getX() + this.settings.get("monsterSpeed"));
           if(boss.getX() > Main.mainScene.getWidth())
           {
               boss.setX(boss.getWidth());
           }
           else if(boss.getX() < 0)
           {
               boss.setX(Main.mainScene.getWidth() - boss.getWidth());
           }

        };


        // Move boss every 50 ms
        this.bossMotion.getKeyFrames().add(new KeyFrame(Duration.millis(50),move));
        this.bossMotion.setCycleCount(Timeline.INDEFINITE);
        this.bossMotion.play();
    }


    // Override animation cleaning function to be able to clear additional animations
    @Override
    protected void clearAnimation()
    {
        // Clean additional animations and sockets etc.
        this.cleanExtras();

        this.aniMonsterBulletUpdate.stop();
        this.aniMonsterBulletUpdate.getKeyFrames().clear();

        this.aniMonsterBulletCreate.stop();
        this.aniPlayerBulletCreate.getKeyFrames().clear();

        this.aniPlayerBulletUpdate.stop();
        this.aniPlayerBulletUpdate.getKeyFrames().clear();

        this.aniPlayerBulletCreate.stop();
        this.aniPlayerBulletCreate.getKeyFrames().clear();


    }

    // Cleaning of additional properties of the multi player version of the game
    private void cleanExtras()
    {
        this.opponentBulletCreate.stop();
        this.opponentBulletCreate.getKeyFrames().clear();

        this.opponentBulletUpdate.stop();
        this.opponentBulletUpdate.getKeyFrames().clear();

        this.bossMotion.stop();
        this.bossMotion.getKeyFrames().clear();

        // Close sockets
        this.server.stop();
        this.server.close();

        this.client.stop();
        this.client.close();
    }

    // If opponent disconnected during the game or while waiting for opponent
    private void handleOpponentDisconnection()
    {
        // If players and boss is alive
        if(opponentPlayer.getHealth() > 0 && player.getHealth() > 0
                && !this.monsters.isEmpty() && this.monsters.get(0).getHealth() > 0)
        {
            // Clear animations and sockets
            this.clearAnimation();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Main.score += this.settings.get("scoreMultiplier");
            int currentScore = Main.score;

            RestConsumer consumer = new RestConsumer();

            // Add score to the leader board
            consumer.addScore(currentScore);

            // Text to be prompted
            String gameWinText = "Opponent is disconnected \n"
                    + "You Won \n"
                    + "Your Score : \n"
                    + currentScore;


            MessageScreen gameWon = new MessageScreen(gameWinText , "Menu","LeaderBoard");

            // Link game over pane left button to game lobby
            gameWon.setLeftBtnLink(Main.gameLobby);
            // Link game over pane right button to leader board lobby
            gameWon.setRightBtnLink(Main.leaderBoardLobby);


            Platform.runLater(() -> Main.mainScene.setRoot(gameWon));

            Main.score = 0;
        }
    }
}
