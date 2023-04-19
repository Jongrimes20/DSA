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

        return maze;
    }

    /*
     * solveMaze
     * @param maze - the maze to solve
     */
}

