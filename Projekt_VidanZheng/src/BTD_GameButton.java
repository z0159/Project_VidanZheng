import java.awt.*;
import javax.swing.*;

/**
 * Represents a game button in the Bloons Tower Defense game.
 * Each button has unique attributes and behaviors based on its label.
 */
public class BTD_GameButton {

    public Image fastForwardImage1 = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\fastforwardoff.png").getImage();
    public Image fastForwardImage2 = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\fastforwardon.png").getImage();
    public int x;
    public int y;
    public String label;
    public Image currentFastForwardImage;
    public int width;
    public int height;

    /**
     * Constructs a new game button with the specified label and initial position.
     *
     * @param label The label of the button.
     * @param x The initial x-coordinate of the button.
     * @param y The initial y-coordinate of the button.
     */
    public BTD_GameButton(String label, int x, int y) {
        this.x = x;
        this.y = y;
        this.label = label;

        switch (label) {
            case "dartMonkey" -> {
                currentFastForwardImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\dartmonkey.jpg").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "sniperMonkey" -> {
                currentFastForwardImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\sniperMonkey.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "bombTower" -> {
                currentFastForwardImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\bombTower.png").getImage().getScaledInstance(50, 55, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "iceTower" -> {
                currentFastForwardImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\iceMonkey.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "glueGunner" -> {
                currentFastForwardImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\glueMonkey.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "superMonkey" -> {
                currentFastForwardImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\superMonkey.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                width = 50;
                height = 50;
            }
            case "fastForward" -> {
                currentFastForwardImage = fastForwardImage1;
                width = 107;
                height = 69;
            }
            case "go" -> {
                currentFastForwardImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\start.png").getImage();
                width = 100;
                height = 40;
            }
            case "sell" -> {
                currentFastForwardImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\sell.png").getImage();
                width = 100;
                height = 100;
            }
        }
    }

    /**
     * Gets the rectangle representing the button's current position and size.
     *
     * @return A BTD_Rect object representing the button's bounding box.
     */
    public BTD_Rect getRect() {
        return new BTD_Rect(x, y, width, height);
    }

    /**
     * Gets the image associated with the button's current label.
     *
     * @return The image of the button.
     */
    public Image getImage() {
        return currentFastForwardImage;
    }
}
