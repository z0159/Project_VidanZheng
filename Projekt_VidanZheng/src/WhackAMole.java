import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class WhackAMole extends JFrame {
    private static final int GRID_SIZE = 4;
    private static final int MOLE_APPEARANCE_DELAY = 1000; // in milliseconds

    private JButton[][] buttons;
    private int score;
    private Timer timer;
    private Random random;

    public WhackAMole() {
        super("Whack-a-Mole");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        buttons = new JButton[GRID_SIZE][GRID_SIZE];
        score = 0;
        random = new Random();

        initializeButtons();
        startGame();
    }

    private void initializeButtons() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(Color.LIGHT_GRAY);
                button.setOpaque(true);
                buttons[i][j] = button;
                add(button);

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
    }

    private void startGame() {
        timer = new Timer(MOLE_APPEARANCE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int randomX = random.nextInt(GRID_SIZE);
                int randomY = random.nextInt(GRID_SIZE);
                showMole(randomX, randomY);
            }
        });
        timer.start();
    }

    private void showMole(int x, int y) {
        buttons[x][y].setBackground(Color.RED);
        new Timer(MOLE_APPEARANCE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideMole(x, y);
            }
        }).start();
    }

    private void hideMole(int x, int y) {
        buttons[x][y].setBackground(Color.LIGHT_GRAY);
    }

    private void handleButtonClick(int x, int y) {
        if (buttons[x][y].getBackground() == Color.RED) {
            score++;
            buttons[x][y].setBackground(Color.LIGHT_GRAY);
        } else {
            score--;
        }
        updateScore();
    }

    private void updateScore() {
        System.out.println("Score: " + score);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WhackAMole().setVisible(true);
            }
        });
    }
}
