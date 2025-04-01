package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainGame extends JPanel implements ActionListener {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 700;

    private Player player;
    private ArrayList<Bullet> bullets;
    private Centipede centipede;
    private ArrayList<Mushroom> mushrooms;
    private Timer timer;
    private int score;
    private int highScore;
    private boolean leftPressed, rightPressed, upPressed, downPressed, spacePressed;
    private float currentSpeed;
    private boolean gameOver;
    private long lastShotTime;
    private final long SHOT_DELAY = 300;
    private int[] starX = new int[50];
    private int[] starY = new int[50];

    public MainGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        // สร้างดาวแบบสุ่มสำหรับพื้นหลัง
        Random rand = new Random();
        for (int i = 0; i < 50; i++) {
            starX[i] = rand.nextInt(WIDTH);
            starY[i] = rand.nextInt(HEIGHT);
        }

        player = new Player(WIDTH / 2 - 15, HEIGHT - 100, HEIGHT - 180, HEIGHT - 50);
        bullets = new ArrayList<>();
        currentSpeed = 2.0f;
        centipede = new Centipede(50, 50, currentSpeed);
        mushrooms = new ArrayList<>();
        score = 0;
        highScore = 0;
        gameOver = false;
        lastShotTime = 0;

        generateMushrooms();

        timer = new Timer(16, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) leftPressed = true;
                if (key == KeyEvent.VK_RIGHT) rightPressed = true;
                if (key == KeyEvent.VK_UP) upPressed = true;
                if (key == KeyEvent.VK_DOWN) downPressed = true;
                if (key == KeyEvent.VK_SPACE) spacePressed = true;
                if (key == KeyEvent.VK_R && gameOver) restartGame();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) leftPressed = false;
                if (key == KeyEvent.VK_RIGHT) rightPressed = false;
                if (key == KeyEvent.VK_UP) upPressed = false;
                if (key == KeyEvent.VK_DOWN) downPressed = false;
                if (key == KeyEvent.VK_SPACE) spacePressed = false;
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void generateMushrooms() {
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            int x = rand.nextInt(WIDTH / 20) * 20;
            int y = 50 + rand.nextInt((HEIGHT - 200) / 20) * 20;
            mushrooms.add(new Mushroom(x, y));
        }
    }

    private void spawnNewCentipede() {
        currentSpeed = 2.0f + (score / 500f);
        int segmentCount = 10 + (score / 200);
        segmentCount = Math.min(segmentCount, 20);
        
        centipede = new Centipede(50, 50, currentSpeed, segmentCount);
        centipede.setVerticalFrequency(0.05f + (score / 2000f));
    }

    private void restartGame() {
        player = new Player(WIDTH / 2 - 15, HEIGHT - 100, HEIGHT - 180, HEIGHT - 50);
        bullets.clear();
        currentSpeed = 2.0f;
        centipede = new Centipede(50, 50, currentSpeed);
        mushrooms.clear();
        generateMushrooms();
        score = 0;
        gameOver = false;
        lastShotTime = 0;
        timer.start();
        requestFocus();
    }

    public Centipede getCentipede() {
        return centipede;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        // วาดพื้นหลัง
        GradientPaint bgGradient = new GradientPaint(
            0, 0, new Color(10, 5, 30),
            0, HEIGHT, new Color(0, 0, 0)
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // วาดดาว
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 50; i++) {
            int size = 1 + (int)(Math.random() * 2);
            g2d.fillOval(starX[i], starY[i], size, size);
        }
        
        // วาดเห็ด
        for (Mushroom mushroom : mushrooms) {
            mushroom.draw(g2d);
        }
        
        // วาดกระสุน
        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
        }
        
        // วาดตะขาบ
        centipede.draw(g2d);
        
        // วาดผู้เล่น
        player.draw(g2d);
        
        // วาด UI
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.fillRoundRect(10, 10, 120, 40, 10, 10);
        g2d.fillRoundRect(WIDTH - 130, 10, 120, 60, 10, 10);
        
        g2d.setColor(Color.BLACK);
        Font uiFont = new Font("Arial", Font.BOLD, 16);
        g2d.setFont(uiFont);
        
        g2d.drawString("LIVES: " + player.getLives(), 20, 30);
        g2d.drawString("SCORE: " + score, WIDTH - 120, 30);
        g2d.drawString("HIGH: " + highScore, WIDTH - 120, 50);
        
        // หน้าจอจบเกม
        if (gameOver) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            
            int boxWidth = 300;
            int boxHeight = 200;
            int boxX = (WIDTH - boxWidth)/2;
            int boxY = (HEIGHT - boxHeight)/2;
            
            g2d.setColor(new Color(30, 30, 30));
            g2d.fillRect(boxX, boxY, boxWidth, boxHeight);
            
            g2d.setColor(new Color(255, 50, 50));
            g2d.drawRect(boxX, boxY, boxWidth, boxHeight);
            
            g2d.setColor(Color.WHITE);
            Font gameOverFont = new Font("Arial", Font.BOLD, 36);
            g2d.setFont(gameOverFont);
            
            String gameOverText = "GAME OVER";
            int gameOverWidth = g2d.getFontMetrics().stringWidth(gameOverText);
            g2d.drawString(gameOverText, boxX + (boxWidth - gameOverWidth)/2, boxY + 50);
            
            Font infoFont = new Font("Arial", Font.PLAIN, 24);
            g2d.setFont(infoFont);
            
            String scoreText = "Score: " + score;
            int scoreWidth = g2d.getFontMetrics().stringWidth(scoreText);
            g2d.drawString(scoreText, boxX + (boxWidth - scoreWidth)/2, boxY + 100);
            
            String restartText = "Press R to Restart";
            int restartWidth = g2d.getFontMetrics().stringWidth(restartText);
            g2d.drawString(restartText, boxX + (boxWidth - restartWidth)/2, boxY + 150);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // การเคลื่อนที่ของผู้เล่น
            int dx = 0, dy = 0;
            if (leftPressed) dx -= 5;
            if (rightPressed) dx += 5;
            if (upPressed) dy -= 3;
            if (downPressed) dy += 3;

            player.move(dx, dy, WIDTH);

            // การยิงกระสุน
            if (spacePressed && System.currentTimeMillis() - lastShotTime > SHOT_DELAY) {
                bullets.add(new Bullet(player.getX() + player.getWidth()/2 - 2, player.getY()));
                lastShotTime = System.currentTimeMillis();
            }

            // อัพเดทกระสุน
            Iterator<Bullet> bulletIter = bullets.iterator();
            while (bulletIter.hasNext()) {
                Bullet bullet = bulletIter.next();
                bullet.move();
                
                if (!bullet.isVisible()) {
                    bulletIter.remove();
                    continue;
                }
                
                // ตรวจสอบการชนกับตะขาบ
                if (centipede.checkCollision(bullet)) {
                    score += 10;
                    if (centipede.isDefeated()) {
                        score += 100;
                        spawnNewCentipede();
                    }
                    bulletIter.remove();
                    continue;
                }
                
                // ตรวจสอบการชนกับเห็ด
                for (Mushroom mushroom : mushrooms) {
                    if (mushroom.isVisible() && bullet.checkPixelPerfectCollision(mushroom.getBounds())) {
                        if (mushroom.takeDamage()) {
                            score += 50;
                            // สร้างเห็ดใหม่ในตำแหน่งสุ่ม
                            Random rand = new Random();
                            int newX = rand.nextInt(WIDTH / 20) * 20;
                            int newY = 50 + rand.nextInt((HEIGHT - 200) / 20) * 20;
                            mushroom.respawn(newX, newY);
                        }
                        bulletIter.remove();
                        break;
                    }
                }
            }

            // อัพเดทตะขาบ
            centipede.move();
            centipede.update();

            // ตรวจสอบการชน
            if (centipede.checkCollisionWithPlayer(player) || centipede.reachedBottom()) {
                player.loseLife();
                if (!player.isAlive()) {
                    if (score > highScore) {
                        highScore = score;
                    }
                    gameOver = true;
                } else {
                    centipede = new Centipede(50, 50, currentSpeed);
                }
            }
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Centipede Game");
        MainGame game = new MainGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}