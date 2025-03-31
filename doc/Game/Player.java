package doc.Game;

import java.awt.*;

public class Player {
    private int x, y;
    private final int width = 30, height = 20;
    private final int minY, maxY;
    private int lives;

    public Player(int startX, int startY, int minY, int maxY) {
        this.x = startX;
        this.y = startY;
        this.minY = minY;
        this.maxY = maxY;
        this.lives = 3;  // เริ่มต้นมี 3 ชีวิต
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
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public int getLives() { return lives; }

    public void loseLife() { lives--; }

    public boolean isAlive() { return lives > 0; }
}
