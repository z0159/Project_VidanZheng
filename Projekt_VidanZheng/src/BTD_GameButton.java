import java.awt.*;
import javax.swing.*;

public class BTD_GameButton {

    public Image fastForwardImage1 = new ImageIcon("src/Data Files/fastforwardoff.png").getImage();
    public Image fastForwardImage2 = new ImageIcon("src/Data Files/fastforwardon.png").getImage();
    public int x;
    public int y;
    public String label;
    public Image currentFastForwardImage;
    public int width;
    public int height;

    public BTD_GameButton(String label, int x, int y) {
        this.x = x;
        this.y = y;
        this.label = label;

        switch (label) {
            case "dartMonkey" -> {
                currentFastForwardImage = new ImageIcon("src/Data Files/dartmonkey.jpg").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "sniperMonkey" -> {
                currentFastForwardImage = new ImageIcon("src/Data Files/sniperMonkey.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "bombTower" -> {
                currentFastForwardImage = new ImageIcon("src/Data Files/bombTower.png").getImage().getScaledInstance(50, 55, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "iceTower" -> {
                currentFastForwardImage = new ImageIcon("src/Data Files/iceMonkey.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "glueGunner" -> {
                currentFastForwardImage = new ImageIcon("src/Data Files/glueMonkey.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "superMonkey" -> {
                currentFastForwardImage = new ImageIcon("src/Data Files/superMonkey.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "fastForward" -> {
                currentFastForwardImage = fastForwardImage1;
                width = 107;
                height = 69;
            }
            case "go" -> {
                currentFastForwardImage = new ImageIcon("src/Data Files/start.png").getImage();
                width = 100;
                height = 40;
            }
            case "sell" -> {
                currentFastForwardImage = new ImageIcon("src/Data Files/sell.png").getImage();
                width = 100;
                height = 100;
            }
        }

    }

    public BTD_Rect getRect() {
        return new BTD_Rect(x, y, width, height);
    }

    public Image getImage() {
        return currentFastForwardImage;
    }
}