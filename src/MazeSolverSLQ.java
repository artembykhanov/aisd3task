import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MazeSolverSLQ {


    private int[][] maze;
    private int[][] distance;
    private int[][] prev;
    private int startRow, startCol, endRow, endCol;

    public MazeSolverSLQ(int[][] maze, int startRow, int startCol, int endRow, int endCol) {
        this.maze = maze;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;

        int numRows = maze.length;
        int numCols = maze[0].length;

        distance = new int[numRows][numCols];
        prev = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                distance[i][j] = Integer.MAX_VALUE;
                prev[i][j] = -1;
            }
        }

        distance[startRow][startCol] = 0;
        prev[startRow][startCol] = -1;
    }

    public List<int[]> solveSLQ() throws Exception {
        List<int[]> path = new ArrayList<>();

        SimpleLinkedListQueue2<int[]> queue2 = new SimpleLinkedListQueue2<>();
        queue2.add(new int[]{startRow, startCol});

        while (queue2.count() != 0) {
            int[] current = queue2.remove();
            int row = current[0];
            int col = current[1];

            if (row == endRow && col == endCol) {
                break;
            }

            int[] dx = {0, 0, 1, -1};
            int[] dy = {1, -1, 0, 0};

            for (int i = 0; i < 4; i++) {
                int newRow = row + dx[i];
                int newCol = col + dy[i];

                if (newRow < 0 || newRow >= maze.length || newCol < 0 || newCol >= maze[0].length) {
                    continue;
                }

                if (maze[newRow][newCol] == 1) {
                    continue;
                }

                int newDistance = distance[row][col] + 1;

                if (newDistance < distance[newRow][newCol]) {
                    distance[newRow][newCol] = newDistance;
                    prev[newRow][newCol] = row * maze[0].length + col;
                    queue2.add(new int[]{newRow, newCol});
                }
            }
        }

        int row = endRow;
        int col = endCol;

        while (prev[row][col] != -1) {
            path.add(0, new int[]{row, col});
            int prevRow = prev[row][col] / maze[0].length;
            int prevCol = prev[row][col] % maze[0].length;
            row = prevRow;
            col = prevCol;
        }

        if (!path.isEmpty()) {
            path.add(0, new int[]{startRow, startCol});
        }

        return path;
    }
}
