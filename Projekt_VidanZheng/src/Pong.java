import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * The Pong class represents the main frame of the Pong game.
 */
public class Pong extends JFrame {

    private PongPanel pongPanel;

    /**
     * Constructs a new Pong game.
     */
    public Pong() {
        initUI();
    }

    /**
     * Initializes the user interface components of the game.
     */
    private void initUI() {
        pongPanel = new PongPanel();
        add(pongPanel);

        setTitle("Pong");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pongPanel.stopGame();
            }
        });
    }

    /**
     * Main method to start the Pong game.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Pong();
            ex.setVisible(true);
        });
    }
}

/**
 * The PongPanel class represents the main panel where the Pong game is played.
 */
class PongPanel extends JPanel implements ActionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 50;
    private static final int BALL_SIZE = 15;
    private static final int PADDLE_SPEED = 15;
    private static final int BALL_SPEED = 5;

    private int paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballXDir = BALL_SPEED;
    private int ballYDir = BALL_SPEED;

    private boolean paddle1Up = false;
    private boolean paddle1Down = false;
    private boolean paddle2Up = false;
    private boolean paddle2Down = false;

    private int score1 = 0;
    private int score2 = 0;
    private final Random random = new Random();

    private Timer timer;

    /**
     * Constructs a new PongPanel.
     */
    public PongPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TAdapter());

        timer = new Timer(10, this);
        timer.start();
    }

    /**
     * Stops the game by stopping the timer.
     */
    public void stopGame() {
        timer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.setColor(Color.WHITE);

        g.fillRect(20, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - 30, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        g.fillRect(ballX, ballY, BALL_SIZE, BALL_SIZE);

        g.setFont(new Font("Helvetica", Font.BOLD, 18));
        g.drawString(String.valueOf(score1), WIDTH / 4, 20);
        g.drawString(String.valueOf(score2), 3 * WIDTH / 4, 20);
    }

    private void movePaddles() {
        if (paddle1Up && paddle1Y > 0) {
            paddle1Y -= PADDLE_SPEED;
        }
        if (paddle1Down && paddle1Y + PADDLE_HEIGHT < HEIGHT) {
            paddle1Y += PADDLE_SPEED;
        }
        if (paddle2Up && paddle2Y > 0) {
            paddle2Y -= PADDLE_SPEED;
        }
        if (paddle2Down && paddle2Y + PADDLE_HEIGHT < HEIGHT) {
            paddle2Y += PADDLE_SPEED;
        }
    }

    private void moveBall() {
        ballX += ballXDir;
        ballY += ballYDir;

        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballYDir = -ballYDir;
        }

        if (ballX <= 30 && ballY + BALL_SIZE >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballXDir = BALL_SPEED;
        } else if (ballX >= WIDTH - 50 && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballXDir = -BALL_SPEED;
        }

        if (ballX < 0) {
            score2++;
            resetBall();
        } else if (ballX > WIDTH) {
            score1++;
            resetBall();
        }

        if (score1 == 10 || score2 == 10) {
            String winner = score1 == 10 ? "Player 1" : "Player 2";
            JOptionPane.showMessageDialog(this, winner + " won!");
            resetGame();
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;

        ballXDir = random.nextBoolean() ? BALL_SPEED : -BALL_SPEED;
        ballYDir = random.nextBoolean() ? BALL_SPEED : -BALL_SPEED;
    }

    private void resetGame() {
        score1 = 0;
        score2 = 0;
        resetBall();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePaddles();
        moveBall();
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_W) {
                paddle1Up = true;
            }

            if (key == KeyEvent.VK_S) {
                paddle1Down = true;
            }

            if (key == KeyEvent.VK_UP) {
                paddle2Up = true;
            }

            if (key == KeyEvent.VK_DOWN) {
                paddle2Down = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_W) {
                paddle1Up = false;
            }

            if (key == KeyEvent.VK_S) {
                paddle1Down = false;
            }

            if (key == KeyEvent.VK_UP) {
                paddle2Up = false;
            }

            if (key == KeyEvent.VK_DOWN) {
                paddle2Down = false;
            }
        }
    }
}
