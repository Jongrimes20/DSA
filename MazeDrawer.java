package Project3;

import java.util.*;

public class MazeDrawer {
    
    private Maze maze;
    private int start, end;
    private boolean[] visited;
    private List<Integer> path;
    
    public MazeDrawer(Maze maze, int start, int end) {
        this.maze = maze;
        this.start = start;
        this.end = end;
        visited = new boolean[maze.rows * maze.columns];
        path = new ArrayList<>();
        dfs(start);
    }
    
    private void dfs(int vertex) {
        visited[vertex] = true;
        if (vertex == end) {
            path.add(vertex);
            return;
        }
        List<Integer> neighbors = getNeighbors(vertex);
        for (int neighbor : neighbors) {
            if (!visited[neighbor]) {
                dfs(neighbor);
                if (!path.isEmpty()) {
                    path.add(vertex);
                    return;
                }
            }
        }
    }
    
    private List<Integer> getNeighbors(int vertex) {
        int row = vertex / maze.columns;
        int col = vertex % maze.columns;
        List<Integer> neighbors = new ArrayList<>();
        if (row > 0 && !maze.wallHorizontal[col][row - 1]) {
            neighbors.add(vertex - maze.columns);
        }
        if (row < maze.rows - 1 && !maze.wallHorizontal[col][row]) {
            neighbors.add(vertex + maze.columns);
        }
        if (col > 0 && !maze.wallVertical[col - 1][row]) {
            neighbors.add(vertex - 1);
        }
        if (col < maze.columns - 1 && !maze.wallVertical[col][row]) {
            neighbors.add(vertex + 1);
        }
        return neighbors;
    }
    
    public String getPath() {
        StringBuilder sb = new StringBuilder();
        for (int i = path.size() - 1; i >= 1; i--) {
            int curr = path.get(i);
            int prev = path.get(i - 1);
            if (prev == curr - maze.columns) {
                sb.append("N");
            } else if (prev == curr + maze.columns) {
                sb.append("S");
            } else if (prev == curr - 1) {
                sb.append("W");
            } else if (prev == curr + 1) {
                sb.append("E");
            }
        }
        return sb.toString();
    }
}
