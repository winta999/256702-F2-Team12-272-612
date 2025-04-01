package Game;

import java.awt.*;
import java.util.ArrayList;

public class Centipede {
    private ArrayList<Segment> segments;
    private int direction;
    private float speed;
    private final int SEGMENT_WIDTH = 28;
    private final int SEGMENT_HEIGHT = 15;
    private final int SEGMENT_GAP = 2;
    private long lastUpdateTime;
    private float waveOffset;
    private float targetY;
    private float verticalSpeed;
    private Color bodyColor = new Color(0, 180, 0);
    private Color headColor = new Color(0, 220, 0);
    private float verticalAmplitude = 3.0f;
    private float verticalFrequency = 0.05f;

    class Segment {
        float x, y;
        float offset;
        float targetX, targetY;
        Rectangle bounds;
        
        public Segment(float x, float y) {
            this.x = this.targetX = x;
            this.y = this.targetY = y;
            this.offset = (float) (Math.random() * 100);
            this.bounds = new Rectangle((int)x, (int)y, SEGMENT_WIDTH, SEGMENT_HEIGHT);
        }
        
        public void updateBounds() {
            bounds.setLocation((int)x, (int)y);
        }
    }
    
    public Centipede(float startX, float startY, float initialSpeed) {
        this(startX, startY, initialSpeed, 10);
    }
    
    public Centipede(float startX, float startY, float initialSpeed, int segmentCount) {
        this.speed = initialSpeed;
        this.direction = 1;
        this.segments = new ArrayList<>();
        this.lastUpdateTime = System.currentTimeMillis();
        this.waveOffset = 0;
        this.targetY = startY;
        this.verticalSpeed = 0.3f;
        
        for (int i = 0; i < segmentCount; i++) {
            segments.add(new Segment(
                startX + i * (SEGMENT_WIDTH + SEGMENT_GAP), 
                startY + (float)(Math.sin(i * 0.3) * 3)
            ));
        }
    }

    public void setVerticalFrequency(float frequency) {
        this.verticalFrequency = frequency;
    }

    public void move() {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000f;
        lastUpdateTime = currentTime;
        
        waveOffset += deltaTime * 1.5f;
        
        if (segments.isEmpty()) return;
        
        Segment head = segments.get(0);
        
        head.targetX += direction * speed * deltaTime * 60;
        
        float waveMovement = (float)Math.sin(waveOffset) * verticalAmplitude;
        head.targetY += verticalSpeed * deltaTime * 30;
        head.targetY += waveMovement;
        
        float distanceToBottom = MainGame.HEIGHT - head.targetY;
        if (distanceToBottom < 200) {
            verticalSpeed = 0.5f + (1.0f - (distanceToBottom / 200)) * 2.0f;
        }
        
        if (head.targetX <= 0 || head.targetX >= MainGame.WIDTH - SEGMENT_WIDTH) {
            direction = -direction;
            head.targetX = Math.max(0, Math.min(head.targetX, MainGame.WIDTH - SEGMENT_WIDTH));
            verticalSpeed = Math.min(verticalSpeed + 0.1f, 3.0f);
        }
        
        head.x += (head.targetX - head.x) * 0.2f;
        head.y += (head.targetY - head.y) * 0.2f;
        head.updateBounds();
    }

    public void update() {
        for (int i = 1; i < segments.size(); i++) {
            Segment current = segments.get(i);
            Segment prev = segments.get(i-1);
            
            float dx = prev.x - current.x;
            float dy = prev.y - current.y;
            float distance = (float)Math.sqrt(dx*dx + dy*dy);
            float targetDistance = SEGMENT_WIDTH + SEGMENT_GAP;
            
            if (distance > 0) {
                float ratio = targetDistance / distance;
                current.targetX = prev.x - dx * ratio;
                current.targetY = prev.y - dy * ratio;
            }
            
            current.x += (current.targetX - current.x) * 0.15f;
            current.y += (current.targetY - current.y) * 0.15f;
            
            preventSegmentOverlap(i);
            current.updateBounds();
        }
    }
    
    private void preventSegmentOverlap(int index) {
        if (index <= 0 || index >= segments.size()) return;
        
        Segment current = segments.get(index);
        Segment prev = segments.get(index-1);
        
        if (current.bounds.intersects(prev.bounds)) {
            float dx = current.x - prev.x;
            float dy = current.y - prev.y;
            float distance = (float)Math.sqrt(dx*dx + dy*dy);
            float minDistance = SEGMENT_WIDTH + SEGMENT_GAP;
            
            if (distance < minDistance && distance > 0) {
                float adjust = (minDistance - distance) / distance;
                current.x += dx * adjust * 0.7f;
                current.y += dy * adjust * 0.7f;
            }
        }
    }

    public boolean reachedBottom() {
        return !segments.isEmpty() && segments.get(0).y >= MainGame.HEIGHT - 50;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        for (int i = 0; i < segments.size(); i++) {
            Segment seg = segments.get(i);
            
            Color mainColor = (i == 0) ? new Color(0, 220, 0) : 
                new Color(0, 180 - (i*2), 0);
            
            GradientPaint bodyGradient = new GradientPaint(
                seg.x, seg.y, mainColor.brighter(),
                seg.x, seg.y + SEGMENT_HEIGHT, mainColor.darker());
            
            g2d.setPaint(bodyGradient);
            g2d.fillRoundRect((int)seg.x, (int)seg.y, SEGMENT_WIDTH, SEGMENT_HEIGHT, 8, 8);
            
            g2d.setColor(new Color(0, 100, 0, 100));
            for (int j = 0; j < 3; j++) {
                int lineY = (int)seg.y + 3 + j*4;
                g2d.drawLine((int)seg.x + 3, lineY, (int)seg.x + SEGMENT_WIDTH - 3, lineY);
            }
            
            if (i == 0) {
                g2d.setColor(Color.WHITE);
                g2d.fillOval((int)seg.x + 5, (int)seg.y + 3, 6, 6);
                g2d.fillOval((int)seg.x + 17, (int)seg.y + 3, 6, 6);
                
                g2d.setColor(Color.RED);
                g2d.fillOval((int)seg.x + 6, (int)seg.y + 4, 3, 3);
                g2d.fillOval((int)seg.x + 18, (int)seg.y + 4, 3, 3);
                
                g2d.setColor(Color.BLACK);
                g2d.fillArc((int)seg.x + 10, (int)seg.y + 8, 8, 5, 180, 180);
            }
        }
    }

    public boolean checkCollision(Bullet bullet) {
        for (int i = 0; i < segments.size(); i++) {
            Segment seg = segments.get(i);
            if (bullet.checkPixelPerfectCollision(seg.bounds)) {
                segments.remove(i);
                
                if (i == 0 && !segments.isEmpty()) {
                    Segment newHead = segments.get(0);
                    newHead.targetX += direction * 20;
                }
                
                return true;
            }
        }
        return false;
    }

    public boolean checkCollisionWithPlayer(Player player) {
        Rectangle playerRect = player.getBounds();
        for (Segment seg : segments) {
            if (playerRect.intersects(seg.bounds)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDefeated() {
        return segments.isEmpty();
    }
    
    public ArrayList<Segment> getSegments() {
        return new ArrayList<>(segments);
    }
    
    public float getSpeed() {
        return speed;
    }
}