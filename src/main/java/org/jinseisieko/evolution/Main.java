package org.jinseisieko.evolution;

import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Состояние анимации — теперь это ПОЛЯ, а не локальные переменные
    private long lastUpdateTime = System.nanoTime();
    private int frameCount = 0;
    private long lastFpsTime = System.currentTimeMillis();

    private double agentY = 50; // пример координаты агента
    private final double agentSpeed = 80.0; // пикс/сек

    public Main(JFrame frame) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);

        Timer timer = new Timer(0, e -> {
            // Теперь можно безопасно читать и менять поля
            long now = System.nanoTime();
            double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
            lastUpdateTime = now;

            // Обновляем логику
            agentY += agentSpeed * deltaTime;
            if (agentY > HEIGHT + 20) {
                agentY = -20;
            }

            // Считаем FPS
            frameCount++;
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFpsTime >= 1000) {
                frame.setTitle("Эволюционный симулятор — FPS: " + frameCount);
                frameCount = 0;
                lastFpsTime = currentTime;
            }

            repaint();
        });
        timer.setDelay(0);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE);
        g2d.fillOval(WIDTH / 2 - 15, (int) agentY - 15, 30, 30);
        g2d.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Эволюционный симулятор");
            Main panel = new Main(frame);
            frame.add(panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}