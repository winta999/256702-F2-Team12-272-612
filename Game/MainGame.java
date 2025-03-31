package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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

    public MainGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        player = new Player(WIDTH / 2 - 15, HEIGHT - 100, HEIGHT - 180, HEIGHT - 50);
        bullets = new ArrayList<>();
        currentSpeed = 2.0f;
        centipede = new Centipede(50, 50, currentSpeed);
        mushrooms = new ArrayList<>();
        score = 0;
        highScore = 0;
        gameOver = false;

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
        currentSpeed += 0.5f;
        centipede = new Centipede(50, 50, currentSpeed);
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
        timer.start();
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // วาดเห็ด
        for (Mushroom mushroom : mushrooms) {
            mushroom.draw(g);
        }
        
        // วาดกระสุน
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        
        // วาดตะขาบ
        centipede.draw(g);
        
        // วาดผู้เล่น
        player.draw(g);
        
        // วาด UI
        g.setColor(Color.WHITE);
        Font uiFont = new Font("Arial", Font.BOLD, 16);
        g.setFont(uiFont);
        
        int uiBaseY = 25;
        int leftPadding = 15;
        int rightPadding = 15;
        
        // ชีวิต
        g.drawString("Lives: " + player.getLives(), leftPadding, uiBaseY);
        
        // ความเร็ว
        String speedText = "Speed: " + String.format("%.1f", centipede.getSpeed());
        int speedTextWidth = g.getFontMetrics().stringWidth(speedText);
        g.drawString(speedText, (WIDTH - speedTextWidth)/2, uiBaseY);
        
        // คะแนน
        g.drawString("Score: " + score, WIDTH - g.getFontMetrics().stringWidth("Score: 00000") - rightPadding, uiBaseY);
        g.drawString("High: " + highScore, WIDTH - g.getFontMetrics().stringWidth("High: 00000") - rightPadding, uiBaseY + 20);
        
        // หน้าจอจบเกม
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            int boxWidth = 300;
            int boxHeight = 200;
            int boxX = (WIDTH - boxWidth)/2;
            int boxY = (HEIGHT - boxHeight)/2;
            
            g.setColor(new Color(30, 30, 30));
            g.fillRect(boxX, boxY, boxWidth, boxHeight);
            
            g.setColor(new Color(255, 50, 50));
            g.drawRect(boxX, boxY, boxWidth, boxHeight);
            
            g.setColor(Color.WHITE);
            Font gameOverFont = new Font("Arial", Font.BOLD, 36);
            g.setFont(gameOverFont);
            
            String gameOverText = "GAME OVER";
            int gameOverWidth = g.getFontMetrics().stringWidth(gameOverText);
            g.drawString(gameOverText, boxX + (boxWidth - gameOverWidth)/2, boxY + 50);
            
            Font infoFont = new Font("Arial", Font.PLAIN, 24);
            g.setFont(infoFont);
            
            String scoreText = "Score: " + score;
            int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
            g.drawString(scoreText, boxX + (boxWidth - scoreWidth)/2, boxY + 100);
            
            String restartText = "Press R to Restart";
            int restartWidth = g.getFontMetrics().stringWidth(restartText);
            g.drawString(restartText, boxX + (boxWidth - restartWidth)/2, boxY + 150);
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
            if (spacePressed) {
                if (bullets.isEmpty() || bullets.get(bullets.size() - 1).getY() < player.getY() - 40) {
                    bullets.add(new Bullet(player.getX() + player.getWidth()/2 - 2, player.getY()));
                }
            }

            // อัพเดทกระสุน
            bullets.removeIf(bullet -> {
                bullet.move();
                if (!bullet.isVisible()) return true;
                
                // ตรวจสอบการชนกับตะขาบ
                if (centipede.checkCollision(bullet)) {
                    score += 10;
                    if (centipede.isDefeated()) {
                        score += 100;
                        spawnNewCentipede();
                    }
                    return true;
                }
                
                // ตรวจสอบการชนกับเห็ด
                for (Mushroom mushroom : mushrooms) {
                    if (mushroom.checkCollision(bullet)) {
                        return true;
                    }
                }
                return false;
            });

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