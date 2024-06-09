import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Tetris_Board extends JPanel implements ActionListener {

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;
    private final int DELAY = 300;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int curX = 0;
    private int curY = 0;
    private Tetris_Shape curPiece;
    private Tetris_Shape.Tetrominoes[] board;
    private JLabel statusBar;
    public Tetris_Board() {
        initBoard();
        newPiece();
    }

    private void initBoard() {
        setFocusable(true);
        statusBar = new JLabel(" 0");
        add(statusBar, BorderLayout.SOUTH);
        board = new Tetris_Shape.Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
        addKeyListener(new TAdapter());
        clearBoard();
        setPreferredSize(new Dimension(300, 600));
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    private Tetris_Shape.Tetrominoes shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetris_Shape.Tetrominoes.NoShape;
        }
    }

    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }
        removeFullLines();
        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void newPiece() {
        curPiece = new Tetris_Shape();
        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();
        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(Tetris_Shape.Tetrominoes.NoShape);
            timer.stop();
            statusBar.setText("Game Over");
        }
    }


    private boolean tryMove(Tetris_Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (shapeAt(x, y) != Tetris_Shape.Tetrominoes.NoShape) {
                return false;
            }
        }
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Tetris_Shape.Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }
        if (numFullLines > 0) {
            String currentText = statusBar.getText().trim(); // Trim whitespace from current text
            statusBar.setText(String.valueOf(Integer.parseInt(currentText) + numFullLines)); // Update the text after parsing and adding the number of full lines
            isFallingFinished = true;
            curPiece.setShape(Tetris_Shape.Tetrominoes.NoShape);
        }
    }


    private void doDrawing(Graphics g) {
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetris_Shape.Tetrominoes shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Tetris_Shape.Tetrominoes.NoShape) {
                    drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
                }
            }
        }
        if (curPiece.getShape() != Tetris_Shape.Tetrominoes.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, x * squareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(), curPiece.getShape());
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, Tetris_Shape.Tetrominoes shape) {
        Color colors[] = {new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102), new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204), new Color(218, 170, 0), new Color(1, 1, 1)};

        var color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece(); // Call newPiece() to create a new piece after the current one has finished falling
        } else {
            oneLineDown();
        }
    }


    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!isPaused) {
                int keycode = e.getKeyCode();
                if (keycode == KeyEvent.VK_P) {
                    pause();
                }
                if (curPiece.getShape() == Tetris_Shape.Tetrominoes.NoShape) {
                    return;
                }
                switch (keycode) {
                    case KeyEvent.VK_LEFT -> tryMove(curPiece, curX - 1, curY);
                    case KeyEvent.VK_RIGHT -> tryMove(curPiece, curX + 1, curY);
                    case KeyEvent.VK_DOWN -> tryMove(curPiece.rotateRight(), curX, curY);
                    case KeyEvent.VK_UP -> tryMove(curPiece.rotateLeft(), curX, curY);
                    case KeyEvent.VK_SPACE -> dropDown();
                    case KeyEvent.VK_D -> oneLineDown();
                }
            }
        }
    }

    private void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            statusBar.setText("Paused");
        } else {
            statusBar.setText(String.valueOf(Integer.parseInt(statusBar.getText())));
        }
        repaint();
    }
}

