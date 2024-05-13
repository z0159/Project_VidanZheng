import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

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

    public Image getImage() {
        return switch (type) {
            case "bombTower" -> bombTowerImage;
            case "glueGunner" -> glueGunnerImage;
            case "superMonkey" -> superMonkeyImage;
            case "sniperMonkey" -> sniperMonkeyImage;
            default -> dartMonkeyImage;
        };
    }

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

    public void explosionDetection() {
        if (type.equals("bombTower") && isExploding) {
            explosionTimer++;
            if (explosionTimer == explosionTime) {
                isRemoved = true;
            }
        }
    }

    public ArrayList<BTD_Bloons> collisionDetection(ArrayList<BTD_Bloons> bloonsList) {
        for (BTD_Bloons b : bloonsList) {
            if (getRect().overlaps(b.getRect())) {

                if (type.equals("glueGunner") && !b.canBeGlued) {
                    b.isGlued = true;
                }
                else if (type.equals("bombTower") && !b.canExplode) {
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
                }
                else if (type.equals("dartMonkey") || type.equals("superMonkey") || type.equals("sniperMonkey")) {
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

    public void paint(Graphics2D g2) {
        saveXform = g2.getTransform();
        at = new AffineTransform();

        at.rotate(angle, x, y);
        g2.transform(at);

        if (type.equals("bombTower") && isExploding) {
            g2.drawImage(bombExplodingImage, x - bombExplodingImage.getWidth(null) / 2,
                    y - bombExplodingImage.getHeight(null) / 2, null);
        }
        else {
            g2.drawImage(getImage(), x - (width / 2), y - (height / 2), null);
        }

        g2.setTransform(saveXform);
    }


    public BTD_Rect getRect() {
        return new BTD_Rect(x, y, width, height);
    }


}
