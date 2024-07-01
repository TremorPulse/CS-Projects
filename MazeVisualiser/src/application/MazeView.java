package application;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import maze.Maze;

import java.util.List;

public class MazeView extends Canvas {
    private Maze maze;
    private List<int[]> steps;
    private int currentStep = 0;
    private long startTime;
    private long elapsedTime;
    private long traversalTime;
    private boolean isMazeSolved = false;
    private boolean animationFinished = false;
    private long lastUpdate = 0;
    private static final long UPDATE_INTERVAL = 100; // milliseconds

    public MazeView(Maze maze) {
        this.maze = maze;
        this.steps = maze.getSteps();

        // Calculate maze and canvas dimensions
        int mazeSize = Math.min(maze.getRow(), maze.getCol());
        int cellSize = 20; 
        int canvasSize = mazeSize * cellSize; // Adjusted canvas size based on maze size

        setWidth(canvasSize);
        setHeight(canvasSize);

        // Set a black background for the canvas
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());

        // Record start time for visual time calculation
        startTime = System.currentTimeMillis();

        startAnimation();
    }

    private void startAnimation() {
        GraphicsContext gc = getGraphicsContext2D();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!maze.isSolved()) {
                    if (!maze.generateMazeDFS()) {
                        maze.solveMaze(); // Solve the maze
                        isMazeSolved = true; // Mark the maze as solved
                        elapsedTime = System.currentTimeMillis() - startTime; // Record the elapsed time once the maze is solved
                        steps = maze.getSolutionSteps(); // Get the solution steps after solving the maze
                        startTime = System.currentTimeMillis(); // Reset startTime for traversal animation
                        traversalTime = 0;
                    }
                }

                // Update the current step at a controlled rate
                if (isMazeSolved && !animationFinished && now - lastUpdate >= UPDATE_INTERVAL * 1_000_000) {
                    traversalTime = System.currentTimeMillis() - startTime;
                    if (currentStep < steps.size() - 1) {
                        currentStep++;
                        maze.mainMemoryWrites++;
                    } else {
                        animationFinished = true;
                    }
                    lastUpdate = now;
                }

                // Redraw the maze at every frame
                gc.clearRect(0, 0, getWidth(), getHeight());
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, getWidth(), getHeight()); 

                // Draw the maze centered in the canvas
                drawMaze();

                // Display metrics in the top left corner of the canvas
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font(14));
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText("Maze Generation Algorithm: Iterative Backtracking (DFS)", 15, 30);
                gc.fillText("Maze Solving Algorithm: Right Hand Rule (Depth-First In-Order Tree Traversal)", 15, 50);

                gc.fillText("Grid Size: " + maze.getRow() + "x" + maze.getCol(), 15, 70); 
                gc.fillText("Visual Time: " + (isMazeSolved ? elapsedTime : System.currentTimeMillis() - startTime) + " ms", 15, 90); 
                gc.fillText("Traversal Time: " + traversalTime + " ms", 15, 110);
                gc.fillText("Number of Writes to Main Memory: " + maze.getMainMemoryWrites(), 15, 130); 
                gc.fillText("Number of Writes to Auxiliary Memory: " + maze.getAuxMemoryWrites(), 15, 150); 

                gc.fillText("Time Complexity: O(C + E)", 15, 170); 
                gc.fillText("Space Complexity: O(C)", 15, 190); 

                gc.setFill(Color.web("#00FF00")); // Bright green
                gc.fillRect(15, 200, 10, 10);
                gc.setFill(Color.web("#00FF00"));
                gc.fillText(": Start Point", 30, 210);

                gc.setFill(Color.RED);
                gc.fillRect(15, 220, 10, 10);
                gc.setFill(Color.RED);
                gc.fillText(": End Point", 30, 230);

                gc.setFill(Color.BLUE);
                gc.fillRect(15, 240, 10, 10);
                gc.setFill(Color.BLUE);
                gc.fillText(": Backtracking Path", 30, 250);

                gc.setFill(Color.PURPLE);
                gc.fillRect(15, 260, 10, 10);
                gc.setFill(Color.PURPLE);
                gc.fillText(": Traversal Path", 30, 270);
            }
        };
        timer.start();
    }
    
    
    private void drawMaze() {
        GraphicsContext gc = getGraphicsContext2D();
        int[][] grid = maze.getGrid();
        int numRows = grid.length;
        int numCols = grid[0].length;

        // Calculate maze dimensions
        double mazeWidth = numCols * 10;
        double mazeHeight = numRows * 10;

        // Calculate offsets to center the maze in the canvas
        double offsetX = (getWidth() - mazeWidth) / 1.75;
        double offsetY = ((getHeight() - mazeHeight) / 5.0) + 180; // Offset to accommodate the metrics

        // Draw walls in black and paths in white
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (grid[row][col] == Maze.getWall()) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(offsetX + col * 8, offsetY + row * 8, 8, 8);
                } else if (grid[row][col] == Maze.PATH) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(offsetX + col * 8, offsetY + row * 8, 8, 8);
                }
            }
        }

        // Draw start and end points
        int[] startCell = maze.getStartCell();
        int[] endCell = maze.getEndCell();
        gc.setFill(Color.web("#00FF00")); // Bright green
        gc.fillRect(offsetX + startCell[1] * 8, offsetY + startCell[0] * 8, 8, 8);
        gc.setFill(Color.RED);
        gc.fillRect(offsetX + endCell[1] * 8, offsetY + endCell[0] * 8, 8, 8);

        // Highlight the steps taken during backtracking (used during maze generation)
        if (!isMazeSolved) {
            for (int i = 0; i < steps.size(); i++) {
                int[] step = steps.get(i);
                double opacity = 1.0 - (double) i / steps.size(); // Calculate opacity based on step order
                gc.setFill(Color.rgb(0, 0, 255, opacity)); // Use opacity to create a gradient effect
                gc.fillRect(offsetX + step[1] * 8, offsetY + step[0] * 8, 8, 8);
            }
        }

        // Draw the solution path step by step (used after the maze is solved)
        if (isMazeSolved) {
            for (int i = 0; i <= currentStep; i++) {
                if (i < steps.size()) {
                    int[] cell = steps.get(i);
                    gc.setFill(Color.PURPLE);
                    gc.fillRect(offsetX + cell[1] * 8, offsetY + cell[0] * 8, 8, 8);
                }
            }
        }
    }
}
