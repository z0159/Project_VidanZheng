import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Arcade extends JFrame {
    private JFrame currentGameFrame;

    public Arcade() {
        setTitle("Arcade");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ArcadePanel arcadePanel = new ArcadePanel(this);
        getContentPane().add(arcadePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Arcade::new);
    }
}

class ArcadePanel extends JPanel {
    private static final int GRID_SIZE = 5;
    private static final int CELL_SIZE = 80;
    private static final int ARC_WIDTH = 400;
    private static final int ARC_HEIGHT = 400;

    private int playerX = GRID_SIZE / 2;
    private int playerY = GRID_SIZE / 2;
    private final Arcade arcade;

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

    private void enterMachine() {
        if (playerX == 0 && playerY == 0) {
            arcade.showGameFrame(new Snake());
        } else if (playerX == 2 && playerY == 0) {
            arcade.showGameFrame(new BloonsTowerDefense());
        } else if (playerX == 4 && playerY == 0) {
            arcade.showGameFrame(new WhackAMole());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the grid
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        drawMachine(g, 0, 0, "Snake");
        drawMachine(g, 2, 0, "Bloons");
        drawMachine(g, 4, 0, " Mole");

        g.setColor(Color.RED);
        int smallerSize = CELL_SIZE / 3;
        int offset = (CELL_SIZE - smallerSize) / 2;
        g.fillRect(playerX * CELL_SIZE + offset, playerY * CELL_SIZE + offset, smallerSize, smallerSize);
    }

    private void drawMachine(Graphics g, int x, int y, String text) {
        g.setColor(Color.BLUE);
        g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        g.setColor(Color.WHITE);
        g.drawString(text, x * CELL_SIZE + CELL_SIZE / 2 - 15, y * CELL_SIZE + CELL_SIZE / 2 + 5);
    }
}