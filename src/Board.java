import javax.swing.*;
import java.awt.*;

public class Board {
    int totalRow;
    int totalCol;

    // The board will be a two-dimensional array of cells.
    Cell[][] cells;

    // The board will have its own panel, which will contain all the cells and only cells.
    JPanel boardPanel;

    // Board initialisation
    public Board(int row, int col) {
        // Create a new panel for the board.
        boardPanel = new JPanel();

        // Create a layout which will organise the board into a grid of size row x col, with horizontal and vertical gap of 0.
        GridLayout boardLayout = new GridLayout(row, col, 0, 0);
        // Set the panel into the previously created layout.
        boardPanel.setLayout(boardLayout);

        // Create the cells in the form of a two-dimensional array in the board.
        cells = new Cell[row][col];

        // The parameters row and col will be placed into the totalRow and totalCol variables, as they represent the total number of rows and columns, or size of the board.
        totalRow = row;
        totalCol = col;
    }


    // This method returns the board panel.
    public JPanel getView() {
        return boardPanel;
    }


    // This method checks if the position is within the board, and therefore a valid position.
    public boolean inBounds(int row, int col) {
        return row >= 0 && col >= 0 && row <= totalRow - 1 && col <= totalCol - 1;
    }


    // This method places a cell at the given position in the board.
    public void place(Cell cell, int row, int col) throws Exception {
        // Check if the provided position is within the board.
        if (inBounds(row, col)) {
            // If the position is valid, a cell will be placed in that position.
            cells[row][col] = cell;
        }
        // If the position is invalid, throw an exception.
        else {
            throw new Exception("Cell out of bounds");
        }
    }


    // This method initialises the UI.
    public void initUI() {
        // Using the getView method, it will reveal all the cells in the board using a "for" loop.
        for (int i = 0; i < totalRow; i++) {
            for (int j = 0; j < totalCol; j++) {
                boardPanel.add(cells[i][j].getView());
            }
        }
    }
}
