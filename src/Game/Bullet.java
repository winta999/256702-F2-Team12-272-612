package Game;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import Game.Main.MainGame;

public class Bullet {
    private float x, y;
    private final int width = 5;
    private final int height = 10;
    private boolean isVisible;
    private float speedX, speedY;
    private long lastUpdateTime;
    private Shape bulletShape;

    public Bullet(int startX, int startY, float speedX, float speedY) {
        this.x = startX;
        this.y = startY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.isVisible = true;
        this.lastUpdateTime = System.currentTimeMillis();
        this.bulletShape = new Ellipse2D.Float(x, y, width, height);
    }

    public void move() {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000f;
        lastUpdateTime = currentTime;
        
        x += speedX * deltaTime * 60;
        y += speedY * deltaTime * 60;
        bulletShape = new Ellipse2D.Float(x, y, width, height);
        
        if (y < 0 || x < 0 || x > MainGame.WIDTH) {
            isVisible = false;
        }
    }
    
    public void draw(Graphics g) {
        if (!isVisible) return;
        
        Graphics2D g2d = (Graphics2D)g;
        
        // เอฟเฟกต์แสงกระสุน
        g2d.setColor(new Color(255, 255, 150, 100));
        g2d.fillOval((int)x - 3, (int)y - 3, width + 6, height + 6);
        
        // ตัวกระสุน
        GradientPaint bulletGradient = new GradientPaint(
            (int)x, (int)y, Color.YELLOW,
            (int)x, (int)y + height, Color.ORANGE
        );
        g2d.setPaint(bulletGradient);
        g2d.fillRect((int)x, (int)y, width, height);
        
        // ไฟท้ายกระสุน
        g2d.setColor(new Color(255, 100, 0, 150));
        g2d.fillRect((int)x - 1, (int)y + height - 3, width + 2, 3);
    }

    public boolean checkPixelPerfectCollision(Shape target) {
        if (!isVisible) return false;
        
        Area bulletArea = new Area(bulletShape);
        Area targetArea = new Area(target);
        bulletArea.intersect(targetArea);
        return !bulletArea.isEmpty();
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public Rectangle getBounds() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBounds'");
    }
}