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
        System.out.println("Solve Maze? 1 for yes or 0 for no.");
        //Get input
        int solve = input.nextInt();

        if (solve == 1) {
            //Directional
            System.out.println("Maze Solution in NSEW format: ");
            solveMaze(maze);

            //Visual
        }else {return;}
	}

    //Cardinal Direction Solve
    public static void solveMaze(Maze maze) {
        boolean[][] visited = new boolean[maze.columns][maze.rows];
        Stack<int[]> stack = new Stack<>();
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};
        
        // Start from the entrance of the maze
        int[] start = {0, 0, -1}; // x, y, direction
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0];
            int y = current[1];
            int dir = current[2];
            
            // Check if current position is the exit
            if (x == maze.columns - 1 && y == maze.rows - 1) {
                // Output the solution in cardinal direction format
                System.out.print("Solution: ");
                Stack<String> solution = new Stack<>();
                while (dir != -1) {
                    switch (dir) {
                        case 0: solution.push("S"); break;
                        case 1: solution.push("E"); break;
                        case 2: solution.push("N"); break;
                        case 3: solution.push("W"); break;
                    }
                    x -= dx[dir];
                    y -= dy[dir];
                    dir = visited[x][y] ? getVisitedDirection(x, y, visited) : -1;
                }
                while (!solution.isEmpty()) {
                    System.out.print(solution.pop());
                }
                System.out.println();
                return;
            }
            
            // Mark current position as visited
            visited[x][y] = true;
            
            // Push unvisited neighbors to the stack
            for (int i = 0; i < dx.length; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];
                if (newX >= 0 && newX < maze.columns && newY >= 0 && newY < maze.rows) {
                    if (dir == -1 || i == dir || i == opposite(dir)) { // only allow straight and u-turn
                        if (i == 0 && !maze.wallHorizontal[x][y]) { // south
                            if (!visited[x][y + 1]) {
                                stack.push(new int[] {x, y + 1, i});
                            }
                        } else if (i == 1 && !maze.wallVertical[x][y]) { // east
                            if (!visited[x + 1][y]) {
                                stack.push(new int[] {x + 1, y, i});
                            }
                        } else if (i == 2 && !maze.wallHorizontal[x][y - 1]) { // north
                            if (!visited[x][y - 1]) {
                                stack.push(new int[] {x, y - 1, i});
                            }
                        } else if (i == 3 && !maze.wallVertical[x - 1][y]) { // west
                            if (!visited[x - 1][y]) {
                                stack.push(new int[] {x - 1, y, i});
                            }
                        }
                    }
                }
            }
        }
        
        // No solution found
        System.out.println("No solution found.");
    }

    private static int getVisitedDirection(int x, int y, boolean[][] visited) {
        if (x > 0 && visited[x - 1][y]) {
            return 3; // west
        } else if (y < visited[0].length - 1 && visited[x][y + 1]) {
            return 0; // south
        } else if (x < visited.length - 1 && visited[x + 1][y]) {
            return 1; // east
        } else if (y > 0 && visited[x][y - 1]) {
            return 2; // north
        } else {
            return -1;
        }
    }
    
    private static int opposite(int dir) {
        switch (dir) {
            case 0: return 2;
            case 1: return 3;
            case 2: return 0;
            case 3: return 1;
            default: return -1;
        }
    }
	
}
