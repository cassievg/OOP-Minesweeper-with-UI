import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.LinkedList;
import javax.swing.*;

class TimeCounter extends Thread {
    Game game;
    int milliseconds = 0;
    int seconds = 0;
    int minutes = 0;
    String secondsString;
    String minutesString;
    boolean ongoing = false;
    static int interval = 100;

    public TimeCounter(Game curGame) {
        game = curGame;
    }

    @Override
    public void run() {
        try {
            while (ongoing) {
                TimeCounter.sleep(interval);
                milliseconds += interval;

                seconds = milliseconds/1000;

                if (seconds == 60) {
                    seconds = 0;
                    minutes += 1;
                }

                if (seconds < 10) {
                    secondsString = "0" + seconds;
                }
                else {
                    secondsString = String.valueOf(seconds);
                }

                if (minutes < 10) {
                    minutesString = "0" + minutes;
                }
                else {
                    minutesString = String.valueOf(minutes);
                }

                game.frame.setTitle("Minesweeper - " + minutesString + ":" + secondsString);
            }
        } catch (Exception ignored) {
        }
    }

    public void start() {
        ongoing = true;
        super.start();
    }

    public void pause() {
        ongoing = false;
    }

    public void reset() {
        milliseconds = 0;
        ongoing = false;
    }
}

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

    public Cell getCell() {
        return board.cells[row][col];
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Cell cell = getCell();

        try {
            if (e.isMetaDown()) {
                if (!cell.isFlagged) {
                    cell.flag();
                }
                else {
                    cell.unflag();
                }
            }
            else if (e.getButton() == MouseEvent.BUTTON1) {
                if (!game.end && board.inBounds(row, col) && !cell.isOpened) {
                    if (cell instanceof EmptyCell && !cell.isFlagged) {
                        game.cascadeOpen(board, row, col);
                    }
                    else {
                        cell.open();
                    }

                    if (cell instanceof MineCell) {
                        game.lose(board);
                    }
                    else {
                        game.checkWin(board);
                    }
                }
            }
        } catch (Exception ignored) {
        }

    }

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

class GameMenu implements ActionListener{
    Game game;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem newGame;

    public GameMenu(Game curGame) {
        game = curGame;

        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        newGame = new JMenuItem("New Game");

        newGame.addActionListener(this);

        menu.add(newGame);

        menuBar.add(menu);
    }

    public JMenuBar getView() {
        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.setup();
    }
}

public class Game {
    JFrame frame;
    GameMenu menuBar;
    TimeCounter timer = new TimeCounter(this);

    int[] buttonSize;
    boolean end;
    int numRow;
    int numCol;
    int difficulty;

    Settings settings;

    public Game() {
        settings = new Settings();
        SettingsAction settingsAction = new SettingsAction(this, settings);
        settings.handleSubmit(settingsAction);

        menuBar = new GameMenu(this);

        frame = new JFrame();
        buttonSize = new int[]{20, 20};
    }

    public void setSettings(int row, int col, int diff) {
        numRow = row;
        numCol = col;
        difficulty = diff;
    }

    private static LinkedList<int[]> getSurrounding(int row, int col) {
        int[] upperLeft = {row - 1, col - 1};
        int[] upperMid = {row - 1, col};
        int[] upperRight = {row - 1, col + 1};
        int[] left = {row, col - 1};
        int[] right = {row, col + 1};
        int[] bottomLeft = {row + 1, col - 1};
        int[] bottomMid = {row + 1, col};
        int[] bottomRight = {row + 1, col + 1};

        LinkedList<int[]> surroundingCoords = new LinkedList<>();

        surroundingCoords.add(upperLeft);
        surroundingCoords.add(upperMid);
        surroundingCoords.add(upperRight);
        surroundingCoords.add(left);
        surroundingCoords.add(right);
        surroundingCoords.add(bottomLeft);
        surroundingCoords.add(bottomMid);
        surroundingCoords.add(bottomRight);

        return surroundingCoords;
    }

    public void lose(Board generatedBoard) throws Exception {
        for (int row = 0; row < generatedBoard.totalRow; row++) {
            for (int col = 0; col < generatedBoard.totalCol; col++) {
                if (generatedBoard.cells[row][col] instanceof MineCell && generatedBoard.cells[row][col].isOpened) {
                    for (int i = 0; i < generatedBoard.totalRow; i++) {
                        for (int j = 0; j < generatedBoard.totalCol; j++) {
                            if (!generatedBoard.cells[i][j].isOpened && generatedBoard.cells[i][j] instanceof MineCell) {
                                generatedBoard.cells[i][j].open();
                            }
                        }
                    }
                    end = true;
                    timer.interrupt();
                    frame.setTitle("You lose! - " + timer.minutesString + ":" + timer.secondsString);
                    break;
                }
            }
        }
    }

    public void checkWin(Board generatedBoard) {
        boolean win = true;

        for (int row = 0; row < generatedBoard.totalRow; row++) {
            for (int col = 0; col < generatedBoard.totalCol; col++) {
                if (!(generatedBoard.cells[row][col] instanceof MineCell) && !generatedBoard.cells[row][col].isOpened) {
                    win = false;
                }
            }
        }

        if (win) {
            end = true;
            timer.interrupt();
            frame.setTitle("You win! - " + timer.minutesString + ":" + timer.secondsString);
        }
    }

    public int calculateMines(Board generatedBoard, int row, int col) throws Exception {
        int totalMines = 0;

        LinkedList<int[]> surroundingCoords = getSurrounding(row, col);

        if (!(generatedBoard.cells[row][col] instanceof MineCell)) {
            for (int[] coord : surroundingCoords) {
                if (generatedBoard.inBounds(coord[0], coord[1])
                        && generatedBoard.cells[coord[0]][coord[1]] instanceof MineCell) {
                    totalMines += 1;
                }
            }
        }
        else {
            throw new Exception("Mine cell selected.");
        }

        return totalMines;
    }

    public Cell assignCell(Board generatedBoard, int row, int col) throws Exception {
        Cell assignedCell = null;
        int numMines = calculateMines(generatedBoard, row, col);

        GameAction gameAction = new GameAction(this, generatedBoard, row, col);

        if (numMines == 0) {
            assignedCell = new EmptyCell(buttonSize[0], buttonSize[1], gameAction);
        } else {
            assignedCell = new NumberCell(buttonSize[0], buttonSize[1], gameAction, numMines);
        }

        return assignedCell;
    }

    public Board generateBoard() throws Exception {
        Board generatedBoard = new Board(numRow, numCol);

        frame.setSize(numCol*20 + 20,numRow*20);

        int numMines = 0;
        int boardSize = numRow * numCol;

        numMines = switch (difficulty) {
            case 1 -> (int) (boardSize * 0.12);
            case 2 -> (int) (boardSize * 0.15);
            case 3 -> (int) (boardSize * 0.20);
            default -> throw new Exception("Difficulty not found");
        };

        LinkedList<int[]> mineCoordList = getMines(generatedBoard);

        int boardMineCounter = 0;

        while (boardMineCounter != numMines) {
            Random random = new Random();
            int randomIndex = random.nextInt(mineCoordList.size() - 1);

            int randomRow = mineCoordList.get(randomIndex)[0];
            int randomCol = mineCoordList.get(randomIndex)[1];

            GameAction gameAction = new GameAction(this, generatedBoard, randomRow, randomCol);

            if (generatedBoard.cells[randomRow][randomCol] == null) {
                generatedBoard.place(new MineCell(buttonSize[0], buttonSize[1], gameAction), randomRow, randomCol);

                boardMineCounter += 1;

                mineCoordList.remove(randomIndex);
            }
        }

        for (int i = 0; i < generatedBoard.totalRow; i++) {
            for (int j = 0; j < generatedBoard.totalCol; j++) {
                if (!(generatedBoard.cells[i][j] instanceof MineCell)) {
                    generatedBoard.cells[i][j] = assignCell(generatedBoard, i, j);
                }
            }
        }

        generatedBoard.initUI();

        return generatedBoard;
    }

    private static LinkedList<int[]> getMines(Board generatedBoard) {
        LinkedList<int[]> mineList = new LinkedList<>();

        for (int i = 0; i < generatedBoard.totalRow; i++) {
            for (int j = 0; j < generatedBoard.totalCol; j++) {
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

    public void cascadeOpen(Board generatedBoard, int row, int col) throws Exception {
        if (generatedBoard.cells[row][col] instanceof EmptyCell) {
            LinkedList<int[]> Stack = new LinkedList<>();

            int[] startingCoord = {row, col};

            generatedBoard.cells[row][col].open();

            Stack.add(startingCoord);

            while (!Stack.isEmpty()) {
                int spreadRow = Stack.getLast()[0];
                int spreadCol = Stack.getLast()[1];

                Stack.removeLast();

                if (generatedBoard.inBounds(spreadRow - 1, spreadCol)
                        && generatedBoard.cells[spreadRow - 1][spreadCol] instanceof EmptyCell) {
                    if (!generatedBoard.cells[spreadRow - 1][spreadCol].isOpened) {
                        int[] upSpread = {spreadRow - 1, spreadCol};

                        Stack.add(upSpread);

                        generatedBoard.cells[spreadRow - 1][spreadCol].open();
                    }
                }

                if (generatedBoard.inBounds(spreadRow + 1, spreadCol)
                        && generatedBoard.cells[spreadRow + 1][spreadCol] instanceof EmptyCell) {
                    if (!generatedBoard.cells[spreadRow + 1][spreadCol].isOpened) {
                        int[] downSpread = {spreadRow + 1, spreadCol};

                        Stack.add(downSpread);

                        generatedBoard.cells[spreadRow + 1][spreadCol].open();
                    }
                }

                if (generatedBoard.inBounds(spreadRow, spreadCol - 1)
                        && generatedBoard.cells[spreadRow][spreadCol - 1] instanceof EmptyCell) {
                    if (!generatedBoard.cells[spreadRow][spreadCol - 1].isOpened) {
                        int[] leftSpread = {spreadRow, spreadCol - 1};

                        Stack.add(leftSpread);

                        generatedBoard.cells[spreadRow][spreadCol - 1].open();
                    }
                }

                if (generatedBoard.inBounds(spreadRow, spreadCol + 1)
                        && generatedBoard.cells[spreadRow][spreadCol + 1] instanceof EmptyCell) {
                    if (!generatedBoard.cells[spreadRow][spreadCol + 1].isOpened) {
                        int[] rightSpread = {spreadRow, spreadCol + 1};

                        Stack.add(rightSpread);

                        generatedBoard.cells[spreadRow][spreadCol + 1].open();
                    }
                }

                if (generatedBoard.inBounds(spreadRow - 1, spreadCol - 1)
                        && generatedBoard.cells[spreadRow - 1][spreadCol - 1] instanceof EmptyCell) {
                    if (!generatedBoard.cells[spreadRow - 1][spreadCol - 1].isOpened) {
                        int[] rightSpread = {spreadRow - 1, spreadCol - 1};

                        Stack.add(rightSpread);

                        generatedBoard.cells[spreadRow - 1][spreadCol - 1].open();
                    }
                }

                if (generatedBoard.inBounds(spreadRow + 1, spreadCol - 1)
                        && generatedBoard.cells[spreadRow + 1][spreadCol - 1] instanceof EmptyCell) {
                    if (!generatedBoard.cells[spreadRow + 1][spreadCol - 1].isOpened) {
                        int[] rightSpread = {spreadRow + 1, spreadCol - 1};

                        Stack.add(rightSpread);

                        generatedBoard.cells[spreadRow + 1][spreadCol - 1].open();
                    }
                }

                if (generatedBoard.inBounds(spreadRow - 1, spreadCol + 1)
                        && generatedBoard.cells[spreadRow - 1][spreadCol + 1] instanceof EmptyCell) {
                    if (!generatedBoard.cells[spreadRow - 1][spreadCol + 1].isOpened) {
                        int[] rightSpread = {spreadRow - 1, spreadCol + 1};

                        Stack.add(rightSpread);

                        generatedBoard.cells[spreadRow - 1][spreadCol + 1].open();
                    }
                }

                if (generatedBoard.inBounds(spreadRow + 1, spreadCol + 1)
                        && generatedBoard.cells[spreadRow + 1][spreadCol + 1] instanceof EmptyCell) {
                    if (!generatedBoard.cells[spreadRow + 1][spreadCol + 1].isOpened) {
                        int[] rightSpread = {spreadRow + 1, spreadCol + 1};

                        Stack.add(rightSpread);

                        generatedBoard.cells[spreadRow + 1][spreadCol + 1].open();
                    }
                }
            }

            for (int i = 0; i < generatedBoard.totalRow; i++) {
                for (int j = 0; j < generatedBoard.totalCol; j++) {
                    if (generatedBoard.cells[i][j] instanceof EmptyCell && generatedBoard.cells[i][j].isOpened) {
                        LinkedList<int[]> surround = getSurrounding(i, j);

                        for (int[] surroundCoord : surround) {
                            if (generatedBoard.inBounds(surroundCoord[0], surroundCoord[1])
                                    && !generatedBoard.cells[surroundCoord[0]][surroundCoord[1]].isOpened) {
                                generatedBoard.cells[surroundCoord[0]][surroundCoord[1]].open();
                            }
                        }
                    }
                }
            }
        }
    }

    public void setup() {
        frame.setVisible(false);
        settings.getView().setVisible(true);
    }

    public void startGame() throws Exception {
        timer.interrupt();

        end = false;
        settings.getView().setVisible(false);

        Board board = generateBoard();
        timer = new TimeCounter(this);

        frame.getContentPane().removeAll();

        frame.add(board.getView());
        frame.setJMenuBar(menuBar.getView());

        frame.setResizable(false);
        frame.setVisible(true);

        timer.start();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
