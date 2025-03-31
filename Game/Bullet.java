package Game;

import java.awt.*;

public class Bullet {
    private int x, y;
    private final int width = 5, height = 10;
    private boolean isVisible;

    public Bullet(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.isVisible = true;
    }

    public void move() {
        y -= 5;  // กระสุนเคลื่อนที่ขึ้น
        if (y < 0) {
            isVisible = false;  // กระสุนหายเมื่อพ้นขอบจอ
        }
    }

    public void draw(Graphics g) {
        if (isVisible) {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, width, height);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
