package Game.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TutorialPanel extends JPanel {
    private JFrame parentFrame;
    private MainGame mainGame;

    public TutorialPanel(JFrame frame, MainGame game) {
        this.parentFrame = frame;
        this.mainGame = game;
        setLayout(new BorderLayout());
        setBackground(new Color(10, 5, 30));

        // สร้างปุ่มกลับ
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> returnToMenu());
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(70, 70, 70));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ส่วนหัวเรื่อง
        JLabel titleLabel = new JLabel("TUTORIAL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        // ส่วนเนื้อหา Tutorial
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50));

        // ข้อมูลการควบคุม
        addTutorialSection(contentPanel, "CONTROLS", 
            "← → ↑ ↓ : Move\n" +
            "SPACE : Shoot\n" +
            "ESC : Return to Menu\n" +
            "R : Restart Game");

        // ข้อมูลคะแนนเห็ด
        addMushroomInfoSection(contentPanel);

        // ข้อมูลเกมทั่วไป
        addTutorialSection(contentPanel, "GAME INFO", 
            "Destroy centipede segments for points\n" +
            "Avoid comets - they reduce your score\n" +
            "Collect power-ups from special mushrooms\n" +
            "Game ends when you lose all lives");

        // จัดวางส่วนประกอบต่างๆ
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // ตั้งค่าให้รับ key event
        setFocusable(true);
        requestFocusInWindow();
    }

    private void addTutorialSection(JPanel parent, String title, String content) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 200, 0));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 18));
        contentArea.setForeground(Color.WHITE);
        contentArea.setOpaque(false);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        parent.add(titleLabel);
        parent.add(contentArea);
    }

    private void addMushroomInfoSection(JPanel parent) {
        JLabel titleLabel = new JLabel("MUSHROOM TYPES", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 200, 0));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel mushroomPanel = new JPanel();
        mushroomPanel.setLayout(new GridLayout(0, 2, 20, 10));
        mushroomPanel.setOpaque(false);
        mushroomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // เพิ่มข้อมูลเห็ดแต่ละประเภท
        addMushroomInfo(mushroomPanel, Color.WHITE, "Normal Mushroom", "+50 Points");
        addMushroomInfo(mushroomPanel, Color.BLUE, "Blue Mushroom", "+20 Points\nIncrease bullet speed");
        addMushroomInfo(mushroomPanel, new Color(255, 215, 0), "Golden Mushroom", "+50 Points\nExtra life");
        addMushroomInfo(mushroomPanel, Color.RED, "Meteor Mushroom", "+30 Points\nDecrease bullet speed");
        addMushroomInfo(mushroomPanel, new Color(255, 0, 255), "Rainbow Mushroom", "+500 Points");

        parent.add(titleLabel);
        parent.add(mushroomPanel);
    }

    private void addMushroomInfo(JPanel parent, Color color, String name, String effect) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // สร้างวงกลมแทนสีเห็ด
        JLabel colorLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(color);
                g.fillOval(0, 0, 20, 20);
            }
        };
        colorLabel.setPreferredSize(new Dimension(20, 20));
        colorLabel.setMinimumSize(new Dimension(20, 20));
        colorLabel.setMaximumSize(new Dimension(20, 20));

        // ข้อมูลเห็ด
        JTextArea textArea = new JTextArea(name + "\n" + effect);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setForeground(Color.WHITE);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setMargin(new Insets(0, 10, 0, 0));

        infoPanel.add(colorLabel);
        infoPanel.add(textArea);
        parent.add(infoPanel);
    }

    private void returnToMenu() {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new GameMenu(parentFrame, mainGame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // วาดพื้นหลัง
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(10, 5, 30),
            0, getHeight(), new Color(0, 0, 0)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // วาดดาว
        g2d.setColor(Color.WHITE);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(getWidth());
            int y = rand.nextInt(getHeight());
            int size = 1 + rand.nextInt(2);
            g2d.fillOval(x, y, size, size);
        }
    }
}