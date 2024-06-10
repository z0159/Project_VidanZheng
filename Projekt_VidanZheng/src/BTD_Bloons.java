import java.util.*;
import java.awt.*;
import javax.swing.*;

public class BTD_Bloons {
    private final Image redBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\red.png").getImage();
    private final Image blueBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\blue.png").getImage();
    private final Image greenBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\green.png").getImage();
    private final Image yellowBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\yellow.png").getImage();
    private final Image pinkBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\purple.png").getImage();
    private final Image blackBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\black.png").getImage();
    private final Image whiteBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\white.png").getImage();
    private final Image camoBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\camo.png").getImage();
    private final Image leadBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\lead.png").getImage();
    private final Image zebraBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\zebra.png").getImage();
    private final Image rainbowBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\rainbow.png").getImage();
    private final Image ceramicBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\ceramic.png").getImage();
    private final Image MOABBloonImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\moab.png").getImage();
    private final Image freezeImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\freeze.png").getImage();
    private final Image gluedImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\glue.png").getImage();

    public double x;
    public double y;

    public String type;
    public int health;
    public int money;
    public double speed;
    public double width;
    public double height;
    public boolean isRemoved;
    public boolean isFrozen;
    public boolean isGlued;
    public boolean canExplode;
    public boolean canFreeze;
    public boolean affectedBySharp;
    public boolean canBeGlued;
    public boolean invisible;
    private int freezeTimer;
    private final int freezeTime = 300;
    private int glueTimer;
    private final int glueTime = 700;
    public int corners;


    public BTD_Bloons(String bloonType, double x, double y, int corners) {
        this.type = bloonType;
        this.x = x;
        this.y = y;
        this.corners = corners;
        speed = 2;

        isFrozen = false;
        isGlued = false;
        isRemoved = false;
        canExplode = false;
        canFreeze = false;
        freezeTimer = 0;
        canBeGlued = false;
        affectedBySharp = false;
        invisible = false;

        switch (bloonType) {
            case "red" -> {
                health = 1;
                speed *= 1;
                money = 2;
                width = redBloonImage.getWidth(null);
                height = redBloonImage.getHeight(null);
            }
            case "blue" -> {
                health = 1;
                speed *= 1.4;
                money = 3;
                width = blueBloonImage.getWidth(null);
                height = blueBloonImage.getHeight(null);
            }
            case "green" -> {
                health = 1;
                speed *= 1.8;
                money = 4;
                width = greenBloonImage.getWidth(null);
                height = greenBloonImage.getHeight(null);
            }
            case "yellow" -> {
                health = 1;
                speed *= 3.2;
                money = 5;
                width = yellowBloonImage.getWidth(null);
                height = yellowBloonImage.getHeight(null);
            }
            case "pink" -> {
                health = 1;
                speed *= 3.5;
                money = 6;
                width = pinkBloonImage.getWidth(null);
                height = pinkBloonImage.getHeight(null);
            }
            case "black" -> {
                health = 1;
                speed *= 1.8;
                money = 10;
                width = blackBloonImage.getWidth(null);
                height = blackBloonImage.getHeight(null);
                canExplode = true;
            }
            case "white" -> {
                health = 1;
                speed *= 2;
                money = 10;
                width = whiteBloonImage.getWidth(null);
                height = whiteBloonImage.getHeight(null);
                canFreeze = true;
            }
            case "camo" -> {
                health = 1;
                speed *= 1;
                money = 1;
                width = camoBloonImage.getWidth(null);
                height = camoBloonImage.getHeight(null);
                invisible = true;
            }
            case "lead" -> {
                health = 1;
                speed *= 1;
                money = 12;
                width = leadBloonImage.getWidth(null);
                height = leadBloonImage.getHeight(null);
                affectedBySharp = true;
            }
            case "zebra" -> {
                health = 2;
                speed *= 1.8;
                money = 13;
                width = zebraBloonImage.getWidth(null);
                height = zebraBloonImage.getHeight(null);
                canExplode = true;
                canFreeze = true;
            }
            case "rainbow" -> {
                health = 3;
                speed *= 2.2;
                money = 15;
                width = rainbowBloonImage.getWidth(null);
                height = rainbowBloonImage.getHeight(null);
            }
            case "ceramic" -> {
                health = 10;
                speed *= 2.5;
                money = 0;
                width = ceramicBloonImage.getWidth(null);
                height = ceramicBloonImage.getHeight(null);
                canBeGlued = true;
            }
            case "MOAB" -> {
                health = 200;
                speed *= 1;
                money = 200;
                width = MOABBloonImage.getWidth(null);
                height = MOABBloonImage.getHeight(null);
                canFreeze = true;
                canBeGlued = true;
            }
        }
        isRemoved = false;
    }




    public ArrayList<BTD_Bloons> breakIntoBloons() {
        ArrayList<BTD_Bloons> bloonsProduced = new ArrayList<>();
        switch (type) {
            case "blue" -> bloonsProduced.add(new BTD_Bloons("red", x, y, corners));
            case "green" -> bloonsProduced.add(new BTD_Bloons("blue", x, y, corners));
            case "yellow" -> bloonsProduced.add(new BTD_Bloons("green", x, y, corners));
            case "pink" -> bloonsProduced.add(new BTD_Bloons("yellow", x, y, corners));
            case "black", "white" -> {
                bloonsProduced.add(new BTD_Bloons("pink", x, y, corners));
                bloonsProduced.add(new BTD_Bloons("pink", x, y, corners));
            }
            case "camo" -> bloonsProduced.add(new BTD_Bloons("pink", x, y, corners));
            case "lead" -> {
                bloonsProduced.add(new BTD_Bloons("black", x, y, corners));
                bloonsProduced.add(new BTD_Bloons("black", x, y, corners));
            }
            case "zebra" -> {
                bloonsProduced.add(new BTD_Bloons("black", x, y, corners));
                bloonsProduced.add(new BTD_Bloons("white", x, y, corners));
            }
            case "rainbow" -> {
                bloonsProduced.add(new BTD_Bloons("zebra", x, y, corners));
                bloonsProduced.add(new BTD_Bloons("zebra", x, y, corners));
            }
            case "ceramic" -> {
                bloonsProduced.add(new BTD_Bloons("rainbow", x, y, corners));
                bloonsProduced.add(new BTD_Bloons("rainbow", x, y, corners));
            }
            case "MOAB" -> {
                bloonsProduced.add(new BTD_Bloons("ceramic", x, y, corners));
                bloonsProduced.add(new BTD_Bloons("ceramic", x, y, corners));
            }
        }
        return bloonsProduced;
    }

    public void move() {
        double movementSpeed = speed;
        if (isFrozen) {
            movementSpeed = 0;
        }
        if (isGlued) {
            movementSpeed = (int)(movementSpeed / 2);
        }

        switch (corners){
            case 0 -> {y += movementSpeed;
                if (getRect().overlaps(new BTD_Rect(380, 125, 1, 1))) {
                    corners += 1;
                }}
            case 1 -> {
                x += movementSpeed;
                if (getRect().overlaps(new BTD_Rect(595, 125, 1, 1))) {
                    corners += 1;
                }}
            case 2 -> {y += movementSpeed;
                if (getRect().overlaps(new BTD_Rect(595, 265, 1, 1))) {
                    corners += 1;
                }}
            case 3 -> {x -= movementSpeed;
                if (getRect().overlaps(new BTD_Rect(445, 265, 1, 1))) {
                    corners += 1;
                }}
            case 4 -> {y += movementSpeed;
                if (getRect().overlaps(new BTD_Rect(445, 380, 1, 1))) {
                    corners += 1;
                }}
            case 5 -> {x += movementSpeed;
                if (getRect().overlaps(new BTD_Rect(595, 380, 1, 1))) {
                    corners += 1;
                }}
            case 6 -> {y += movementSpeed;
                if (getRect().overlaps(new BTD_Rect(595, 525, 1, 1))) {
                    corners += 1;
                }}
            case 7 -> {x -= movementSpeed;
                if (getRect().overlaps(new BTD_Rect(110, 525, 1, 1))) {
                    corners += 1;
                }}
            case 8 -> {y -= movementSpeed;
                if (getRect().overlaps(new BTD_Rect(110, 380, 1, 1))) {
                    corners += 1;
                }}
            case 9 -> {x += movementSpeed;
                if (getRect().overlaps(new BTD_Rect(315, 380, 5, 5))) {
                    corners += 1;
                }}
            case 10 -> {y -= movementSpeed;
                if (getRect().overlaps(new BTD_Rect(315, 110, 5, 5))) {
                    corners += 1;
                }}
            case 11 -> {x -= movementSpeed;
                if (getRect().overlaps(new BTD_Rect(155, 110, 5, 5))) {
                    corners += 1;
                }}
            case 12 -> {y += movementSpeed;
                if (getRect().overlaps(new BTD_Rect(155, 290, 5, 5))) {
                    corners += 1;
                }}
            case 13 -> x -= movementSpeed;

        }
    }

    public void statusCheck() {
        if (isFrozen) {
            freezeTimer++;
            if (freezeTimer == freezeTime) {
                freezeTimer = 0;
                isFrozen = false;
            }
        }
        if (isGlued) {
            glueTimer++;
            if (glueTimer == glueTime) {
                glueTimer = 0;
                isGlued = false;
            }
        }
    }

    public Image getImage() {
        return switch (type) {
            case "blue" -> blueBloonImage;
            case "green" -> greenBloonImage;
            case "yellow" -> yellowBloonImage;
            case "pink" -> pinkBloonImage;
            case "black" -> blackBloonImage;
            case "white" -> whiteBloonImage;
            case "camo" -> camoBloonImage;
            case "lead" -> leadBloonImage;
            case "zebra" -> zebraBloonImage;
            case "rainbow" -> rainbowBloonImage;
            case "ceramic" -> ceramicBloonImage;
            case "MOAB" -> MOABBloonImage;
            default -> redBloonImage;
        };
    }

    public void paint(Graphics2D g2) {
        g2.drawImage(getImage(), (int)(x), (int)(y), null);
        if (isFrozen) {
            g2.drawImage(freezeImage, (int)(x), (int)(y), null);
        }
        else if (isGlued) {
            g2.drawImage(gluedImage, (int)(x), (int)(y), null);
        }
    }

    public BTD_Rect getRect() {
        return new BTD_Rect((int)(x), (int)(y), (int)(width), (int)(height));
    }
}