package Game;

import java.awt.*;
import java.awt.geom.Path2D;

public class Player {
    private int x, y;
    private final int width = 40;  // ขนาดใหญ่ขึ้นเล็กน้อย
    private final int height = 30;
    private final int minY, maxY;
    private int lives;
    private float engineGlow = 0f;
    private boolean engineGlowIncreasing = true;

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
        
        // อัพเดทเอฟเฟกต์เครื่องยนต์เมื่อเคลื่อนที่
        if (dx != 0 || dy != 0) {
            updateEngineGlow();
        }
    }

    private void updateEngineGlow() {
        if (engineGlowIncreasing) {
            engineGlow += 0.05f;
            if (engineGlow >= 1.0f) engineGlowIncreasing = false;
        } else {
            engineGlow -= 0.05f;
            if (engineGlow <= 0.3f) engineGlowIncreasing = true;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // วาดเอฟเฟกต์เครื่องยนต์
        drawEngineEffect(g2d);
        
        // วาดตัวยาน
        drawShipBody(g2d);
        
        // วาดหน้าต่าง cockpit
        drawCockpit(g2d);
        
        // วาดรายละเอียดเพิ่มเติม
        drawShipDetails(g2d);
    }

    private void drawEngineEffect(Graphics2D g2d) {
        // เอฟเฟกต์แสงเครื่องยนต์
        int glowAlpha = (int)(150 * engineGlow);
        int glowWidth = (int)(15 * engineGlow);
        
        GradientPaint engineGradient = new GradientPaint(
            x + width/2, y + height,
            new Color(255, 100, 0, glowAlpha),
            x + width/2, y + height + 20,
            new Color(255, 200, 0, 0)
        );
        
        g2d.setPaint(engineGradient);
        g2d.fillRect(x + width/2 - glowWidth/2, y + height, glowWidth, 20);
    }

    private void drawShipBody(Graphics2D g2d) {
        // สร้างรูปร่างยานแบบกำหนดเอง
        Path2D.Double shipShape = new Path2D.Double();
        shipShape.moveTo(x + width/2, y);
        shipShape.lineTo(x + width, y + height/3);
        shipShape.lineTo(x + width - 10, y + height);
        shipShape.lineTo(x + 10, y + height);
        shipShape.lineTo(x, y + height/3);
        shipShape.closePath();

        // สีตัวยาน
        GradientPaint bodyGradient = new GradientPaint(
            x, y, new Color(30, 120, 220),
            x, y + height, new Color(0, 50, 150)
        );
        g2d.setPaint(bodyGradient);
        g2d.fill(shipShape);

        // กรอบยาน
        g2d.setColor(new Color(200, 230, 255));
        g2d.draw(shipShape);
    }

    private void drawCockpit(Graphics2D g2d) {
        // หน้าต่าง cockpit
        Path2D.Double cockpit = new Path2D.Double();
        cockpit.moveTo(x + width/2 - 5, y + height/4);
        cockpit.curveTo(
            x + width/2, y + height/3,
            x + width/2, y + height/2,
            x + width/2 - 5, y + height/2 + 5
        );
        cockpit.curveTo(
            x + width/2 - 10, y + height/2,
            x + width/2 - 10, y + height/3,
            x + width/2 - 5, y + height/4
        );
        cockpit.closePath();

        GradientPaint cockpitGradient = new GradientPaint(
            x + width/2, y + height/4,
            new Color(150, 220, 255, 200),
            x + width/2, y + height/2 + 5,
            new Color(80, 150, 255, 180)
        );
        g2d.setPaint(cockpitGradient);
        g2d.fill(cockpit);

        // กรอบ cockpit
        g2d.setColor(new Color(200, 230, 255));
        g2d.draw(cockpit);
    }

    private void drawShipDetails(Graphics2D g2d) {
        // เส้นรายละเอียดบนตัวยาน
        g2d.setColor(new Color(100, 180, 255));
        g2d.drawLine(x + width/4, y + height/3, x + width*3/4, y + height/3);
        g2d.drawLine(x + width/3, y + height/2, x + width*2/3, y + height/2);

        // ไฟนำทาง
        g2d.setColor(Color.RED);
        g2d.fillOval(x + 5, y + height/4, 4, 4);
        g2d.setColor(Color.GREEN);
        g2d.fillOval(x + width - 9, y + height/4, 4, 4);

        // เกราะเสริม
        g2d.setColor(new Color(200, 200, 200, 150));
        g2d.fillRoundRect(x + width/2 - 15, y + height - 5, 30, 5, 3, 3);
    }

    public void gainLife() {
        lives = Math.min(5, lives + 1); // ไม่ให้ชีวิตเกิน 5
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