import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WhackAMole is a simple game where the player has to click on moles that randomly appear on a grid.
 * Each mole clicked increases the player's score, while missing a mole decreases the score.
 * The game lasts for a fixed duration, and the player's score is displayed at the end of the game.
 */
public class WhackAMole extends JFrame {
    private static final int GRID_SIZE = 4;
    private static final int MOLE_APPEARANCE_DELAY = 1000; // in milliseconds
    private static final int GAME_DURATION = 30000; // in milliseconds (30 seconds)
    private final Icon moleIcon;
    private static final Color GRASS_GREEN = new Color(0, 204, 68);

    private JButton[][] buttons;
    private int score;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private Timer moleTimer;
    private Timer gameTimer;
    private Timer countdownTimer;
    private Random random;
    private ExecutorService executor;
    private int remainingTime;

    /**
     * Constructor to initialize the WhackAMole game.
     */
    public WhackAMole() {
        super("Whack A Mole");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 450);
        setLayout(new BorderLayout());

        buttons = new JButton[GRID_SIZE][GRID_SIZE];
        score = 0;
        random = new Random();
        executor = Executors.newFixedThreadPool(3); // Allow up to 3 simultaneous moles
        remainingTime = GAME_DURATION / 1000; // Convert to seconds

        int buttonWidth = 400 / GRID_SIZE;
        int buttonHeight = 400 / GRID_SIZE;
        moleIcon = getScaledImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\WhackAMole.png", buttonWidth, buttonHeight);

        initializeButtons();
        initializeScoreLabel();
        initializeTimeLabel();
        startGame();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopGame();
            }
        });
    }

    /**
     * Initializes the grid of buttons representing the game grid.
     */
    private void initializeButtons() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(GRASS_GREEN);
                button.setOpaque(true);
                buttons[i][j] = button;
                gridPanel.add(button);

                int finalI = i;
                int finalJ = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleButtonClick(finalI, finalJ);
                    }
                });
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the label to display the player's score.
     */
    private void initializeScoreLabel() {
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        add(scoreLabel, BorderLayout.NORTH);
    }

    /**
     * Initializes the label to display the remaining time in the game.
     */
    private void initializeTimeLabel() {
        timeLabel = new JLabel("Time left: " + remainingTime + " sec", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        add(timeLabel, BorderLayout.SOUTH);
    }

    /**
     * Starts the game by initiating timers for mole appearance, game duration, and countdown.
     */
    private void startGame() {
        moleTimer = new Timer(MOLE_APPEARANCE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int moleCount = random.nextInt(3) + 1; // 1 to 3 moles
                for (int i = 0; i < moleCount; i++) {
                    executor.execute(() -> {
                        int randomX = random.nextInt(GRID_SIZE);
                        int randomY = random.nextInt(GRID_SIZE);
                        showMole(randomX, randomY);
                    });
                }
            }
        });
        moleTimer.start();

        gameTimer = new Timer(GAME_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endGame();
            }
        });
        gameTimer.setRepeats(false);
        gameTimer.start();

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                timeLabel.setText("Time left: " + remainingTime + " sec");
                if (remainingTime <= 0) {
                    countdownTimer.stop();
                }
            }
        });
        countdownTimer.start();
    }

    /**
     * Displays a mole icon at the specified position on the game grid.
     *
     * @param x The x-coordinate of the mole.
     * @param y The y-coordinate of the mole.
     */
    private void showMole(int x, int y) {
        buttons[x][y].setIcon(moleIcon);
        buttons[x][y].setBackground(GRASS_GREEN);
        Timer hideTimer = new Timer(MOLE_APPEARANCE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideMole(x, y);
            }
        });
        hideTimer.setRepeats(false);
        hideTimer.start();
    }

    /**
     * Hides the mole icon at the specified position on the game grid.
     *
     * @param x The x-coordinate of the mole.
     * @param y The y-coordinate of the mole.
     */
    private void hideMole(int x, int y) {
        buttons[x][y].setIcon(null);
        buttons[x][y].setBackground(GRASS_GREEN);
    }

    /**
     * Handles the button click event when a player clicks on a button.
     * If the clicked button contains a mole icon, the player's score is increased.
     *
     * @param x The x-coordinate of the clicked button.
     * @param y The y-coordinate of the clicked button.
     */
    private void handleButtonClick(int x, int y) {
        if (buttons[x][y].getIcon() == moleIcon) {
            score++;
            buttons[x][y].setIcon(null);
            buttons[x][y].setBackground(GRASS_GREEN);
        } else {
            score--;
        }
        updateScore();
    }

    /**
     * Updates the score label with the current score.
     */
    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    /**
     * Ends the game by stopping all timers and displaying the final score.
     */
    private void endGame() {
        stopGame();
        JOptionPane.showMessageDialog(this, "Time's up! Your score is: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Stops the game by stopping all timers and shutting down the executor service.
     */
    private void stopGame() {
        if (moleTimer != null) {
            moleTimer.stop();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }

    /**
     * Scales an image icon to the specified width and height.
     *
     * @param imagePath The path to the image file.
     * @param width     The width to scale the image to.
     * @param height    The height to scale the image to.
     * @return The scaled image icon.
     */
    private ImageIcon getScaledImageIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    /**
     * Main method to start the WhackAMole game.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WhackAMole().setVisible(true);
            }
        });
    }
}
