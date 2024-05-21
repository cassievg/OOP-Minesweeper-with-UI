import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;

// Settings action is responsible for the functionality of the settings window. This class contains the code that will be triggered when any of the buttons are clicked.
class SettingsAction implements ActionListener {
    // Settings implements ActionListener, which is responsible in tracking the button interactions and triggering a set of code once the button is clicked.
    Game game;
    Settings settings;

    // Settings initialisation
    public SettingsAction(Game curGame, Settings curSettings) {
        game = curGame;
        settings = curSettings;
    }


    // To customise the code or function of the button, the actionPerformed method from ActionListener must be overriden, and the code to be triggered upon button interaction is inside.
    @Override
    public void actionPerformed(ActionEvent e) {
        // This button is only used for the submit button in the menu window.
        try {
            // When clicked, the settings will call the game to set its size and difficulty as entered by the player.
            game.setSettings(settings.boardRow, settings.boardCol, settings.difficulty);
            // Upon clicking submit, the settings will call the game once again to start a new game with the set size and difficulty.
            game.startGame();
        // Catch exception for error handling.
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}


public class Settings {
    // Create a new frame for the settings.
    JFrame settingsFrame = new JFrame();
    // Create a submit button for the frame with displayed text "Start".
    JButton submitButton = new JButton("Start");

    int boardRow;
    int boardCol;
    int difficulty;


    // This function will simply add the action listener previously created, settingsAction, into the submit button.
    public void handleSubmit(ActionListener settingsAction) {
        submitButton.addActionListener(settingsAction);
    }


    // This function is to display and properly organise every component that will be in the settings window, and display that window. In short, this is for the creation of the settings window.
    public void gameSettings(){
        // Atomic booleans are used to alter the value of both size set and difficulty set.
        // This is used because it supports operations on underlying variables, and these two variables are in the action performed class.
        // Therefore, atomic boolean is most suitable for this.
        AtomicBoolean sizeSet = new AtomicBoolean(false);
        AtomicBoolean difficultySet = new AtomicBoolean(false);

        // The size and difficulty selectors will each have their own panels to keep the window organised.
        JPanel sizePanel = new JPanel();
        JPanel difficultyPanel = new JPanel();

        // A grid layout with 1 row, 4 columns, 5 horizontal gap, and 10 vertical gap, is used for each of the panel to give them an organised, table-like look.
        GridLayout gridLayout = new GridLayout(1, 4, 5, 10);

        // Create the frame for the settings window.
        settingsFrame = new JFrame();
        // Give the settings frame the following size.
        settingsFrame.setSize(500,400);
        // Setting its layout to null so that it does not stick to its default.
        // The default layout distorts the window, and the grid layout would have no effect on it.
        settingsFrame.setLayout(null);

        // Size selector creation
        // Create the label for the size selection with displayed text "Size:".
        JLabel sizeLabel = new JLabel("Size:");
        // Set the position of the label to x = 0 and y = 0, with width of 75 and height of 40.
        sizeLabel.setBounds(0, 50, 75, 40);
        // Add the label component into the panel for the size selection.
        sizePanel.add(sizeLabel);

        // Create 3 buttons for the size options, with text representing the sizes of the board.
        JButton sizeButton1 = new JButton("15x15");
        JButton sizeButton2 = new JButton("20x20");
        JButton sizeButton3 = new JButton("30x30");

        // Set the size of the 3 buttons. I set it to the size of the label just so that it looks neat.
        sizeButton1.setSize(75,40);
        sizeButton2.setSize(75,40);
        sizeButton3.setSize(75,40);

        // The 3 action listeners below are for the size selector buttons.
        // Create an action listener for the size selection's first option.
        ActionListener sizeButton1Listener = e -> {
            // The first button will set the board row and column count to 15.
            boardRow = 15;
            boardCol = 15;
            // The button is given a light gray background once it is clicked to notify the player that this size has been selected.
            sizeButton1.setBackground(Color.LIGHT_GRAY);

            // Remove the background from the other 2 options.
            // The aim of this is so that when clicked on a button, the display will only show the latest clicked button, which is the size that will be used by the board.
            // To do so, the backgrounds of the other buttons are set to null once this button has been clicked.
            sizeButton2.setBackground(null);
            sizeButton3.setBackground(null);

            // Using an atomic boolean operation set(), set the sizeSet variable to true, to tell the program that a size has been selected.
            sizeSet.set(true);

            // The difficultySet variable is checked, to see whether the player has selected a difficulty.
            if (difficultySet.get()) {
                // If a difficulty has also been set, the submit button can be clicked and the game can be started.
                submitButton.setEnabled(true);
            }
        };
        // Add this action listener to the first size button.
        sizeButton1.addActionListener(sizeButton1Listener);

        // Create an action listener for the size selection's second option.
        ActionListener sizeButton2Listener = e -> {
            // The second button will set the board row and column count to 20.
            boardRow = 20;
            boardCol = 20;
            // The button is given a light gray background once it is clicked to notify the player that this size has been selected.
            sizeButton2.setBackground(Color.LIGHT_GRAY);

            // Remove the background from the other 2 options.
            // To do so, the backgrounds of the other buttons are set to null once this button has been clicked.
            sizeButton1.setBackground(null);
            sizeButton3.setBackground(null);

            // Using an atomic boolean operation set(), set the sizeSet variable to true, to tell the program that a size has been selected.
            sizeSet.set(true);

            // The difficultySet variable is checked, to see whether the player has selected a difficulty.
            if (difficultySet.get()) {
                // If a difficulty has also been set, the submit button can be clicked and the game can be started.
                submitButton.setEnabled(true);
            }
        };
        // Add this action listener to the second size button.
        sizeButton2.addActionListener(sizeButton2Listener);

        // Create an action listener for the size selection's third option.
        ActionListener sizeButton3Listener = e -> {
            // The third button will set the board row and column count to 30.
            boardRow = 30;
            boardCol = 30;
            // The button is given a light gray background once it is clicked to notify the player that this size has been selected.
            sizeButton3.setBackground(Color.LIGHT_GRAY);

            // Remove the background from the other 2 options.
            // To do so, the backgrounds of the other buttons are set to null once this button has been clicked.
            sizeButton1.setBackground(null);
            sizeButton2.setBackground(null);

            // Using an atomic boolean operation set(), set the sizeSet variable to true, to tell the program that a size has been selected.
            sizeSet.set(true);

            // The difficultySet variable is checked, to see whether the player has selected a difficulty.
            if (difficultySet.get()) {
                // If a difficulty has also been set, the submit button can be clicked and the game can be started.
                submitButton.setEnabled(true);
            }
        };
        // Add this action listener to the third size button.
        sizeButton3.addActionListener(sizeButton3Listener);
        // End of size selector creation

        // Difficulty selector creation
        // Create the label for the difficulty selection with displayed text "Difficulty:".
        JLabel difficultyLabel = new JLabel("Difficulty: ");
        // Set the position of the label to x = 0 and y = 50 (below the size selection), with width of 75 and height of 40.
        difficultyLabel.setBounds(0, 50, 75, 40);
        // Add the label component into the panel for the difficulty selection.
        difficultyPanel.add(difficultyLabel);

        // Create 3 buttons for the difficulty options, with text representing the difficulties of the board.
        JButton difficultyButton1 = new JButton("Easy");
        JButton difficultyButton2 = new JButton("Normal");
        JButton difficultyButton3 = new JButton("Hard");

        // Set the difficulty of the 3 buttons. I set it to the size of the label just so that it looks neat.
        difficultyButton1.setSize(75,40);
        difficultyButton2.setSize(75,40);
        difficultyButton3.setSize(75,40);

        // The 3 action listeners below are for the difficulty selector buttons.
        // Create an action listener for the difficulty selection's first option.
        ActionListener difficultyButton1Listener = e -> {
            // The first button will set the game difficulty to 1 (easy).
            difficulty = 1;
            // The button is given a light gray background once it is clicked to notify the player that this difficulty has been selected.
            difficultyButton1.setBackground(Color.LIGHT_GRAY);

            // Remove the background from the other 2 options.
            // To do so, the backgrounds of the other buttons are set to null once this button has been clicked.
            difficultyButton2.setBackground(null);
            difficultyButton3.setBackground(null);

            // Using an atomic boolean operation set(), set the difficultySet variable to true, to tell the program that a difficulty has been selected.
            difficultySet.set(true);

            // The sizeSet variable is checked, to see whether the player has selected a size.
            if (sizeSet.get()) {
                // If a size has also been set, the submit button can be clicked and the game can be started.
                submitButton.setEnabled(true);
            }
        };
        // Add this action listener to the first difficulty button.
        difficultyButton1.addActionListener(difficultyButton1Listener);

        // Create an action listener for the difficulty selection's second option.
        ActionListener difficultyButton2Listener = e -> {
            // The first button will set the game difficulty to 2 (medium).
            difficulty = 2;
            // The button is given a light gray background once it is clicked to notify the player that this difficulty has been selected.
            difficultyButton2.setBackground(Color.LIGHT_GRAY);

            // Remove the background from the other 2 options.
            // To do so, the backgrounds of the other buttons are set to null once this button has been clicked.
            difficultyButton1.setBackground(null);
            difficultyButton3.setBackground(null);

            // Using an atomic boolean operation set(), set the difficultySet variable to true, to tell the program that a difficulty has been selected.
            difficultySet.set(true);

            // The sizeSet variable is checked, to see whether the player has selected a size.
            if (sizeSet.get()) {
                // If a size has also been set, the submit button can be clicked and the game can be started.
                submitButton.setEnabled(true);
            }
        };
        // Add this action listener to the second difficulty button.
        difficultyButton2.addActionListener(difficultyButton2Listener);

        // Create an action listener for the difficulty selection's third option.
        ActionListener difficultyButton3Listener = e -> {
            // The first button will set the game difficulty to 3 (hard).
            difficulty = 3;
            // The button is given a light gray background once it is clicked to notify the player that this difficulty has been selected.
            difficultyButton3.setBackground(Color.LIGHT_GRAY);

            // Remove the background from the other 2 options.
            // To do so, the backgrounds of the other buttons are set to null once this button has been clicked.
            difficultyButton1.setBackground(null);
            difficultyButton2.setBackground(null);

            // Using an atomic boolean operation set(), set the difficultySet variable to true, to tell the program that a difficulty has been selected.
            difficultySet.set(true);

            // The sizeSet variable is checked, to see whether the player has selected a size.
            if (sizeSet.get()) {
                // If a size has also been set, the submit button can be clicked and the game can be started.
                submitButton.setEnabled(true);
            }
        };
        // Add this action listener to the third difficulty button.
        difficultyButton3.addActionListener(difficultyButton3Listener);

        // Add all the size buttons into the panel.
        sizePanel.add(sizeButton1);
        sizePanel.add(sizeButton2);
        sizePanel.add(sizeButton3);

        // Set the position of the size panel to x = 50 and y = 50, with width of 400 and height of 40.
        // I set the width to the width of the frame and the height to the height of the buttons so that they will fit perfectly without needing to change the position or layout more.
        sizePanel.setBounds(50, 50, 400, 40);
        // Give the size panel the grid layout previously made.
        sizePanel.setLayout(gridLayout);

        // Add all the difficulty buttons into the panel.
        difficultyPanel.add(difficultyButton1);
        difficultyPanel.add(difficultyButton2);
        difficultyPanel.add(difficultyButton3);

        // Set the position of the difficulty panel to x = 50 and y = 150 (below the size panel), with width of 400 and height of 40.
        // I set the width to the width of the frame and the height to the height of the buttons so that they will fit perfectly without needing to change the position or layout more.
        difficultyPanel.setBounds(50, 150, 400, 40);
        // Give the difficulty panel the grid layout previously made.
        difficultyPanel.setLayout(gridLayout);

        // Set the position of the submit button to x = 200 and y = 200, with width of 100 and height of 40.
        // To maintain consistency, I set the submit button's height same as the other buttons.
        submitButton.setBounds(200, 250, 100, 40);

        // Add the size panel into the settings frame.
        settingsFrame.add(sizePanel);
        // Add the difficulty panel into the settings frame.
        settingsFrame.add(difficultyPanel);

        // Add the submit button into the settings frame
        settingsFrame.add(submitButton);
        // Disable the submit button. It will be enabled when both the difficulty and size has already been selected.
        submitButton.setEnabled(false);

        // Set the frame to not be resizable. Resizing the frame may ruin the layout and visuals.
        settingsFrame.setResizable(false);
        // Set the visibility of the settings frame to true to display it.
        settingsFrame.setVisible(true);
    }


    Settings() {
        // Run the game settings.
        gameSettings();
    }


    // This method returns the settings in the form of its frame.
    public JFrame getView() {
        return settingsFrame;
    }


    // This is only for testing.
    public static void main(String[] args) {
        new Settings();
    }
}
