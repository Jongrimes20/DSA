package Project3;

import java.util.*;

public class MazeGenerator {

	public static class Maze 
   {
		int rows, columns;
		boolean wallHorizontal[][], wallVertical[][];

		//construct the maze
		public Maze(int r, int c) 
        {
			rows = r;
			columns = c;

			if (rows > 1) {
				wallHorizontal = new boolean[columns][rows];
				for (int j = 0; j < rows; j++) {
					for (int i = 0; i < columns; i++) {
						wallHorizontal[i][j] = true;
					}
				}
			}

			if (columns > 1) {
				wallVertical = new boolean[columns][rows];
				for (int i = 0; i < columns; i++) {
					for (int j = 0; j < rows; j++) {
						wallVertical[i][j] = true;
					}
				}
			}
		}

      //draw the maze using " " , "_" , and "|"
		public String toString() 
        {
			int i, j;
			String s = "  ";

			wallHorizontal[columns - 1][rows - 1] = false;

			for (i = 0; i < columns - 1; i++) {
				s = s + " _";
			}
			s = s + " \n";

			for (j = 0; j < rows; j++) {
				s = s + "|";
				for (i = 0; i < columns; i++) {
					if (wallHorizontal[i][j]) {
						s = s + "_";
					} else {
						s = s + " ";
					}
					if (i < columns - 1) {
						if (wallVertical[i][j]) {
							s = s + "|";
						} else {
							s = s + " ";
						}
					}
				}
				s = s + "|\n";
			}
			return s + "\n";
		}

		//remove wall
		public boolean removeWall(int r, int c, int dir) 
        {
			if (dir == 0) 
         {
				if (wallHorizontal[r][c] == true) 
            {
					wallHorizontal[r][c] = false;
					return true;
				} 
            else
					return false;
			}
         else 
         {
				if (wallVertical[r][c] == true) 
            {
					wallVertical[r][c] = false;
					return true;
				} 
            else
					return false;
			}
        }

	}
    
    public static void main(String[] args) 
   {
		int rows, columns;
		int internalWallHorizontal, internalWallVertical;
        int maze1_row, maze1_column, maze2_row, maze2_column;
		int maze1, maze2, set1, set2;
      
		Scanner input = new Scanner(System.in);

      //Take user input
		System.out.println("Please enter number of rows (2 or more): ");
		rows = input.nextInt();
		while (rows < 2) 
        {
			System.out.println("Rows must be 2 or more.");
			rows = input.nextInt();
		}

		System.out.println("Please enter number of columns (2 or more): ");
		columns = input.nextInt();
		while (columns < 2) 
        {
			System.out.println("Columns must be 2 or more.");
			columns = input.nextInt();
		}

		DisjointSet ds = new DisjointSet(rows * columns);
      
        Maze maze = new Maze(rows, columns);

		Random r1 = new Random();
		Random r2 = new Random();

        int size = rows * columns;
      
		//Generating 2 mazes with random walls
		while (size > 1) {
         
         //random a wall direction (0 or 1)
			int wallDirection = r1.nextInt(2);
         
         //0 is a horizontal wall, 1 is a vertical wall
			if (wallDirection == 0) 
         {
				
				internalWallHorizontal = r2.nextInt(columns);
				internalWallVertical = r2.nextInt(rows - 1);

				maze1_row = internalWallVertical + 1;
				maze1_column = internalWallHorizontal + 1;
				
				maze2_row = internalWallVertical + 2;
				maze2_column = internalWallHorizontal + 1;
				
				maze1 = (maze1_row - 1) * columns + maze1_column - 1;
				maze2 = (maze2_row - 1) * columns + maze2_column - 1;
			} 
         
         else 
         {
				
				internalWallHorizontal = r2.nextInt(columns - 1);
				internalWallVertical = r2.nextInt(rows);

				maze1_row = internalWallVertical + 1;
				maze1_column = internalWallHorizontal + 1;

				maze2_row = internalWallVertical + 1;
				maze2_column = internalWallHorizontal + 2;

				maze1 = (maze1_row - 1) * columns + maze1_column - 1;
				maze2 = (maze2_row - 1) * columns + maze2_column - 1;
			}

         //find path compression of 2 mazes
			set1 = ds.find(maze1);
			set2 = ds.find(maze2);
         
         //remove the walls to create a random maze
			if (set1 != set2) {
				
				if (maze.removeWall(internalWallHorizontal, internalWallVertical,
						wallDirection) == true) {
					size--;
					ds.union(set1, set2);
				}
			}
		}
      
        //print out maze
        System.out.println("---------------------------------");
	    System.out.printf("Maze with %d rows and %d columns:", rows, columns);
        System.out.println();
        System.out.print(maze);


        ////////////////////////////////////////////////
        //Solving
        System.out.println("Solve Maze? Enter 1 for yes and 0 for no.");
        //Get input
        int solve = input.nextInt();

        if (solve == 1) {
            //Directional Solution
            List<String> cardinalDirections = solveMazeDirectional(maze);
            //Print cardinal directions
            System.out.print("Maze Solution in NSEW format: " + cardinalDirections);
            //print maze with path

            
        }else {return;}
	}

            //Cardinal Direction Solve
            public static List<String> solveMazeDirectional(MazeGenerator.Maze maze) 
            {
                final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
                
                int startRow = 0;
                int startCol = 0;
                int endRow = maze.rows - 1;
                int endCol = maze.columns - 1;
    
                // Initialize the queue and visited set for the BFS algorithm
                Queue<int[]> queue = new LinkedList<>();
                Set<String> visited = new HashSet<>();
                queue.add(new int[]{startRow, startCol, -1});
                visited.add(startRow + "," + startCol);
    
                // Perform the BFS algorithm to find the shortest path through the maze
                while (!queue.isEmpty()) {
                    int[] current = queue.poll();
                    int currentRow = current[0];
                    int currentCol = current[1];
                    int currentDir = current[2];
    
                    if (currentRow == endRow && currentCol == endCol) {
                        // We have reached the end of the maze, so backtrack through the visited nodes to construct the path
                        List<String> path = new ArrayList<>();
                        while (currentDir != -1) {
                            if (currentDir == 0) {
                                path.add("NORTH");
                                currentRow++;
                            } else if (currentDir == 1) {
                                path.add("EAST");
                                currentCol--;
                            } else if (currentDir == 2) {
                                path.add("SOUTH");
                                currentRow--;
                            } else if (currentDir == 3) {
                                path.add("WEST");
                                currentCol++;
                            }
                            currentDir = maze.wallHorizontal[currentCol][currentRow] ? 0 : maze.wallVertical[currentCol][currentRow] ? 1 : maze.wallHorizontal[currentCol][currentRow - 1] ? 2 : 3;
                        }
                        Collections.reverse(path);
                        return path;
                    }
    
                    // Try moving in each of the four directions
                    for (int i = 0; i < 4; i++) {
                        int newRow = currentRow + DIRECTIONS[i][0];
                        int newCol = currentCol + DIRECTIONS[i][1];
                        if (newRow >= 0 && newRow < maze.rows && newCol >= 0 && newCol < maze.columns && !visited.contains(newRow + "," + newCol)) {
                            if (i == 0 && maze.wallHorizontal[currentCol][currentRow]) {
                                continue;
                            } else if (i == 1 && maze.wallVertical[currentCol][currentRow]) {
                                continue;
                            } else if (i == 2 && maze.wallHorizontal[currentCol][currentRow + 1]) {
                                continue;
                            } else if (i == 3 && maze.wallVertical[currentCol + 1][currentRow]) {
                                continue;
                            }
                            queue.add(new int[]{newRow, newCol, i});
                            visited.add(newRow + "," + newCol);
                        }
                    }
                }
    
                // If we reach this point, there is no path from the start to the end of the maze
                return null;
            }
	
}
