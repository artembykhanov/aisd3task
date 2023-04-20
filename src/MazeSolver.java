import java.util.*;

public class MazeSolver {

    private int[][] maze;
    private int[][] distance;
    private int[][] prev;
    private int startRow, startCol, endRow, endCol;

    public MazeSolver(int[][] maze, int startRow, int startCol, int endRow, int endCol) {
        this.maze = maze;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;

        int numRows = maze.length;
        int numCols = maze[0].length;

        distance = new int[numRows][numCols];
        prev = new int[numRows][numCols];

        // Дистанция и пред. значения
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                distance[i][j] = Integer.MAX_VALUE;
                prev[i][j] = -1;
            }
        }

        distance[startRow][startCol] = 0;
        prev[startRow][startCol] = -1;
    }

    public List<int[]> solve() {
        List<int[]> path = new ArrayList<>();

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});

        while (!queue.isEmpty()) {
            int[] current = queue.remove();
            int row = current[0];
            int col = current[1];

            // Проверка дошли ли до финиша
            if (row == endRow && col == endCol) {
                break;
            }

            // Для проверки соседей
            int[] dx = {0, 0, 1, -1};
            int[] dy = {1, -1, 0, 0};

            for (int i = 0; i < 4; i++) {
                int newRow = row + dx[i];
                int newCol = col + dy[i];

                if (newRow < 0 || newRow >= maze.length || newCol < 0 || newCol >= maze[0].length) {
                    continue; // Скип если вышли за пределы лабирнта
                }

                if (maze[newRow][newCol] == 1) {
                    continue; // Скип если стена
                }

                int newDistance = distance[row][col] + 1;

                //Короче ли новое расстояние до соседней клетки, чем уже найденное расстояние (distance[newRow][newCol]).
                if (newDistance < distance[newRow][newCol]) {
                    distance[newRow][newCol] = newDistance;
                    //Вычисляем индекс предыдущей клетки в одномерном массиве, используя координаты текущей клетки и размерность по горизонтали
                    prev[newRow][newCol] = row * maze[0].length + col;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }

        // Кратчайший путь
        int row = endRow;
        int col = endCol;

        //Проверяем, не достигли ли мы начальной клетки
        while (prev[row][col] != -1) {
            path.add(0, new int[]{row, col});

            //Вычисляем индекс предыдущей клетки в одномерном массиве
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
