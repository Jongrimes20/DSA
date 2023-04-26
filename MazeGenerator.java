package Project3;
import java.util.*;

public class MazeGenerator {
    private int width;
    private int height;
    private DisjointSet cells;

    /*
     * Constructor
     */
    public MazeGenerator(int n, int m) {
        width = n;
        height = m;
        cells = new DisjointSet(n * m);
    }

    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        MazeGenerator mazeGenerator = new MazeGenerator(width, height);
        int[][] maze = mazeGenerator.generate();      
        displayMaze(maze);
    }

    /*
     * Generate the maze
     */
    public int[][] generate() {
        int[][] maze = new int[height][width];

        // Initialize all cells as walls
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }

        // Start with a random cell
        Random rand = new Random();
        int startRow = rand.nextInt(height);
        int startCol = rand.nextInt(width);

        // Remove walls until all cells are connected
        while (cells.find(startRow * width + startCol) != cells.find((height - 1) * width)) {
            // Find all neighbors of the current cell that are not yet in the same set
            List<int[]> neighbors = new ArrayList<>();
            if (startRow > 0 && cells.find((startRow - 1) * width + startCol) != cells.find(startRow * width + startCol)) {
                neighbors.add(new int[]{startRow - 1, startCol, 0}); // North neighbor
            }
            if (startCol < width - 1 && cells.find(startRow * width + startCol + 1) != cells.find(startRow * width + startCol)) {
                neighbors.add(new int[]{startRow, startCol + 1, 1}); // East neighbor
            }
            if (startRow < height - 1 && cells.find((startRow + 1) * width + startCol) != cells.find(startRow * width + startCol)) {
                neighbors.add(new int[]{startRow + 1, startCol, 2}); // South neighbor
            }
            if (startCol > 0 && cells.find(startRow * width + startCol - 1) != cells.find(startRow * width + startCol)) {
                neighbors.add(new int[]{startRow, startCol - 1, 3}); // West neighbor
            }

            // Choose a random neighbor and remove the wall between the current cell and the neighbor
            if (!neighbors.isEmpty()) {
                int[] randomNeighbor = neighbors.get(rand.nextInt(neighbors.size()));
                int neighborRow = randomNeighbor[0];
                int neighborCol = randomNeighbor[1];
                int wallIndex = randomNeighbor[2];
                maze[startRow][startCol] &= ~(1 << wallIndex);
                maze[neighborRow][neighborCol] &= ~(1 << (wallIndex + 2) % 4); // Remove opposite wall of neighbor
                cells.union(startRow * width + startCol, neighborRow * width + neighborCol);
            }

            // Move to the next cell
            int[] rootCell = getRootCell(cells.find(startRow * width + startCol));
            startRow = rootCell[0];
            startCol = rootCell[1];
        }

        return maze;
    }

    /*
     * Get the coordinates of the root cell of a set
     */
    private int[] getRootCell(int root) {
        int row = root / width;
        int col = root % width;
        return new int[]{row, col};
    }

    /*
     * displayMaze
     * prints out each cell as a 3x3 grid of characters, 
     * and uses the bit representation of the walls to determine which characters to print.
     * @param maze - the maze to be printed
     */
    public static void displayMaze(int[][] maze) {
        int width = maze[0].length;
        int height = maze.length;
    
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
}
