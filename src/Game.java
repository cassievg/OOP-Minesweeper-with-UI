import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.LinkedList;
import javax.swing.*;

// Time counter is in the form of a thread, so that the program can multitask (and time the game) without affecting the speed of the game itself.
class TimeCounter extends Thread {
    Game game;
    int milliseconds = 0;
    int seconds = 0;
    int minutes = 0;
    String secondsString;
    String minutesString;
    boolean ongoing = false;
    static int interval = 100;

    // Board initialisation
    public TimeCounter(Game curGame) {
        game = curGame;
    }


    @Override
    public void run() {
        // Try/catch condition. Catches errors of the thread.
        // Since a new thread is created for every game, the exception is ignored (as it will only give an error message when the thread has been interrupted, which does not affect the game whatsoever).
        try {
            // Will continuously run when the ongoing variable is true.
            while (ongoing) {
                // Pause the time counter for the given interval (controls how fast the timer goes).
                TimeCounter.sleep(interval);
                // Since I set the interval to count by milliseconds, it will add the interval to the milliseconds variable for every count.
                milliseconds += interval;

                // Continuously calculate the seconds using the milliseconds.
                seconds = milliseconds/1000;

                // This is to make the program count minutes too.
                if (seconds == 60) {
                    // When seconds has reached 60 (1 min), set the seconds and milliseconds back to 0, and add 1 to the minutes.
                    seconds = 0;
                    milliseconds = 0;
                    minutes += 1;
                }

                // To ensure that the timer is always in the form of MM:SS, if the seconds is still 1 digit, it will add the 0 in front of it so it looks like 01, 02, 03, etc.
                // This will be the seconds string that will be displayed on the frame.
                if (seconds < 10) {
                    secondsString = "0" + seconds;
                }
                // Otherwise if it is already 2 digits, just append it to the seconds string.
                else {
                    secondsString = String.valueOf(seconds);
                }

                // This is the same as the seconds string, but for minutes. If the minutes is still 1 digit, add 0 to the front of the minutes string.
                if (minutes < 10) {
                    minutesString = "0" + minutes;
                }
                // Otherwise if it is already 2 digits, just append it to the minutes string.
                else {
                    minutesString = String.valueOf(minutes);
                }

                // Put the minutes and seconds strings onto the game's frame title, in the form of MM:SS.
                game.frame.setTitle("Minesweeper - " + minutesString + ":" + secondsString + " - " + game.numFlags);
            }
        } catch (Exception ignored) {
        }
    }


    public void start() {
        // Start is one of the methods for threads. So to start the thread, the ongoing variable will be set to true, and use super method to also use the thread's default start function alongside.
        ongoing = true;
        super.start();
    }
}


// Game action class controls all mouse actions in the game (board).
class GameAction implements MouseListener {
    Game game;
    Board board;
    int row;
    int col;

    public GameAction(Game curGame, Board curBoard, int cellRow, int cellCol) {
        game = curGame;
        board = curBoard;
        row = cellRow;
        col = cellCol;
    }


    // This method will return the cell at the given row and column.
    public Cell getCell() {
        return board.cells[row][col];
    }


    // Override the mouse clicked function to run my own custom functions.
    @Override
    public void mouseClicked(MouseEvent e) {
        // When a cell is clicked, the cell will first be identified (mine cell, empty cell, or number cell).
        Cell cell = getCell();

        // Once again, this try/catch will ignore errors since the errors won't stop the game (and never occurs anyway).
        try {
            // Check whether the cell has been right-clicked or left-clicked. This first condition is for right click.
            if (e.isMetaDown()) {
                // Check the number of flags remaining first. If the cell has not been flagged and the number of flags is still above 0, and the player can and will flag the cell upon right click.
                if (game.numFlags > 0 && !cell.isFlagged) {
                    // Flag the cell and reduce the flag count by 1.
                    game.numFlags--;
                    cell.flag();
                }
                // If the cell is already flagged, the player will unflag the cell upon right click.
                else if (cell.isFlagged) {
                    // Unflag the cell and increase the flag count by 1.
                    game.numFlags++;
                    cell.unflag();
                }
            }
            // If it is left click, then the player will open the cell.
            else if (e.getButton() == MouseEvent.BUTTON1) {
                // Check if the game has not yet ended (still ongoing), the cell clicked is within the board (valid position), and the cell is not opened yet.
                if (!game.end && board.inBounds(row, col) && !cell.isOpened) {
                    // Check if the cell is an empty cell and is not flagged.
                    if (cell instanceof EmptyCell && !cell.isFlagged) {
                        // Cascade open the cell.
                        game.cascadeOpen(board, row, col);
                    }
                    // Otherwise if the cell is not an empty cell, open it (open only that individual cell).
                    else {
                        cell.open();
                    }

                    // Afterward it will check if the opened cell is a mine cell or not.
                    if (cell instanceof MineCell) {
                        // If it is indeed a mine cell, then the game ends with a loss.
                        game.lose(board);
                    }
                    // Otherwise if it is not a mine cell, it will check if the board has been completed.
                    else {
                        game.checkWin(board);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }


    // The remaining functions below are left blank since they will not be used for the game.
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


// The game menu class is responsible for the creation and the function of the menu (top left of the frame).
class GameMenu implements ActionListener{
    Game game;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem newGame;

    public GameMenu(Game curGame) {
        game = curGame;

        // First create a menu bar (the top area of the frame).
        menuBar = new JMenuBar();
        // Then a menu (the button that will be displayed at top left of the frame).
        menu = new JMenu("Menu");
        // And the menu item itself, new game.
        newGame = new JMenuItem("New Game");

        // Add the action listener (created down below) to the menu item.
        newGame.addActionListener(this);

        // Add the new game item to the menu
        menu.add(newGame);

        // Add the menu to the menu bar.
        menuBar.add(menu);
    }


    // This method returns the menu bar as JMenuBar.
    public JMenuBar getView() {
        return menuBar;
    }


    // When the menu item, new game, is clicked, it will call game setup (basically halts the current game and brings the user back to the settings).
    @Override
    public void actionPerformed(ActionEvent e) {
        game.setup();
    }
}


// The game class controls everything else about the game and how it will run.
public class Game {
    JFrame frame;
    GameMenu menuBar;
    TimeCounter timer = new TimeCounter(this);

    int[] buttonSize;
    boolean end;
    int numRow;
    int numCol;
    int difficulty;
    int numFlags;

    Settings settings;

    public Game() {
        // Create a settings for the game.
        settings = new Settings();
        // Create a settings action that will control the game's settings actions.
        SettingsAction settingsAction = new SettingsAction(this, settings);
        // Handle the submit function of the settings action (will add the settings action listener to the settings).
        settings.handleSubmit(settingsAction);

        // Create a menu bar for the game.
        menuBar = new GameMenu(this);

        // Create the game frame.
        frame = new JFrame();
        // The button size will be used for all the buttons (the cells) in the board.
        buttonSize = new int[]{20, 20};
    }


    // This method will set the settings of the game (used for the submit settings) according to the user input in the settings frame.
    public void setSettings(int row, int col, int diff) {
        numRow = row;
        numCol = col;
        difficulty = diff;
    }


    // This method is to collect all the positions surrounding a cell (the 8 squares around it) in a linked list.
    private static LinkedList<int[]> getSurrounding(int row, int col) {
        // Manual calculations for each position, stored in an array.
        int[] upperLeft = {row - 1, col - 1};
        int[] upperMid = {row - 1, col};
        int[] upperRight = {row - 1, col + 1};
        int[] left = {row, col - 1};
        int[] right = {row, col + 1};
        int[] bottomLeft = {row + 1, col - 1};
        int[] bottomMid = {row + 1, col};
        int[] bottomRight = {row + 1, col + 1};

        // Create a linked list for the results.
        LinkedList<int[]> surroundingCoords = new LinkedList<>();

        // Add all the above calculated results into the linked list.
        surroundingCoords.add(upperLeft);
        surroundingCoords.add(upperMid);
        surroundingCoords.add(upperRight);
        surroundingCoords.add(left);
        surroundingCoords.add(right);
        surroundingCoords.add(bottomLeft);
        surroundingCoords.add(bottomMid);
        surroundingCoords.add(bottomRight);

        // Return the list of arrays.
        return surroundingCoords;
    }


    // This method is for the losing condition of the board.
    public void lose(Board generatedBoard) throws Exception {
        // Go through every cell in the board, accessed by position.
        for (int row = 0; row < generatedBoard.totalRow; row++) {
            for (int col = 0; col < generatedBoard.totalCol; col++) {
                // Check if a mine cell has been opened.
                if (generatedBoard.cells[row][col] instanceof MineCell && generatedBoard.cells[row][col].isOpened) {
                    // If a mine cell has been opened, open all the mine cells existing in the board (lose screen will show all the mines in the current board).
                    for (int i = 0; i < generatedBoard.totalRow; i++) {
                        for (int j = 0; j < generatedBoard.totalCol; j++) {
                            // To display all the mines, open all the mine cells that have yet to be opened.
                            if (!generatedBoard.cells[i][j].isOpened && generatedBoard.cells[i][j] instanceof MineCell) {
                                generatedBoard.cells[i][j].open();
                            }
                        }
                    }
                    // The game will end when a mine cell is opened.
                    end = true;
                    // The timer will be stopped.
                    timer.interrupt();
                    // The title will display as below (show that the player has lost, keep the timer paused and the number of remaining flags displayed).
                    frame.setTitle("You lose! - " + timer.minutesString + ":" + timer.secondsString + " - " + numFlags);
                    break;
                }
            }
        }
    }


    // This method is for the winning condition of the board.
    public void checkWin(Board generatedBoard) {
        // First set the win boolean to true. The checking conditions will change this when necessary.
        boolean win = true;

        // Go through every cell in the board, accessed by position.
        for (int row = 0; row < generatedBoard.totalRow; row++) {
            for (int col = 0; col < generatedBoard.totalCol; col++) {
                // The player has not won yet if there are still cells aside from mine cells (number cells and empty cells) not opened.
                if (!(generatedBoard.cells[row][col] instanceof MineCell) && !generatedBoard.cells[row][col].isOpened) {
                    win = false;
                }
            }
        }

        // Win will stay true if all the cells aside from mine cells are opened.
        if (win) {
            // The game will end with a win.
            end = true;
            // The timer will be stopped.
            timer.interrupt();
            // The title will display as below (show that the player has wpn, keep the timer paused and the number of remaining flags displayed).
            frame.setTitle("You win! - " + timer.minutesString + ":" + timer.secondsString + " - " + numFlags);
        }
    }


    // This method is to calculate the number of mines surrounding a cell.
    public int calculateMines(Board generatedBoard, int row, int col) throws Exception {
        int totalMines = 0;

        // Get the surrounding positions of the cell using the getSurrounding method.
        LinkedList<int[]> surroundingCoords = getSurrounding(row, col);

        // Check first if the current cell itself is a mine cell (this method is to determine whether it is a number or empty cell).
        if (!(generatedBoard.cells[row][col] instanceof MineCell)) {
            // Go through all the surrounding positions
            for (int[] coord : surroundingCoords) {
                // Check if the positions in the list are in bounds and is a mine cell.
                if (generatedBoard.inBounds(coord[0], coord[1])
                        && generatedBoard.cells[coord[0]][coord[1]] instanceof MineCell) {
                    // If it is a mine cell, add the total mines by 1.
                    totalMines += 1;
                }
            }
        }
        // Will throw exception if a mine cell has been selected instead.
        else {
            throw new Exception("Mine cell selected.");
        }

        // Return the total number of mines.
        return totalMines;
    }


    // This method will assign the cells in the board (as either empty cell or number cell).
    public Cell assignCell(Board generatedBoard, int row, int col) throws Exception {
        Cell assignedCell;
        // Calculate the number of mines surrounding that cell.
        int numMines = calculateMines(generatedBoard, row, col);

        // Game action to control what will happen when a button is interacted with.
        GameAction gameAction = new GameAction(this, generatedBoard, row, col);

        // If there are no mines around that cell...
        if (numMines == 0) {
            // It will be assigned as an empty cell with the button sizes previously provided and the game action previously made.
            assignedCell = new EmptyCell(buttonSize[0], buttonSize[1], gameAction);
        }
        // If there are mines around that cell...
        else {
            // It will be assigned as a number cell with the button sizes previously provided and the game action previously made, with the number assignment being the number of mines around it.
            assignedCell = new NumberCell(buttonSize[0], buttonSize[1], gameAction, numMines);
        }

        // Return the cell after its assignment.
        return assignedCell;
    }


    // This method will generate/create the game board.
    public Board generateBoard() throws Exception {
        // Create a new board with the provided number of row and column (according to user input from the settings frame).
        Board generatedBoard = new Board(numRow, numCol);

        // Set the size of the frame to be the size of the buttons multiplied by the number of row and column.
        // Added a bit of space for the width because the cell borded won't appear if it's too compact.
        frame.setSize(numCol*buttonSize[1] + 20,numRow*buttonSize[0]);

        // This variable will store the intended number of mines in the board.
        int numMines;
        // Calculate the total cells of the board by row * column.
        int boardSize = numRow * numCol;

        // Number of mines will change according to the difficulty.
        numMines = switch (difficulty) {
            // Difficulty 1 (easy) will have the number of mines as 12% of the total cells.
            case 1 -> (int) (boardSize * 0.12);
            // Difficulty 2 (normal) will have the number of mines as 15% of the total cells.
            case 2 -> (int) (boardSize * 0.15);
            // Difficulty 3 (hard) will have the number of mines as 20% of the total cells.
            case 3 -> (int) (boardSize * 0.20);
            // Throw exception if it's an invalid difficulty.
            default -> throw new Exception("Difficulty not found");
        };

        // The number of flags is set to the number of mines.
        // Basically allows the player to keep track of the mines they have found.
        numFlags = numMines;

        // Get the positions that allows mines to be placed (cannot be at the corners of the board).
        LinkedList<int[]> mineCoordList = getMinesValidPosition(generatedBoard);

        // This variable is to keep track of the number of mines generated in the board.
        int boardMineCounter = 0;

        // Board generation will begin by assigning the mine cells first.
        // This will keep going until the number of mines already placed in the board has reached numMines.
        while (boardMineCounter != numMines) {
            // Use Random util.
            Random random = new Random();
            // Randomly select one of the positions from the list with the valid possible positions for the mines.
            int randomIndex = random.nextInt(mineCoordList.size() - 1);

            // Get the row and column of the randomly-selected position.
            int randomRow = mineCoordList.get(randomIndex)[0];
            int randomCol = mineCoordList.get(randomIndex)[1];

            // Create game action for the mines later.
            GameAction gameAction = new GameAction(this, generatedBoard, randomRow, randomCol);

            // Check if that position does not have a cell yet.
            if (generatedBoard.cells[randomRow][randomCol] == null) {
                // If it does not, then allow placing of mine cell, and give it the button size and game action.
                generatedBoard.place(new MineCell(buttonSize[0], buttonSize[1], gameAction), randomRow, randomCol);

                // Add the board mine counter by 1.
                boardMineCounter += 1;

                // Remove the assigned position from the list.
                mineCoordList.remove(randomIndex);
            }
            // This loop will continue until the number of mines on the board matches the intended number of mines previously determined..
        }

        // For the rest of the cells, it will simply assign its identity using the assignCell method.
        for (int i = 0; i < generatedBoard.totalRow; i++) {
            for (int j = 0; j < generatedBoard.totalCol; j++) {
                if (!(generatedBoard.cells[i][j] instanceof MineCell)) {
                    generatedBoard.cells[i][j] = assignCell(generatedBoard, i, j);
                }
            }
        }

        // Initialise the board generation UI.
        generatedBoard.initUI();

        // Return the generated board.
        return generatedBoard;
    }


    // This method is to list down all positions that mines can be placed.
    private static LinkedList<int[]> getMinesValidPosition (Board generatedBoard) {
        LinkedList<int[]> mineList = new LinkedList<>();

        // It will list down every single position on the board...
        for (int i = 0; i < generatedBoard.totalRow; i++) {
            for (int j = 0; j < generatedBoard.totalCol; j++) {
                // ...except for the 4 corners.
                if (
                        !((i == 0 && j == 0)
                        || (i == 0 && j == generatedBoard.totalCol - 1)
                        || (i == generatedBoard.totalRow - 1 && j == generatedBoard.totalCol - 1)
                        || (i == generatedBoard.totalRow - 1 && j == 0))
                ) {
                    int[] coord = {i, j};
                    mineList.add(coord);
                }
            }
        }
        return mineList;
    }


    // Cascade open is a feature in minesweeper, where when the player clicks on an empty cell, it will open all adjacent empty cells until the nearest number cells.
    public void cascadeOpen(Board generatedBoard, int row, int col) throws Exception {
        // First check if the selected cell is an empty cell.
        if (generatedBoard.cells[row][col] instanceof EmptyCell) {
            // A stack will be used for this.
            LinkedList<int[]> Stack = new LinkedList<>();

            // This is the starting position where the empty cell was initially clicked.
            int[] startingCoord = {row, col};

            // Open that cell first.
            generatedBoard.cells[row][col].open();

            // Add that position to the stack.
            Stack.add(startingCoord);

            // This loop will occur until the stack is empty.
            while (!Stack.isEmpty()) {
                // The spread will always occur from the position at the very top of the stack (the last position inserted).
                int spreadRow = Stack.getLast()[0];
                int spreadCol = Stack.getLast()[1];

                // After putting the values of the top of the stack into variables, remove this position.
                Stack.removeLast();

                // Check first if the cell on top of it is an empty cell.
                if (generatedBoard.inBounds(spreadRow - 1, spreadCol)
                        && generatedBoard.cells[spreadRow - 1][spreadCol] instanceof EmptyCell) {
                    // If it's an empty cell, check if it's open.
                    if (!generatedBoard.cells[spreadRow - 1][spreadCol].isOpened) {
                        // If it's not open yet, take the position of that cell...
                        int[] upSpread = {spreadRow - 1, spreadCol};

                        // ...and add it into the stack.
                        Stack.add(upSpread);

                        // Then open that cell.
                        generatedBoard.cells[spreadRow - 1][spreadCol].open();
                    }
                }

                // Check first if the cell below it is an empty cell.
                if (generatedBoard.inBounds(spreadRow + 1, spreadCol)
                        && generatedBoard.cells[spreadRow + 1][spreadCol] instanceof EmptyCell) {
                    // If it's an empty cell, check if it's open.
                    if (!generatedBoard.cells[spreadRow + 1][spreadCol].isOpened) {
                        // If it's not open yet, take the position of that cell...
                        int[] downSpread = {spreadRow + 1, spreadCol};

                        // ...and add it into the stack.
                        Stack.add(downSpread);

                        // Then open that cell.
                        generatedBoard.cells[spreadRow + 1][spreadCol].open();
                    }
                }

                // Check first if the cell to the left of it is an empty cell.
                if (generatedBoard.inBounds(spreadRow, spreadCol - 1)
                        && generatedBoard.cells[spreadRow][spreadCol - 1] instanceof EmptyCell) {
                    // If it's an empty cell, check if it's open.
                    if (!generatedBoard.cells[spreadRow][spreadCol - 1].isOpened) {
                        // If it's not open yet, take the position of that cell...
                        int[] leftSpread = {spreadRow, spreadCol - 1};

                        // ...and add it into the stack.
                        Stack.add(leftSpread);

                        generatedBoard.cells[spreadRow][spreadCol - 1].open();
                    }
                }

                // Check first if the cell to the right of it is an empty cell.
                if (generatedBoard.inBounds(spreadRow, spreadCol + 1)
                        && generatedBoard.cells[spreadRow][spreadCol + 1] instanceof EmptyCell) {
                    // If it's an empty cell, check if it's open.
                    if (!generatedBoard.cells[spreadRow][spreadCol + 1].isOpened) {
                        // If it's not open yet, take the position of that cell...
                        int[] rightSpread = {spreadRow, spreadCol + 1};

                        // ...and add it into the stack.
                        Stack.add(rightSpread);

                        generatedBoard.cells[spreadRow][spreadCol + 1].open();
                    }
                }

                // Check first if the cell top left of it is an empty cell.
                if (generatedBoard.inBounds(spreadRow - 1, spreadCol - 1)
                        && generatedBoard.cells[spreadRow - 1][spreadCol - 1] instanceof EmptyCell) {
                    // If it's an empty cell, check if it's open.
                    if (!generatedBoard.cells[spreadRow - 1][spreadCol - 1].isOpened) {
                        // If it's not open yet, take the position of that cell...
                        int[] upLeftSpread = {spreadRow - 1, spreadCol - 1};

                        // ...and add it into the stack.
                        Stack.add(upLeftSpread);

                        generatedBoard.cells[spreadRow - 1][spreadCol - 1].open();
                    }
                }

                // Check first if the cell bottom left of it is an empty cell.
                if (generatedBoard.inBounds(spreadRow + 1, spreadCol - 1)
                        && generatedBoard.cells[spreadRow + 1][spreadCol - 1] instanceof EmptyCell) {
                    // If it's an empty cell, check if it's open.
                    if (!generatedBoard.cells[spreadRow + 1][spreadCol - 1].isOpened) {
                        // If it's not open yet, take the position of that cell...
                        int[] downLeftSpread = {spreadRow + 1, spreadCol - 1};

                        // ...and add it into the stack.
                        Stack.add(downLeftSpread);

                        generatedBoard.cells[spreadRow + 1][spreadCol - 1].open();
                    }
                }

                // Check first if the cell top right of it is an empty cell.
                if (generatedBoard.inBounds(spreadRow - 1, spreadCol + 1)
                        && generatedBoard.cells[spreadRow - 1][spreadCol + 1] instanceof EmptyCell) {
                    // If it's an empty cell, check if it's open.
                    if (!generatedBoard.cells[spreadRow - 1][spreadCol + 1].isOpened) {
                        // If it's not open yet, take the position of that cell...
                        int[] upRightSpread = {spreadRow - 1, spreadCol + 1};

                        // ...and add it into the stack.
                        Stack.add(upRightSpread);

                        generatedBoard.cells[spreadRow - 1][spreadCol + 1].open();
                    }
                }

                // Check first if the cell bottom right of it is an empty cell.
                if (generatedBoard.inBounds(spreadRow + 1, spreadCol + 1)
                        && generatedBoard.cells[spreadRow + 1][spreadCol + 1] instanceof EmptyCell) {
                    // If it's an empty cell, check if it's open.
                    if (!generatedBoard.cells[spreadRow + 1][spreadCol + 1].isOpened) {
                        // If it's not open yet, take the position of that cell...
                        int[] downRightSpread = {spreadRow + 1, spreadCol + 1};

                        // ...and add it into the stack.
                        Stack.add(downRightSpread);

                        generatedBoard.cells[spreadRow + 1][spreadCol + 1].open();
                    }
                }
            }

            // The above group of conditions only opens the adjacent empty cells, next isto open the number cells beside where the spread stops.
            // Go through the whole board.
            for (int i = 0; i < generatedBoard.totalRow; i++) {
                for (int j = 0; j < generatedBoard.totalCol; j++) {
                    // Check every cell if it's an empty cell and has already been opened (by the cascade).
                    if (generatedBoard.cells[i][j] instanceof EmptyCell && generatedBoard.cells[i][j].isOpened) {
                        // Get the positions around the empty cells.
                        LinkedList<int[]> surround = getSurrounding(i, j);

                        // For every position around the empty cell, check if they're in bounds and if they have not been opened yet.
                        for (int[] surroundCoord : surround) {
                            if (generatedBoard.inBounds(surroundCoord[0], surroundCoord[1])
                                    && !generatedBoard.cells[surroundCoord[0]][surroundCoord[1]].isOpened) {
                                // If it passes the above conditions, open it.
                                generatedBoard.cells[surroundCoord[0]][surroundCoord[1]].open();
                            }
                        }
                    }
                }
            }
        }
    }


    // This is for the game set up.
    public void setup() {
        // Hides the current game frame.
        frame.setVisible(false);
        // Display the settings frame.
        settings.getView().setVisible(true);
    }


    // This is to begin a new game.
    public void startGame() throws Exception {
        // Interrupt the timer so that the program can make a new one for the new game later.
        timer.interrupt();

        // Set end to false so the game can begin.
        end = false;
        // Display the settings frame first to allow difficulty and size selection.
        settings.getView().setVisible(false);

        // Generate a new board.
        Board board = generateBoard();
        // Create a new timer for this game.
        timer = new TimeCounter(this);

        // Remove everything from the current frame (if there's a game running, it will be removed).
        frame.getContentPane().removeAll();

        // Add the newly generated board into the frame.
        frame.add(board.getView());
        // Add the menu bar into the frame.
        frame.setJMenuBar(menuBar.getView());

        // Disallow resizing (because it may ruin the board grid and layout.
        frame.setResizable(false);
        // Display the frame.
        frame.setVisible(true);

        // Begin the timer
        timer.start();

        // Exit the application if the user closes the frame.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
