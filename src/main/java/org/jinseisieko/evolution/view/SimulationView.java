// src/main/java/org/jinseisieko/evolution/view/SimulationView.java
package org.jinseisieko.evolution.view;

import javax.swing.*;

import java.awt.*;

import org.jinseisieko.evolution.base.DrawableBasicSimulation;

public class SimulationView extends JPanel {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private final DrawableBasicSimulation simulation;
    private final JFrame frame;

    // Временные переменные для анимации
    private long lastUpdateTime = System.nanoTime();
    private int frameCount = 0;
    private long lastFpsTime = System.currentTimeMillis();

    public SimulationView(DrawableBasicSimulation simulation, JFrame frame) {
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
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Делегируем отрисовку модели (или рисуем здесь, если модель не знает о графике)
        simulation.draw(graphics2D);

        graphics2D.dispose();
    }

    public JFrame getFrame() {
        return frame;
    }

    public DrawableBasicSimulation getSimulation() {
        return simulation;
    }
}