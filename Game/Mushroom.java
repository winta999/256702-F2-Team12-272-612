package Game;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Mushroom {
    private int x, y;
    private final int size = 20;
    private int health = 3;
    private boolean visible = true;
    private Shape mushroomShape;

    public Mushroom(int x, int y) {
        this.x = x;
        this.y = y;
        this.mushroomShape = new Rectangle2D.Float(x, y, size, size);
    }

    public void draw(Graphics g) {
        if (!visible) return;
        
        Graphics2D g2d = (Graphics2D)g;
        
        // เปลี่ยนสีตามสุขภาพ
        Color mushroomColor;
        switch(health) {
            case 3: mushroomColor = new Color(240, 50, 50); break;
            case 2: mushroomColor = new Color(200, 70, 70); break;
            default: mushroomColor = new Color(160, 90, 90); break;
        }

        // เงา
        g2d.setColor(new Color(80, 0, 0, 150));
        g2d.fillOval(x + 2, y + size - 5, size - 4, 6);
        
        // ลำต้น
        g2d.setColor(new Color(180, 120, 80));
        g2d.fillRect(x + size/2 - 3, y + size/2, 6, size/2);
        
        // หมวกเห็ด
        RadialGradientPaint capGradient = new RadialGradientPaint(
            x + size/2, y + size/3, size/2,
            new float[]{0.1f, 0.9f},
            new Color[]{mushroomColor, mushroomColor.darker()}
        );
        g2d.setPaint(capGradient);
        g2d.fillOval(x, y, size, size/2 + 2);
        
        // จุดบนหมวกเห็ด
        g2d.setColor(new Color(240, 240, 240, 200));
        int[][] spots = {{4,3}, {12,3}, {8,6}, {15,7}, {2,5}};
        for (int[] spot : spots) {
            int spotSize = 2 + (int)(Math.random() * 3);
            g2d.fillOval(x + spot[0], y + spot[1], spotSize, spotSize);
        }
    }

    public boolean takeDamage() {
        health--;
        if (health <= 0) {
            visible = false;
            return true;
        }
        return false;
    }

    public boolean isVisible() {
        return visible;
    }

    public Shape getBounds() {
        return mushroomShape;
    }

    public void respawn(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.health = 3;
        this.visible = true;
        this.mushroomShape = new Rectangle2D.Float(newX, newY, size, size);
    }
}