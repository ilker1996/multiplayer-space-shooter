package com.swconstruction.frontend.multiplayer;

import com.swconstruction.frontend.Main;
import com.swconstruction.frontend.apiutils.RestConsumer;
import com.swconstruction.frontend.game.GameSettings;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Connector extends Pane {

    private ImageView loadingImage;
    private Text matcherText;
    private HBox buttonBox;

    public Connector(){
        super();
        // Bind to css style class
        this.getStyleClass().add("connector");

        // Load waiting image
        this.loadingImage = new ImageView("loading.gif");


        // Text object for displaying connection info
        this.matcherText = new Text();

        // Format the text object
        this.matcherText.setLineSpacing(20);
        this.matcherText.setTextAlignment(TextAlignment.CENTER);
        this.matcherText.setFont(Font.font("Arial", FontWeight.BOLD,20));
        this.matcherText.setFill(Color.DARKRED);

        // Format size of the text box
        HBox textBox = new HBox();
        textBox.setAlignment(Pos.CENTER);
        textBox.layoutXProperty().bind(this.widthProperty().divide(2).subtract(textBox.widthProperty().divide(2)));
        textBox.layoutYProperty().bind(this.heightProperty().divide(3).subtract(textBox.heightProperty().divide(2)));
        textBox.minHeightProperty().bind(this.heightProperty().divide(10));
        textBox.maxHeightProperty().bind(this.heightProperty().divide(10));
        textBox.minWidthProperty().bind(this.widthProperty().divide(5));
        textBox.maxWidthProperty().bind(this.widthProperty().divide(5));

        // Add text object to the horizontal box
        textBox.getChildren().add(this.matcherText);


        // Horizontal box including button
        this.buttonBox = new HBox();

        // Position and resize the box
        this.buttonBox.layoutXProperty().bind(this.widthProperty().divide(2).
                subtract(this.buttonBox.widthProperty().divide(2)));
        this.buttonBox.layoutYProperty().bind(this.heightProperty().divide(2).
                subtract(this.buttonBox.heightProperty().divide(2)));
        this.buttonBox.minHeightProperty().bind(this.heightProperty().divide(10));
        this.buttonBox.maxHeightProperty().bind(this.heightProperty().divide(10));
        this.buttonBox.minWidthProperty().bind(this.widthProperty().divide(5));
        this.buttonBox.maxWidthProperty().bind(this.widthProperty().divide(5));

        // Position waiting animation
        this.loadingImage.xProperty().bind(this.buttonBox.layoutXProperty()
                .add(this.buttonBox.widthProperty().divide(2)));
        this.loadingImage.yProperty().bind(this.buttonBox.layoutYProperty().
                add(this.buttonBox.heightProperty().divide(2)));

        // Button for finding opponent
        Button connectButton = new Button("Find opponent");
        this.buttonBox.setAlignment(Pos.CENTER);
        this.buttonBox.getChildren().add(connectButton);

        // Rezise button
        connectButton.minWidthProperty().bind(this.buttonBox.widthProperty());
        connectButton.maxWidthProperty().bind(this.buttonBox.widthProperty());
        connectButton.maxHeightProperty().bind(this.buttonBox.heightProperty());
        connectButton.minHeightProperty().bind(this.buttonBox.heightProperty());


        // Find opponent when clicked
        connectButton.setOnAction(e -> this.connect2Other());

        this.getChildren().add(this.loadingImage);
        this.getChildren().add(textBox);
        this.getChildren().add(this.buttonBox);

        // Initially connection info and waiting image will not be visible
        this.loadingImage.setVisible(false);
        this.matcherText.setVisible(false);
    }

    // Request to the API for connecting
    private void connect2Other()
    {

        // Hide button and make waiting image visible
        this.buttonBox.setVisible(false);
        this.loadingImage.setVisible(true);

        // Make connection info visible
        this.matcherText.setText("Searching for opponent...");
        this.matcherText.setVisible(true);

        // Start a new thread for waiting the response
        Thread connectorThread = new Thread(() -> {
            RestConsumer restConsumer = new RestConsumer();
            OnlinePlayer opponent = restConsumer.connectOpponent();
            try {
                // Give necessary info to the player
                giveMessage(opponent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // When program close unexpectedly , stop the thread
        connectorThread.setDaemon(true);
        // Start the thread
        connectorThread.start();

    }

    private void giveMessage(OnlinePlayer opponent) throws  InterruptedException
    {
        // Opponent found
        if(opponent != null)
        {
            this.matcherText.setText("You matched with : " + opponent.getUsername()
                                    + "\n Starting the game ...");
            // Wait specified time  before starting the game
            Thread.sleep(GameSettings.getWaitDuration());

            // Hide the image
            this.loadingImage.setVisible(false);
            // Create new online game instance
            MultiPlayerGame onlineGame = new MultiPlayerGame(opponent);

            // Set game as root to the scene
            Main.mainScene.setRoot(onlineGame);

            // This is for hacking the "requestFocus() in the thread is not allowed" error
            Platform.runLater(() -> {
                onlineGame.setFocusTraversable(true);
                onlineGame.requestFocus();

            });


        }
        // Opponent is not found
        else
        {
            // Try to connect again
            this.matcherText.setText("No opponent found. Try again!");
            this.loadingImage.setVisible(false);
            // Make button visible for trying again
            this.buttonBox.setVisible(true);
        }
    }

}
