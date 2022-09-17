package com.swconstruction.frontend.forms;

import com.swconstruction.frontend.Main;
import com.swconstruction.frontend.apiutils.RestConsumer;
import com.swconstruction.frontend.apiutils.User;
import javafx.scene.Parent;

import static com.swconstruction.frontend.Main.mainScene;

public class LoginForm extends CustomForm {


    public LoginForm(String submitText)
    {
        super(submitText);


    }
    @Override
    public void setSubmitLink(Parent newPane) {
        this.submitBtn.setOnAction(e -> {
            RestConsumer consumer = new RestConsumer();

            User user = new User(this.usernameField.getText() , this.passwordField.getText());

             User loggedUser = consumer.logUser(user);

            // If login successful
            if(loggedUser != null )
            {
                Main.loggedPlayer = loggedUser;

                mainScene.setRoot(newPane);

                //For event handlers to be work on new Pane
                newPane.setFocusTraversable(true);
                newPane.requestFocus();
            }
            else{
                this.lblMessage.setText("Username or password wrong!");
            }
        });
    }
}
