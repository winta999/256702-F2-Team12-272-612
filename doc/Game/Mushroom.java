package doc.Game;

import java.awt.*;

public class Mushroom {
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
