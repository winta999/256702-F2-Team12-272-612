package Game.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import Game.Player;
import Game.Bullet;
import Game.Centipede;
import Game.Mushroom;
import Game.Comet;

public class MainGame extends JPanel implements ActionListener {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 700;

    private Player player;
    private ArrayList<Bullet> bullets;
    private Centipede centipede;
    private ArrayList<Mushroom> mushrooms;
    private ArrayList<Comet> comets;
    private Timer timer;
    private int score;
    private int highScore;
    private boolean leftPressed, rightPressed, upPressed, downPressed, spacePressed;
    private float currentSpeed;
    private boolean gameOver;
    private long lastShotTime;
    private long lastCometSpawnTime;
    private final long SHOT_DELAY = 300;
    private final long COMET_SPAWN_DELAY = 5000;
    private int[] starX = new int[50];
    private int[] starY = new int[50];
    private float baseBulletSpeed = 5.0f;
    private float bulletSpeedMultiplier = 1.0f;
    private float cometBaseSpeed = 3.0f;
    private Random rand = new Random();
    
    public MainGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        // Initialize background stars
        for (int i = 0; i < 50; i++) {
            starX[i] = rand.nextInt(WIDTH);
            starY[i] = rand.nextInt(HEIGHT);
        }

        player = new Player(WIDTH / 2 - 15, HEIGHT - 100, HEIGHT - 180, HEIGHT - 50);
        bullets = new ArrayList<>();
        comets = new ArrayList<>();
        currentSpeed = 2.0f;
        centipede = new Centipede(50, 50, currentSpeed);
        mushrooms = new ArrayList<>();
        score = 0;
        highScore = 0;
        gameOver = false;
        lastShotTime = 0;
        lastCometSpawnTime = System.currentTimeMillis();

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
            
            private void handleKeyPress(int keyCode, boolean pressed) {
                switch(keyCode) {
                    case KeyEvent.VK_LEFT: leftPressed = pressed; break;
                    case KeyEvent.VK_RIGHT: rightPressed = pressed; break;
                    case KeyEvent.VK_UP: upPressed = pressed; break;
                    case KeyEvent.VK_DOWN: downPressed = pressed; break;
                    case KeyEvent.VK_SPACE: spacePressed = pressed; break;
                    case KeyEvent.VK_R: if (pressed && gameOver) restartGame(); break;
                    case KeyEvent.VK_ESCAPE: 
                        if (pressed) returnToMenu();
                        break;
                }
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void returnToMenu() {
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new GameMenu(frame, this));
        frame.pack();
        frame.repaint();
    }

    private void generateMushrooms() {
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
        comets.clear();
        currentSpeed = 2.0f;
        centipede = new Centipede(50, 50, currentSpeed);
        mushrooms.clear();
        generateMushrooms();
        score = 0;
        gameOver = false;
        lastShotTime = 0;
        lastCometSpawnTime = System.currentTimeMillis();
        bulletSpeedMultiplier = 1.0f;
        timer.start();
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        // Draw background
        GradientPaint bgGradient = new GradientPaint(
            0, 0, new Color(10, 5, 30),
            0, HEIGHT, new Color(0, 0, 0)
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Draw stars
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 50; i++) {
            int size = 1 + (int)(Math.random() * 2);
            g2d.fillOval(starX[i], starY[i], size, size);
        }
        
        // Draw game objects
        for (Mushroom mushroom : mushrooms) {
            mushroom.draw(g2d);
        }
        
        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
        }
        
        for (Comet comet : comets) {
            comet.draw(g2d);
        }
        
        centipede.draw(g2d);
        player.draw(g2d);
        
        // Draw UI
        drawUI(g2d);
        
        // Draw game over screen if needed
        if (gameOver) {
            drawGameOverScreen(g2d);
        }
    }

    private void drawUI(Graphics2D g2d) {
        
        g2d.setColor(Color.WHITE);
        Font uiFont = new Font("Arial", Font.BOLD, 16);
        g2d.setFont(uiFont);
        
        g2d.drawString("LIVES: " + player.getLives(), 20, 30);
        g2d.drawString("SCORE: " + score, WIDTH - 120, 30);
        g2d.drawString("HIGH: " + highScore, WIDTH - 120, 50);
    }

    private void drawGameOverScreen(Graphics2D g2d) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            handlePlayerMovement();
            handleShooting();
            handleBullets();
            handleComets();
            handleCentipede();
        }
        repaint();
    }

    private void handlePlayerMovement() {
        int dx = 0, dy = 0;
        if (leftPressed) dx -= 5;
        if (rightPressed) dx += 5;
        if (upPressed) dy -= 3;
        if (downPressed) dy += 3;
        player.move(dx, dy, WIDTH);
    }

    private void handleShooting() {
        if (spacePressed && System.currentTimeMillis() - lastShotTime > SHOT_DELAY) {
            bullets.add(new Bullet(
                player.getX() + player.getWidth()/2 - 2, 
                player.getY(),
                0,
                -baseBulletSpeed * bulletSpeedMultiplier
            ));
            lastShotTime = System.currentTimeMillis();
        }
    }

    private void handleBullets() {
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            bullet.move();
            
            if (!bullet.isVisible()) {
                bulletIter.remove();
                continue;
            }
            
            if (centipede.checkCollision(bullet)) {
                score += 10;
                if (centipede.isDefeated()) {
                    score += 100;
                    spawnNewCentipede();
                }
                bulletIter.remove();
                continue;
            }
            
            checkBulletMushroomCollision(bulletIter, bullet);
        }
    }

    private void checkBulletMushroomCollision(Iterator<Bullet> bulletIter, Bullet bullet) {
        for (Mushroom mushroom : mushrooms) {
            if (mushroom.isVisible() && bullet.checkPixelPerfectCollision(mushroom.getBounds())) {
                if (mushroom.takeDamage()) {
                    handleMushroomEffect(mushroom);
                    mushroom.respawn(rand.nextInt(WIDTH / 20) * 20, 50 + rand.nextInt((HEIGHT - 200) / 20) * 20);
                }
                bulletIter.remove();
                break;
            }
        }
    }

    private void handleMushroomEffect(Mushroom mushroom) {
        switch(mushroom.getType()) {
            case RAINBOW: score += 500; break;
            case BLUE: 
                bulletSpeedMultiplier += 0.5f;
                score += 20;
                break;
            case METEOR: 
                bulletSpeedMultiplier = Math.max(0.5f, bulletSpeedMultiplier - 0.5f);
                score += 30;
                break;
            case GOLDEN: 
                player.gainLife();
                score += 50;
                break;
            default: score += 50;
        }
    }

    private void handleComets() {
        // Spawn new comets
        if (System.currentTimeMillis() - lastCometSpawnTime > COMET_SPAWN_DELAY) {
            float speedScale = 1.0f + (score / 2000f);
            comets.add(new Comet(
                WIDTH, HEIGHT, 
                HEIGHT - 180, HEIGHT - 50, 
                Math.min(cometBaseSpeed * speedScale, 8.0f)
            ));
            lastCometSpawnTime = System.currentTimeMillis();
        }

        // Move and check comets
        Iterator<Comet> cometIter = comets.iterator();
        while (cometIter.hasNext()) {
            Comet comet = cometIter.next();
            comet.move();
            
            if (!comet.isVisible()) {
                cometIter.remove();
                continue;
            }
            
            if (comet.checkCollisionWithPlayer(player)) {
                score = Math.max(0, score - 50);
                player.loseLife();
                cometIter.remove();
                
                if (!player.isAlive()) {
                    if (score > highScore) {
                        highScore = score;
                    }
                    gameOver = true;
                }
            }
        }
    }

    private void handleCentipede() {
        centipede.move();
        centipede.update();

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        MainGame game = new MainGame();
        GameMenu menu = new GameMenu(frame, game);
        
        frame.add(menu);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}