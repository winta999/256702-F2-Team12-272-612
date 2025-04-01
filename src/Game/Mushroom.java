package Game;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Mushroom {
    public enum MushroomType {
        NORMAL, RAINBOW, METEOR, GOLDEN, BLUE  // เปลี่ยนจาก RED เป็น BLUE
    }

    private int x, y;
    private final int size = 20;
    private int health = 3;
    private boolean visible = true;
    private Shape mushroomShape;
    private MushroomType type;
    private Random rand = new Random();

    public Mushroom(int x, int y) {
        this.x = x;
        this.y = y;
        this.mushroomShape = new Rectangle2D.Float(x, y, size, size);
        determineType();
    }

    private void determineType() {
        double chance = rand.nextDouble() * 100;
        
        if (chance < 5) { // เห็ดรุ้ง 5%
            this.type = MushroomType.RAINBOW;
        } else if (chance < 15) { // เห็ดอุกกาบาต 10%
            this.type = MushroomType.METEOR;
        } else if (chance < 20) { // เห็ดทอง 5%
            this.type = MushroomType.GOLDEN;
        } else if (chance < 25) { // เห็ดฟ้า 5% (แทนที่สีแดง)
            this.type = MushroomType.BLUE;
        } else { // เห็ดปกติ 75%
            this.type = MushroomType.NORMAL;
        }
    }

    public void draw(Graphics g) {
        if (!visible) return;
        
        Graphics2D g2d = (Graphics2D)g;
        
        // Draw shadow
        g2d.setColor(new Color(80, 0, 0, 150));
        g2d.fillOval(x + 2, y + size - 5, size - 4, 6);
        
        // Draw stem
        Color stemColor = type == MushroomType.GOLDEN ? 
            new Color(210, 175, 60) : new Color(180, 120, 80);
        g2d.setColor(stemColor);
        g2d.fillRect(x + size/2 - 3, y + size/2, 6, size/2);
        
        // Draw cap based on type
        switch(type) {
            case RAINBOW:
                drawRainbowCap(g2d);
                break;
            case METEOR:
                drawMeteorCap(g2d);
                break;
            case GOLDEN:
                drawGoldenCap(g2d);
                break;
            case BLUE:  // เปลี่ยนจาก RED เป็น BLUE
                drawBlueCap(g2d);
                break;
            case NORMAL:
                drawNormalCap(g2d);
                break;
        }
        
        // Draw spots for normal and golden mushrooms
        if (type == MushroomType.NORMAL || type == MushroomType.GOLDEN) {
            drawSpots(g2d, type == MushroomType.GOLDEN ? 
                new Color(255, 255, 200, 220) : new Color(240, 240, 240, 200));
        }
    }

    private void drawNormalCap(Graphics2D g2d) {
        GradientPaint capGradient = new GradientPaint(
            x + size/2, y + size/3, new Color(240, 50, 50),
            x + size/2, y + size/2 + 2, new Color(160, 90, 90)
        );
        g2d.setPaint(capGradient);
        g2d.fillOval(x, y, size, size/2 + 2);
    }

    private void drawBlueCap(Graphics2D g2d) {
        GradientPaint blueGradient = new GradientPaint(
            x + size/2, y + size/3, new Color(100, 200, 255),  // สีฟ้าอ่อน
            x + size/2, y + size/2 + 2, new Color(0, 100, 200)  // สีฟ้าเข้ม
        );
        g2d.setPaint(blueGradient);
        g2d.fillOval(x, y, size, size/2 + 2);
        
        // ลายจุดสีขาว
        g2d.setColor(new Color(255, 255, 255, 200));
        int[][] spots = {{4,3}, {12,3}, {8,6}, {15,7}, {2,5}};
        for (int[] spot : spots) {
            int spotSize = 2 + (int)(Math.random() * 2);
            g2d.fillOval(x + spot[0], y + spot[1], spotSize, spotSize);
        }
    }

    private void drawRainbowCap(Graphics2D g2d) {
        for (int i = 0; i < size; i++) {
            float hue = (System.currentTimeMillis() % 2000) / 2000f + i * 0.05f;
            g2d.setColor(Color.getHSBColor(hue, 1, 1));
            g2d.fillRect(x + i, y, 1, size/2 + 2);
        }
    }

    private void drawMeteorCap(Graphics2D g2d) {
        GradientPaint meteorGradient = new GradientPaint(
            x, y, new Color(100, 100, 100),
            x + size, y + size, new Color(50, 50, 50)
        );
        g2d.setPaint(meteorGradient);
        g2d.fillOval(x, y, size, size);
        
        g2d.setColor(new Color(150, 150, 150));
        g2d.drawLine(x + 5, y + 5, x + 15, y + 15);
        g2d.drawLine(x + 15, y + 5, x + 5, y + 15);
        g2d.drawLine(x + 10, y + 3, x + 10, y + 17);
    }

    private void drawGoldenCap(Graphics2D g2d) {
        GradientPaint goldGradient = new GradientPaint(
            x + size/2, y + size/3, new Color(255, 215, 0),
            x + size/2, y + size/2 + 2, new Color(218, 165, 32)
        );
        g2d.setPaint(goldGradient);
        g2d.fillOval(x, y, size, size/2 + 2);
    }

    private void drawSpots(Graphics2D g2d, Color spotColor) {
        g2d.setColor(spotColor);
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

    public MushroomType getType() {
        return type;
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
        determineType();
    }
}