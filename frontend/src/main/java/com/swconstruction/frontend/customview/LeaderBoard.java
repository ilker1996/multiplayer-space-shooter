package com.swconstruction.frontend.customview;


import com.swconstruction.frontend.apiutils.RestConsumer;
import com.swconstruction.frontend.apiutils.Score;
import com.swconstruction.frontend.sidecomponents.LeaderBoardType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.swconstruction.frontend.Main.mainScene;

public class LeaderBoard extends VBox {

    // Type of the leaderboard (e.g. weekly or all times)
    private LeaderBoardType type;
    // List of score entries
    private ObservableList<Score> leaderBoard;
    // Button that links to back
    private Button backButton;

    public LeaderBoard(LeaderBoardType type)
    {
        super();
        this.type = type;
        this.leaderBoard = FXCollections.observableArrayList();
        this.backButton = new Button("Back");

        TableView<Score> table = new TableView<>(this.leaderBoard);

        // Adjust height and width of the table
        table.maxHeightProperty().bind(super.heightProperty().multiply(0.9));
        table.minHeightProperty().bind(super.heightProperty().multiply(0.9));
        table.maxWidthProperty().bind(super.widthProperty());
        table.minWidthProperty().bind(super.widthProperty());

        // Constrain size of columns
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Column headers of the table
        TableColumn<Score,String> userCol = new TableColumn<>("Username");

        TableColumn<Score,String> scoreCol = new TableColumn<>("Score");

        TableColumn<Score,String> dateCol = new TableColumn<>("Date");




        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(userCol, scoreCol, dateCol);


        // Just show 10 highest entries
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(30).multiply(0.1));

        // Button for refreshing the score entriees
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> this.fillTable());

        // Add buttons to HBox
        HBox buttons = new HBox(this.backButton , refreshButton);

        // Adjust HBox posiiton and size properly
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(10,10,10,10));
        buttons.minWidthProperty().bind(this.widthProperty());
        buttons.maxWidthProperty().bind(this.widthProperty());
        buttons.minHeightProperty().bind(this.heightProperty().multiply(0.09));
        buttons.maxHeightProperty().bind(this.heightProperty().multiply(0.09));

        // Adjust buttons size
        refreshButton.minWidthProperty().bind(buttons.widthProperty().divide(5));
        this.backButton.minWidthProperty().bind(buttons.widthProperty().divide(5));
        refreshButton.minHeightProperty().bind(buttons.heightProperty());
        this.backButton.minHeightProperty().bind(buttons.heightProperty());

        refreshButton.maxWidthProperty().bind(buttons.widthProperty().divide(5));
        this.backButton.maxWidthProperty().bind(buttons.widthProperty().divide(5));
        refreshButton.maxHeightProperty().bind(buttons.heightProperty());
        this.backButton.maxHeightProperty().bind(buttons.heightProperty());

        super.getChildren().add(table);
        super.getChildren().add(buttons);

    }

    // Gets leader board list from the server
    public void fillTable()
    {
        // Clear for every refresh
        this.leaderBoard.clear();

        RestConsumer consumer = new RestConsumer();

        // List for score entries
        List<Score> scoreList = consumer.getLeaderBoard(this.type);


        // Just get 10 highest entries to show
        this.leaderBoard.addAll(scoreList.subList(0 , Math.min(10 , scoreList.size())));
    }
    // Set back button's link to given pane
    public void setBackLink(Parent newPane)
    {
        this.backButton.setOnAction(e -> {
            mainScene.setRoot(newPane);

            //For event handlers to be work on new Pane
            newPane.setFocusTraversable(true);
            newPane.requestFocus();
        });
    }

}
