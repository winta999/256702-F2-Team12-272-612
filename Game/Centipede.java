package Game;

import java.awt.*;
import java.util.ArrayList;

public class Centipede {
    private ArrayList<Point> segments;
    private int direction; // 1 = move right, -1 = move left

    public Centipede(int startX, int startY) {
        segments = new ArrayList<>();
        // เพิ่มหลายๆ จุดเพื่อสร้าง Centipede (ตะขาม)
        for (int i = 0; i < 10; i++) {
            segments.add(new Point(startX + i * 30, startY)); // ระยะห่างของส่วนคือ 30px
        }
        direction = 1;  // เริ่มจากขวาไปซ้าย
    }

    public void move() {
        // เคลื่อนที่ทั้งหมดในทุกๆ ส่วน
        for (int i = 0; i < segments.size(); i++) {
            Point p = segments.get(i);
            p.x += direction * 5;  // เคลื่อนที่ตามทิศทาง

            // ถ้าส่วนแรกชนขอบจอ ให้ทุกส่วนเปลี่ยนทิศทางและลงไปข้างล่าง
            if (p.x <= 0 || p.x >= MainGame.WIDTH - 30) {
                direction = -direction;
                for (Point segment : segments) {
                    segment.y += 10;  // ทุกส่วนลงมา 10px
                }
                break;
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point p : segments) {
            g.fillRect(p.x, p.y, 30, 10);  // วาดทุกส่วนของ Centipede
        }
    }

    public boolean checkCollision(Bullet bullet) {
        for (int i = 0; i < segments.size(); i++) {
            Point p = segments.get(i);
            // ตรวจสอบการชนระหว่างกระสุนกับส่วนของ Centipede
            if (bullet.getX() >= p.x && bullet.getX() <= p.x + 30 &&
                bullet.getY() >= p.y && bullet.getY() <= p.y + 10) {
                segments.remove(i); // ลบส่วนที่โดนยิง
                return true;  // กลับมาเมื่อพบการชน
            }
        }
        return false;
    }

    public boolean checkCollisionWithPlayer(Player player) {
        for (Point p : segments) {
            if (player.getX() >= p.x && player.getX() <= p.x + 30 &&
                player.getY() >= p.y && player.getY() <= p.y + 10) {
                return true;  // ถ้าชนกับ Player
            }
        }
        return false;
    }

    public void increaseSpeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'increaseSpeed'");
    }

    public boolean isDefeated() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDefeated'");
    }
}
