package com.swconstruction.frontend.forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static com.swconstruction.frontend.Main.mainScene;

public abstract class CustomForm extends GridPane {
    protected Button submitBtn;
    protected Button backBtn;
    protected final TextField usernameField;
    protected final PasswordField passwordField;
    protected Label lblMessage;

    public CustomForm(String submitText)
    {
        super();

        // Add style class
        super.getStyleClass().add("form");
        // Add some properties
        super.setPadding(new Insets(20,20,20,20));
        super.setHgap(5);
        super.setVgap(10);

        // Username input field
        this.usernameField = new TextField();

        // Password input field
        this.passwordField = new PasswordField();

        // Label for showing error messages in screen
        this.lblMessage = new Label();
        this.lblMessage.setTextFill(Color.RED);

        // Create submit and back link buttons of the form
        this.submitBtn = new Button(submitText);
        this.backBtn = new Button("Back");

        // Horizontal box for buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5 ,5,5 ,5));
        buttons.setSpacing(10);
        buttons.getChildren().addAll(this.backBtn,this.submitBtn);

        // Username label
        Label lblUserName = new Label("Username");
        lblUserName.setTextFill(Color.DARKRED);

        // Password label
        Label lblPassword = new Label("Password");
        lblPassword.setTextFill(Color.DARKRED);

        // Add fields to GridPane
        this.add(lblUserName,0,1);
        this.add(this.usernameField,0,2);
        this.add(lblPassword,0,3);
        this.add(this.passwordField,0,4);
        this.add(buttons,0,5);
        this.add(this.lblMessage,0,6);

    }

    // Setting a new pane when back button clicked
    public void setBackLink(Parent newPane)
    {
        this.backBtn.setOnAction(e -> {
            mainScene.setRoot(newPane);

            //For event handlers to be work on new Pane
            newPane.setFocusTraversable(true);
            newPane.requestFocus();
        });
    }

    public abstract void setSubmitLink(Parent newPane);
}
