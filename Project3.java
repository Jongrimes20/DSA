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
        char[][] maze = GenerateMaze(20, 30);
        System.out.println(maze);
    }

    /*
     * generateMaze
     * @param n - height param
     * @param m - width param
     */
    public static char[][] GenerateMaze(int n, int m) {
        // Create a grid to represent the maze
        char[][] maze = new char[2 * n + 1][2 * m + 1];
        
        // Initialize the grid with walls and spaces
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (i % 2 == 0 || j % 2 == 0) {
                  maze[i][j] = '#';
                } else {
                    maze[i][j] = ' ';
                }               
            }
        }
                
        // Initialize the disjoint set data structure
        int numSets = n * m;
        int[] parent = new int[numSets];
        int[] rank = new int[numSets];
        Arrays.fill(parent, -1);

        // Generate the maze
        Random rand = new Random();
        while (numSets > 1) {
            int row = rand.nextInt(n);
            int col = rand.nextInt(m);
            int setIndex = getIndex(row, col, m);
            int[] directions = {1, 2, 3, 4};
            Collections.shuffle(Arrays.asList(directions), rand);
            for (int direction : directions) {
                int newRow = row;
                int newCol = col;
                if (direction == 1) {
                    newRow--;
                } else if (direction == 2) {
                    newRow++;
                } else if (direction == 3) {
                    newCol--;
                } else {
                    newCol++;
                }
                if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < m) {
                    int neighborIndex = getIndex(newRow, newCol, m);
                    if (find(setIndex, parent) != find(neighborIndex, parent)) {
                    union(setIndex, neighborIndex, parent, rank);
                        if (direction == 1) {
                            maze[2 * row + 1][2 * col] = ' ';
                        } else if (direction == 2) {
                            maze[2 * row + 3][2 * col] = ' ';
                        } else if (direction == 3) {
                            maze[2 * row + 1][2 * col - 1] = ' ';
                        } else {
                            maze[2 * row + 1][2 * col + 1] = ' ';
                        }
                    }
                }
            }
        }
                
        // Set the start and end points
        maze[1][0] = 'S';
        maze[2 * n - 1][2 * m] = 'E';
                
        return maze;
    }

    //Helper Funcs

    //getIndex
    // Function to get the set index for a given cell
    public static int getIndex(int row, int col, int m) {
        return row * m + col;
    }

    //find
    //func to find the root of a set
    public static int find(int x, int[] parent) {
        if (parent[x] == -1) {
            return x;
        }
        parent[x] = find(parent[x], parent);
        return parent[x];
    }

    //union
    public static void union(int x, int y, int[] parent, int[] rank) {
        int rootX = find(x, parent);
        int rootY = find(y, parent);
        if (rootX != rootY) {
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootX] = rootY;
                rank[rootY]++;
            }
        }
    }

    /*
     * solveMaze
     * @param maze - the maze to solve
     */
            
}

    

