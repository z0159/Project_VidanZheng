import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Arcade extends JFrame implements ActionListener {
    private JButton snakeButton;
    private JButton bloonsButton;
    private JButton whackAMoleButton;
    private JFrame currentGameFrame;

    public Arcade() {
        setTitle("Arcade");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(1, 3));

        snakeButton = new JButton("Snake");
        bloonsButton = new JButton("Bloons Tower Defense");
        whackAMoleButton = new JButton("Whack A Mole");

        snakeButton.addActionListener(this);
        bloonsButton.addActionListener(this);
        whackAMoleButton.addActionListener(this);

        mainPanel.add(snakeButton);
        mainPanel.add(bloonsButton);
        mainPanel.add(whackAMoleButton);

        getContentPane().add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        if (sourceButton == snakeButton) {
            showGameFrame(new Snake());
        } else if (sourceButton == bloonsButton) {
            showGameFrame(new BloonsTowerDefense());
        } else if (sourceButton == whackAMoleButton) {
            showGameFrame(new WhackAMole());
        }
    }

    private void showGameFrame(JFrame gameFrame) {
        if (currentGameFrame != null) {
            currentGameFrame.dispose(); // Close the current game frame
        }
        currentGameFrame = gameFrame;
        gameFrame.setTitle("Arcade");
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose when closed
        gameFrame.pack(); // Pack to set the frame size based on its contents
        gameFrame.setLocationRelativeTo(this); // Center the game frame relative to the main frame
        gameFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Arcade::new);
    }
}
