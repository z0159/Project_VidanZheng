import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {

    public Tetris() {
        initUI();
    }

    private void initUI() {
        add(new Tetris_Board());
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 600);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Tetris tetris = new Tetris();
            tetris.setVisible(true);
        });
    }
}
