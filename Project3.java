package Project3;
import java.util.*;

public class Project3 {
    /*
     * Project 3- Textbook section 8.7
     * 1.) Generates a maze given the parameters n(width) & m(height).
     * 2.) Output maze path in 'NSEW'(cardinal directions) format.
     * 3.) Draw the maze and then at the press of a button, show the path.
     */
    public static void main(String[] args) {
        // int n = 10;
        // int m = 10;

        // //generate maze
        // char[][] maze = generateMaze(n, m);

        // //Solve Maze
        // boolean[][] visited = new boolean[n][m];
        // String path = solveMaze(maze, visited, n, m, 0, 0);
        // System.out.println("Path to solve maze: " + path);

        // //Print Maze
        // DrawMaze(maze);

        //create maze
        int width = 10;
        int height = 10;
        MazeGenerator mazeGenerator = new MazeGenerator(width, height);
        int[][] maze = mazeGenerator.generate();

         // Print out the maze
         for (int i = 0; i < height; i++) {
            // Print the top row of each cell
            for (int j = 0; j < width; j++) {
                System.out.print("+");
                if ((maze[i][j] & 1) == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print("-");
                }
            }
            System.out.println("+");

            // Print the left and right walls of each cell
            for (int j = 0; j < width; j++) {
                if ((maze[i][j] & 8) == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print("|");
                }
                System.out.print(" ");
                if (j == width - 1 && (maze[i][j] & 2) == 0) {
                    System.out.println("|");
                }
            }
        }

        // Print the bottom row of each cell
        for (int j = 0; j < width; j++) {
            System.out.print("+");
            if ((maze[height - 1][j] & 4) == 0) {
                System.out.print(" ");
            } else {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }
    

    /*
     * generateMaze
     * @param n - height param
     * @param m - width param
     */
    public static char[][] generateMaze(int n, int m) {
        char[][] maze = new char[2*n+1][2*m+1]; // Initialize maze with walls and spaces
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (i % 2 == 0 || j % 2 == 0) {
                    maze[i][j] = '#';
                } else {
                    maze[i][j] = ' ';
                }
            }
        }
        
        int[] parent = new int[n*m]; // Initialize disjoint set data structure
        Arrays.fill(parent, -1);
        int[] rank = new int[n*m];
        
        Random rand = new Random(); // Randomly generate maze using disjoint set
        while (find(0, parent) != find(n*m-1, parent)) {
            int row = rand.nextInt(n);
            int col = rand.nextInt(m);
            int index = row*m+col;
            int[] directions = {1, 2, 3, 4};
            Collections.shuffle(Arrays.asList(directions), rand);
            for (int direction : directions) {
                int newRow = row, newCol = col;
                if (direction == 1) newRow--;
                else if (direction == 2) newRow++;
                else if (direction == 3) newCol--;
                else newCol++;
                if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < m) {
                    int newIndex = newRow*m+newCol;
                    if (find(index, parent) != find(newIndex, parent)) {
                        union(index, newIndex, parent, rank);
                        if (direction == 1) maze[2*row+1][2*col] = ' ';
                        else if (direction == 2) maze[2*row+3][2*col] = ' ';
                        else if (direction == 3) maze[2*row+1][2*col-1] = ' ';
                        else maze[2*row+1][2*col+1] = ' ';
                    }
                }
            }
        }
        
        maze[0][0] = 'S'; // Set start and end points
        maze[2*n][2*m-1] = 'E';
        return maze;
    }

    //Maze Solver
    public static String solveMaze(char[][] maze, boolean[][] visited, int n, int m, int row, int col) {
        // Mark current cell as visited
        visited[row][col] = true;
        
        // Check if current cell is the end point
        if (row == n - 1 && col == m - 1) {
            return "";
        }
        
        // Try to move in each direction (up, down, left, right)
        String[] directions = {"N", "S", "W", "E"};
        int[] rowOffsets = {-1, 1, 0, 0};
        int[] colOffsets = {0, 0, -1, 1};
        List<Integer> shuffledDirections = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(shuffledDirections);
        
        for (int i : shuffledDirections) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];
            
            if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < m) {
                int[] cellOffsets = getCellOffsets(row, col, newRow, newCol);
                char cell = maze[row + cellOffsets[0]][col + cellOffsets[1]];
                
                if (cell == ' ' && !visited[newRow][newCol]) {
                    String path = solveMaze(maze, visited, n, m, newRow, newCol);
                    
                    if (path != null) {
                        return directions[i] + path;
                    }
                }
            }
        }
        
        // No valid path found
        return null;
    }
    
    public static int[] getCellOffsets(int row1, int col1, int row2, int col2) {
        int[] offsets = new int[2];
        
        if (row1 == row2) {
            if (col1 < col2) {
                offsets[1] = 1;
            } else {
                offsets[1] = -1;
            }
        } else {
            if (row1 < row2) {
                offsets[0] = 1;
            } else {
                offsets[0] = -1;
            }
        }
        
        return offsets;
    }

    //Helper Funcs

    //find
    //func to find the root of a set
    public static int find(int x, int[] parent) {
        if (parent[x] == -1) return x;
        parent[x] = find(parent[x], parent);
        return parent[x];
    }

    //union
    public static void union(int x, int y, int[] parent, int[] rank) {
        int rootX = find(x, parent);
        int rootY = find(y, parent);
        if (rootX != rootY) {
            if (rank[rootX] > rank[rootY]) parent[rootY] = rootX;
            else if (rank[rootX] < rank[rootY]) parent[rootX] = rootY;
            else {
                parent[rootX] = rootY;
                rank[rootY]++;
            }
        }
    }

    /*
     * Draw Maze
     * @param maze - the 2D char maze returned by GenerateMaze
     */
    public static void DrawMaze(char[][] maze) {
        // Draw the maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }
            
}

    

