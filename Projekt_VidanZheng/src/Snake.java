import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main class for the Snake game. This class initializes the game window and handles
 * the game's lifecycle events such as closing the window.
 */
public class Snake extends JFrame {

    /**
     * Constructs a Snake object and initializes the game window.
     */
    public Snake() {
        initUI();
    }

    /**
     * Initializes the user interface for the Snake game.
     * Sets up the game board, window properties, and window listener for game control.
     */
    private void initUI() {
        Snake_Board board = new Snake_Board();
        add(board);

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add WindowListener to stop the game when the window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                board.stopGame();
            }
        });
    }

    /**
     * The main method to launch the Snake game application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
