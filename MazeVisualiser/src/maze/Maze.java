package maze;

import java.util.*;

public class Maze {

	private int[][] grid; // The maze grid
	private int row; // Number of rows in the maze
	private int col; // Number of columns in the maze
	private static final int WALL = 1; // Constant representing a wall
	public static final int PATH = 0; // Constant representing a path
	private List<int[]> steps; // List to store the steps during maze generation
	private List<int[]> solutionSteps; // List to store the steps of the solution steps
	private boolean solved = false;
	private Deque<int[]> stack = new ArrayDeque<>(); // Stack for backtracking

	private int[] startCell; // Start point coordinates
	private int[] endCell; // End point coordinates

	// Metrics variables
	private String algorithmType; // Type of maze generation algorithm used
	private long visualTime; // Time taken to visually complete the maze generation in milliseconds
	public int mainMemoryWrites; // Number of writes to main memory
	private int auxMemoryWrites; // Number of writes to auxiliary memory

	private int currentStep; // Current step in maze generation

	public Maze(int row, int col) {
		this.row = row;
		this.col = col;
		grid = new int[row][col];
		steps = new ArrayList<>();

		// Initialise the grid with walls
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				grid[r][c] = getWall();
			}
		}

		startCell = new int[]{1, 1}; // Start maze generation from cell (1,1)
		endCell = new int[]{row - 1, col - 2}; // Bottom-right corner
		grid[startCell[0]][startCell[1]] = PATH; // Mark the start cell as a path
		grid[endCell[0]][endCell[1]] = PATH; // Mark the end cell as a path

		stack.push(startCell); // Push the start cell onto the stack

		// Initialise metrics
		algorithmType = "Depth-First Search"; // Default algorithm type
		visualTime = 0; // Initial visual time
		mainMemoryWrites = 0; // Initial main memory writes
		auxMemoryWrites = 0; // Initial auxiliary memory writes
		currentStep = 0; // Initial step count

		solutionSteps = new ArrayList<>();
	}


	// Randomised depth-first search implementation (also known as the “recursive backtracker” algorithm)
	public boolean generateMazeDFS() {
		if (stack.isEmpty()) {
			endCell = steps.get(steps.size() - 1); // Set end cell to last visited cell
			return false; // Maze generation is complete
		}

		long startTime = System.currentTimeMillis(); // Start time for visual time calculation

		int[] cell = stack.peek(); // Get the current cell from the stack
		int x = cell[0];
		int y = cell[1];

		// Define directions: right, down, left, up
		int[] dx = {1, 0, -1, 0};
		int[] dy = {0, 1, 0, -1};

		List<Integer> directions = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			directions.add(i);
		}
		Collections.shuffle(directions); // Randomise the order of directions

		for (int i : directions) {
			int nx = x + dx[i] * 2; // Calculate the next cell coordinates
			int ny = y + dy[i] * 2;

			if (nx > 0 && ny > 0 && nx < row - 1 && ny < col - 1 && grid[nx][ny] == WALL) {
				grid[x + dx[i]][y + dy[i]] = PATH; // Remove the wall
				grid[nx][ny] = PATH; // Mark the chosen cell as a path
				steps.add(new int[]{nx, ny}); // Add step to the list
				stack.push(new int[]{nx, ny}); // Push the chosen cell onto the stack

				// Update metrics
				mainMemoryWrites += 2; // Two writes to main memory (grid[x + dx[i]][y + dy[i]] and grid[nx][ny])
				auxMemoryWrites++; // One push operation to auxiliary memory (stack.push)

				currentStep++; // Increment step count

				long endTime = System.currentTimeMillis(); // End time for visual time calculation
				visualTime = endTime - startTime; // Calculate visual time

				return true; // A step was generated
			}
		}

		// If no unvisited neighbours, backtrack by popping the current cell
		stack.pop();

		// Update metrics
		auxMemoryWrites++; // One pop operation from auxiliary memory (stack.pop)

		currentStep++; // Increment step count

		return true; // Maze generation is not yet complete
	}

	// Maze-solving method using the right-hand rule
	public void solveMaze() {
	    int[] cell = startCell;
	    int direction = 0; // 0: right, 1: down, 2: left, 3: up

	    // Define directions: right, down, left, up
	    int[] dx = {1, 0, -1, 0};
	    int[] dy = {0, 1, 0, -1};

	    System.out.println("Solving maze...");

	    while (!Arrays.equals(cell, endCell)) {
	        System.out.println("Current cell: " + Arrays.toString(cell));
	        System.out.println("Current direction: " + direction);

	        // Try to turn right (relative to the current direction)
	        int newDirection = (direction + 3) % 4;
	        int[] newCell = {cell[0] + dx[newDirection], cell[1] + dy[newDirection]};

	        if (isPath(newCell)) {
	            // If the cell to the right is a path, turn right and move forward
	            cell = newCell;
	            direction = newDirection;
	            System.out.println("Turned right to cell: " + Arrays.toString(cell));
	        } else {
	            // Check the cell in the current direction
	            newCell = new int[]{cell[0] + dx[direction], cell[1] + dy[direction]};
	            if (isPath(newCell)) {
	                // If the cell straight ahead is a path, move forward
	                cell = newCell;
	                System.out.println("Moved forward to cell: " + Arrays.toString(cell));
	            } else {
	                // If the cells to the right and straight ahead are not paths, turn left
	                direction = (direction + 1) % 4;
	                System.out.println("Turned left, new direction: " + direction);
	            }
	        }

	        // Add the current cell to the solution path
	        solutionSteps.add(new int[]{cell[0], cell[1]});

	        // Check if the current cell is the end cell
	        if (Arrays.equals(cell, endCell)) {
	            solved = true;
	            System.out.println("Maze solved!");
	            break;
	        }
	    }

	    if (!solved) {
	        System.out.println("Maze cannot be solved.");
	    }
	}

	// Helper method to check if a cell is a valid path
	private boolean isPath(int[] cell) {
	    int x = cell[0];
	    int y = cell[1];
	    return x >= 0 && y >= 0 && x < row && y < col && grid[x][y] == PATH;
	}
	
		public List<int[]> getSolutionSteps() {
			return solutionSteps;
		}

		public boolean isSolved() {
			return solved;
		}

		public int[][] getGrid() {
			return grid;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}

		public int[] getStartCell() {
			return startCell;
		}

		public int[] getEndCell() {
			return endCell;
		}


		public List<int[]> getSteps() {
			return steps;
		}

		public String getAlgorithmType() {
			return algorithmType;
		}

		public void setAlgorithmType(String algorithmType) {
			this.algorithmType = algorithmType;
		}

		public long getVisualTime() {
			return visualTime;
		}

		public int getMainMemoryWrites() {
			return mainMemoryWrites;
		}

		public int getAuxMemoryWrites() {
			return auxMemoryWrites;
		}

		public static int getWall() {
			return WALL;
		}

		public int getCurrentStep() {
			return currentStep;
		}

		public void resetMetrics() {
			visualTime = 0;
			mainMemoryWrites = 0;
			auxMemoryWrites = 0;
			currentStep = 0;
		}

		public void printMaze() {
			for (int r = 0; r < row; r++) {
				for (int c = 0; c < col; c++) {
					if (r == startCell[0] && c == startCell[1]) {
						System.out.print("S"); // Mark the start point
					} else if (r == endCell[0] && c == endCell[1]) {
						System.out.print("E"); // Mark the end point
					} else {
						System.out.print(grid[r][c] == getWall() ? "#" : " ");
					}
				}
				System.out.println();
			}
		}
	}
