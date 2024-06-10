import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The main class for the Arcade game. This class initializes the arcade frame
 * and handles the display of individual game frames.
 */
public class Arcade extends JFrame {
    private JFrame currentGameFrame;

    /**
     * Constructs an Arcade object and initializes the arcade window.
     */
    public Arcade() {
        setTitle("Arcade");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ArcadePanel arcadePanel = new ArcadePanel(this);
        getContentPane().add(arcadePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Displays the specified game frame. If a game is already running, it closes
     * the current game frame before opening the new one.
     *
     * @param gameFrame the JFrame of the game to be displayed.
     */
    public void showGameFrame(JFrame gameFrame) {
        if (currentGameFrame != null) {
            currentGameFrame.dispose();
        }
        currentGameFrame = gameFrame;
        gameFrame.setTitle("Arcade");
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setLocationRelativeTo(this);
        gameFrame.setVisible(true);
    }

    /**
     * The main method to launch the Arcade application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Arcade::new);
    }
}

/**
 * The panel class for the Arcade. This class represents the grid layout of the
 * arcade and handles user input for navigating the grid and selecting games.
 */
class ArcadePanel extends JPanel {
    private static final int GRID_SIZE = 5;
    private static final int CELL_SIZE = 80;
    private static final int ARC_WIDTH = 400;
    private static final int ARC_HEIGHT = 400;

    private int playerX = GRID_SIZE / 2;
    private int playerY = GRID_SIZE / 2;
    private final Arcade arcade;

    /**
     * Constructs an ArcadePanel object.
     *
     * @param arcade the parent Arcade object
     */
    public ArcadePanel(Arcade arcade) {
        this.arcade = arcade;
        setPreferredSize(new Dimension(ARC_WIDTH, ARC_HEIGHT));
        setFocusable(true);
        setOpaque(false);
        setLayout(null);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    /**
     * Handles key press events for navigating the arcade grid and selecting games.
     *
     * @param e the KeyEvent triggered by a key press
     */
    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (playerX > 0) playerX--;
                break;
            case KeyEvent.VK_RIGHT:
                if (playerX < GRID_SIZE - 1) playerX++;
                break;
            case KeyEvent.VK_UP:
                if (playerY > 0) playerY--;
                break;
            case KeyEvent.VK_DOWN:
                if (playerY < GRID_SIZE - 1) playerY++;
                break;
            case KeyEvent.VK_ENTER:
                enterMachine();
                break;
        }
        repaint();
    }

    /**
     * Opens the selected game based on the player's current position in the grid.
     */
    private void enterMachine() {
        if (playerX == 0 && playerY == 0) {
            arcade.showGameFrame(new Snake());
        } else if (playerX == 2 && playerY == 0) {
            arcade.showGameFrame(new BloonsTowerDefense());
        } else if (playerX == 4 && playerY == 0) {
            arcade.showGameFrame(new WhackAMole());
        } else if (playerX == 0 && playerY == 4) {
            arcade.showGameFrame(new Pong());
        } else if (playerX == 2 && playerY == 4) {
            arcade.showGameFrame(new Tetris());
        } else if (playerX == 4 && playerY == 4) {
            arcade.showGameFrame(new GrapplingHook());
        }
    }

    /**
     * Paints the arcade grid and game icons.
     *
     * @param g the Graphics object for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        drawMachine(g, 0, 0, "Snake");
        drawMachine(g, 2, 0, "Bloons");
        drawMachine(g, 4, 0, " Mole");
        drawMachine(g, 0, 4, " Pong");
        drawMachine(g, 2, 4, "Tetris");
        drawMachine(g, 4, 4, "Hook");

        g.setColor(Color.RED);
        int smallerSize = CELL_SIZE / 3;
        int offset = (CELL_SIZE - smallerSize) / 2;
        g.fillRect(playerX * CELL_SIZE + offset, playerY * CELL_SIZE + offset, smallerSize, smallerSize);
    }

    /**
     * Draws a game machine icon on the grid.
     *
     * @param g    the Graphics object for drawing
     * @param x    the x-coordinate of the grid cell
     * @param y    the y-coordinate of the grid cell
     * @param text the text label of the game machine
     */
    private void drawMachine(Graphics g, int x, int y, String text) {
        g.setColor(Color.BLUE);
        g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        g.setColor(Color.WHITE);
        g.drawString(text, x * CELL_SIZE + CELL_SIZE / 2 - 15, y * CELL_SIZE + CELL_SIZE / 2 + 5);
    }
}
