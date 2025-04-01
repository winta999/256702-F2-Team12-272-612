package Game;

import java.awt.*;
import java.awt.geom.*;

import Game.Comet.CometDirection;

public class CometWarning {
    private Comet.CometDirection direction;
    private boolean movingRight;
    private boolean movingDown;
    private long startTime;
    private final long WARNING_DURATION = 1000; // 1 วินาที
    private int screenWidth;
    private int screenHeight;
    private float pulseIntensity = 0f;
    private Color warningColor = new Color(255, 150, 0, 180);
    private Point[] arrowPoints = new Point[3];
    private int cometSize = 30;

    public CometWarning(Comet.CometDirection direction, boolean movingRight, boolean movingDown, 
                       int screenWidth, int screenHeight, int cometX, int cometY) {
        this.direction = direction;
        this.movingRight = movingRight;
        this.movingDown = movingDown;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.startTime = System.currentTimeMillis();
        
        calculateArrowPosition(cometX, cometY);
    }

    private void calculateArrowPosition(int cometX, int cometY) {
        int arrowSize = 20; // ลูกศรขนาดเล็ก
        int margin = 15;
        
        if (direction == CometDirection.HORIZONTAL) {
            if (movingRight) {
                arrowPoints[0] = new Point(margin, cometY + cometSize/2);
                arrowPoints[1] = new Point(margin + arrowSize, cometY + cometSize/4);
                arrowPoints[2] = new Point(margin + arrowSize, cometY + 3*cometSize/4);
            } else {
                arrowPoints[0] = new Point(screenWidth - margin, cometY + cometSize/2);
                arrowPoints[1] = new Point(screenWidth - margin - arrowSize, cometY + cometSize/4);
                arrowPoints[2] = new Point(screenWidth - margin - arrowSize, cometY + 3*cometSize/4);
            }
        } else {
            if (movingDown) {
                arrowPoints[0] = new Point(cometX + cometSize/2, margin);
                arrowPoints[1] = new Point(cometX + cometSize/4, margin + arrowSize);
                arrowPoints[2] = new Point(cometX + 3*cometSize/4, margin + arrowSize);
            } else {
                arrowPoints[0] = new Point(cometX + cometSize/2, screenHeight - margin);
                arrowPoints[1] = new Point(cometX + cometSize/4, screenHeight - margin - arrowSize);
                arrowPoints[2] = new Point(cometX + 3*cometSize/4, screenHeight - margin - arrowSize);
            }
        }
    }

    public void update() {
        long elapsed = System.currentTimeMillis() - startTime;
        pulseIntensity = 0.5f + 0.5f * (float)Math.sin(elapsed / 200.0);
    }

    public boolean isActive() {
        return System.currentTimeMillis() - startTime < WARNING_DURATION;
    }

    public void draw(Graphics2D g2d) {
        if (!isActive()) return;

        g2d.setColor(new Color(
            warningColor.getRed(),
            warningColor.getGreen(),
            warningColor.getBlue(),
            (int)(warningColor.getAlpha() * pulseIntensity)
        ));
        
        Path2D arrow = new Path2D.Float();
        arrow.moveTo(arrowPoints[0].x, arrowPoints[0].y);
        for (int i = 1; i < arrowPoints.length; i++) {
            arrow.lineTo(arrowPoints[i].x, arrowPoints[i].y);
        }
        arrow.closePath();
        g2d.fill(arrow);
    }
}