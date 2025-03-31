package Game;

import java.awt.*;

public class Player {
    private int x, y;
    private final int width = 35;  // ขยายขนาดผู้เล่นเล็กน้อย
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
        g.setColor(new Color(30, 120, 255));  // สีน้ำเงินเข้มขึ้น
        g.fillRoundRect(x, y, width, height, 5, 5);  // มุมโค้งเล็กน้อย
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