package com.swconstruction.frontend.forms;

import com.swconstruction.frontend.apiutils.RestConsumer;
import com.swconstruction.frontend.apiutils.User;
import javafx.scene.Parent;
import org.springframework.http.HttpStatus;

import static com.swconstruction.frontend.Main.mainScene;

public class SignUpForm extends CustomForm {

    public SignUpForm(String submitText)
    {
        super(submitText);

    }

    @Override
    public void setSubmitLink(Parent newPane) {
        this.submitBtn.setOnAction(e -> {
            RestConsumer consumer = new RestConsumer();
            User user = new User(this.usernameField.getText() , this.passwordField.getText());

            HttpStatus status = consumer.addUser(user);

            // If user is not created in the database
            if(status != HttpStatus.CREATED) {
                // If there is same user
                if (status == HttpStatus.CONFLICT) {
                    super.lblMessage.setText("Username already taken");
                }
                // If username or password is bad
                else if (status == HttpStatus.BAD_REQUEST) {
                    super.lblMessage.setText("Your username should be " +
                            "\n smaller than 12 characters and" +
                            "\n your password and username must not be empty");
                }
                // If any other error
                else {
                    super.lblMessage.setText("Even I don't know the error :)");
                }
            }
            // If created, redirect to login page
            else
            {
                mainScene.setRoot(newPane);

                //For event handlers to be work on new Pane
                newPane.setFocusTraversable(true);
                newPane.requestFocus();
            }

        });
    }
}
