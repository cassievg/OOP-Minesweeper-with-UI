import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;

class SettingsAction implements ActionListener {
    Game game;
    Settings settings;

    public SettingsAction(Game curGame, Settings curSettings) {
        game = curGame;
        settings = curSettings;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            game.setSettings(settings.boardRow, settings.boardCol, settings.difficulty);
            game.startGame();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

public class Settings {
    JFrame menuFrame = new JFrame();
    JButton submitButton = new JButton("Start");

    int boardRow;
    int boardCol;
    int difficulty;

    public void handleSubmit(ActionListener menuAction) {
        submitButton.addActionListener(menuAction);
    }

    public void gameSettings(){
        AtomicBoolean sizeSet = new AtomicBoolean(false);
        AtomicBoolean difficultySet = new AtomicBoolean(false);

        JPanel sizePanel = new JPanel();
        JPanel difficultyPanel = new JPanel();

        GridLayout gridLayout = new GridLayout(1, 4, 5, 10);

        menuFrame = new JFrame();
        menuFrame.setSize(500,400);
        menuFrame.setLayout(null);

        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setBounds(0, 50, 75, 40);
        sizePanel.add(sizeLabel);

        JButton sizeButton1 = new JButton("15x15");
        JButton sizeButton2 = new JButton("20x20");
        JButton sizeButton3 = new JButton("30x30");

        sizeButton1.setSize(75,40);
        sizeButton2.setSize(75,40);
        sizeButton3.setSize(75,40);

        ActionListener sizeButton1Listener = e -> {
            boardRow = 15;
            boardCol = 15;
            sizeButton1.setBackground(Color.LIGHT_GRAY);

            sizeButton2.setBackground(null);
            sizeButton3.setBackground(null);

            sizeSet.set(true);

            if (difficultySet.get()) {
                submitButton.setEnabled(true);
            }
        };
        sizeButton1.addActionListener(sizeButton1Listener);

        ActionListener sizeButton2Listener = e -> {
            boardRow = 20;
            boardCol = 20;
            sizeButton2.setBackground(Color.LIGHT_GRAY);

            sizeButton1.setBackground(null);
            sizeButton3.setBackground(null);

            sizeSet.set(true);

            if (difficultySet.get()) {
                submitButton.setEnabled(true);
            }
        };
        sizeButton2.addActionListener(sizeButton2Listener);

        ActionListener sizeButton3Listener = e -> {
            boardRow = 30;
            boardCol = 30;
            sizeButton3.setBackground(Color.LIGHT_GRAY);

            sizeButton1.setBackground(null);
            sizeButton2.setBackground(null);

            sizeSet.set(true);

            if (difficultySet.get()) {
                submitButton.setEnabled(true);
            }
        };
        sizeButton3.addActionListener(sizeButton3Listener);

        JLabel difficultyLabel = new JLabel("Difficulty: ");
        difficultyLabel.setBounds(0, 50, 75, 40);
        difficultyPanel.add(difficultyLabel);

        JButton difficultyButton1 = new JButton("Easy");
        JButton difficultyButton2 = new JButton("Normal");
        JButton difficultyButton3 = new JButton("Hard");

        difficultyButton1.setSize(75,40);
        difficultyButton2.setSize(75,40);
        difficultyButton3.setSize(75,40);

        ActionListener difficultyButton1Listener = e -> {
            difficulty = 1;
            difficultyButton1.setBackground(Color.LIGHT_GRAY);

            difficultyButton2.setBackground(null);
            difficultyButton3.setBackground(null);

            difficultySet.set(true);

            if (sizeSet.get()) {
                submitButton.setEnabled(true);
            }
        };
        difficultyButton1.addActionListener(difficultyButton1Listener);

        ActionListener difficultyButton2Listener = e -> {
            difficulty = 2;
            difficultyButton2.setBackground(Color.LIGHT_GRAY);

            difficultyButton1.setBackground(null);
            difficultyButton3.setBackground(null);

            difficultySet.set(true);

            if (sizeSet.get()) {
                submitButton.setEnabled(true);
            }
        };
        difficultyButton2.addActionListener(difficultyButton2Listener);

        ActionListener difficultyButton3Listener = e -> {
            difficulty = 3;
            difficultyButton3.setBackground(Color.LIGHT_GRAY);

            difficultyButton1.setBackground(null);
            difficultyButton2.setBackground(null);

            difficultySet.set(true);

            if (sizeSet.get()) {
                submitButton.setEnabled(true);
            }
        };
        difficultyButton3.addActionListener(difficultyButton3Listener);

        sizePanel.add(sizeButton1);
        sizePanel.add(sizeButton2);
        sizePanel.add(sizeButton3);

        sizePanel.setBounds(50, 50, 400, 40);
        sizePanel.setLayout(gridLayout);

        difficultyPanel.add(difficultyButton1);
        difficultyPanel.add(difficultyButton2);
        difficultyPanel.add(difficultyButton3);

        difficultyPanel.setBounds(50, 150, 400, 40);
        difficultyPanel.setLayout(gridLayout);

        submitButton.setBounds(200, 250, 100, 40);

        menuFrame.add(sizePanel);
        menuFrame.add(difficultyPanel);

        menuFrame.add(submitButton);
        submitButton.setEnabled(false);

        menuFrame.setResizable(false);
        menuFrame.setVisible(true);
    }

    Settings() {
        gameSettings();
    }

    public JFrame getView() {
        return menuFrame;
    }

    public static void main(String[] args) {
        new Settings();
    }
}
