import java.awt.*;
import java.util.Random;

/**
 * The Tetris_Shape class represents a Tetris shape.
 */
public class Tetris_Shape {
    /** Enumeration of Tetrominoes shapes. */
    static enum Tetrominoes {
        /** No Shape */ NoShape,
        /** Z Shape */ ZShape,
        /** S Shape */ SShape,
        /** Line Shape */ LineShape,
        /** T Shape */ TShape,
        /** Square Shape */ SquareShape,
        /** L Shape */ LShape,
        /** Mirrored L Shape */ MirroredLShape
    }

    /** The shape of the Tetris piece. */
    private Tetrominoes pieceShape;

    /** The coordinates of the Tetris piece. */
    private int coords[][];

    /** The table of coordinates for all possible Tetris shapes. */
    private int[][][] coordsTable;

    /**
     * Constructs a new Tetris shape.
     */
    public Tetris_Shape() {
        coords = new int[4][2];
        setShape(Tetrominoes.NoShape);
    }

    /**
     * Sets the shape of the Tetris piece.
     *
     * @param shape The shape of the Tetris piece.
     */
    public void setShape(Tetrominoes shape) {
        coordsTable = new int[][][] {
                { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
                { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } },
                { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },
                { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },
                { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },
                { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
                { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
                { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
    }

    /**
     * Sets the X coordinate of a specified index.
     *
     * @param index The index of the coordinate.
     * @param x     The X coordinate value.
     */
    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    /**
     * Sets the Y coordinate of a specified index.
     *
     * @param index The index of the coordinate.
     * @param y     The Y coordinate value.
     */
    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    /**
     * Gets the X coordinate of a specified index.
     *
     * @param index The index of the coordinate.
     * @return The X coordinate value.
     */
    public int x(int index) {
        return coords[index][0];
    }

    /**
     * Gets the Y coordinate of a specified index.
     *
     * @param index The index of the coordinate.
     * @return The Y coordinate value.
     */
    public int y(int index) {
        return coords[index][1];
    }

    /**
     * Gets the shape of the Tetris piece.
     *
     * @return The shape of the Tetris piece.
     */
    public Tetrominoes getShape() {
        return pieceShape;
    }

    /**
     * Sets a random shape for the Tetris piece.
     */
    public void setRandomShape() {
        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoes[] values = Tetrominoes.values();
        setShape(values[x]);
    }

    /**
     * Gets the minimum X coordinate of the Tetris piece.
     *
     * @return The minimum X coordinate.
     */
    public int minX() {
        int m = coords[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }

    /**
     * Gets the minimum Y coordinate of the Tetris piece.
     *
     * @return The minimum Y coordinate.
     */
    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    /**
     * Rotates the Tetris piece to the left.
     *
     * @return The rotated Tetris piece.
     */
    public Tetris_Shape rotateLeft() {
        if (pieceShape == Tetrominoes.SquareShape) {
            return this;
        }
        var result = new Tetris_Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    /**
     * Rotates the Tetris piece to the right.
     *
     * @return The rotated Tetris piece.
     */
    public Tetris_Shape rotateRight() {
        if (pieceShape == Tetrominoes.SquareShape) {
            return this;
        }
        var result = new Tetris_Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}
