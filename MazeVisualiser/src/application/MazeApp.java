package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import maze.Maze;

public class MazeApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Maze maze = new Maze(35, 35); // Create a smaller 31x31 maze
        MazeView mazeView = new MazeView(maze);
        root.setCenter(mazeView);

        Scene scene = new Scene(root, mazeView.getWidth(), mazeView.getHeight());

        primaryStage.setTitle("Maze Generation and Path Finder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}