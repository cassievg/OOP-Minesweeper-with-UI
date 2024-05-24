import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main {
    public static void main(String[] args) {
        // Create a new game when the program runs.
        Game game = new Game();
        // Display game setup first for the player to set the settings.
        game.setup();
    }
}