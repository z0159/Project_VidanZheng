import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class BTD_Monkey {
    private final Image dartMonkeyImage = new ImageIcon("src/Data Files/dartmonkey.jpg").getImage();
    private final Image sniperMonkeyImage = new ImageIcon("src/Data Files/sniperMonkey.png").getImage();
    private final Image bombTowerImage = new ImageIcon("src/Data Files/bombTower.png").getImage();
    private final Image iceTowerImage = new ImageIcon("src/Data Files/iceMonkey.png").getImage();
    private final Image glueGunnerImage = new ImageIcon("src/Data Files/glueMonkey.png").getImage();
    private final Image superMonkeyImage = new ImageIcon("src/Data Files/superMonkey.png").getImage();

    public int x;
    public int y;
    public String type;
    public int damage;
    public int range;
    public int width;
    public int height;
    public int cost;
    public int sellPrice;
    public int dartSpeed;
    public double angle;
    public boolean isRemoved;
    public int attackTimer;
    public int attackTime;
    private AffineTransform at;

    public BTD_Monkey(String type, int x, int y) {
        this.type = type;
        width = getImage().getWidth(null);
        height = getImage().getHeight(null);
        this.x = x;
        this.y = y;
        attackTimer = 0;

        switch (type) {
            case "dartMonkey" -> {
                damage = 1;
                range = 100;
                cost = 200;
                sellPrice = 159;
                attackTime = 29;
                dartSpeed = 6;
            }
            case "sniperMonkey" -> {
                damage = 10;
                range = 30;
                cost = 350;
                sellPrice = 279;
                attackTime = 66;
                dartSpeed = 40;
            }
            case "bombTower" -> {
                damage = 20;
                range = 90;
                cost = 650;
                sellPrice = 519;
                attackTime = 47;
                dartSpeed = 5;
            }
            case "iceTower" -> {
                damage = 0;
                range = 60;
                cost = 300;
                sellPrice = 239;
                attackTime = 73;
                dartSpeed = 0;
            }
            case "glueGunner" -> {
                damage = 0;
                range = 120;
                cost = 270;
                sellPrice = 215;
                attackTime = 25;
                dartSpeed = 7;
            }
            case "superMonkey" -> {
                damage = 2;
                range = 150;
                cost = 3500;
                sellPrice = 2799;
                attackTime = 10;
                dartSpeed = 6;
            }
        }
        isRemoved = false;
    }

    public Image getImage() {
        return switch (type) {
            case "sniperMonkey" -> sniperMonkeyImage;
            case "bombTower" -> bombTowerImage;
            case "iceTower" -> iceTowerImage;
            case "glueGunner" -> glueGunnerImage;
            case "superMonkey" -> superMonkeyImage;
            default -> dartMonkeyImage;
        };
    }

    public ArrayList<BTD_Dart> isInRange(ArrayList<BTD_Bloons> bloonsList, ArrayList<BTD_Dart> projList) {
        boolean bloonInRadius = false;
        int shortestDistance = Integer.MAX_VALUE;
        int shortestDistanceX = 0;
        int shortestDistanceY = 0;
        int checkDistance;

        for (BTD_Bloons b : bloonsList) {
            if (!b.invisible || type.equals("superMonkey") || type.equals("sniperMonkey")) {
                checkDistance = (this.x - (int)(b.x + b.width / 2)) * (this.x - (int)(b.x + b.width / 2)) +
                        (this.y - (int)(b.y + b.height / 2)) * (this.y - (int)(b.y + b.height / 2));
                if (type.equals("sniperMonkey")) {
                    if (checkDistance < shortestDistance) {
                        shortestDistance = checkDistance;
                        shortestDistanceX = (int)(b.x + b.width / 2);
                        shortestDistanceY = (int)(b.y + b.height / 2);
                    }
                }else if (checkDistance < (range * range)) {
                    bloonInRadius = true;
                    if (checkDistance < shortestDistance) {
                        shortestDistance = checkDistance;
                        shortestDistanceX = (int)(b.x + b.width / 2);
                        shortestDistanceY = (int)(b.y + b.height / 2);
                    }

                }
            }

        }
        if (type.equals("sniperMonkey") || bloonInRadius) {
            attackTimer++;
            if (attackTimer == attackTime) {
                attackTimer = 0;
                if (shortestDistanceX != 0 && shortestDistanceY != 0) {
                    rotate(shortestDistanceX, shortestDistanceY);
                    projList = shoot(projList);
                }
            }
        }
        return projList;
    }

    public ArrayList<BTD_Bloons> isAttackable(ArrayList<BTD_Bloons> bloonsList) {
        if ("iceTower".equals(type)) {
            attackTimer++;
            if (attackTimer == attackTime) {
                attackTimer = 0;
                for (BTD_Bloons b : bloonsList) {
                    if ((x - b.x) * (x - b.x) + (y - b.y) * (y - b.y) < (range * range) && !b.canFreeze) {
                        b.isFrozen = true;
                    }
                }
            }
        }
        return bloonsList;
    }

    public void rotate(int shortestDistanceX, int shortestDistanceY) {
        if (!type.equals("iceTower")) {
            angle = Math.PI / 2 + Math.atan2(shortestDistanceY - y, shortestDistanceX - x);
        }
    }

    public ArrayList<BTD_Dart> shoot(ArrayList<BTD_Dart> projList) {
        if (!type.equals("iceTower")) {
            projList.add(new BTD_Dart(type, x, y, damage, dartSpeed, range, angle));
        }
        return projList;
    }


    public void paint(Graphics2D g2) {
        AffineTransform saveXform = g2.getTransform();
        at = new AffineTransform();
        at.rotate(angle, x, y);
        g2.transform(at);
        g2.drawImage(getImage(), x - (width / 2), y - (height / 2), null);
        g2.setTransform(saveXform);

    }

    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y + height / 2;
    }

    public BTD_Rect getRect() {
        return new BTD_Rect(x - width / 2, y - height / 2, width, height);
    }
}
