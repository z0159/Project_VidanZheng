public class BTD_Rect {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public BTD_Rect(int xCoor, int yCoor, int widthValue, int heightValue) {
        x = xCoor;
        y = yCoor;
        width = widthValue;
        height = heightValue;

    }

    public boolean overlaps(BTD_Rect rectangle) {
        return x < rectangle.x + rectangle.width &&
                x + width > rectangle.x &&
                y < rectangle.y + rectangle.height &&
                y + height > rectangle.y;
    }

    public boolean isInside(BTD_Rect rectangle) {
        return x > rectangle.x && x + width < rectangle.x + rectangle.width &&
                y > rectangle.y && y + height < rectangle.y + rectangle.height;
    }

    public boolean contains(int mx, int my) {
        return mx < x + width && mx > x && my < y + height && my > y;
    }


}