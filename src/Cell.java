import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

// Interface CellView, as the name says, is used for viewing the cell.
interface CellView {
    JPanel getView();
}


// This class applies for all cells.
public class Cell implements CellView {
    boolean isOpened;
    boolean isFlagged;

    // Every cell will be represented as a panel. This panel will consist of a button, and underneath the button is the label that tells what cell it is, such as a bomb icon for mines, and coloured numbers for number cells.
    JButton button;
    JPanel panel;
    JLabel label;

    // Cell initialisation
    public Cell(int width, int height, MouseListener action, String labelText) {
        // Cells, when initialised, will not be opened nor flagged.
        isOpened = false;
        isFlagged = false;

        // Create the button for the cell.
        button = new JButton();
        // Give the button a size. This size will technically represent the size of the cells in this game.
        button.setSize(width, height);
        // Give the button a black border.
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Give the button a mouse listener that will trigger when the button is clicked.
        button.addMouseListener(action);

        // Create the label for the cell.
        label = new JLabel(labelText, SwingConstants.CENTER);
        // Give the label the same size as the button (or "cell").
        label.setSize(width, height);
        // Give the label a black border.
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Create the panel for the cell.
        panel = new JPanel();
        // Give the panel the same size as the button (or "cell").
        panel.setSize(width,height);

        // Setting its layout to null so that it does not stick to its default. The default layout distorts the board, so this part is crucial for the display.
        panel.setLayout(null);

        // Add the other components, button and label, into the cell panel.
        panel.add(button);
        panel.add(label);
    }


    // This method will return the cell panel.
    public JPanel getView() {
        return panel;
    }


    // This method is for opening the cells.
    public void open() throws Exception {
        // First check if the cell can be opened. The cell must be closed and must not be flagged.
        if (!isOpened && !isFlagged) {
            // If the cell met the above conditions, open the cell by setting its isOpened attribute to true.
            isOpened = true;
            // In turn, it will make the button invisible in the UI...
            button.setVisible(false);
            // ...and make the label underneath that button visible to reveal the identity of the opened cell.
            label.setVisible(true);
        }
        // Throw an exception if the cell has already been opened.
        else if (isOpened) {
            throw new Exception("Cell is already opened");
        }
    }


    // This method is for flagging cells, preventing it from being opened.
    public void flag() throws Exception {
        // First check if the cell can be flagged. The cell must be closed and not already flagged.
        if (!isOpened && !isFlagged) {
            // If the cell met the above condition, flag the cell by setting its isFlagged attribute to true.
            isFlagged = true;
            // In turn, the button will display a flag icon.
            button.setText("\uD83D\uDEA9");
        }
        // Throw an exception if the cell has already been opened.
        else {
            throw new Exception("Cell cannot be flagged");
        }
    }


    // This method is to unflag the cell.
    public void unflag() throws Exception {
        // First check if the cell can be unflagged. The cell must already be flagged.
        if (isFlagged) {
            // If the cell met the above condition, unflag the cell by setting its isFlagged attribute to false.
            isFlagged = false;
            // In turn, the button's flag icon will be removed by setting the button text to an empty string.
            button.setText("");
        }
        // Throw an exception if the cell has not been flagged.
        else {
            throw new Exception("Cell is not flagged");
        }
    }
}


// Empty cell class, which is a cell without any mines around it.
class EmptyCell extends Cell {
    // Empty cell will inherit from its parent class, Cell, with an empty label.
    public EmptyCell(int width, int height, MouseListener action) {
        super(width, height, action, "");
    }
}


// Number cell class, which is a cell that will represent the number of mines around it.
class NumberCell extends Cell {
    int cellNum;

    // Number cell will inherit from its parent class, Cell, with the number of mines around it as the label.
    public NumberCell(int width, int height, MouseListener action, int numMines) {
        super(width, height, action, String.valueOf(numMines));

        // Set colours for every number.
        switch (numMines) {
            case 1:
                label.setForeground(Color.decode("#3734fe"));
                break;
            case 2:
                label.setForeground(Color.decode("#10942e"));
                break;
            case 3:
                label.setForeground(Color.decode("#fe0606"));
                break;
            case 4:
                label.setForeground(Color.decode("#2c3c49"));
                break;
            case 5:
                label.setForeground(Color.decode("#825d0d"));
                break;
            case 6:
                label.setForeground(Color.decode("#149071"));
                break;
            case 7:
                label.setForeground(Color.decode("#221c31"));
                break;
            case 8:
                label.setForeground(Color.decode("#000000"));
                break;
            default:
                System.out.println("Invalid number of mines");
        }

        cellNum = numMines;
    }
}


// Mine cell class, which will trigger game lose when opened.
class MineCell extends Cell {
    // Mine cell will inherit from its parent class, Cell, with a mine icon.
    public MineCell(int width, int height, MouseListener action) {
        super(width, height, action, "\uD83D\uDCA3");
    }
}