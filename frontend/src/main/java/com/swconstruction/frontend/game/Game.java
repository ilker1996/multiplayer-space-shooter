package com.swconstruction.frontend.game;

import com.swconstruction.frontend.Main;
import com.swconstruction.frontend.apiutils.RestConsumer;
import com.swconstruction.frontend.customview.MessageScreen;
import com.swconstruction.frontend.mobs.Monster;
import com.swconstruction.frontend.mobs.Player;
import com.swconstruction.frontend.multiplayer.Connector;
import com.swconstruction.frontend.sidecomponents.HealthBar;
import com.swconstruction.frontend.sidecomponents.ScoreBar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

import static com.swconstruction.frontend.Main.*;

public class Game extends Pane {

    // Health and score indicator during the game
    protected HealthBar healthBar;
    protected ScoreBar scoreBar;

    // Monster list and monster bullets list
    protected List<Monster> monsters;
    protected List<Circle> monsterBullets;

    // Player list and player bullets list
    protected Player player;
    protected List<Circle> playerBullets;

    // Animations of the game
    protected Timeline aniPlayerBulletCreate;
    protected Timeline aniPlayerBulletUpdate;
    protected Timeline aniMonsterBulletCreate;
    protected Timeline aniMonsterBulletUpdate;


    protected Map<String,Integer> settings;


    public Game(int level)
    {
        // Construct Pane
        super();
        this.getStyleClass().add("game-screen");

        // Load game settings
        this.settings = GameSettings.getSettings(level);


        this.healthBar = new HealthBar(this.settings.get("playerHealth"));
        this.scoreBar = new ScoreBar(Main.score);

        // Position health bar on the up-left accordingly
        this.healthBar.layoutXProperty().bind(this.widthProperty().divide(60));
        this.healthBar.layoutYProperty().bind(this.heightProperty().divide(60));
        // Position score bar on the up-left accordingly
        this.scoreBar.layoutXProperty().bind(this.widthProperty().multiply(50).divide(60));
        this.scoreBar.layoutYProperty().bind(this.heightProperty().divide(60));

        // Resize health bar accordingly
        this.healthBar.minHeightProperty().bind(this.heightProperty().divide(40));
        this.healthBar.minWidthProperty().bind(this.widthProperty().divide(15));
        this.healthBar.maxHeightProperty().bind(this.heightProperty().divide(40));
        this.healthBar.maxWidthProperty().bind(this.widthProperty().divide(15));

        // Resize score bar accordingly
        this.scoreBar.minHeightProperty().bind(this.heightProperty().divide(40));
        this.scoreBar.minWidthProperty().bind(this.widthProperty().divide(10));
        this.scoreBar.maxHeightProperty().bind(this.heightProperty().divide(40));
        this.scoreBar.maxWidthProperty().bind(this.widthProperty().divide(10));


        // Animations of the game
        this.aniMonsterBulletCreate = new Timeline();
        this.aniMonsterBulletUpdate = new Timeline();
        this.aniPlayerBulletCreate = new Timeline();
        this.aniPlayerBulletUpdate = new Timeline();

        // Initialize monster list and player
        this.addMonsters( settings.get("monsterCount") , settings.get("monsterHealth"));
        this.player = new Player(settings.get("playerHealth"));

        // Bind width and height of the player
        this.player.widthProperty().bind(this.widthProperty().divide(40));
        // Bind height to its width to be square
        this.player.heightProperty().bind(this.player.widthProperty());

        // Don't bind x location of player
        // Since it will move during the the game
        this.player.yProperty().bind(this.heightProperty().multiply(1 - GameSettings.getOffsetRate()));
        this.player.setX(mainScene.getWidth() / 2);

        // Add to the pane
        this.getChildren().add(this.player);
        this.getChildren().add(this.healthBar);
        this.getChildren().add(this.scoreBar);

        this.playerBullets = new ArrayList<>();
        this.monsterBullets = new ArrayList<>();

        // Set keyEvent handlers
        this.registerEventHandlers();

        // Set player shooting animations
        this.updatePlayerBulletAnimation();
        this.createPlayerBulletAnimation();

        // Set monster shooting animations
        this.updateMonsterBulletAnimation();
        this.createMonsterBulletAnimation();

    }

    private void addMonsters(int monsterCount, int monsterHealth)
    {

        this.monsters = new ArrayList<>();
        // Add monster with their given health
        for(int i = 0  ; i < monsterCount ; i++)
        {

            // Order of the monster row-wise and column-wise
            int xStep  = i % GameSettings.getMonsterNumPerRow();
            int yStep = (i - xStep) / GameSettings.getMonsterNumPerRow();

            // Create new monster
            Monster monster = new Monster(monsterHealth);


            // Bind monster location to screen properties
            SimpleDoubleProperty hGap = new SimpleDoubleProperty();
            SimpleDoubleProperty vGap = new SimpleDoubleProperty();
            SimpleDoubleProperty offsetX = new SimpleDoubleProperty();
            SimpleDoubleProperty offsetY = new SimpleDoubleProperty();

            hGap.bind(this.widthProperty().multiply(GameSettings.gethGapRate()));
            vGap.bind(this.heightProperty().multiply(GameSettings.getvGapRate()));
            offsetX.bind(this.widthProperty().multiply(GameSettings.getOffsetRate()));
            offsetY.bind(this.heightProperty().multiply(GameSettings.getOffsetRate()));



            // Bind monster location to pane width and height

            monster.widthProperty().bind((this.widthProperty()
                    .subtract(offsetX.multiply(2))
                    .subtract(hGap.multiply(GameSettings.getMonsterNumPerRow() - 1)))
                    .divide(GameSettings.getMonsterNumPerRow()));
            // Set height equal to width
            monster.heightProperty().bind(monster.widthProperty());

            //monster.setX(GameSettings.getOffsetRate() * paneWidth + ((GameSettings.gethGapRate() * paneWidth + monster.getWidth()) * xStep));
            monster.xProperty().bind(offsetX
                    .add(hGap.add(monster.widthProperty()).multiply(xStep)));
            //monster.setY(GameSettings.getOffsetRate() * paneHeight + ((GameSettings.getvGapRate() * paneHeight + monster.getHeight()) * yStep));
            monster.yProperty().bind(offsetY
                    .add(vGap.add(monster.heightProperty()).multiply(yStep)));

            // Add to monster list
            this.monsters.add(monster);

            // Add to pane
            this.getChildren().add(monster);
        }
    }

    // Event handlers for movement of the player
    protected void registerEventHandlers()
    {
        // Left and right pressed sets specific speed to the player in the corresponding direction
        this.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode())
            {
                case LEFT:
                    if(this.player.getX() > 0)
                        this.player.goLeft();
                    break;
                case RIGHT:
                    if(this.player.getX() < this.getWidth() - this.player.getWidth())
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
                    this.player.stop();
                    break;
            }
        });
    }


    // Bullets are constructed in player's location
    private void createPlayerBulletAnimation()
    {

        // Shoot a bullet from player position
        EventHandler<ActionEvent> createBullet = actionEvent -> {

            // Start from up of the player
            double startingX = this.player.getX() + this.player.getWidth() / 2;
            double startingY = this.player.getY() - GameSettings.getBulletRadius();

            Circle bullet = new Circle(startingX,startingY,GameSettings.getBulletRadius());
            bullet.setStroke(Color.DARKGRAY);
            bullet.setFill(Color.AQUA);
            bullet.setStrokeWidth(1);

            this.playerBullets.add(bullet);
            this.getChildren().add(bullet);
        };


        // Create new bullet every 300 ms
        this.aniPlayerBulletCreate.getKeyFrames().add(new KeyFrame(Duration.millis(300),createBullet));
        this.aniPlayerBulletCreate.setCycleCount(Timeline.INDEFINITE);
        this.aniPlayerBulletCreate.play();
    }

    // Move player's bullets
    private void updatePlayerBulletAnimation()
    {
        // Move bullets up
        EventHandler<ActionEvent> updateBullets = actionEvent -> {
            for (Iterator<Circle> iterator = this.playerBullets.iterator();iterator.hasNext();) {

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


                    // Remove monster if its health = 0
                    if(monsterHit.decHealth() == 0 )
                    {
                        // Remove from list
                        this.monsters.remove(monsterHit);
                        // Remove from pane
                        this.getChildren().remove(monsterHit);

                        // Increase the score of player
                        Main.score += this.settings.get("scoreMultiplier");
                        this.scoreBar.incScore(this.settings.get("scoreMultiplier"));

                        // If there is no monster left
                        // Pass the next level
                        if(this.monsters.isEmpty())
                        {
                            this.goNextLevel();
                        }
                    }


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

        // Update bullet's location every 10 ms
        this.aniPlayerBulletUpdate.getKeyFrames().add(new KeyFrame(Duration.millis(10),updateBullets));
        this.aniPlayerBulletUpdate.setCycleCount(Timeline.INDEFINITE);
        this.aniPlayerBulletUpdate.play();
    }

    // Bullets are constructed in monsters' locations
    private void createMonsterBulletAnimation() {

        // Shoot a bullet from the monster's position
        EventHandler<ActionEvent> createBullet = actionEvent -> {
            Random generator = new Random();
            for (Monster monster : this.monsters) {
                // Randomly choose to create or not
                // This modifies creating speed of monster bullets
                if(generator.nextInt(100) < this.settings.get("monsterBulletLuck")) {

                    // Create at monster location
                    double startingX = monster.getX() + monster.getWidth() / 2;
                    double startingY = monster.getY() + monster.getHeight() + GameSettings.getBulletRadius();

                    Circle bullet = new Circle(startingX, startingY, GameSettings.getBulletRadius());
                    bullet.setStroke(Color.DARKGRAY);
                    bullet.setFill(Color.AQUA);
                    bullet.setStrokeWidth(1);

                    // Add to monster bullets list
                    this.monsterBullets.add(bullet);
                    this.getChildren().add(bullet);
                }
            }
        };


        // Create a bullet every 500 ms
        this.aniMonsterBulletCreate.getKeyFrames().add(new KeyFrame(Duration.millis(500), createBullet));
        this.aniMonsterBulletCreate.setCycleCount(Timeline.INDEFINITE);
        this.aniMonsterBulletCreate.play();
    }

    // Moves monster's bullets
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
                if (isHit(bullet , this.player) ) {

                    // Decrease the health bar
                    this.healthBar.decreaseHealth();

                    // End game if player's health is 0
                    if(this.player.decHealth() == 0 )
                    {
                        this.endGame();

                    }

                    // Remove bullet from list
                    iterator.remove();

                    //Remove from pane
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

    protected boolean isOut(Circle bullet)
    {
        return bullet.getCenterY() - bullet.getRadius() < 0
                || bullet.getCenterY() + bullet.getRadius() > this.heightProperty().doubleValue();
    }

    // Which monster bullet hit
    // If none, returns null
    protected Monster isHit(Circle bullet)
    {
        for(Monster monster : this.monsters)
        {
            if(this.isHit(bullet , monster))
            {
                return monster;
            }
        }
        return null;
    }

    // Bullet hit the target or not
    protected boolean isHit(Circle bullet , Rectangle target)
    {
        // Make bounding box
        double left = target.getX();
        double right = target.getX() + target.getWidth();
        double bottom = target.getY() + target.getHeight();
        double top = target.getY();

        return bullet.getCenterX() + bullet.getRadius() >= left
                && bullet.getCenterX() - bullet.getRadius() <= right
                && bullet.getCenterY() + bullet.getRadius() >= top
                && bullet.getCenterY() - bullet.getRadius() <= bottom;
    }
    // Stop all the animations (i.e. TimeLine objects)
    // For peaceful exiting

    protected void clearAnimation()
    {
        this.aniMonsterBulletUpdate.stop();
        this.aniMonsterBulletUpdate.getKeyFrames().clear();
        this.aniMonsterBulletCreate.stop();
        this.aniPlayerBulletCreate.getKeyFrames().clear();
        this.aniPlayerBulletUpdate.stop();
        this.aniPlayerBulletUpdate.getKeyFrames().clear();
        this.aniPlayerBulletCreate.stop();
        this.aniPlayerBulletCreate.getKeyFrames().clear();
    }

    protected void endGame()
    {
        this.clearAnimation();

        // Add score to leader board
        int currentScore = Main.score;

        RestConsumer consumer = new RestConsumer();

        consumer.addScore(currentScore);


        // game over text
        String gameOverText = "Game Over \n"
                + "Your Score : "
                + currentScore;


        // Message screen for game over
        MessageScreen gameOver = new MessageScreen(gameOverText , "Menu","LeaderBoard");

        // Link game over pane left button to game lobby
        gameOver.setLeftBtnLink(Main.gameLobby);
        // Link game over pane right button to leader board lobby
        gameOver.setRightBtnLink(Main.leaderBoardLobby);

        mainScene.setRoot(gameOver);
        Main.score = 0;
    }

    protected void goNextLevel()
    {
        this.clearAnimation();

        int nextLevel = this.settings.get("level") + 1;

        // If next level is multi player
        if(nextLevel == GameSettings.getMaxLevel())
        {
            // Go to the connection scene
            mainScene.setRoot(new Connector());
        }

        // If this is not the last level
        else if(GameSettings.isValidLevel(nextLevel))
        {
            Game next = new Game(nextLevel);
            mainScene.setRoot(next);

            // Request focus to event handlers work
            next.requestFocus();
            next.setFocusTraversable(true);
        }
        // If this is last level, finish
        else
        {

            // Add score to leader board
            int currentScore = Main.score;
            RestConsumer consumer = new RestConsumer();

            consumer.addScore(currentScore);

            // Text to be prompted
            String gameWinText = "You Won \n"
                    + "Your Score : \n"
                    + currentScore;


            MessageScreen gameWon = new MessageScreen(gameWinText , "Menu","LeaderBoard");

            // Link game over pane left button to game lobby
            gameWon.setLeftBtnLink(Main.gameLobby);
            // Link game over pane right button to leader board lobby
            gameWon.setRightBtnLink(Main.leaderBoardLobby);

            mainScene.setRoot(gameWon);
            Main.score = 0;
        }

    }

}
