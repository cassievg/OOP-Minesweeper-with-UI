import javax.swing.*;
import java.awt.*;

public class Board {
    int totalRow;
    int totalCol;
    Cell[][] cells;
    JPanel boardPanel;

    public Board(int row, int col) {
        boardPanel = new JPanel();
        GridLayout boardLayout = new GridLayout(row, col, 0, 0);
        boardPanel.setLayout(boardLayout);
        boardPanel.setBounds(0,0,col*20,row*20);

        cells = new Cell[row][col];
        totalRow = row;
        totalCol = col;
    }

    public JPanel getView() {
        return boardPanel;
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && col >= 0 && row <= totalRow - 1 && col <= totalCol - 1;
    }

    public void place(Cell cell, int row, int col) throws Exception {
        if (inBounds(row, col)) {
            cells[row][col] = cell;
        }
        else {
            throw new Exception("Cell out of bounds");
        }
    }

    public void initUI() {
        for (int i = 0; i < totalRow; i++) {
            for (int j = 0; j < totalCol; j++) {
                boardPanel.add(cells[i][j].getView());
            }
        }
    }
}
