package Game;

import java.awt.*;
import java.util.Random;

public class Comet {
    public enum CometDirection { HORIZONTAL, VERTICAL }
    
    private int x, y;
    private int width, height;
    private float speed;
    private boolean visible;
    private Random rand;
    private int screenWidth, screenHeight;
    private Color[] tailColors;
    private int tailLength;
    private boolean movingRight; // For horizontal comets
    private boolean movingDown;  // For vertical comets
    private CometDirection direction;
    private int playerMinY, playerMaxY;

    public Comet(int screenWidth, int screenHeight, int playerMinY, int playerMaxY, float baseSpeed) {
        this.rand = new Random();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.playerMinY = playerMinY;
        this.playerMaxY = playerMaxY;
        this.width = 30;
        this.height = 30;
        this.speed = baseSpeed * (1.5f + rand.nextFloat() * 0.5f);
        this.tailLength = 10 + rand.nextInt(20);
        
        // Randomly choose direction (50% horizontal, 50% vertical)
        this.direction = rand.nextBoolean() ? CometDirection.HORIZONTAL : CometDirection.VERTICAL;
        
        if (direction == CometDirection.HORIZONTAL) {
            this.movingRight = rand.nextBoolean();
            this.y = playerMinY + rand.nextInt(playerMaxY - playerMinY - height);
            if (movingRight) {
                this.x = -width;
            } else {
                this.x = screenWidth;
            }
        } else { // VERTICAL
            this.movingDown = rand.nextBoolean();
            this.x = rand.nextInt(screenWidth - width);
            if (movingDown) {
                this.y = -height;
            } else {
                this.y = screenHeight;
            }
        }
        
        this.visible = true;
        
        this.tailColors = new Color[tailLength];
        for (int i = 0; i < tailLength; i++) {
            float alpha = 1.0f - (float)i/tailLength;
            tailColors[i] = new Color(
                200 + rand.nextInt(55),
                150 + rand.nextInt(105),
                100 + rand.nextInt(155),
                (int)(alpha * 255)
            );
        }
    }

    public void move() {
        if (direction == CometDirection.HORIZONTAL) {
            if (movingRight) {
                x += speed;
                if (x > screenWidth) visible = false;
            } else {
                x -= speed;
                if (x < -width) visible = false;
            }
        } else { // VERTICAL
            if (movingDown) {
                y += speed;
                if (y > screenHeight) visible = false;
            } else {
                y -= speed;
                if (y < -height) visible = false;
            }
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean checkCollisionWithPlayer(Player player) {
        Rectangle cometRect = new Rectangle(x, y, width, height);
        Rectangle playerRect = new Rectangle(
            player.getX(), player.getY(),
            player.getWidth(), player.getHeight()
        );
        return cometRect.intersects(playerRect);
    }

    public void draw(Graphics2D g2d) {
        // Draw tail
        for (int i = 0; i < tailLength; i++) {
            int tailX, tailY;
            
            if (direction == CometDirection.HORIZONTAL) {
                tailX = movingRight ? x - i * 5 : x + i * 5;
                tailY = y + height/4;
            } else { // VERTICAL
                tailX = x + width/4;
                tailY = movingDown ? y - i * 5 : y + i * 5;
            }
            
            boolean onScreen = false;
            if (direction == CometDirection.HORIZONTAL) {
                onScreen = (movingRight && tailX + width >= 0) || (!movingRight && tailX <= screenWidth);
            } else {
                onScreen = (movingDown && tailY + height >= 0) || (!movingDown && tailY <= screenHeight);
            }
            
            if (onScreen) {
                g2d.setColor(tailColors[i]);
                if (direction == CometDirection.HORIZONTAL) {
                    g2d.fillOval(tailX + (movingRight ? 0 : width/2), tailY, width/2, height/2);
                } else {
                    g2d.fillOval(tailX, tailY + (movingDown ? 0 : height/2), width/2, height/2);
                }
            }
        }
        
        // Draw comet head
        GradientPaint cometGradient = new GradientPaint(
            x, y, new Color(255, 255, 200),
            x + width, y + height, new Color(255, 150, 50)
        );
        g2d.setPaint(cometGradient);
        g2d.fillOval(x, y, width, height);
        
        // Draw glowing effect
        g2d.setColor(new Color(255, 200, 100, 100));
        g2d.fillOval(x - 5, y - 5, width + 10, height + 10);
    }
}