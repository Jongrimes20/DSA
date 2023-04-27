import java.util.*;

public class MazePath {

    private static MazeGenerator.Maze maze;
    private static boolean[][] visited;
    private static int[][] deltas = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private static char[] directions = {'N', 'E', 'S', 'W'};
    private static String path = "";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Take user input
        System.out.println("Please enter number of rows (2 or more): ");
        int rows = input.nextInt();
        while (rows < 2) {
            System.out.println("Rows must be 2 or more.");
            rows = input.nextInt();
        }

        System.out.println("Please enter number of columns (2 or more): ");
        int columns = input.nextInt();
        while (columns < 2) {
            System.out.println("Columns must be 2 or more.");
            columns = input.nextInt();
        }

        // Generate maze
        MazeGenerator generator = new MazeGenerator();
        maze = generator.generate(rows, columns);

        // Initialize visited array
        visited = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(visited[i], false);
        }

        // Find path
        findPath(0, 0, rows - 1, columns - 1);

        // Output path
        System.out.println("Path: " + path);
    }

    private static void findPath(int row, int col, int endRow, int endCol) {
        visited[row][col] = true;

        if (row == endRow && col == endCol) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            int newRow = row + deltas[i][0];
            int newCol = col + deltas[i][1];

            if (newRow >= 0 && newRow < maze.rows && newCol >= 0 && newCol < maze.columns &&
                    !visited[newRow][newCol] && !hasWall(row, col, i)) {
                path += directions[i];
                findPath(newRow, newCol, endRow, endCol);
                return;
            }
        }
    }

    private static boolean hasWall(int row, int col, int dir) {
        if (dir == 0) {
            return maze.wallHorizontal[col][row];
        } else if (dir == 1) {
            return maze.wallVertical[row][col + 1];
        } else if (dir == 2) {
            return maze.wallHorizontal[col + 1][row];
        } else {
            return maze.wallVertical[row][col];
        }
    }
}
