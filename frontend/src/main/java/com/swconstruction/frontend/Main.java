package com.swconstruction.frontend;

import com.swconstruction.frontend.apiutils.User;
import com.swconstruction.frontend.customview.GameLobby;
import com.swconstruction.frontend.customview.LeaderBoard;
import com.swconstruction.frontend.customview.MessageScreen;
import com.swconstruction.frontend.forms.LoginForm;
import com.swconstruction.frontend.forms.ServerForm;
import com.swconstruction.frontend.forms.SignUpForm;
import com.swconstruction.frontend.sidecomponents.LeaderBoardType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    // API and matchmaker server url
    public static String backEndServer;
    public static String matchMakerServer;

    // Global scene for stage
    public static Scene mainScene;

    // Global panes for every object to access easily
    public static ServerForm serverForm;
    public static MessageScreen startScreen;
    public static LoginForm loginForm;
    public static SignUpForm signUpForm;
    public static MessageScreen leaderBoardLobby;
    public static LeaderBoard weeklyBoard;
    public static LeaderBoard allTimesBoard;
    public static GameLobby gameLobby;

    //  Logged in user
    public static User loggedPlayer;

    //  loggedPlayer score
    public static Integer score = 0;

    // Screen width and height
    private int screenWidth = 1200;
    private int screenHeight = 1000;

    public static void constructPanes()
    {

        // Weekly leader board view
        weeklyBoard = new LeaderBoard(LeaderBoardType.WEEKLY);
        weeklyBoard.fillTable();

        // All times leader board view
        allTimesBoard = new LeaderBoard(LeaderBoardType.ALL);
        allTimesBoard.fillTable();

        // The menu for leader board options
        leaderBoardLobby = new MessageScreen("LEADERBOARD", "Last week","All times");

        // Game menu
        gameLobby = new GameLobby("ALIEN\nSHOOTER","Start game", "LeaderBoard");

        // Sign up and login form
        signUpForm = new SignUpForm("Sign up");
        loginForm = new LoginForm("Login");

        // Initial screen after the server credentials form
        startScreen = new MessageScreen("ALIEN\nSHOOTER","Sign up","Login");

        // Set back link of forms to startScreen
        signUpForm.setBackLink(startScreen);
        loginForm.setBackLink(startScreen);

        // Set forward link of sign up form to to login form
        signUpForm.setSubmitLink(loginForm);
        // Set forward link of login form to game lobby
        loginForm.setSubmitLink(gameLobby);

        // Set start screens links
        startScreen.setLeftBtnLink(Main.signUpForm);
        startScreen.setRightBtnLink(Main.loginForm);


        // Link leader board lobby left button to weekly leader board
        leaderBoardLobby.setLeftBtnLink(Main.weeklyBoard);
        // Link leader board lobby right button to all times leader board
        leaderBoardLobby.setRightBtnLink(Main.allTimesBoard);

        // Link back button of leader boards to game lobby
        weeklyBoard.setBackLink(Main.gameLobby);
        allTimesBoard.setBackLink(Main.gameLobby);
    }

    @Override
    public void start(Stage primaryStage){




        // Server form for getting the server addresses
        serverForm = new ServerForm("Backend server address","Multiplayer server address","Connect");

        mainScene = new Scene(serverForm,screenWidth,screenHeight);

        // Add css stylesheet
        mainScene.getStylesheets().add("style.css");

        // Set title
        primaryStage.setTitle("ALIEN SHOOTER");

        // Forbid the resizing of the screen
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
