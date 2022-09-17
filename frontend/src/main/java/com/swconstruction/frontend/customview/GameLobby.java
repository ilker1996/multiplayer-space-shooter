package com.swconstruction.frontend.customview;

import com.swconstruction.frontend.Main;
import com.swconstruction.frontend.game.Game;
import javafx.scene.Parent;

import static com.swconstruction.frontend.Main.mainScene;

public class GameLobby extends MessageScreen {

    public GameLobby(String screenMsg , String leftBtnText ,String rightBtnText)
    {
        super(screenMsg , leftBtnText , rightBtnText);

        this.setRightBtnLink(Main.leaderBoardLobby);

        // Left button is hard linked to new game
        // So , put null in parameter
        this.setLeftBtnLink(null);
    }

    // Override left button link to create new game Pane every time
    public void  setLeftBtnLink(Parent newPane)
    {
        this.leftBtn.setOnAction(e -> {
            // Create new game level1 pane and set it as root pane
            Game level1 = new Game(1);
            //Set score to 0 before starting to level1
            Main.score = 0;
            mainScene.setRoot(level1);

            //For event handlers to be work on new Pane
            level1.setFocusTraversable(true);
            level1.requestFocus();
        });


    }
}
