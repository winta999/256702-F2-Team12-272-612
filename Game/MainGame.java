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
    private int level;
    private int highScore;
    private boolean leftPressed, rightPressed, upPressed, downPressed, spacePressed;

    public MainGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        int playerStartY = HEIGHT - 100;
        int minY = HEIGHT - 180;
        int maxY = HEIGHT - 50;

        player = new Player(WIDTH / 2 - 15, playerStartY, minY, maxY);
        bullets = new ArrayList<>();
        centipede = new Centipede(50, 50);
        mushrooms = new ArrayList<>();
        score = 0;
        level = 1;
        highScore = 0;

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
        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(WIDTH / 30) * 30;
            int y = rand.nextInt(HEIGHT / 30) * 30;
            mushrooms.add(new Mushroom(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        player.draw(g);
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        centipede.draw(g);
        for (Mushroom mushroom : mushrooms) {
            mushroom.draw(g);
        }

        g.setColor(Color.WHITE);
        g.drawString("Lives: " + player.getLives(), 10, 20);
        g.drawString("Score: " + score, WIDTH - 100, 20);
        g.drawString("Level: " + level, WIDTH / 2 - 30, 20);
        g.drawString("High Score: " + highScore, WIDTH - 200, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dx = 0, dy = 0;
        if (leftPressed) dx -= 5;
        if (rightPressed) dx += 5;
        if (upPressed) dy -= 3;
        if (downPressed) dy += 3;

        player.move(dx, dy, WIDTH);

        if (spacePressed) {
            if (bullets.isEmpty() || bullets.get(bullets.size() - 1).getY() < player.getY() - 40) {
                bullets.add(new Bullet(player.getX() + 13, player.getY()));
            }
        }

        bullets.removeIf(bullet -> {
            bullet.move();
            if (!bullet.isVisible()) return true;
            if (centipede.checkCollision(bullet)) {
                score += 10;
                if (score % 100 == 0) {
                    levelUp();
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

        centipede.move();

        if (centipede.checkCollisionWithPlayer(player)) {
            player.loseLife();
            if (!player.isAlive()) {
                updateHighScore();
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }

        repaint();
    }

    private void levelUp() {
        level++;
        centipede.increaseSpeed();
    }

    private void updateHighScore() {
        if (score > highScore) {
            highScore = score;
        }
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

class Mushroom {
    private int x, y;
    private final int size = 20;

    public Mushroom(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, size, size);
    }

    public boolean checkCollision(Bullet bullet) {
        if (bullet.getX() >= x && bullet.getX() <= x + size &&
            bullet.getY() >= y && bullet.getY() <= y + size) {
            return true;
        }
        return false;
    }
}
