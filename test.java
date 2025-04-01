

import javax.swing.*;

import Game.Bullet;
import Game.Centipede;
import Game.Mushroom;
import Game.Player;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class test extends JPanel implements ActionListener {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 700;

    private Player player;
    private ArrayList<Bullet> bullets;
    private Centipede centipede;
    private ArrayList<Mushroom> mushrooms;
    private ArrayList<Bullet> enemyBullets;
    private Timer timer;
    private int score;
    private int highScore;
    private boolean leftPressed, rightPressed, upPressed, downPressed, spacePressed;
    private int currentSpeed;
    private boolean gameOver;
    private Random random = new Random();

    public test() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        player = new Player(WIDTH / 2 - 15, HEIGHT - 100, HEIGHT - 180, HEIGHT - 50);
        bullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        currentSpeed = 2;
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
                handleKeyPress(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), false);
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void handleKeyPress(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT: leftPressed = pressed; break;
            case KeyEvent.VK_RIGHT: rightPressed = pressed; break;
            case KeyEvent.VK_UP: upPressed = pressed; break;
            case KeyEvent.VK_DOWN: downPressed = pressed; break;
            case KeyEvent.VK_SPACE: spacePressed = pressed; break;
            case KeyEvent.VK_R: if (gameOver && pressed) restartGame(); break;
        }
    }

    private void generateMushrooms() {
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(WIDTH / 30) * 30;
            int y = 50 + random.nextInt((HEIGHT - 200) / 30) * 30;
            mushrooms.add(new Mushroom(x, y));
        }
    }

    private void spawnNewCentipede() {
        currentSpeed = Math.min(currentSpeed + 1, 10);
        centipede = new Centipede(50, 50, currentSpeed);
    }

    private void restartGame() {
        player = new Player(WIDTH / 2 - 15, HEIGHT - 100, HEIGHT - 180, HEIGHT - 50);
        bullets.clear();
        enemyBullets.clear();
        currentSpeed = 2;
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
        
        // Draw game elements
        for (Mushroom mushroom : mushrooms) {
            mushroom.draw(g);
        }
        
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        
        for (Bullet bullet : enemyBullets) {
            bullet.draw(g);
        }
        
        centipede.draw(g);
        player.draw(g);
        
        drawUI(g);
        
        if (gameOver) {
            drawGameOverScreen(g);
        }
    }

    private void drawUI(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        
        g.drawString("Lives: " + player.getLives(), 10, 20);
        g.drawString("Score: " + score, WIDTH - 100, 20);
        g.drawString("Speed: " + currentSpeed, WIDTH / 2 - 30, 20);
        g.drawString("High Score: " + highScore, WIDTH - 200, 20);
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        
        String gameOverText = "GAME OVER";
        g.drawString(gameOverText, WIDTH/2 - g.getFontMetrics().stringWidth(gameOverText)/2, HEIGHT/2 - 50);
        
        String scoreText = "Score: " + score;
        g.drawString(scoreText, WIDTH/2 - g.getFontMetrics().stringWidth(scoreText)/2, HEIGHT/2);
        
        String restartText = "Press R to Restart";
        g.drawString(restartText, WIDTH/2 - g.getFontMetrics().stringWidth(restartText)/2, HEIGHT/2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            handlePlayerMovement();
            handleShooting();
            updateBullets();
            updateEnemyBullets();
            updateCentipede();
            checkCollisions();
        }
        repaint();
    }

    private void handlePlayerMovement() {
        int dx = 0, dy = 0;
        if (leftPressed) dx -= 5;
        if (rightPressed) dx += 5;
        if (upPressed) dy -= 4;  // Increased vertical speed
        if (downPressed) dy += 4; // Increased vertical speed
        
        player.move(dx, dy, WIDTH);
    }

    private void handleShooting() {
        if (spacePressed && player.canShoot()) {
            bullets.add(new Bullet(player.getX() + player.getWidth()/2 - 2, player.getY()));
            player.confirmShot();
        }
    }

    private void updateBullets() {
        bullets.removeIf(bullet -> {
            bullet.move();
            if (!bullet.isVisible()) return true;
            
            if (centipede.checkCollision(bullet)) {
                score += 10;
                if (centipede.isDefeated()) {
                    score += 100;
                    spawnNewCentipede();
                }
                return true;
            }
            
            for (Mushroom mushroom : mushrooms) {
                if (mushroom.checkCollision(bullet)) {
                    return true;
                }
            }
            return false;
        });
    }

    private void updateEnemyBullets() {
        if (centipede.shouldShoot()) {
            Bullet newBullet = centipede.shoot();
            if (newBullet != null) {
                enemyBullets.add(newBullet);
            }
        }

        enemyBullets.removeIf(bullet -> {
            bullet.move();
            if (!bullet.isVisible()) return true;
            
            if (player.getBounds().intersects(bullet.getBounds())) {
                player.loseLife();
                if (!player.isAlive()) {
                    if (score > highScore) {
                        highScore = score;
                    }
                    gameOver = true;
                }
                return true;
            }
            
            return false;
        });
    }

    private void updateCentipede() {
        centipede.move();
        centipede.update();
        player.adjustShotDelayBasedOnCentipedeDistance(centipede);
    }

    private void checkCollisions() {
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Centipede Game");
        test game = new test();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}