// src/main/java/org/jinseisieko/evolution/view/SimulationView.java
package org.jinseisieko.evolution.view;

import org.jinseisieko.evolution.simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class SimulationView extends JPanel {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private final Simulation simulation;
    private final JFrame frame;

    // Временные переменные для анимации
    private long lastUpdateTime = System.nanoTime();
    private int frameCount = 0;
    private long lastFpsTime = System.currentTimeMillis();

    public SimulationView(Simulation simulation, JFrame frame) {
        this.simulation = simulation;
        this.frame = frame;
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.WHITE);

        // Запуск игрового цикла
        Timer timer = new Timer(0, e -> {
            long now = System.nanoTime();
            double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
            lastUpdateTime = now;

            simulation.update(deltaTime); // обновляем модель

            // FPS
            frameCount++;
            if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                frame.setTitle("Evolution simulator — FPS: " + frameCount);
                frameCount = 0;
                lastFpsTime = System.currentTimeMillis();
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
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Делегируем отрисовку модели (или рисуем здесь, если модель не знает о графике)
        simulation.draw(g2d);

        g2d.dispose();
    }

    public JFrame getFrame() {
        return frame;
    }
}