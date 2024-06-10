import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The Tetris_Board class represents the Tetris game board.
 */
public class Tetris_Board extends JPanel implements ActionListener {

    /** The width of the board. */
    private final int BOARD_WIDTH = 10;

    /** The height of the board. */
    private final int BOARD_HEIGHT = 22;

    /** The delay for the game timer. */
    private final int DELAY = 300;

    /** The game timer. */
    private Timer timer;

    /** Flag to indicate if the current piece has finished falling. */
    private boolean isFallingFinished = false;

    /** Flag to indicate if the game is paused. */
    private boolean isPaused = false;

    /** The current X position of the piece. */
    private int curX = 0;

    /** The current Y position of the piece. */
    private int curY = 0;

    /** The current Tetris piece. */
    private Tetris_Shape curPiece;

    /** The Tetris board. */
    private Tetris_Shape.Tetrominoes[] board;

    /** The status bar to display information. */
    private JLabel statusBar;

    /**
     * Constructs a new Tetris board.
     */
    public Tetris_Board() {
        initBoard();
        newPiece();
    }

    /**
     * Initializes the Tetris board.
     */
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

    /**
     * Gets the width of a single square on the board.
     *
     * @return The width of a square.
     */
    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    /**
     * Gets the height of a single square on the board.
     *
     * @return The height of a square.
     */
    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    /**
     * Gets the shape at the specified position on the board.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The shape at the specified position.
     */
    private Tetris_Shape.Tetrominoes shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    /**
     * Clears the Tetris board.
     */
    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetris_Shape.Tetrominoes.NoShape;
        }
    }

    /**
     * Moves the current piece down until it reaches the bottom.
     */
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

    /**
     * Moves the current piece down by one square.
     */
    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    /**
     * Handles the event when a piece is dropped.
     */
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

    /**
     * Creates a new Tetris piece.
     */
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

    /**
     * Attempts to move a Tetris piece to the specified position.
     *
     * @param newPiece The Tetris piece to move.
     * @param newX     The new X coordinate for the piece.
     * @param newY     The new Y coordinate for the piece.
     * @return True if the move is successful, false otherwise.
     */
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

    /**
     * Removes full lines from the board and updates the status bar.
     */
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
            String currentText = statusBar.getText().trim();
            statusBar.setText(String.valueOf(Integer.parseInt(currentText) + numFullLines));
            isFallingFinished = true;
            curPiece.setShape(Tetris_Shape.Tetrominoes.NoShape);
        }
    }

    /**
     * Handles the drawing of the Tetris board and pieces.
     *
     * @param g The Graphics object used for drawing.
     */
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

    /**
     * Draws a square representing a Tetris piece.
     *
     * @param g     The Graphics object used for drawing.
     * @param x     The x-coordinate of the square.
     * @param y     The y-coordinate of the square.
     * @param shape The shape of the Tetris piece.
     */
    private void drawSquare(Graphics g, int x, int y, Tetris_Shape.Tetrominoes shape) {
        Color colors[] = {new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
                new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0), new Color(1, 1, 1)};

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

    /**
     * Paints the Tetris board and pieces on the panel.
     *
     * @param g The Graphics object used for painting.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    /**
     * ActionListener implementation that handles the game logic and animation.
     *
     * @param e The ActionEvent associated with the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece(); // Call newPiece() to create a new piece after the current one has finished falling
        } else {
            oneLineDown();
        }
    }

    /**
     * KeyAdapter subclass to handle keyboard input for controlling the game.
     */
    private class TAdapter extends KeyAdapter {
        /**
         * Handles key presses for controlling the game.
         *
         * @param e The KeyEvent associated with the key press.
         */
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

    /**
     * Pauses or resumes the game.
     */
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