package com.swconstruction.frontend.sidecomponents;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class HealthBar extends HBox {

    private List<Rectangle> barList;

    public HealthBar(int maxHealth)
    {
        super();
        // Make text start from center-left
        this.setAlignment(Pos.CENTER_LEFT);

        // Start with "Health : "
        Text text = new Text("Health : ");
        text.setFont(Font.font("Courier New", FontWeight.BOLD,20));
        text.setFill(Color.RED);

        // Add text to HBox
        this.getChildren().add(text);

        // Add rectangle bar as player initial health
        this.addBars(maxHealth);
    }

    private void addBars(int barCount)
    {
        this.barList = new ArrayList<>();
        for(int i = 0 ; i < barCount ; i++)
        {
            // Carefully position rectangle (bar)
            // Rectangle shows 1 health
            Rectangle bar = new Rectangle();
            bar.heightProperty().bind(this.heightProperty());
            bar.widthProperty().bind(this.widthProperty().divide(barCount));
            bar.setStroke(Color.BLACK);
            bar.setStrokeWidth(1.5);
            bar.setFill(Color.RED);

            this.getChildren().add(bar);
            this.barList.add(bar);
        }

    }

    // Remove last bar from the health bar
    public void decreaseHealth()
    {
        if(this.barList != null && !this.barList.isEmpty()) {
            Rectangle lastBar = this.barList.get(this.barList.size() - 1);
            this.getChildren().remove(lastBar);
            this.barList.remove(lastBar);
        }
    }

}
