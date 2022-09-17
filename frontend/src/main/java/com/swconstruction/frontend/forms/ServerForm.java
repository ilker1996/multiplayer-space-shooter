package com.swconstruction.frontend.forms;

import com.swconstruction.frontend.Main;
import com.swconstruction.frontend.apiutils.RestConsumer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class ServerForm extends GridPane {

    private Button submitBtn;
    private final TextField topField;
    private final TextField bottomField;
    private Label lblMessage;

    public ServerForm(String topLabel,String bottomLabel,String submitText)
    {
        super();

        // Add style class
        super.getStyleClass().add("form");

        // Add some properties
        super.setPadding(new Insets(20,20,20,20));
        super.setHgap(5);
        super.setVgap(10);

        // http://144.122.71.144:8080 - Database server
        // http://144.122.71.144:8082 - Multiplayer Server

        // Top input field and default value
        this.topField = new TextField("http://144.122.71.144:8080");

        // Bottom input field and default value
        this.bottomField = new TextField("http://144.122.71.144:8082");

        // Label for showing error messages in screen
        this.lblMessage = new Label();
        this.lblMessage.setTextFill(Color.RED);

        // Create submit and back link buttons of the form
        this.submitBtn = new Button(submitText);

        // Horizontal box for buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5 ,5,5 ,5));
        buttons.setSpacing(10);
        buttons.getChildren().add(this.submitBtn);

        // Username label
        Label lblTop = new Label(topLabel);
        lblTop.setTextFill(Color.DARKRED);

        // Password label
        Label lblBottom = new Label(bottomLabel);
        lblBottom.setTextFill(Color.DARKRED);

        // Add fields to GridPane
        this.add(lblTop,0,1);
        this.add(this.topField,0,2);
        this.add(lblBottom,0,3);
        this.add(this.bottomField,0,4);
        this.add(buttons,0,5);
        this.add(this.lblMessage,0,6);

        this.setSubmitBtn();

    }

    public void setSubmitBtn()
    {
        this.submitBtn.setOnAction(e -> {
            // Check if the addresses of the server is reachable

            boolean isTop;
            boolean isBottom;

            // Check if server addresses is alive (i.e. servers are up)
            isTop = RestConsumer.isServerValid(this.topField.getText());
            isBottom = RestConsumer.isServerValid(this.bottomField.getText());

            // If top field is invalid address
            if(!isTop)
            {
                this.lblMessage.setText(this.topField.getText() + " is not reachable!");
            }
            // If bottom field is invalid address
            else if(!isBottom)
            {
                this.lblMessage.setText(this.bottomField.getText() + " is not reachable!");
            }
            // If both addresses are valid
            else
            {
                // Get server addresses
                Main.backEndServer = this.topField.getText();
                Main.matchMakerServer = this.bottomField.getText();

                // Construct global panes and set main pane as start screen
                Thread constructor = new Thread(() -> {
                    System.out.println("Loading...");
                    // Construct global panes in the main
                    // After getting server addresses
                    Main.constructPanes();
                    System.out.println("Loading finished");

                    // Pass to the start screen
                    Platform.runLater(() -> Main.mainScene.setRoot(Main.startScreen));
                });

                // When program closed suddenly , stop the thread
                constructor.setDaemon(true);
                // Start the constructor threads
                constructor.start();




            }


        });
    }
}
