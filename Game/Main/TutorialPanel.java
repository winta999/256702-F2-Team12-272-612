


package Game.Main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TutorialPanel extends JPanel {
    public TutorialPanel(JFrame frame, MainGame mainGame) {
        setLayout(new BorderLayout());
        setBackground(new Color(10, 5, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("HOW TO PLAY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        String[] instructions = {
            "1. Use ARROW KEYS to move your spaceship",
            "2. Press SPACE to shoot bullets",
            "3. Destroy the centipede to earn points",
            "4. Avoid collisions with the centipede",
            "5. Collect special mushrooms for bonuses:",
            "   - RAINBOW: +1000 points",
            "   - Blue: Increase bullet speed",
            "   - METEOR: Decrease bullet speed",
            "   - GOLDEN: Extra life"
        };

        for (String line : instructions) {
            JLabel label = new JLabel(line);
            label.setFont(new Font("Arial", Font.PLAIN, 18));
            label.setForeground(Color.WHITE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            centerPanel.add(label);
        }

        JButton backButton = new JButton("BACK TO MENU");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0, 100, 0));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 0), 2),
            BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new GameMenu(frame, mainGame));
            frame.pack();
        });

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(backButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        GradientPaint bgGradient = new GradientPaint(
            0, 0, new Color(10, 5, 30),
            0, getHeight(), new Color(0, 0, 0)
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.WHITE);
        Random rand = new Random();
        for (int i = 0; i < 50; i++) {
            int x = rand.nextInt(getWidth());
            int y = rand.nextInt(getHeight());
            int size = 1 + rand.nextInt(2);
            g2d.fillOval(x, y, size, size);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MainGame.WIDTH, MainGame.HEIGHT);
    }
}