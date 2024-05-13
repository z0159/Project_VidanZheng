import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;



public class BloonsTowerDefense extends JFrame implements ActionListener {
    public JPanel cards;
    public CardLayout cLayout;
    public Timer gameTimer;
    public GamePanel game;

    public BloonsTowerDefense() {
        super("Bloons Tower Defense Simple");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 720);

        cLayout = new CardLayout();
        gameTimer = new Timer(1, this);
        game = new GamePanel();
        cards = new JPanel(cLayout);
        cards.add(game, "game");
        cLayout.show(cards, "game");
        gameTimer.start();
        game.requestFocus();
        add(cards);
        setResizable(false);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == gameTimer) {
            if (game != null && game.isReady && game.isRunning) {
                game.update();
                game.repaint();
            }
            assert game != null;
            if (!game.isRunning) {
                assert gameTimer != null;
                gameTimer.stop();
                if (game.lives <= 0) {
                    System.exit(0);
                }
            }
        }

    }


    public static void main(String[] args) {
        BloonsTowerDefense frame = new BloonsTowerDefense();
    }
}


class GamePanel extends JPanel implements MouseMotionListener, MouseListener {
    private Image mapImage = new ImageIcon("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\map.png").getImage();
    public boolean isReady;
    public int money;
    public int lives;
    public int round;
    private Scanner scanner;

    public String currentMonkey;
    public boolean isPlacingMonkey;
    public boolean isSelectingMonkey;
    public BTD_Monkey monkey;
    public BTD_Monkey selectedMonkey;
    public BTD_SideBar sideBar;

    public ArrayList<BTD_GameButton> monkeyButtons;
    public BTD_GameButton fastForwardButton;
    public BTD_GameButton startButton;
    public BTD_GameButton sellButton;

    public boolean fastForward;

    public int mouseX;
    public int mouseY;
    public int DelayCounter;
    public final int DelayTick = 25;
    public boolean isRunning;
    public boolean roundIsRunning;
    public ArrayList<BTD_Monkey> monkeyList;
    public ArrayList<BTD_Bloons> bloonsList;
    public LinkedList<BTD_Bloons> bloonsStoreList;
    public ArrayList<BTD_Dart> projectileList;

    public GamePanel() {

        isRunning = true;

        monkeyList = new ArrayList<>();
        bloonsList = new ArrayList<>();
        bloonsStoreList = new LinkedList<>();
        projectileList = new ArrayList<>();

        mapImage = mapImage.getScaledInstance(775, 570, Image.SCALE_SMOOTH);

        money = 650;
        lives = 100;
        round = 0;
        currentMonkey = "";

        monkey = null;

        roundIsRunning = false;
        isPlacingMonkey = false;
        isSelectingMonkey = false;
        fastForward = false;
        DelayCounter = 0;

        try {
            scanner = new Scanner(new File("C:\\Users\\kevin\\OneDrive\\Dokumente\\GitHub\\Project_VidanZheng\\Projekt_VidanZheng\\src\\Data Files\\Round Data.txt"));
        }
        catch (IOException ignored) {
        }

        scanner.nextLine();
        addBloons();

        sideBar = new BTD_SideBar(money, lives, round, selectedMonkey, currentMonkey);

        monkeyButtons = new ArrayList<>();
        monkeyButtons.add(new BTD_GameButton("dartMonkey", 800, 93));
        monkeyButtons.add(new BTD_GameButton("sniperMonkey", 875, 93));
        monkeyButtons.add(new BTD_GameButton("bombTower", 800, 149));
        monkeyButtons.add(new BTD_GameButton("iceTower", 875, 149));
        monkeyButtons.add(new BTD_GameButton("glueGunner", 800, 206));
        monkeyButtons.add(new BTD_GameButton("superMonkey", 875, 206));
        fastForwardButton = new BTD_GameButton("fastForward", 815, 490);
        startButton = new BTD_GameButton("go", 815, 575);
        sellButton = new BTD_GameButton("sell", 680, 575);

        mouseX = 0;
        mouseY = 0;

        addMouseMotionListener(this);
        addMouseListener(this);
        addNotify();

    }

    public void clearMap() {
        projectileList.clear();
        for (int j = monkeyList.size() - 1; j >= 0; j--) {
            monkeyList.get(j).attackTimer = 0;
        }
    }

    public void addNotify() {
        super.addNotify();
        isReady = true;
    }

    public void update() {
        if (!fastForward) {
            try {
                Thread.sleep(15);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        sideBar.update(money, lives, round, selectedMonkey, currentMonkey);

        if (bloonsList.size() == 0 && bloonsStoreList.size() == 0) {
            DelayCounter = 0;
            addBloons();
            roundIsRunning = false;
            clearMap();
            System.gc();
        }

        if (roundIsRunning) {
            if (bloonsStoreList.size() > 0 && DelayCounter == DelayTick) {
                DelayCounter = 0;
                bloonsList.add(bloonsStoreList.pop());
            }else if (bloonsStoreList.size() > 0) {
                DelayCounter++;
            }
            for (int i = bloonsList.size() - 1; i >= 0; i--) {
                bloonsList.get(i).statusCheck();
                bloonsList.get(i).move();

                if (bloonsList.get(i).x < 0 || bloonsList.get(i).x > 775 || bloonsList.get(i).y < 0 || bloonsList.get(i).y > 570) {
                    lives -= bloonsList.get(i).health;
                    bloonsList.addAll(bloonsList.get(i).breakIntoBloons());
                    bloonsList.remove(i);
                }

                if (i < bloonsList.size() && bloonsList.get(i).isRemoved) {
                    money += bloonsList.get(i).money;
                    bloonsList.addAll(bloonsList.get(i).breakIntoBloons());
                    bloonsList.remove(i);
                }
            }

            for (int k = projectileList.size() - 1; k >= 0; k--) {
                projectileList.get(k).move();
                projectileList.get(k).explosionDetection();
                bloonsList = projectileList.get(k).collisionDetection(bloonsList);

                if (projectileList.get(k).isRemoved) {
                    projectileList.remove(k);
                }
            }

            for (int j = monkeyList.size() - 1; j >= 0; j--) {
                if ("iceTower".equals(monkeyList.get(j).type)) {
                    bloonsList = monkeyList.get(j).isAttackable(bloonsList);
                } else {
                    projectileList = monkeyList.get(j).isInRange(bloonsList, projectileList);
                }

                if (monkeyList.get(j).isRemoved) {
                    monkeyList.remove(j);
                }
            }
        }

        if (lives <= 0) {
            stopGame();
        }
    }

    public void addBloons() {
        int startingX = 380;
        int startingY = 1;
        bloonsList.clear();
        bloonsStoreList.clear();
        if (bloonsList.size() == 0) {
            round++;
            if (scanner.hasNextLine()) {
                String dataLine = scanner.nextLine().substring(5);
                String[] dataList = dataLine.split(";");
                String[] numberBloonPair;
                for (String line : dataList) {
                    numberBloonPair = line.trim().split(" ");
                    for (int i = 0; i < Integer.parseInt(numberBloonPair[0]); i++) {
                        bloonsStoreList.add(new BTD_Bloons(numberBloonPair[1], startingX, startingY, 0));
                    }
                }
            } else {
                stopGame();
            }

        }
    }

    public void place(int mx, int my) {
        if (isPlacingMonkey) {
            if (!currentMonkey.equals("")) {
                monkey = new BTD_Monkey(currentMonkey, mx, my);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Color colour;

        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(mapImage, 0, 0, this);

        sideBar.paintSideBar(g);

        Graphics2D g2 = (Graphics2D)g;
        Stroke stroke = new BasicStroke(4f);
        g2.setStroke(stroke);

        for (BTD_GameButton btn : monkeyButtons) {
            g.drawImage(btn.getImage(), btn.x, btn.y, this);
            if (currentMonkey.equals(btn.label)) {
                g2.setColor(new Color(225, 0, 0));
                g2.drawRect(btn.x - 2, btn.y - 2, btn.width + 2, btn.height + 2);
            }else if (btn.getRect().contains(mouseX, mouseY)) {
                g2.setColor(new Color(255, 255, 255));
                g2.drawRect(btn.x - 2, btn.y - 2, btn.width + 2, btn.height + 2);
            }
        }

        g2.setColor(new Color(255, 255, 255));
        if (fastForwardButton.getRect().contains(mouseX, mouseY)) {
            g2.drawRect(fastForwardButton.x - 2, fastForwardButton.y - 2, fastForwardButton.width + 2, fastForwardButton.height + 2);
        }else if (!roundIsRunning && startButton.getRect().contains(mouseX, mouseY)) {
            g2.drawRect(startButton.x - 2, startButton.y - 2, startButton.width + 2, startButton.height + 2);
        }else if (selectedMonkey != null && sellButton.getRect().contains(mouseX, mouseY)) {
            g2.drawRect(sellButton.x - 2, sellButton.y - 2, sellButton.width + 2, sellButton.height + 2);
        }

        g.drawImage(fastForwardButton.getImage(), fastForwardButton.x, fastForwardButton.y, this);

        if (selectedMonkey != null) {
            g.drawImage(sellButton.getImage(), sellButton.x, sellButton.y, this);
        }
        if (!roundIsRunning) {
            g.drawImage(startButton.getImage(), startButton.x, startButton.y, this);
        }

        for (BTD_Bloons bloon : bloonsList) {
            bloon.paint(g2);
        }

        for (BTD_Monkey monkey : monkeyList) {
            if (selectedMonkey != null && selectedMonkey.equals(monkey)) {
                colour = new Color(0, 0, 0, 0.25f);
                g2.setPaint(colour);
                g2.fillOval(monkey.getCenterX() - monkey.range - (monkey.width / 2),
                        monkey.getCenterY() - monkey.range - (monkey.height / 2),
                        monkey.range * 2, monkey.range * 2);
            }
            monkey.paint(g2);
        }

        for (BTD_Dart projectile : projectileList) {
            projectile.paint(g2);
        }

        if (monkey != null) {
            if (money >= monkey.cost && monkey.getRect().isInside(new BTD_Rect(-50,
                    -50, 775 + 50, 570 + 50))) {
                colour = new Color(0.2f, 0.2f, 0.2f, 0.8f);
            }
            else {
                colour = new Color(0.5f, 0, 0, 0.8f);
            }

            g2.setPaint(colour);

            int centerX = monkey.getCenterX();
            int centerY = monkey.getCenterY();

            g2.fillOval(centerX - monkey.range - (monkey.width / 2),
                    centerY - monkey.range - (monkey.height / 2),
                    monkey.range * 2, monkey.range * 2);
            g.drawImage(monkey.getImage(), monkey.x - (monkey.width / 2),
                    monkey.y - (monkey.height / 2), this);
        }
        sideBar.paintText(g);
    }

    public void stopGame() {
        isRunning = false;
    }

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (isPlacingMonkey) {
            place(mouseX, mouseY);
        }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!isSelectingMonkey && isPlacingMonkey && monkey != null && money >= monkey.cost
                    && monkey.getRect().isInside(new BTD_Rect(-50, -50, 775 + 50, 570 + 50))) {
                money -= monkey.cost;
                monkeyList.add(monkey);
            }

            for (BTD_GameButton btn : monkeyButtons) {
                if (btn.getRect().contains(mouseX, mouseY)) {
                    isSelectingMonkey = false;
                    selectedMonkey = null;
                    isPlacingMonkey = true;
                    currentMonkey = btn.label;
                }
            }

            if (!isPlacingMonkey) {
                for (BTD_Monkey monkey : monkeyList) {
                    if (monkey.getRect().contains(mouseX, mouseY)) {
                        isSelectingMonkey = true;
                        selectedMonkey = monkey;
                    }
                }
            }

            if (fastForwardButton.getRect().contains(mouseX, mouseY)) {
                if (fastForward) {
                    fastForward = false;
                    fastForwardButton.currentFastForwardImage = fastForwardButton.fastForwardImage1;
                }else{
                    fastForward = true;
                    fastForwardButton.currentFastForwardImage = fastForwardButton.fastForwardImage2;
                }
            }

            if (sellButton.getRect().contains(mouseX, mouseY)) {
                for (int j = monkeyList.size() - 1; j >= 0; j--) {
                    if (monkeyList.get(j).equals(selectedMonkey)) {
                        money += monkeyList.get(j).sellPrice;
                        monkeyList.remove(j);
                    }
                }
                isSelectingMonkey = false;
                selectedMonkey = null;
            }

            if (!roundIsRunning && startButton.getRect().contains(mouseX, mouseY)) {
                DelayCounter = 0;
                roundIsRunning = true;
            }

        }

        else if (e.getButton() == MouseEvent.BUTTON3) {
            if (isPlacingMonkey) {
                monkey = null;
                isPlacingMonkey = false;
                currentMonkey = "";
            }
            if (isSelectingMonkey) {
                isSelectingMonkey = false;
                selectedMonkey = null;
            }
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

}
