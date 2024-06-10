import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

/**
 * Represents a dart or projectile in the Bloons Tower Defense game.
 * Each dart has unique attributes and behaviors based on its type.
 */
public class BTD_Dart {
    private final Image dartMonkeyImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\dart.png").getImage();
    private final Image sniperMonkeyImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\dart.png").getImage();
    private final Image bombTowerImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\bomb.png").getImage();
    private final Image glueGunnerImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\glue.png").getImage();
    private final Image superMonkeyImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\dart.png").getImage();
    private final Image bombExplodingImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\explosion.png").getImage();

    public int x;
    public int y;
    public String type;
    public int damage;
    public int width;
    public int height;
    public int speed;
    public int range;
    public int travelled;
    public double angle;
    public int health;
    public boolean isRemoved;
    public boolean isExploding;
    private int explosionTimer;
    private final int explosionTime = 5;
    private AffineTransform saveXform;
    private AffineTransform at;

    /**
     * Constructs a new dart with the specified type, initial position, attack power, speed, range, and angle.
     *
     * @param projectileType The type of the dart.
     * @param x The initial x-coordinate of the dart.
     * @param y The initial y-coordinate of the dart.
     * @param attack The attack power of the dart.
     * @param speed The speed of the dart.
     * @param rangeRadius The range of the dart.
     * @param angle The angle of movement for the dart.
     */
    public BTD_Dart(String projectileType, int x, int y, int attack, int speed, int rangeRadius, double angle) {
        this.x = x;
        this.y = y;
        this.type = projectileType;
        this.damage = attack;
        this.speed = speed;
        this.range = rangeRadius;
        this.angle = angle;

        width = getImage().getWidth(null);
        height = getImage().getHeight(null);
        travelled = 0;
        health = 1;

        isRemoved = false;
        isExploding = false;
    }

    /**
     * Gets the image associated with the dart's current type.
     *
     * @return The image of the dart.
     */
    public Image getImage() {
        return switch (type) {
            case "bombTower" -> bombTowerImage;
            case "glueGunner" -> glueGunnerImage;
            case "superMonkey" -> superMonkeyImage;
            case "sniperMonkey" -> sniperMonkeyImage;
            default -> dartMonkeyImage;
        };
    }

    /**
     * Moves the dart along its path based on its speed and angle.
     * Marks the dart as removed if it travels beyond its range or the game bounds.
     */
    public void move() {
        x += speed * Math.cos(angle - Math.PI / 2);
        y += speed * Math.sin(angle - Math.PI / 2);

        travelled += speed;

        if (!type.equals("sniperMonkey") && travelled > range) {
            isRemoved = true;
        }
        if (!getRect().isInside(new BTD_Rect(-50, -50, 775 + 50, 570 + 50))) {
            isRemoved = true;
        }
    }

    /**
     * Handles the explosion logic for bomb tower darts.
     * Marks the dart as removed after the explosion duration.
     */
    public void explosionDetection() {
        if (type.equals("bombTower") && isExploding) {
            explosionTimer++;
            if (explosionTimer == explosionTime) {
                isRemoved = true;
            }
        }
    }

    /**
     * Detects and handles collisions between the dart and a list of bloons.
     *
     * @param bloonsList The list of bloons to check for collisions.
     * @return The updated list of bloons after applying the effects of collisions.
     */
    public ArrayList<BTD_Bloons> collisionDetection(ArrayList<BTD_Bloons> bloonsList) {
        for (BTD_Bloons b : bloonsList) {
            if (getRect().overlaps(b.getRect())) {
                if (type.equals("glueGunner") && !b.canBeGlued) {
                    b.isGlued = true;
                } else if (type.equals("bombTower") && !b.canExplode) {
                    isExploding = true;
                    BTD_Rect bombRect = new BTD_Rect(x - bombExplodingImage.getWidth(null) / 2,
                            y - bombExplodingImage.getHeight(null) / 2,
                            bombExplodingImage.getWidth(null), bombExplodingImage.getHeight(null));
                    if (bombRect.overlaps(b.getRect())) {
                        b.health -= damage;
                        if (b.health <= 0) {
                            b.isRemoved = true;
                        }
                    }
                } else if (type.equals("dartMonkey") || type.equals("superMonkey") || type.equals("sniperMonkey")) {
                    if (!b.affectedBySharp || b.isFrozen) {
                        health -= damage;
                        b.health -= damage;
                        if (health <= 0) {
                            isRemoved = true;
                        }
                        if (b.health <= 0) {
                            b.isRemoved = true;
                        }
                    }
                }
            }
        }
        return bloonsList;
    }

    /**
     * Draws the dart on the screen at its current position.
     *
     * @param g2 The graphics context.
     */
    public void paint(Graphics2D g2) {
        saveXform = g2.getTransform();
        at = new AffineTransform();

        at.rotate(angle, x, y);
        g2.transform(at);

        if (type.equals("bombTower") && isExploding) {
            g2.drawImage(bombExplodingImage, x - bombExplodingImage.getWidth(null) / 2,
                    y - bombExplodingImage.getHeight(null) / 2, null);
        } else {
            g2.drawImage(getImage(), x - (width / 2), y - (height / 2), null);
        }

        g2.setTransform(saveXform);
    }

    /**
     * Gets the rectangle representing the dart's current position and size.
     *
     * @return A BTD_Rect object representing the dart's bounding box.
     */
    public BTD_Rect getRect() {
        return new BTD_Rect(x, y, width, height);
    }
}
