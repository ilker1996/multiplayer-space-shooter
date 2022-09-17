package com.swconstruction.frontend.mobs;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Monster extends Rectangle {

    private int health;

    public Monster(int health)
    {
        super();
        this.health = health;


        // If monster's health drops to "1" turn it to red
        if(this.health == 1)
            this.setFill(Color.RED);
        else
            this.setFill(Color.GREEN);
    }

    // Decrease the health of the monster
    public synchronized int decHealth()
    {
        this.health--;
        // If health is 1 , turn color to red
        if(this.health == 1)
        {
            this.setFill(Color.RED);
        }
        return this.health;
    }
    public synchronized int getHealth()
    {
        return this.health;
    }

}
