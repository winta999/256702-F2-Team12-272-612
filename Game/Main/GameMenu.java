package Game.Main;

import javax.swing.*;

import java.awt.*;
import java.util.Random;

public class GameMenu extends JPanel {
    private MainGame mainGame;
    private JFrame frame;

    public GameMenu(JFrame frame, MainGame mainGame) {
        this.frame = frame;
        this.mainGame = mainGame;
        setLayout(new BorderLayout());
        setBackground(new Color(10, 5, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Centipede");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        JButton startButton = createMenuButton("START");
        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(mainGame);
            frame.pack();
            mainGame.requestFocus();
        });

        JButton tutorialButton = createMenuButton("TUTORIAL");
tutorialButton.addActionListener(e -> {
    frame.getContentPane().removeAll();
    TutorialPanel tutorialPanel = new TutorialPanel(frame, mainGame); // สร้างใหม่ทุกครั้ง
    frame.getContentPane().add(tutorialPanel);
    frame.revalidate(); // เพิ่มบรรทัดนี้
    frame.repaint();
    tutorialPanel.requestFocus();
});

        JButton exitButton = createMenuButton("EXIT");
        exitButton.addActionListener(e -> System.exit(0));

        centerPanel.add(titleLabel);
        centerPanel.add(startButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(tutorialButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(exitButton);

        add(centerPanel, BorderLayout.CENTER);

        JLabel creditLabel = new JLabel("© 2023 Centipede Game");
        creditLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        creditLabel.setForeground(Color.GRAY);
        creditLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        creditLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.add(creditLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 100, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 0), 2),
            BorderFactory.createEmptyBorder(10, 40, 10, 40)
        ));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 0));
            }
        });

        return button;
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
        for (int i = 0; i < 100; i++) {
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