package Game;

import java.awt.*;

public class Player {
    private int x, y;
    private final int width = 35;
    private final int height = 25;
    private final int minY, maxY;
    private int lives;

    public Player(int startX, int startY, int minY, int maxY) {
        this.x = startX;
        this.y = startY;
        this.minY = minY;
        this.maxY = maxY;
        this.lives = 3;
    }

    public void move(int dx, int dy, int screenWidth) {
        if (x + dx >= 0 && x + width + dx <= screenWidth) {
            x += dx;
        }
        if (y + dy >= minY && y + height + dy <= maxY) {
            y += dy;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        GradientPaint shipGradient = new GradientPaint(
            x, y, new Color(0, 150, 255),
            x, y + height, new Color(0, 80, 220));
        g2d.setPaint(shipGradient);
        g2d.fillRoundRect(x, y, width, height, 10, 10);
        
        g2d.setColor(new Color(200, 230, 255));
        g2d.drawRoundRect(x, y, width, height, 10, 10);
        
        g2d.setColor(new Color(255, 100, 0, 150));
        g2d.fillOval(x + 5, y + height - 8, 10, 5);
        
        g2d.setColor(new Color(150, 220, 255, 200));
        g2d.fillArc(x + width - 15, y + 5, 15, 15, 0, 180);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getLives() { return lives; }
    public void loseLife() { lives--; }
    public boolean isAlive() { return lives > 0; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
}