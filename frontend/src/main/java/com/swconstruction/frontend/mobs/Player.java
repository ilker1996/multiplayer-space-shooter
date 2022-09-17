package com.swconstruction.frontend.mobs;


import com.swconstruction.frontend.game.GameSettings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Player extends Rectangle {

    private int health;
    private int moveSpeed;

    public Player(int health)
    {
        super();

        this.health = health;

        // Set moving speed to 0 as default
        this.moveSpeed = 0;
        this.setFill(Color.BLUEVIOLET);

        // EventHandler for movement of the player,
        // It moves every 10 ms according to its speed
        EventHandler<ActionEvent> move = e -> {
            // If not at the right edge
            // Go right
            if(this.moveSpeed > 0 && this.getX() < ((Pane) this.getParent()).getWidth() - this.getWidth())
                this.setX(this.getX() + this.moveSpeed);
            // If not at the left edge
            // Go left
            else if(this.moveSpeed < 0 && this.getX() > 0 )
                this.setX(this.getX() + this.moveSpeed);
        };

        // Move every 10 ms
        Timeline motion = new Timeline(new KeyFrame(Duration.millis(10), move));
        motion.setCycleCount(Timeline.INDEFINITE);
        motion.play();
    }

    public void goRight()
    {
        this.moveSpeed = GameSettings.getPlayerSpeed();
    }

    public void goLeft()
    {
        this.moveSpeed = -1 * GameSettings.getPlayerSpeed();
    }

    public void stop()
    {
        this.moveSpeed = 0 ;
    }

    // Decrease the health of the player by 1
    public int decHealth()
    {
        return --this.health;

    }

    public int getHealth()
    {
        return  this.health;
    }

}
