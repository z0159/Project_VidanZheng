import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GrapplingHook extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel frontViewPanel;
    private GamePanel sideViewPanel;
    private JLabel switchViewLabel;
    private boolean isFrontView = true; //um zu schauen ob gerade front oder sideview aktiv ist

    private double depthFront=0; //um die tiefe auf den verschiedenen Ansichten zu speichern
    private double depthSide=0;

    /**
     * Konstruktor
     */
    public GrapplingHook() {
        setTitle("Greifautomatspiel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //GUI aufrufen
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        //verschieden guis anhand der Ansichten
        frontViewPanel = new GamePanel(Color.RED, false, true);
        sideViewPanel = new GamePanel(Color.BLUE, true, true); // isControllable Aktiviere Steuerung in der Seitenansicht

        //um zu sagen ob gerade front oder side view ist
        mainPanel.add(frontViewPanel, "Front View");
        mainPanel.add(sideViewPanel, "Side View");

        //label unten, mit welchem man ansicht wechselt
        switchViewLabel = new JLabel("Klicken, um die Ansicht zu wechseln");
        //zentrierter text
        switchViewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //mouseklick actionlistener weil auf mausklick getriggert
        switchViewLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //wechseln des views
                switchView();
            }
        });


        add(mainPanel, BorderLayout.CENTER);
        add(switchViewLabel, BorderLayout.SOUTH);

        frontViewPanel.setFocusable(true);
        frontViewPanel.requestFocusInWindow();
    }

    /**
     * switchView Methode, mit welcher man Ansichten wechseln kann
     * erstellt greifhaken neu
     * ändert farbe des greifhakens
     */
    private void switchView() {
        isFrontView = !isFrontView;
        cardLayout.show(mainPanel, isFrontView ? "Front View" : "Side View");
        (isFrontView ? frontViewPanel : sideViewPanel).requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GrapplingHook game = new GrapplingHook();
            game.setVisible(true);
        });
    }

    /**
     * Actionlistener etc
     */
    class GamePanel extends JPanel implements KeyListener {
        private int clawX = 0; // Startposition frontansicht
        private int clawY = 50; //Startposition frontansicht
        private int clawZ = 0; //für tiefe
        private int originalClawY = 50; //um nach entscheidung nochmal zu spielen zu reseten
        private boolean clawLowered = false; //man darf nur einmal Haken runterlassen
        private Color clawColor;
        private Timer timer;
        private boolean canDropClaw = true;//man darf nur einmal Haken runterlassen
        private boolean isSideView; //ob sideView ist
        private boolean isControllable; //ob man nach herunterlassen noch steuern darf
        private Ball[] balls; //gespawnten bälle
        private int clawWidth = 100; // Initial claw width
        private int initialClawWidth; // Speichert den ursprünglichen Wert für die Breite des Greifhakens
        private int depthStep = 10; // Schritte für die Tiefe des Greifhakens in der Frontansicht

        /**
         * Konstruktor für das Spiel
         * @param clawColor
         * @param isSideView ob seitenansicht ist
         * @param isControllable ob man noch steuern darf
         */
        public GamePanel(Color clawColor, boolean isSideView, boolean isControllable) {
            this.clawColor = clawColor;
            this.isSideView = isSideView;
            this.isControllable = isControllable;
            setBackground(Color.WHITE);
            setFocusable(true); //das der Fokus auf der GUI ist
            if (isControllable) {
                //nur wenn man noch steuern darf soll keylistener aktiv sein
                addKeyListener(this);
            }

            //bälle setzen
            balls = new Ball[] {
                    new Ball((int) (Math.random() * 700), 500, (int) (Math.random() * 400)),
                    new Ball((int) (Math.random() * 700), 500, (int) (Math.random() * 400)),
                    new Ball((int) (Math.random() * 700), 500, (int) (Math.random() * 400))
            };

            initialClawWidth = clawWidth; // Speichere den ursprünglichen Wert für die Breite des Greifhakens
        }

        /**
         * die sachen halt auf die gui zeichnen
         * @param g the <code>Graphics</code> object to protect
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int clawHeight = 20;
            int clawDropHeight = 100;

            g.setColor(clawColor);
            g.fillRect(clawX, clawY, clawWidth, clawHeight); // Claw

            //wenn der Haken heruntergelassen wird, das zwite rechteck
            if (clawLowered) {
                g.fillRect(clawX + clawWidth / 2 - 10, clawY + clawHeight, 20, clawDropHeight); // Lowered claw
            }

            // Draw the balls
            for (Ball ball : balls) {
                int ballSize = 50;
                g.setColor(Color.GREEN);
                if (isSideView) {
                    g.fillOval(ball.x, getHeight() - ballSize, ballSize, ballSize);
                } else {
                    g.fillOval(500 - ballSize, ball.y, ballSize, ballSize);
                }
            }
        }

        /**
         * keylistener
         * @param e the event to be processed
         */
        @Override
        public void keyPressed(KeyEvent e) {
            //schauen ob man noch steuern kann und ob man den Haken noch triggern kann
            if (isControllable && canDropClaw) {
                //welche sicht ist gerade da
                if (isSideView) {
                    // Steuerung für Seitenansicht
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP://obere Pfeiltaste wurde aktiviert
                            depthFront+=1;
                            clawX = Math.min(clawX + 10, getWidth() - clawWidth); // Bewege nach rechts
                            repaint();//neuzeichnen
                            break;
                        case KeyEvent.VK_DOWN://untere Pfeiltaste wurde aktiviert
                            depthFront-=1;
                            clawX = Math.max(clawX - 10, 0); // Bewege nach links
                            repaint();
                            break;
                        // In der keyPressed-Methode der GamePanel-Klasse für die Seitenansicht
                        case KeyEvent.VK_LEFT://linke Pfeiltaste wurde aktiviert
                            depthSide+=1;
                            clawWidth = Math.min(clawWidth + 10, initialClawWidth); // Vergrößern, aber nicht größer als die Anfangsgröße
                            repaint();
                            break;
                        case KeyEvent.VK_RIGHT://rechte Pfeiltaste wurde aktiviert
                            depthSide-=1;
                            clawWidth = Math.max(clawWidth - 10, 20); // Verkleinern
                            repaint();
                            break;

                        case KeyEvent.VK_ENTER://enter wurde aktiviert
                            //falls noch nicht enter gedrückt wurde
                            if (!clawLowered) {
                                //Haken runterlassen
                                startClawAnimation();
                            }
                            break;
                    }
                } else {
                    // Steuerung für Frontansicht
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            depthSide-=1;
                            clawX = Math.max(clawX - 10, 0);//wenn clawX größer als 0 ist die position
                            repaint();
                            break;
                        case KeyEvent.VK_RIGHT:
                            depthSide+=1;
                            clawX = Math.min(clawX + 10, getWidth() - 100); //kleinere aus den beiden postionen
                            repaint();
                            break;
                        case KeyEvent.VK_UP:
                            depthFront-=1;
                            clawWidth = Math.min(clawWidth + 10, initialClawWidth); // Vergrößern, aber nicht größer als die Anfangsgröße
                            repaint();
                            break;
                        case KeyEvent.VK_DOWN:
                            depthFront+=1;
                            clawWidth = Math.max(clawWidth - 10, 20); // Verkleinern
                            repaint();
                            break;

                        case KeyEvent.VK_ENTER:
                            if (!clawLowered) {
                                startClawAnimation();
                            }
                            break;
                    }
                }
            }
        }

        //unbenützt
        @Override
        public void keyReleased(KeyEvent e) {}

        //unbenützt
        @Override
        public void keyTyped(KeyEvent e) {}

        /**
         * Greifhaken runterlassen animation
         */
        private void startClawAnimation() {
            canDropClaw = false;
            timer = new Timer(10, e -> {
                if (clawY < 400) {
                    clawY += 2;
                    repaint();
                } else {
                    timer.stop();
                    clawLowered = true; //damit greifer nicht nochmal heruntergelassen werden kann
                    repaint();
                    //erst wenn greifer unten ist checken ob er auf ball ist
                    checkCollision();
                }
            });
            timer.start();
        }

        /**
         * checken ob der greifhaken auf dem ball ist
         */
        private void checkCollision() {
            Rectangle clawRect;//zweites rechteck beim runterlassen
            if (isSideView) {
                clawRect = new Rectangle(clawX, getHeight() - 100, clawWidth, 100); // Seitenansicht
            } else {
                clawRect = new Rectangle(clawX + clawWidth / 2 - 10, clawY + 120, 20, 20); // Frontansicht
            }

            //schauen ob greifhaken drauf ist
            boolean prizeCaught = false;
            for (Ball ball : balls) {
                int ballSize = 50;
                Rectangle ballRect;
                //Bälle als Rechteck sehen, damit man einfacher checken kann und ein bisschen gnädiger ist
                if (isSideView) {
                    ballRect = new Rectangle(ball.x, getHeight() - ballSize, ballSize, ballSize); // Seitenansicht
                } else {
                    ballRect = new Rectangle(500 - ballSize, ball.y, ballSize, ballSize); // Frontansicht
                }
                if (clawRect.intersects(ballRect)) {
                    prizeCaught = true;
                    break;
                }
            }

            //Meldung wenn man schafft/nicht schafft
            if (prizeCaught) {
                JOptionPane.showMessageDialog(this, "Du hast einen Preis gefangen!");
            } else {
                JOptionPane.showMessageDialog(this, "Kein Preis gefangen. Versuche es erneut!");
            }

            //fragen ob man nochmal spielen will
            int response = JOptionPane.showConfirmDialog(this, "Möchtest du es nochmal versuchen?", "Nochmal spielen?", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }


        /**
         * Spiel neustarten
         */
        private void resetGame() {
            clawY = originalClawY;
            clawX = 0; // Startposition ganz links
            clawZ = 0;
            clawLowered = false;
            canDropClaw = true;
            clawWidth = initialClawWidth; // Setze die Breite des Greifhakens auf den ursprünglichen Wert zurück

            // Reset balls with random x and z coordinates, and y fixed at the bottom
            for (Ball ball : balls) {
                ball.x = (int) (Math.random() * 700);
                ball.y = 500;
                ball.z = (int) (Math.random() * 400);
            }

            repaint();
        }
    }

    /**
     * Bälle Klasse
     */
    class Ball {
        int x, y, z;

        Ball(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
