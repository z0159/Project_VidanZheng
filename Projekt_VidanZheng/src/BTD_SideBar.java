import java.awt.*;
import javax.swing.*;

public class BTD_SideBar extends JPanel {
    private final Image sideBarRightImage = new ImageIcon("src/Data Files/sidePanel.png").getImage().getScaledInstance(200, 600, Image.SCALE_SMOOTH);
    private final Image sideBarBottomImage = new ImageIcon("src/Data Files/bottomPanel.png").getImage().getScaledInstance(960, 150, Image.SCALE_SMOOTH);
    private final Image monkeyImage = new ImageIcon("src/Data Files/frame.png").getImage();

    private int money;
    private int lives;
    private int round;
    private BTD_Monkey selectedMonkey;
    private String monkeyButton;

    public BTD_SideBar(int money, int lives, int round, BTD_Monkey selectedMonkey, String monkeyButton) {
        this.money = money;
        this.lives = lives;
        this.round = round;
        this.selectedMonkey = selectedMonkey;
        this.monkeyButton = monkeyButton;
    }

    public void update(int currentMoney, int currentLives, int currentRound, BTD_Monkey selectedMonkey, String monkeyButton) {
        this.money = currentMoney;
        this.lives = currentLives;
        this.round = currentRound;
        this.selectedMonkey = selectedMonkey;
        this.monkeyButton = monkeyButton;
    }

    public void paintSideBar(Graphics g) {
        g.drawImage(sideBarRightImage, 770, 0, this);
        g.drawImage(sideBarBottomImage, 0, 550, this);
        g.drawImage(monkeyImage, 780, 5, this);
    }

    public void paintText(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.drawString("Money: " + money, 810, 20);
        g.drawString("Lives: " + lives, 810, 50);
        g.drawString("Round: " + round, 25, 670);

        if (selectedMonkey == null) {
            String textToBeDrawn = getInfo();
            int lineCounter = 0;
            for (String line : textToBeDrawn.split("\n")) {
                g.drawString(line, 25, 590 + 18 * lineCounter);
                lineCounter++;
            }
        }
        else {
            g.drawString("Name: " + selectedMonkey.type.substring(0, 1).toUpperCase() + selectedMonkey.type.substring(1), 150, 600);
            g.drawString("Damage: " + selectedMonkey.damage, 150, 620);
            g.drawString("Attackspeed: " + selectedMonkey.attackTime, 400, 620);
            g.drawString("Range: " + selectedMonkey.range, 150, 640);
            g.drawString("Projectile Speed: " + selectedMonkey.dartSpeed, 400, 640);
            g.drawString("Sell for ", 690, 620);
            g.drawString("$" + selectedMonkey.sellPrice, 690, 640);
        }
    }

    public String getInfo() {
        if (!monkeyButton.equals("")) {
            switch (monkeyButton) {
                case "dartMonkey":
                    return "Dart Monkey, Cost: $200.";
                case "sniperMonkey":
                    return "Sniper Monkey, Cost: $350.";
                case "bombTower":
                    return "Bomb Tower, Cost: $650.";
                case "iceTower":
                    return "Ice Tower, Cost: $300.";
                case "glueGunner":
                    return "Glue Gunner, Cost: $270.";
                case "superMonkey":
                    return "Super Monkey, Cost: $3500.";
            }
        }
        return "";
    }

}