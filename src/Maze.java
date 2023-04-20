import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.*;

public class Maze implements ActionListener {
    private static int numRows = 6;
    private static int numCols = 6;
    private static final int startRows = 0;
    private static final int startCols = 0;
    private static final int finalRows = numRows - 1;
    private static final int finalCols = numCols - 1;
    private final boolean[][] mazeBool;
    private final JTable table;
    private JButton startJavaButton, startSLQButton, importButton, outputButton, clearButton;

    public Maze(int numRows, int numCols) {

        this.numRows = numRows;
        this.numCols = numCols;
        this.mazeBool = new boolean[numRows][numCols];
        this.table = new JTable(new DefaultTableModel(numRows, numCols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (isWall(row, column)) {
                    component.setBackground(Color.BLACK);
                } else if ((row == startRows && column == startCols) || (row == finalRows && column == finalCols)) {
                    component.setBackground(Color.RED);
                } else if (!isWall(row, column) && table.getValueAt(row, column) == "*") {
                    component.setBackground(Color.RED);
                } else {
                    component.setBackground(Color.WHITE);
                }
                return component;
            }
        };

        table.setRowHeight(20);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                table.getColumnModel().getColumn(j).setPreferredWidth(20);
                table.setValueAt("0", i, j);
            }
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row != -1 && col != -1) {
                    if (isWall(row, col)) {
                        removeWall(row, col);
                    } else {
                        setWall(row, col);
                    }
                }
            }
        });

        startJavaButton = new JButton("Старт Java");
        startJavaButton.addActionListener(this);

        startSLQButton = new JButton("Старт SLQ");
        startSLQButton.addActionListener(this);

        importButton = new JButton("Загрузка");
        importButton.addActionListener(this);

        outputButton = new JButton("Выгрузить");
        outputButton.addActionListener(this);

        clearButton = new JButton("Очистить");
        clearButton.addActionListener(this);
    }

    public void setWall(int row, int col) {
        mazeBool[row][col] = true;
        table.setValueAt("1", row, col);
        table.getCellRenderer(row, col).getTableCellRendererComponent(table, null, true, true, row, col).setBackground(Color.BLACK);
    }

    public void removeWall(int row, int col) {
        mazeBool[row][col] = false;
        table.setValueAt("0", row, col);
        table.getCellRenderer(row, col).getTableCellRendererComponent(table, null, true, true, row, col).setBackground(Color.white);
    }

    public boolean isWall(int row, int col) {
        return mazeBool[row][col];
    }


    public void setRoad(int row, int col) {
        mazeBool[row][col] = false;
        table.setValueAt("*", row, col);
        table.getCellRenderer(row, col).getTableCellRendererComponent(table, null, true, true, row, col).setBackground(Color.RED);
    }


    public JTable getTable() {
        return table;
    }

    public JButton getStartJavaButton() {
        return startJavaButton;
    }

    public JButton getSQLButton() {
        return startSLQButton;
    }

    public JButton getOutputButton() {
        return outputButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getImportButton() {
        return importButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == startJavaButton) {
            try {

                int[][] maze = JTableUtils.readIntMatrixFromJTable(table);
                MazeSolver solver = new MazeSolver(maze, startRows, startCols, finalRows, finalCols);
                List<int[]> path = solver.solve();

                if (path.isEmpty()) {
                    System.out.println("Путей нет");
                } else {
                    System.out.println("Путь найден:");
                    for (int[] cell : path) {
                        System.out.println(cell[0] + "," + cell[1]);
                        setRoad(cell[0], cell[1]);
                    }
                }
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource() == startSLQButton) {

            try {

                int[][] maze = JTableUtils.readIntMatrixFromJTable(table);
                MazeSolverSLQ solverSLQ = new MazeSolverSLQ(maze, startRows, startCols, finalRows, finalCols);
                List<int[]> path = solverSLQ.solveSLQ();

                if (path.isEmpty()) {
                    System.out.println("Путей нет");
                } else {
                    System.out.println("Путь найден:");
                    for (int[] cell : path) {
                        System.out.println(cell[0] + "," + cell[1]);
                        setRoad(cell[0], cell[1]);
                    }
                }
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

        if (e.getSource() == importButton) {
            try {
                Scanner input = new Scanner(new File("src/input.txt"));

                while (input.hasNextInt()) {
                    for (int i = 0; i < numRows; i++) {
                        for (int j = 0; j < numCols; j++) {
                            int a = input.nextInt();
                            if (a == 1) {
                                mazeBool[i][j] = true;
                                table.setValueAt(a, i, j);
                                table.getCellRenderer(i, j).getTableCellRendererComponent(table, null, true, true, i, j).setBackground(Color.BLACK);
                            }
                            if (a == 0) {
                                mazeBool[i][j] = false;
                                table.setValueAt(a, i, j);
                                table.getCellRenderer(i, j).getTableCellRendererComponent(table, null, true, true, i, j).setBackground(Color.WHITE);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        }


        if (e.getSource() == outputButton) {

            try {
                FileWriter fileWriter = new FileWriter("src/output.txt");
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                TableModel model = table.getModel();

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        bufferedWriter.write(model.getValueAt(i, j).toString() + " ");
                    }
                    bufferedWriter.newLine();
                }

                bufferedWriter.close();
                fileWriter.close();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        }

        if (e.getSource() == clearButton) {

            try {
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        mazeBool[i][j] = false;
                        table.setValueAt(0, i, j);
                        table.getCellRenderer(i, j).getTableCellRendererComponent(table, null, true, true, i, j).setBackground(Color.white);
                    }
                }
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }


        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Лабиринт");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Maze maze = new Maze(numRows, numCols);
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(maze.getImportButton(), c);

        c.gridx = 1;
        c.gridy = 0;
        panel.add(maze.getOutputButton(), c);

        c.gridx = 2;
        c.gridy = 0;
        panel.add(maze.getClearButton(), c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        panel.add(maze.getTable(), c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(maze.getStartJavaButton(), c);

        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        panel.add(maze.getSQLButton(), c);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
