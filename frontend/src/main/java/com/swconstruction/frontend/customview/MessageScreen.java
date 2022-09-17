package com.swconstruction.frontend.customview;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static com.swconstruction.frontend.Main.mainScene;


public class MessageScreen extends BorderPane {

    protected Button leftBtn;
    protected Button rightBtn;
    protected Text screenText;

    public MessageScreen(String screenMsg, String leftBtnText, String rightBtnText)
    {
        super();
        this.leftBtn = new Button(leftBtnText);
        this.rightBtn = new Button(rightBtnText);
        this.screenText = new Text(screenMsg);

        // Modify properties of components

        this.leftBtn.getStyleClass().addAll("button");
        this.leftBtn.setPadding(new Insets(15, 15, 15, 15));
        this.leftBtn.setPrefHeight(120);
        this.leftBtn.setPrefWidth(180);

        this.rightBtn.getStyleClass().addAll("button");
        this.rightBtn.setPadding(new Insets(15, 15, 15, 15));
        this.rightBtn.setPrefHeight(120);
        this.rightBtn.setPrefWidth(180);

        // Make space between lines
        this.screenText.setLineSpacing(20);
        this.screenText.setTextAlignment(TextAlignment.CENTER);
        this.screenText.setFont(Font.font("Arial", FontWeight.BOLD,50));
        this.screenText.setFill(Color.DARKRED);

        super.getStyleClass().add("message-background");

        this.createPane();
    }

    // Constructs components of the pane ( BorderPane)
    private void  createPane()
    {
        // Horizontal box for buttons
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15,15,15,15));
        buttonBox.setSpacing(50);
        buttonBox.getChildren().addAll(this.leftBtn,this.rightBtn);

        buttonBox.prefHeightProperty().bind(super.heightProperty().divide(2));
        buttonBox.prefHeightProperty().bind(super.widthProperty().divide(2));


        // Horizontal box for message text
        HBox textBox = new HBox();
        textBox.setAlignment(Pos.CENTER);
        textBox.getChildren().add(this.screenText);

        textBox.prefHeightProperty().bind(super.heightProperty().divide(2));
        textBox.prefWidthProperty().bind(super.widthProperty().divide(2));

        super.setCenter(textBox);
        super.setBottom(buttonBox);
    }

    // Set new pane to the given scene when clicked on button on the left in the pane
    public void  setLeftBtnLink(Parent newPane)
    {
        this.leftBtn.setOnAction(e -> {
            mainScene.setRoot(newPane);

            //For event handlers to be work on new Pane
            newPane.setFocusTraversable(true);
            newPane.requestFocus();
        });


    }

    // Set new pane to the  given scene when clicked on button on the right in the pane
    public void  setRightBtnLink(Parent newPane)
    {
        this.rightBtn.setOnAction(e -> {
            mainScene.setRoot(newPane);

            //For event handlers to be work on new Pane
            newPane.setFocusTraversable(true);
            newPane.requestFocus();
        });
    }



}
