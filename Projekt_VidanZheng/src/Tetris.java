import javax.swing.*;
import java.awt.*;

/**
 * The Tetris class represents the main frame of the Tetris game.
 */
public class Tetris extends JFrame {

    /**
     * Constructs a new Tetris game.
     */
    public Tetris() {
        initUI();
    }

    /**
     * Initializes the user interface components of the game.
     */
    private void initUI() {
        add(new Tetris_Board());
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Main method to start the Tetris game.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Tetris tetris = new Tetris();
            tetris.setVisible(true);
        });
    }
}
