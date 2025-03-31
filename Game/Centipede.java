package Game;

import java.awt.*;
import java.util.ArrayList;

public class Centipede {
    private ArrayList<Segment> segments;
    private int direction;
    private float speed;
    private final int SEGMENT_WIDTH = 40;
    private final int SEGMENT_HEIGHT = 15;
    private long lastUpdateTime;
    private float waveOffset;
    private float targetY;
    private float verticalSpeed;

    private class Segment {
        float x, y;
        float offset;
        float targetX, targetY;
        
        public Segment(float x, float y) {
            this.x = this.targetX = x;
            this.y = this.targetY = y;
            this.offset = (float) (Math.random() * 100);
        }
    }
    
    public Centipede(float startX, float startY, float initialSpeed) {
        this.speed = initialSpeed;
        this.direction = 1;
        this.segments = new ArrayList<>();
        this.lastUpdateTime = System.currentTimeMillis();
        this.waveOffset = 0;
        this.targetY = startY;
        this.verticalSpeed = 0.5f;
        
        for (int i = 0; i < 10; i++) {
            segments.add(new Segment(
                startX + i * (SEGMENT_WIDTH + 5), 
                startY + (float)(Math.sin(i * 0.5) * 10)
            ));
        }
    }

    @SuppressWarnings("unused")
    public void move() {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000f;
        lastUpdateTime = currentTime;
        
        waveOffset += deltaTime * 2;
        
        int maxY = MainGame.HEIGHT - 100;
        Segment head = segments.get(0);
        
        // เคลื่อนที่ส่วนหัวในแนวนอน
        head.targetX += direction * speed * deltaTime * 60;
        
        // เคลื่อนที่ลงด้านล่างอย่างช้าๆ
        targetY += verticalSpeed * deltaTime * 60;
        head.targetY = targetY + (float)Math.sin(waveOffset) * 10;
        
        // ตรวจสอบการชนขอบ
        if (head.targetX <= 0 || head.targetX >= MainGame.WIDTH - SEGMENT_WIDTH) {
            direction = -direction;
            head.targetX = Math.max(0, Math.min(head.targetX, MainGame.WIDTH - SEGMENT_WIDTH));
            verticalSpeed += 0.1f;
        }
        
        // จำกัดความเร็วลงสูงสุด
        verticalSpeed = Math.min(verticalSpeed, 2.0f);
        
        // อัพเดทตำแหน่งจริงของส่วนหัว
        head.x += (head.targetX - head.x) * 0.2f;
        head.y += (head.targetY - head.y) * 0.2f;
    }

    public void update() {
        // อัพเดทส่วนต่อๆ มาด้วยความล่าช้า
        for (int i = 1; i < segments.size(); i++) {
            Segment current = segments.get(i);
            Segment prev = segments.get(i-1);
            
            current.targetX = prev.x - (SEGMENT_WIDTH + 5);
            current.targetY = prev.y;
            
            current.x += (current.targetX - current.x) * 0.1f;
            current.y += (current.targetY - current.y) * 0.1f;
            
            // เพิ่มการเคลื่อนที่แบบคลื่นเล็กน้อย
            current.y += (float)Math.sin(waveOffset + current.offset) * 2;
        }
    }

    public boolean reachedBottom() {
        return segments.get(0).y >= MainGame.HEIGHT - 50;
    }

    public void draw(Graphics g) {
        for (int i = segments.size()-1; i >= 0; i--) {
            Segment seg = segments.get(i);
            
            int segmentColor = 180 - (i * 12);
            g.setColor(new Color(0, Math.max(50, segmentColor), 0));
            g.fillRoundRect((int)seg.x, (int)seg.y, SEGMENT_WIDTH, SEGMENT_HEIGHT, 10, 10);
            
            g.setColor(new Color(0, Math.max(30, segmentColor/2), 0));
            g.drawRoundRect((int)seg.x, (int)seg.y, SEGMENT_WIDTH, SEGMENT_HEIGHT, 10, 10);
            
            if (i > 0) {
                g.drawLine((int)seg.x, (int)seg.y + SEGMENT_HEIGHT/2, 
                           (int)seg.x - 5, (int)seg.y + SEGMENT_HEIGHT/2);
            }
            
            if (i == 0) {
                g.setColor(Color.WHITE);
                g.fillOval((int)seg.x + 5, (int)seg.y + 3, 8, 8);
                g.fillOval((int)seg.x + 25, (int)seg.y + 3, 8, 8);
                g.setColor(Color.BLACK);
                g.fillOval((int)seg.x + 7, (int)seg.y + 5, 4, 4);
                g.fillOval((int)seg.x + 27, (int)seg.y + 5, 4, 4);
            }
        }
    }

    public boolean checkCollision(Bullet bullet) {
        for (int i = 0; i < segments.size(); i++) {
            Segment seg = segments.get(i);
            if (bullet.getX() >= seg.x && bullet.getX() <= seg.x + SEGMENT_WIDTH &&
                bullet.getY() >= seg.y && bullet.getY() <= seg.y + SEGMENT_HEIGHT) {
                segments.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean checkCollisionWithPlayer(Player player) {
        Rectangle playerRect = player.getBounds();
        for (Segment seg : segments) {
            if (playerRect.intersects(new Rectangle((int)seg.x, (int)seg.y, SEGMENT_WIDTH, SEGMENT_HEIGHT))) {
                return true;
            }
        }
        return false;
    }

    public boolean isDefeated() {
        return segments.isEmpty();
    }
    
    public float getSpeed() {
        return speed;
    }
}