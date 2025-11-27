// src/main/java/io.github/jinseisieko/evolution/view/SimulationView.java
package io.github.jinseisieko.evolution.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import io.github.jinseisieko.evolution.base.DrawableBasicSimulation;

public class SimulationView extends JPanel {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 1200;

    private final DrawableBasicSimulation simulation;
    private final Viewport viewport;
    private final JFrame frame;

    // Временные переменные для анимации
    private long lastUpdateTime = System.nanoTime();
    private int frameCount = 0;
    private long lastFpsTime = System.currentTimeMillis();

    public SimulationView(DrawableBasicSimulation simulation, JFrame frame) {
        this.simulation = simulation;
        this.viewport = new Viewport(WINDOW_WIDTH, WINDOW_WIDTH, 0, 0);
        this.frame = frame;
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.WHITE);

        // Запуск игрового цикла
        Timer timer = new Timer(0, e -> {
            long now = System.nanoTime();
            double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
            lastUpdateTime = now;

             // обновляем модель

            // FPS
            frameCount++;
            if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                System.out.println("Evolution simulator — FPS: " + frameCount);
                frameCount = 0;
                lastFpsTime = System.currentTimeMillis();
            }
            simulation.update(deltaTime);
            repaint();
        });
        timer.setDelay(8);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        // graphics2D.setRenderingHint(
        //     RenderingHints.KEY_ANTIALIASING,
        //     RenderingHints.VALUE_ANTIALIAS_ON
        // );
        viewport.updateGraphics2D(graphics2D);

        // Делегируем отрисовку модели (или рисуем здесь, если модель не знает о графике)
        viewport.drawAll(simulation.getDrawables());

        graphics2D.dispose();
    }

    public JFrame getFrame() {
        return frame;
    }

    public DrawableBasicSimulation getSimulation() {
        return simulation;
    }
}