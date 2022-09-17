package com.swconstruction.frontend.sidecomponents;


import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ScoreBar extends HBox {

    // Score
    private Integer score;
    // Text that indicates player score
    private Text scoreText;

    // Initial score for scorebar
    // It is not 0 if in level2 or level3
    public ScoreBar(Integer initialScore)
    {
        super();

        this.setAlignment(Pos.CENTER_LEFT);

        this.score = initialScore;
        this.scoreText = new Text("Score : " + this.score.toString());


        scoreText.setFill(Color.RED);

        scoreText.setFont(Font.font("Arial", FontWeight.BOLD,20));

        this.getChildren().add(scoreText);
    }

    // Increment score of the player
    public  void incScore(int amount)
    {
        this.score += amount;
        this.scoreText.setText("Score : " + this.score.toString());
    }

}
