import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

interface CellView {

    public JPanel getView();
    public void display();
}

public class Cell implements CellView {
    boolean isOpened;
    boolean isFlagged;

    JButton button;
    JPanel panel;
    JLabel label;

    public Cell(int width, int height, MouseListener action, String labelText) {
        isOpened = false;
        isFlagged = false;

        button = new JButton();
        button.setSize(width, height);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button.addMouseListener(action);

        label = new JLabel(labelText, SwingConstants.CENTER);
        label.setSize(width, height);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel = new JPanel();
        panel.setSize(width,height);

        panel.setLayout(null);
        panel.add(button);
        panel.add(label);
    }

    public JPanel getView() {
        return panel;
    }

    public void open() throws Exception {
        if (!isOpened && !isFlagged) {
            isOpened = true;
            button.setVisible(false);
            label.setVisible(true);
        }
        else if (isOpened) {
            throw new Exception("Cell is already opened");
        }
    }

    public void flag() throws Exception {
        if (!isOpened && !isFlagged) {
            isFlagged = true;
            button.setText("F");
        }
        else if (!isOpened) {
            isFlagged = false;
        }
        else {
            throw new Exception("Cell is already opened");
        }
    }

    public void unflag() throws Exception {
        if (isFlagged) {
            isFlagged = false;
            button.setText("");
        }
        else {
            throw new Exception("Cell is not flagged");
        }
    }

    public void display() {
        panel.setVisible(true);
        button.setVisible(true);
    }
}

class EmptyCell extends Cell {
    public EmptyCell(int width, int height, MouseListener action) {
        super(width, height, action, "");
    }
}

class NumberCell extends Cell {
    int cellNum;

    public NumberCell(int width, int height, MouseListener action, int numMines) {
        super(width, height, action, String.valueOf(numMines));
        cellNum = numMines;
    }
}

class MineCell extends Cell {
    public MineCell(int width, int height, MouseListener action) {
        super(width, height, action, "X");
    }
}